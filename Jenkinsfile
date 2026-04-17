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
                sh "mvn ${MAVEN_ARGS} clean test-compile"
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
                sh "mvn ${MAVEN_ARGS} test -Dselenium.grid.url=${params.SELENIUM_GRID_URL}"
            }
        }

        stage('Allure Report') {
            steps {
                allure includeProperties: false, jdk: '', results: [[path: 'target/allure-results']]
            }
        }
    }

    post {
        always {
            junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
            archiveArtifacts allowEmptyArchive: true, artifacts: 'target/surefire-reports/**'
        }
        cleanup {
            deleteDir()
        }
        success {
                        sh """
                        curl -s -X POST https://api.telegram.org/bot<8688912458:AAGb3weBLWUCXoD5yamTpXiTz8PPbYwe-08>/sendMessage \
                        -d chat_id=<8688912458> \
                        -d text="✅ SUCCESS: ${env.JOB_NAME}"
                        """
                    }
                    failure {
                        sh """
                        curl -s -X POST https://api.telegram.org/bot<8688912458:AAGb3weBLWUCXoD5yamTpXiTz8PPbYwe-08>/sendMessage \
                        -d chat_id=<8688912458> \
                        -d text="❌ FAILED: ${env.JOB_NAME}"
                        """
                    }
    }
}
