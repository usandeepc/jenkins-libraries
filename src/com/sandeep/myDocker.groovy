package com.sandeep

def dockerBuild(name) {
  docker.build("${name}:0.1")
}

return this
