pipeline {
    agent any

    environment {
        ANDROID_HOME = '/path/to/android-sdk'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout([$class: 'GitSCM', branches: [[name: '*/main']], extensions: [[$class: 'RelativeTargetDirectory', relativeTargetDir: 'checkout']]])
            }
        }

        stage('SonarQube analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    sh '/path/to/gradlew --build-cache clean assembleDebug sonarqube'
                }
            }
        }
    }

    post {
        always {
            junit 'app/build/test-results/**/*.xml'
            archiveArtifacts 'app/build/outputs/**/*.apk'
            deleteDir()
        }
    }
}
