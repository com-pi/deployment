pipeline {
    agent any

    tools {
        gradle 'gradle-8.7'
        jdk 'jdk-17'
    }

    parameters {
        string defaultValue: 'dev', description: '버전 선택', name: 'VERSION', trim: true
        choice choices: ['모듈 선택', 'auth-service', 'api-gateway', 'discovery-eureka', 'board-service', 'my-plant', 'encyclo-service'], description: '빌드할 모듈 선택', name: 'Module'
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
            when {
                expression { params.Module != '모듈 선택'}
            }
            steps {
                dir('backend-source-code') {
                    git branch: 'develop', changelog: false, credentialsId: 'backend', poll: false, url: 'git@github.com:com-pi/backend.git'
                    sh "./gradlew :${params.Module}:buildDockerImage -Ptag=${env.TAG}"
                }
            }
        }

        stage('도커 허브로 이미지 푸시') {
            when {
                expression { params.Module != '모듈 선택'}
            }
            steps {
                withCredentials([usernamePassword(credentialsId: env.DOCKERHUB_CREDENTIALS, passwordVariable: 'DOCKER_PASSWORD', usernameVariable: 'DOCKER_USERNAME')]) {
                    sh "echo $DOCKER_PASSWORD | docker login -u $DOCKER_USERNAME --password-stdin"
                    sh "docker push ${DOCKERHUB_USERNAME}/${PROJECT_NAME}-${params.Module}:${env.TAG}"
                }
            }
        }
    }
}