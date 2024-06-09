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
        stage('깃허브 리포지토리 체크 아웃'){
            parallel {
                stage('스크랩퍼 모듈') {
                    steps {
                        dir('plant-scraper') {
                            git branch: 'main', changelog: false, credentialsId: 'scraper', poll: false, url: 'git@github.com:com-pi/plant-scraper.git'
                        }
                    }
                }
            }
        }

        stage('이미지 빌드에 필요한 파일 복사') {
            steps {
                dir('deployment') {
                    git branch: 'main', changelog: false, credentialsId: 'deployment-key', poll: false, url: 'git@github.com:com-pi/deployment.git'
                }

                // jar 파일 복사
                sh "cp -r plant-scraper/app ${DOCKER_FILE_PATH}/scraper"
            }
        }

        stage('도커 이미지 빌드') {
            steps {
                // 컨테이너 빌드 및 업로드
                sh "docker build --no-cache -t ${DOCKERHUB_USERNAME}/compi-scraper ${DOCKER_FILE_PATH}/scraper"
            }
        }

        stage('도커 허브에 이미지 푸시') {
            steps {
                sh "docker push ${DOCKERHUB_USERNAME}/compi-scraper"
            }
        }

        stage('쿠버네티스를 이용하여 배포') {
            steps {
                sh "kubectl apply -f ${K8S_SCRIPT_PATH}/deployment/deployment.yaml"
                sh "kubectl apply -f ${K8S_SCRIPT_PATH}/service/service.yaml"
            }
        }

    }

}