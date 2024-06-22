pipeline {
    agent any

    tools {
        gradle 'gradle-8.7'
        jdk 'jdk-17'
    }


    properties([
            parameters([
                    string(name: 'VERSION', defaultValue: 'dev', trim: true),
            ])
    ])


    environment {
        DOCKERHUB_USERNAME = 'utopiandrmer'
        DOCKER_FILE_PATH = 'containers'

        DATE = sh(script: 'date +%Y%m%d', returnStdout: true).trim()
        TIME = sh(script: 'date +%H%M%S', returnStdout: true).trim()
        TAG = "${params.VERSION}-${env.DATE}-${env.TIME}"
    }

    stages {
        stage('scraper 리포지토리 체크 아웃'){
            steps {
                dir('scraper') {
                    git branch: 'main', changelog: false, credentialsId: 'repository-scraper', poll: false, url: 'git@github.com:com-pi/plant-scraper.git'
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
                sh "docker build --no-cache -t ${DOCKERHUB_USERNAME}/comp-nginx ${DOCKER_FILE_PATH}/nginx"
                sh "docker build --no-cache -t ${DOCKERHUB_USERNAME}/comp-scraper ${DOCKER_FILE_PATH}/scraper"
                sh "docker build --no-cache -t ${DOCKERHUB_USERNAME}/comp-mysql ${DOCKER_FILE_PATH}/mysql"
                sh "docker build --no-cache -t ${DOCKERHUB_USERNAME}/comp-mongodb ${DOCKER_FILE_PATH}/mongodb"
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