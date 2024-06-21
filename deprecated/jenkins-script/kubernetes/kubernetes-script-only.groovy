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
        stage('쿠버네티스 스크립트 적용') {
            steps {
                dir('deployment') {
                    git branch: 'main', changelog: false, credentialsId: 'deployment-key', poll: false, url: 'git@github.com:com-pi/deployment.git'
                }
                sh "kubectl apply -f ${K8S_SCRIPT_PATH}/deployment/deployment.yaml"
                sh "kubectl apply -f ${K8S_SCRIPT_PATH}/service/service.yaml"
            }
        }

    }

}