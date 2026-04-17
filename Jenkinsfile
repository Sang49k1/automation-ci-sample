pipeline {
    agent any

    stages {

        stage('Checkout') {
            steps {
                git 'https://github.com/Sang49k1/automation-ci-sample.git'
            }
        }

        stage('Build & Test') {
            steps {
                sh 'mvn clean test'
            }
        }
    }
}