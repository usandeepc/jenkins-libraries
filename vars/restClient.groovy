@Grab(group='org.codehaus.groovy.modules.http-builder', module='http-builder', version='0.7.1')

import groovyx.net.http.HTTPBuilder

def call(String url, String method, String username, String password, Map<String, String> headers = [:], Map<String, String> queryParams = [:], String requestBody = null) {
    def http = new HTTPBuilder(url)

    http.request(method, ContentType.JSON) { req ->
        headers.each { key, value ->
            req.headers[key] = value
        }

        // Set Basic Authentication credentials
        def authString = "${username}:${password}".bytes.encodeBase64().toString()
        req.headers['Authorization'] = "Basic ${authString}"

        queryParams.each { key, value ->
            req.uri.query = "${req.uri.query}&${key}=${value}"
        }

        if (requestBody) {
            req.body = requestBody
        }

        response.success = { resp, json ->
            println "Request successful!"
            println "Response data: $json"
        }

        response.failure = { resp ->
            println "Request failed: ${resp.statusLine}"
        }
    }
}
