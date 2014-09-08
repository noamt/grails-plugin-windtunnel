package org._10ne.grails.windtunnel.executor

import org._10ne.grails.windtunnel.model.PluginSource

import java.nio.file.Path
import java.nio.file.Paths

/**
 * @author Noam Y. Tenne.
 */
abstract class FlightPlanScript extends Script {

    def usingGrails(String grailsVersion) {
        this.binding.flight.grailsVersion = grailsVersion
    }

    def testPlugin(Map<Object, Object> pluginSource) {
        this.binding.flight.pluginSource = new PluginSource(pluginSource)
    }

    def at(String testDirectoryAbsolutePath) {
        this.binding.flight.testDirectory = Paths.get(testDirectoryAbsolutePath)
    }

    def at(Path testDirectory) {
        this.binding.flight.testDirectory = testDirectory
    }

    def at(File testDirectory) {
        this.binding.flight.testDirectory = testDirectory.toPath()
    }

    def withConfig(Closure grailsConfig) {
        this.binding.flight.grailsConfig = grailsConfig
    }
}
