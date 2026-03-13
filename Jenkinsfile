pipeline {
  agent any

  stages {
    stage('Deploy') {
      steps {
        sh 'docker compose -f docker-compose-calorie-tracker-backend.yml down'
        sh 'docker image prune -af'
        sh 'docker compose -f docker-compose-calorie-tracker-backend.yml up --build -d'
      }
    }
  }
}
