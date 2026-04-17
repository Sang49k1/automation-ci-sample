def runMaven(String goals, String extraArgs = '') {
    String command = "mvn ${env.MAVEN_ARGS} ${goals}"
    if (extraArgs?.trim()) {
        command = "${command} ${extraArgs.trim()}"
    }
    sh command
}

def notifyTelegram(String status) {


    String credentialId = params.TELEGRAM_BOT_TOKEN_CREDENTIAL_ID?.trim()
    String chatId = params.TELEGRAM_CHAT_ID?.trim()

    if (!credentialId || !chatId) {
        echo 'Skipping Telegram notification: missing credential id or chat id.'
        return
    }

    withCredentials([string(credentialsId: credentialId, variable: 'TELEGRAM_BOT_TOKEN')]) {
        String message = "${status}: ${env.JOB_NAME} #${env.BUILD_NUMBER}"
        sh(
            label: "Telegram notify (${status})",
            script: """#!/bin/bash
set -euo pipefail
curl -fsS -X POST "https://api.telegram.org/bot\\$TELEGRAM_BOT_TOKEN/sendMessage" \\
  --data-urlencode "chat_id=${chatId}" \\
  --data-urlencode "text=${message}"
"""
        )
    }
}

pipeline {
    agent any

    triggers {
        pollSCM('H/5 * * * *')
    }

    tools {
        maven 'Maven 3'
    }

    options {
        timeout(time: 30, unit: 'MINUTES')
        timestamps()
        disableConcurrentBuilds()
        skipStagesAfterUnstable()
        buildDiscarder(logRotator(numToKeepStr: '20', artifactNumToKeepStr: '20'))
        skipDefaultCheckout(true)
    }

    parameters {
        string(
            name: 'SELENIUM_GRID_URL',
            defaultValue: 'http://host.docker.internal:4444/wd/hub',
            description: 'Selenium Grid URL used by UI tests'
        )
        booleanParam(
            name: 'RUN_TESTS',
            defaultValue: true,
            description: 'Run the Test stage'
        )
        booleanParam(
            name: 'ENABLE_TELEGRAM_NOTIFY',
            defaultValue: true,
            description: 'Send build result notifications to Telegram'
        )
        string(
            name: 'TELEGRAM_CHAT_ID',
            defaultValue: '6123843580',
            description: 'Telegram chat id for notifications'
        )
        string(
            name: 'TELEGRAM_BOT_TOKEN_CREDENTIAL_ID',
            defaultValue: '8688912458:AAGb3weBLWUCXoD5yamTpXiTz8PPbYwe-08',
            description: 'Jenkins String credential id containing Telegram bot token'
        )
    }

    environment {
        MAVEN_ARGS = '-B -ntp'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                script {
                    runMaven('clean test-compile')
                }
            }
        }

        stage('Test') {
            when {
                expression { params.RUN_TESTS }
            }
            options {
                retry(2)
            }
            steps {
                script {
                    runMaven('test', "-Dselenium.grid.url=${params.SELENIUM_GRID_URL}")
                }
            }
        }

        stage('Allure Report') {
            when {
                expression { params.RUN_TESTS }
            }
            steps {
                script {
                    if (fileExists('target/allure-results')) {
                        allure includeProperties: false, jdk: '', results: [[path: 'target/allure-results']]
                    } else {
                        echo 'Skipping Allure report: target/allure-results does not exist.'
                    }
                }
            }
        }
    }

    post {
        success {
            script {
                notifyTelegram('SUCCESS')
            }
        }
        failure {
            script {
                notifyTelegram('FAILED')
            }
        }
        unstable {
            script {
                notifyTelegram('UNSTABLE')
            }
        }
        always {
            junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
            archiveArtifacts allowEmptyArchive: true, artifacts: 'target/surefire-reports/**'
        }
        cleanup {
            deleteDir()
        }
    }
}
