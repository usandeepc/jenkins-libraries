def call(Map config = [:]) {

    def nexusUrl = "http://ec2-54-189-118-149.us-west-2.compute.amazonaws.com:8081/service/rest/v1/search?repository=ria-spa-repo"
    def releaseBranch = config.release_branch
    def response = httpRequest authentication: 'nexusrepositorycreds', url: "${nexusUrl}&group=/epm-spa-integration&name=epm-spa-integration/epm-integration-release-${releaseBranch}*"
    def json = readJSON text: response.content
    def latestBuildNumber = 0
    for (item in json.items) {
        def path = item.assets[0].path
        def VersionStringRegex = path =~ /[0-9]+\.[0-9]+\.[0-9]+/
        def extractedReleaseNumber = VersionStringRegex[0]
        println("extractedReleaseNumber: ${extractedReleaseNumber}")
        def buildNumber = extractedReleaseNumber.tokenize('.')[-1]
        println("buildNumber: ${buildNumber}")
        if (buildNumber) {
            if (buildNumber.toInteger() > latestBuildNumber) {
                latestBuildNumber = buildNumber.toInteger()
            }
        }
    }
    println("LatestBuildNo: ${latestBuildNumber}")
    
}
