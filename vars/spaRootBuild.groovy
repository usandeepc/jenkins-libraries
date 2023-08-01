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
    def response = httpRequest authentication: 'nexusrepositorycreds', url: "http://ec2-54-189-118-149.us-west-2.compute.amazonaws.com:8081/service/rest/v1/search?repository=ria-spa-repo&group=/epm-spa-integration&name=epm-spa-integration/epm-integration-2023.07*"
    println("Response: ${response.content}")
    def json = readJSON text: response.content
    def latestBuildNumber = 0
    for (item in json.items) {
        def path = item.assets[0].path
        def buildNumber = path =~ /[0-9]+\.[0-9]+\.[0-9]+/
        if (buildNumber) {
            def extractedBuildNumber = buildNumber[0]
            def numericBuildNumber = extractedBuildNumber.tokenize('.').collect { it.toInteger() }.join('')
            if (numericBuildNumber.toInteger() > latestBuildNumber) {
                latestBuildNumber = numericBuildNumber.toInteger()
            }
        }
    }
    println("Response: ${latestBuildNumber}")
    
}
