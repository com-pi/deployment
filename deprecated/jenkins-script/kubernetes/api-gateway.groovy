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
        PROJECT_NAME = 'comp'
    }

    stages {
        stage('깃허브 리포지토리 체크 아웃'){
            parallel {
                stage('백엔드 어플리케이션 모듈') {
                    steps {
                        dir('application') {
                            git branch: 'develop', changelog: false, credentialsId: 'jenkins-github', poll: false, url: 'git@github.com:com-pi/backend.git'
                        }
                    }

                }
            }
        }

        stage('도커 이미지 빌드') {
            steps {
                dir('application') {
                    sh "chmod +x ./gradlew"
                    sh "./gradlew :api-gateway:bootBuildImage"
                }
            }
        }

        stage('도커 허브에 이미지 푸시') {
            steps {
                sh "docker push ${DOCKERHUB_USERNAME}/${PROJECT_NAME}-api-gateway"
            }
        }

        stage('쿠버네티스 배포') {
            steps {
                dir('deployment') {
                    git branch: 'main', changelog: false, credentialsId: 'deployment-key', poll: false, url: 'git@github.com:com-pi/deployment.git'
                }
                sh "kubectl apply -f ${K8S_SCRIPT_PATH}/deployment/deployment.yaml"
                sh "kubectl apply -f ${K8S_SCRIPT_PATH}/service/service.yaml"
                sh "kubectl rollout restart deployment api-gateway-deployment"
            }
        }
    }

}