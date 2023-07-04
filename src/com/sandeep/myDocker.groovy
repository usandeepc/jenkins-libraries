package com.sandeep

def dockerBuild(name) {
  docker.build("${name}:0.1")
}

def greetName(name)
  printenv
  echo "${name}"

return this
