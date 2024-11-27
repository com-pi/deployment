pipeline {
    agent any

    parameters {
        choice(choices: ['option', 'gateway', 'api-gateway', 'auth-service', 'board-service', 'encyclo-service', 'my-plant', 'minio', 'redis', 'discovery-eureka'], name: 'APPLICATION', description: '어플리케이션 선택')
        choice(choices: ['option', 'dev', 'prod'], name: 'ENVIRONMENT', description: '배포 환경')
        choice(choices: ['option', 'upgrade', 'uninstall'], name: 'DEPLOY_TYPE', description: '배포/삭제')
    }

    environment {
        DOCKERHUB_USERNAME = 'utopiandrmer'
        GITHUB_URL = 'git@github.com:com-pi/deployment.git'
        ARGO_PATH = './argo'
        NAMESPACE = 'argo-cd'
    }

    stages {
        stage('헬름 배포 관리') {
            steps {
                script {
                    if (params.DEPLOY_TYPE == "upgrade") {
                        if (params.APPLICATION == "gateway") {
                            HELM_DEPLOY_COMMAND = "helm upgrade ${params.APPLICATION} ${ARGO_PATH}/${params.APPLICATION} " +
                                    " -f ${ARGO_PATH}/${params.APPLICATION}/values.yaml " +
                                    " -n ${NAMESPACE} --install --wait --timeout=10m "
                            sh "eval ${HELM_DEPLOY_COMMAND}"
                        } else {
                            HELM_DEPLOY_COMMAND = "helm upgrade ${params.APPLICATION}-${params.ENVIRONMENT} ${ARGO_PATH}/${params.APPLICATION} " +
                                    " -f ${ARGO_PATH}/${params.APPLICATION}/values-${params.ENVIRONMENT}.yaml " +
                                    " -n ${NAMESPACE} --install --wait --timeout=10m "
                            sh "eval ${HELM_DEPLOY_COMMAND}"
                        }
                    } else if (params.DEPLOY_TYPE == "uninstall") {
                        if (params.APPLICATION == "gateway"){
                            sh "helm uninstall ${params.APPLICATION} -n argo"
                        }
                            sh "helm uninstall ${params.APPLICATION}-${params.ENVIRONMENT} -n argo"
                    } else {
                        echo "skip deploy"
                    }
                }
            }
        }
    }
}