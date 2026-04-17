pipeline {
    agent any

    tools {
        maven 'Maven 3'
    }

    stages {
        stage('Build & Test') {
            steps {
                sh 'mvn clean test'
            }
        }
    }
}