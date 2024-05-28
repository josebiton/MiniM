pipeline {
    agent any

    environment {
        ANDROID_HOME = 'C:\Users\biton\AppData\Local\Android\Sdk' // Asegúrate de que esta ruta sea correcta
    }

    stages {
        stage('Checkout') {
            steps {
                checkout([$class: 'GitSCM', branches: [[name: '*/main']], extensions: [[$class: 'RelativeTargetDirectory', relativeTargetDir: 'checkout']]])
            }
        }

        stage('SonarQube analysis') {
            steps {
                dir('checkout') {
                    withSonarQubeEnv('SonarQube') {
                        bat './gradlew.bat --build-cache clean assembleDebug sonarqube'
                    }
                }
            }
        }
    }

    post {
        always {
            junit 'checkout/app/build/test-results/**/*.xml'
            archiveArtifacts 'checkout/app/build/outputs/**/*.apk'
            deleteDir()
        }
    }
}
