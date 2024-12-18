pipeline {
    agent any

    tools {
        gradle 'gradle'
        jdk 'java17'
    }

    parameters {
        choice choices: ['모듈 선택', 'auth-service', 'api-gateway', 'discovery-eureka', 'board-service', 'my-plant', 'encyclo-service'], description: '빌드할 모듈 선택', name: 'Module'
        choice choices: ['dev', 'prod'], description: '배포 환경', name: 'ENVIRONMENT'
        string defaultValue: 'null', description: '배포 버전', name: 'VERSION', trim: true
    }

    environment {
        DOCKERHUB_CREDENTIALS = 'dockerhub_account'
        DOCKERHUB_USERNAME = 'utopiandrmer'
        PROJECT_NAME = 'comppi'
        DOCKER_FILE_PATH = 'deployment/docker/containers'
        DOCKER_COMPOSE_SCRIPT = 'deployment/docker/docker-compose'
    }

    stages {
        stage('태그 생성') {
            steps {
                script {
                    env.DATE = sh(script: 'date +%Y%m%d', returnStdout: true).trim()
                    env.TIME = sh(script: 'date +%H%M%S', returnStdout: true).trim()
                    if(params.VERSION == 'null'){
                        env.TAG = "${params.ENVIRONMENT}-${env.DATE}-${env.TIME}"
                    } else {
                        env.TAG = "${params.ENVIRONMENT}-${params.VERSION}"
                    }
                }
            }
        }

        stage('체크 아웃 및 이미지 빌드') {
            when {
                expression { params.Module != '모듈 선택'}
            }
            steps {
                script {
                    def branch = params.ENVIRONMENT == 'dev' ? 'develop' : 'production'
                    dir('backend-source-code') {
                        git branch: branch, changelog: false, credentialsId: 'backend.comppi.github', poll: false, url: 'git@github.com:com-pi/backend.git'
                        sh "./gradlew :${params.Module}:buildDockerImage -Ptag=${env.TAG}"
                    }

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
