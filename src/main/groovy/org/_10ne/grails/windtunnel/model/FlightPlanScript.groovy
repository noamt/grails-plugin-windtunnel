package org._10ne.grails.windtunnel.model

/**
 * @author Noam Y. Tenne.
 */
abstract class FlightPlanScript extends Script {

    def usingGrails(String grailsVersion) {
        this.binding.flight.grailsVersion = grailsVersion
    }

    def testPlugin(String pluginAbsolutePath) {
        this.binding.flight.plugin = pluginAbsolutePath
    }

    def at(String testDirectoryAbsolutePath) {
        this.binding.flight.testDirectory = testDirectoryAbsolutePath
    }
}
