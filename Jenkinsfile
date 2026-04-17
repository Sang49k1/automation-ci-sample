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
    }

    post {
        always {
            junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
            archiveArtifacts allowEmptyArchive: true, artifacts: 'target/surefire-reports/**'
        }
        cleanup {
            deleteDir()
        }
    }
}
