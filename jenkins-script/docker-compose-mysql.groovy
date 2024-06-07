pipeline {
    agent any

    tools {
        gradle 'gradle-8.7'
        jdk 'jdk-17'
    }

    environment {
        DOCKERHUB_USERNAME = 'utopiandrmer'
        DOCKER_FILE_PATH = 'deployment/docker/containers'
        DOCKER_COMPOSE_SCRIPT = 'deployment/docker/docker-compose'
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

        stage('도커 컴포즈를 이용하여 배포') {
            steps {
                sh "docker compose -f ${DOCKER_COMPOSE_SCRIPT}/docker-compose.yml up -d"
            }
        }

    }

}