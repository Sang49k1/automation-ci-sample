pipeline {
triggers {
        pollSCM('* * * * *') // mỗi phút check
    }

    agent any

    tools {
        maven 'Maven 3'
    }

    options {
        timestamps()
        disableConcurrentBuilds()
        buildDiscarder(logRotator(numToKeepStr: '20'))
        skipDefaultCheckout(true)
    }

    parameters {
        string(
            name: 'SELENIUM_GRID_URL',
            defaultValue: 'http://host.docker.internal:4444/wd/hub',
            description: 'Selenium Grid URL used by UI tests'
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
            steps {
                sh "mvn ${MAVEN_ARGS} test -Dselenium.grid.url=${params.SELENIUM_GRID_URL}"
            }
        }
    }

    post {
        always {
            junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
            archiveArtifacts allowEmptyArchive: true, artifacts: 'target/surefire-reports/**'
        }
    }
}
