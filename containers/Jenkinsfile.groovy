pipeline {
    agent any

    tools {
        gradle 'gradle-8.7'
        jdk 'jdk-17'
    }

    environment {
        DOCKERHUB_USERNAME = 'utopiandrmer'
        DOCKER_FILE_PATH = 'deployment/containers'
    }

    stages {
        stage('리포지토리 체크 아웃'){
            steps {
                dir('plant-scraper') {
                    git branch: 'main', changelog: false, credentialsId: 'scraper', poll: false, url: 'git@github.com:com-pi/plant-scraper.git'
                }
            }
        }

        stage('깃허브 리포지토리 체크 아웃'){
            parallel {
                stage('scraper 모듈') {
                    steps {
                        dir('scraper') {
                            git branch: 'main', changelog: false, credentialsId: 'scraper', poll: false, url: 'git@github.com:com-pi/plant-scraper.git'
                        }
                    }
                }
            }
        }

        stage('이미지 빌드에 필요한 파일 복사') {
            steps {
                sh "cp -r scraper/app ${DOCKER_FILE_PATH}/scraper"
            }
        }

        stage('도커 이미지 빌드') {
            steps {
                // 컨테이너 빌드 및 업로드
                sh "docker build --no-cache -t ${DOCKERHUB_USERNAME}/compi-nginx ${DOCKER_FILE_PATH}/nginx"
                sh "docker build --no-cache -t ${DOCKERHUB_USERNAME}/compi-scraper ${DOCKER_FILE_PATH}/scraper"
                sh "docker build --no-cache -t ${DOCKERHUB_USERNAME}/compi-mysql ${DOCKER_FILE_PATH}/mysql"
                sh "docker build --no-cache -t ${DOCKERHUB_USERNAME}/compi-mysql ${DOCKER_FILE_PATH}/mongodb"
            }
        }

        stage('도커 허브에 이미지 푸시') {
            steps {
                sh "docker push ${DOCKERHUB_USERNAME}/comp-mysql"
                sh "docker push ${DOCKERHUB_USERNAME}/comp-scraper"
                sh "docker push ${DOCKERHUB_USERNAME}/comp-nginx"
                sh "docker push ${DOCKERHUB_USERNAME}/comp-mongodb"
            }
        }
    }

}