pipeline {
    agent any

    environment {
        ANDROID_HOME = 'D:\\path\\to\\android-sdk' // Asegúrate de que esta ruta sea correcta
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
                        bat 'gradlew.bat sonar --warning-mode all -Dsonar.projectKey=ShopMarket -Dsonar.projectName="ShopMarket" -Dsonar.host.url=http://localhost:9000 -Dsonar.token=sqp_a9089595ff4aa93e25bcaae7f458a68fc6a58cc5'
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
