package org._10ne.grails.windtunnel.model

/**
 * @author Noam Y. Tenne.
 */
abstract class FlightPlanScript extends Script {

    def usingGrails(String grailsVersion) {
        this.binding.flight.grailsVersion = grailsVersion
    }

    def testPlugin(String repositoryUrl, String groupId, String artifactId, String version) {
        this.binding.flight.pluginData.repositoryUrl = repositoryUrl
        this.binding.flight.pluginData.groupId = groupId
        this.binding.flight.pluginData.artifactId = artifactId
        this.binding.flight.pluginData.version = version
     }

    def at(String testDirectoryAbsolutePath) {
        this.binding.flight.testDirectory = testDirectoryAbsolutePath
    }
}
