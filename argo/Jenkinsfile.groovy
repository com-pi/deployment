pipeline {
    agent any

    parameter {
        choice(choices: ['option', 'namespace_create', 'namespace_delete', 'helm_upgrade', 'helm_uninstall'], name: 'DEPLOY_TYPE', description: '배포 타입 선택')
        choice(choices: ['option', 'argo-cd', 'argocd-image-updater', 'argo-rollouts'], name: 'TARGET_ARGO', description: 'Argo 대상 선택')
    }

    environment {
        DOCKERHUB_USERNAME = 'utopiandrmer'
        GITHUB_URL = 'git@github.com:com-pi/deployment.git'
        ARGO_PATH = './argo'
    }

    stages {
        stage('네임스페이스 관리') {
            steps {
                script {
                    if (params.DEPLOY_TYPE == "namespace_create") {
                        sh "kubectl apply -f ${ARGO_PATH}/namespace.yaml"
                    } else if (params.DEPLOY_TYPE == "namespace_delete") {
                        sh "kubectl delete -f ${ARGO_PATH}/namespace.yaml"
                    } else {
                        echo "skip namespace"
                    }
                }
            }
        }

        stage('헬름 배포 관리') {
            steps {
                script {
                    if (params.DEPLOY_TYPE == "helm_upgrade") {
                        HELM_DEPLOY_COMMAND = "helm upgrade ${params.TARGET_ARGO} ${ARGO_PATH}/${params.TARGET_ARGO} " +
                                " -f ${ARGO_PATH}/${params.TARGET_ARGO}/custom-values.yaml " +
                                " -n argo --wait --timeout=10m "

                        // image-updater일 경우 도커허브 credentials 주입
                        if (params.TARGET_ARGO == "argocd-image-updater") {
                            withCredentials([usernamePassword(credentialsId: 'dockerhub_account', passwordVariable: 'PASSWORD', usernameVariable: 'USERNAME')]) {
                                HELM_DEPLOY_COMMAND += " --set config.registries[0].credentials=env:DOCKER_HUB_CREDS=" + '${USERNAME}' + ":" + '${PASSWORD}'
                            }
                        }

                        sh "eval ${HELM_DEPLOY_COMMAND}"
                    } else if (params.DEPLOY_TYPE == "helm_uninstall") {
                        sh "helm uninstall ${params.TARGET_ARGO} -n argo"

                        // CRD 삭제
                        if (params.TARGET_ARGO == "argo-cd") {
                            sh "kubectl delete crd applications.argoproj.io applicationsets.argoproj.io appprojects.argoproj.io"
                        }
                        if (params.TARGET_ARGO == "argo-rollouts") {
                            sh "kubectl delete crd analysisruns.argoproj.io analysistemplates.argoproj.io clusteranalysistemplates.argoproj.io"
                            sh "kubectl delete crd experiments.argoproj.io rollouts.argoproj.io"
                        }

                    } else {
                        echo "skip deploy"
                    }
                }
            }
        }
    }
}