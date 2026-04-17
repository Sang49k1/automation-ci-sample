pipeline {
    agent any

    stages {
        stage('Build & Test') {
            steps {
                sh 'mvn clean test'
            }
        }
    }
}