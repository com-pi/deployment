pipeline {
    agent any

    tools {
        gradle 'gradle-8.7'
        jdk 'jdk-17'
    }

    environment {
        DOCKERHUB_USERNAME = 'utopiandrmer'
        CONTAINER_FILE_PATH = 'deployment/docker/containers'
        DOCKER_COMPOSE_SCRIPT = 'deployment/docker/docker-compose'
    }

    stages {
        stage('Source Build') {
            steps {
                dir('backend') {
                    git branch: 'develop', changelog: false, credentialsId: 'jenkins-github', poll: false, url: 'git@github.com:com-pi/backend.git'

                    sh "chmod +x ./gradlew"
                    sh "./gradlew clean build -x test"
                }
            }
        }

        stage('Get Scraper Source') {
            steps {
                dir('plant-scraper') {
                    git branch: 'main', changelog: false, credentialsId: 'scraper', poll: false, url: 'git@github.com:com-pi/plant-scraper.git'
                }
            }
        }

        stage('Container Build') {
            steps {
                dir('deployment') {
                    git branch: 'main', changelog: false, credentialsId: 'deployment-key', poll: false, url: 'git@github.com:com-pi/deployment.git'
                }

                // jar 파일 복사
                sh "cp backend/discovery-eureka/build/libs/discovery-eureka.jar ${CONTAINER_FILE_PATH}/discovery-eureka/discovery-eureka.jar"
                sh "cp backend/api-gateway/build/libs/api-gateway.jar ${CONTAINER_FILE_PATH}/api-gateway/api-gateway.jar"
                sh "cp backend/auth-service/build/libs/auth-service.jar ${CONTAINER_FILE_PATH}/auth-service/auth-service.jar"
                sh "cp backend/board-service/build/libs/board-service.jar ${CONTAINER_FILE_PATH}/board-service/board-service.jar"
                sh "cp backend/encyclo-service/build/libs/encyclo-service.jar ${CONTAINER_FILE_PATH}/encyclo-service/encyclo-service.jar"
                sh "cp -r plant-scraper/app ${CONTAINER_FILE_PATH}/scraper"

                // 컨테이너 빌드 및 업로드
                sh "docker build --no-cache -t ${DOCKERHUB_USERNAME}/compi-discovery-eureka ${CONTAINER_FILE_PATH}/discovery-eureka"
                sh "docker push ${DOCKERHUB_USERNAME}/compi-discovery-eureka"

                sh "docker build --no-cache -t ${DOCKERHUB_USERNAME}/compi-api-gateway ${CONTAINER_FILE_PATH}/api-gateway"
                sh "docker push ${DOCKERHUB_USERNAME}/compi-api-gateway"

                sh "docker build --no-cache -t ${DOCKERHUB_USERNAME}/compi-auth-service ${CONTAINER_FILE_PATH}/auth-service"
                sh "docker push ${DOCKERHUB_USERNAME}/compi-auth-service"

                sh "docker build --no-cache -t ${DOCKERHUB_USERNAME}/compi-board-service ${CONTAINER_FILE_PATH}/board-service"
                sh "docker push ${DOCKERHUB_USERNAME}/compi-board-service"

                sh "docker build --no-cache -t ${DOCKERHUB_USERNAME}/compi-encyclo-service ${CONTAINER_FILE_PATH}/encyclo-service"
                sh "docker push ${DOCKERHUB_USERNAME}/compi-encyclo-service"

                sh "docker build --no-cache -t ${DOCKERHUB_USERNAME}/compi-nginx ${CONTAINER_FILE_PATH}/nginx"
                sh "docker push ${DOCKERHUB_USERNAME}/compi-nginx"

                sh "docker build --no-cache -t ${DOCKERHUB_USERNAME}/compi-scraper ${CONTAINER_FILE_PATH}/scraper"
                sh "docker push ${DOCKERHUB_USERNAME}/compi-scraper"
            }
        }

        stage('Deploy') {
            steps {
                sh "docker compose -f ${DOCKER_COMPOSE_SCRIPT}/docker-compose.yml up -d"
            }
        }
    }
}
