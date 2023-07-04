@Library('my-library@main') _

pipeline {
    agent any 
    stages {
        stage('Print env') { 
            steps {
                script {
                    def z = new com.sandeep.myDocker()
                    z.greetName('sandeep')
                }
            }
        }
        stage('Docker Build') { 
            steps {
                script {
                      z.dockerBuild('my-image')
                }
            }
        }

    }
}
