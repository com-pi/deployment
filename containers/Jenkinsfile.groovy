pipeline {
    agent any

    parameters {
        choice choices: ['모듈 선택', 'nginx', 'mysql', 'scraper', 'mongodb'], description: '빌드할 모듈 선택', name: 'Module'
        choice choices: ['dev', 'prod'], description: '배포 환경', name: 'ENVIRONMENT'
        string defaultValue: 'null', description: '배포 버전', name: 'VERSION', trim: true
    }


    environment {
        DOCKERHUB_USERNAME = 'utopiandrmer'
        DOCKER_FILE_PATH = 'containers'
        PROJECT_NAME = 'comppi'
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

        stage('scraper 리포지토리 체크 아웃'){
            when {
                expression { params.Module != '모듈 선택' && params.Module == 'scraper'}
            }
            steps {
                dir('scraper') {
                    git branch: 'main', changelog: false, credentialsId: 'kihong', poll: false, url: 'git@github.com:com-pi/plant-scraper.git'
                }
            }
        }

        stage('도커 이미지 빌드') {
            steps {
                script {
                    if (params.Module == "scraper") {
                        sh "cp -r scraper/app ${DOCKER_FILE_PATH}/scraper"
                    }
                    sh "docker build --no-cache -t ${DOCKERHUB_USERNAME}/${env.PROJECT_NAME}-${params.Module}:${env.TAG} ${DOCKER_FILE_PATH}/${params.Module}"
                }
            }
        }

        stage('도커 허브에 이미지 푸시') {
            steps {
                script {
                    sh "docker push ${DOCKERHUB_USERNAME}/${env.PROJECT_NAME}-${params.Module}:${env.TAG}"
                }
            }
        }
    }

}