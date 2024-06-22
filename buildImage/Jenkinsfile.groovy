pipeline {
    agent any

    tools {
        gradle 'gradle-8.7'
        jdk 'jdk-17'
    }

    parameters {
        string(name: 'VERSION', defaultValue: 'dev', trim: true)
    }

    environment {
        DOCKERHUB_CREDENTIALS = 'dockerhub_account'
        DOCKERHUB_USERNAME = 'utopiandrmer'
        PROJECT_NAME = 'comp'
        DOCKER_FILE_PATH = 'deployment/docker/containers'
        DOCKER_COMPOSE_SCRIPT = 'deployment/docker/docker-compose'

        DATE = sh(script: 'date +%Y%m%d', returnStdout: true).trim()
        TIME = sh(script: 'date +%H%M%S', returnStdout: true).trim()
        TAG = "${params.VERSION}-${env.DATE}-${env.TIME}"
    }

    stages {
        stage('체크 아웃 및 이미지 빌드') {
            steps {
                dir('backend-source-code') {
                    git branch: 'develop', changelog: false, credentialsId: 'backend', poll: false, url: 'git@github.com:com-pi/backend.git'
                    sh "./gradlew buildAllImages -Dtag=${env.TAG}"
                }
            }
        }

        stage('도커 허브로 이미지 푸시') {
            steps {
                withCredentials([usernamePassword(credentialsId: env.DOCKERHUB_CREDENTIALS, passwordVariable: 'DOCKER_PASSWORD', usernameVariable: 'DOCKER_USERNAME')]) {
                    sh "echo $DOCKER_PASSWORD | docker login -u $DOCKER_USERNAME --password-stdin"
                    sh "docker push ${DOCKERHUB_USERNAME}/${PROJECT_NAME}-auth-service:${env.TAG}"
                    sh "docker push ${DOCKERHUB_USERNAME}/${PROJECT_NAME}-api-gateway:${env.TAG}"
                    sh "docker push ${DOCKERHUB_USERNAME}/${PROJECT_NAME}-discovery-eureka:${env.TAG}"
                    sh "docker push ${DOCKERHUB_USERNAME}/${PROJECT_NAME}-board-service:${env.TAG}"
                    sh "docker push ${DOCKERHUB_USERNAME}/${PROJECT_NAME}-my-plant:${env.TAG}"
                    sh "docker push ${DOCKERHUB_USERNAME}/${PROJECT_NAME}-encyclo-service:${env.TAG}"
                }
            }
        }
    }
}