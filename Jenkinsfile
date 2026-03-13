pipeline {
  agent any

  tools {
    jdk 'jdk-21'
  }

  stages {
    stage('Build') {
      steps {
        sh './gradlew assemble --no-daemon'
      }
    }

    stage('Test') {
      steps {
        sh './gradlew test --no-daemon'
      }
    }

    stage('Deploy') {
      steps {
        sh 'docker compose -f docker-compose-calorie-tracker-backend.yml down'
        sh 'docker image prune -af'
        sh 'docker compose -f docker-compose-calorie-tracker-backend.yml up --build -d'
      }
    }
  }
}
