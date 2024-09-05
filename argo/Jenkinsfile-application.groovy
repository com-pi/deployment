pipeline {
    agent any

    parameters {
        choice(choices: ['option', 'comppi-dev', 'comppi-prod', 'comppi-gateway'], name: 'APPLICATION', description: '어플리케이션 선택')
        choice(choices: ['option', 'upgrade', 'uninstall'], name: 'DEPLOY_TYPE', description: '배포/삭제')
    }

    environment {
        DOCKERHUB_USERNAME = 'utopiandrmer'
        GITHUB_URL = 'git@github.com:com-pi/deployment.git'
        ARGO_PATH = './argo'
    }

    stages {
        stage('헬름 배포 관리') {
            steps {
                script {
                    if (params.DEPLOY_TYPE == "upgrade") {
                        HELM_DEPLOY_COMMAND = "helm upgrade ${params.TARGET_ARGO} ${ARGO_PATH}/${params.TARGET_ARGO} " +
                                " -f ${ARGO_PATH}/${params.APPLICATION}/values.yaml " +
                                " -n argo --install --wait --timeout=10m "
                            sh "eval ${HELM_DEPLOY_COMMAND}"
                    } else if (params.DEPLOY_TYPE == "uninstall") {
                        sh "helm uninstall ${params.TARGET_ARGO} -n argo"
                    } else {
                        echo "skip deploy"
                    }
                }
            }
        }
    }
}