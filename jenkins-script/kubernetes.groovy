pipeline {
    agent any

    tools {
        gradle 'gradle-8.7'
        jdk 'jdk-17'
    }

    stages {
        stage('Deploy Using Kubernetes') {
            steps {
                dir('deployment') {
                    git branch: 'main', changelog: false, credentialsId: 'deployment-key', poll: false, url: 'git@github.com:com-pi/deployment.git'

                    sh 'kubectl apply -f k8s/nginx/nginx-deployment.yaml'
                    sh 'kubectl apply -f k8s/nginx/nginx-service.yaml'

                    sh 'kubectl apply -f k8s/api-gateway/api-gateway-deployment.yaml'
                    sh 'kubectl apply -f k8s/api-gateway/api-gateway-service.yaml'
                }
            }
        }
    }

}