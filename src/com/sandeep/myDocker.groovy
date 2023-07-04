package com.sandeep

def dockerBuild(name) {
  docker.build("${name}:0.1")
}

def greetName(name) {
  sh "printenv"
  sh 'echo "${name}"'
}
return this
