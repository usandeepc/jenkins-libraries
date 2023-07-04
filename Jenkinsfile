@Library('my-library@main') _
def z = new com.sandeep.myDocker()
pipeline {
    agent any 
    stages {
        stage('Docker Build') { 
            steps {
                script {
                      z.dockerBuild('my-image')
                }
            }
        }
        stage('Print env') { 
            steps {
                script {
                      z.greetName('sandeep')
                }
            }
        }
    }
}
