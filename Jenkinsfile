@Library('my-library')


pipeline {
    agent any 
    stages {
        stage('Build') { 
            steps {
                script {
                  def z = new com.sandeep.myDocker()
                      z.dockerBuild('my-image')
                }
            }
        }

    }
}
