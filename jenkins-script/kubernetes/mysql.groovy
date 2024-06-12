pipeline {
    agent any

    tools {
        gradle 'gradle-8.7'
        jdk 'jdk-17'
    }

    environment {
        DOCKERHUB_USERNAME = 'utopiandrmer'
        DOCKER_FILE_PATH = 'deployment/docker/containers'
        K8S_SCRIPT_PATH = 'deployment/k8s'
    }

    stages {
        stage('도커 이미지 빌드') {
            steps {
                dir('deployment') {
                    git branch: 'main', changelog: false, credentialsId: 'deployment-key', poll: false, url: 'git@github.com:com-pi/deployment.git'
                }

                // 컨테이너 빌드 및 업로드
                sh "docker build --no-cache -t ${DOCKERHUB_USERNAME}/compi-mysql ${DOCKER_FILE_PATH}/mysql"
            }
        }


        stage('도커 허브에 이미지 푸시') {
            steps {
                sh "docker push ${DOCKERHUB_USERNAME}/compi-mysql"
            }
        }

        stage('도커 컴포즈를 이용하여 배포') {
            steps {
                sh "kubectl rollout restart deployment mysql-deployment"
            }
        }

    }

}