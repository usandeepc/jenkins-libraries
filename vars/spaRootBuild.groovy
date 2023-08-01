@Grab('org.codehaus.groovy.modules.http-builder:http-builder:0.7.1')
import groovy.json.JsonSlurper
import groovyx.net.http.HttpBuilder
import groovyx.net.http.AuthType
def call(Map config = [:]) {

    def nexusUrl = "http://ec2-54-189-118-149.us-west-2.compute.amazonaws.com:8081/repository/ria-spa-repo/"
    def artifactVersion = config.artifact_version

    def repo_map = [
        'epm-spa-admin' : "pnm-admin",
        'epm-spa-customer' : "epm-customer",
        'epm-spa-dealmngt' : "epm-deal-management",
        'epm-spa-integration' : "epm-integration",
        'epm-spa-invoice' : "epm-invoice",
        'epm-spa-pricing' : "epm-pricelist",
        'epm-spa-product' : "epm-product",
        'epm-spa-reporting-lite' : "epm-reporting-lite",
        'epm-spa-dashboard' : "epm-dashboard",
    ]
    sh 'pwd'
    withCredentials([usernamePassword(credentialsId: 'nexusrepositorycreds', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
      utitlities_url = """${nexusUrl}service/rest/v1/search?repository=ria-spa-repo&group=/epm-spa-utilities&name=epm-spa-utilities/epm-utilities-2023.07*"""
      def http = new HttpBuilder(url)
      http.auth.basic(${USERNAME}, ${PASSWORD}, AuthType.BASIC)
      def response = http.get() { resp ->
        // Check if the response is successful
        if (resp.statusLine.statusCode == 200) {
            return new JsonSlurper().parseText(resp.entity.content.text)
        } else {
            throw new RuntimeException("Failed to get JSON data. Status: ${resp.statusLine}")
        }
      }
      def buildNumbers = response.items.collect { item ->
        def path = item.assets[0].path
        def buildNumber = (path =~ /.*release-2023\.07\.(\d+)\.tar\.gz/)?.group(1)
        return buildNumber ? buildNumber.toInteger() : null
      }
      buildNumbers.sort()
      utilities_version = buildNumbers.last()
      sh """
      echo ${utilities_version}
      //curl -u ${USERNAME}:${PASSWORD} ${nexusUrl}/epm-spa-utilities/epm-utilities-\$utilities_version.tar.gz -o epm-utilities.tar.gz;
      mkdir -p epm-spa-utilities/dist;
      //tar -xvzf epm-utilities.tar.gz -C epm-spa-utilities/dist;
      """
      repo_map.each{k,v->
        sh """
        echo $component_version
        //curl -u ${USERNAME}:${PASSWORD} ${nexusUrl}/${k}/${v}-\$component_version.tar.gz -o ${v}.tar.gz;
        mkdir -p ${k}/dist/release;
        //tar -xvzf ${v}.tar.gz -C ${k}/dist/release;
        """
      }

    }
    nodejs(nodeJSInstallationName: 'node-16') {
        
        //sh 'npm install'
        //sh 'npm run release'
    }
    
    
    sh 'ls'
}
