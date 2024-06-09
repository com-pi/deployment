pipeline {
    agent any

    tools {
        gradle 'gradle-8.7'
        jdk 'jdk-17'
    }

    environment {
        DOCKERHUB_USERNAME = 'utopiandrmer'
        K8S_SCRIPT_PATH = 'deployment/k8s'
    }

    stages {
        stage('도커 컴포즈를 이용하여 배포') {
            dir('deployment') {
                git branch: 'main', changelog: false, credentialsId: 'deployment-key', poll: false, url: 'git@github.com:com-pi/deployment.git'
            }
            steps {
                sh "kubectl apply -f ${K8S_SCRIPT_PATH}/deployment/deployment.yaml"
                sh "kubectl apply -f ${K8S_SCRIPT_PATH}/service/service.yaml"
            }
        }

    }

}