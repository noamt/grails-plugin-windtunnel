package org._10ne.grails.windtunnel.executor

import org._10ne.grails.windtunnel.model.FlightPlan
import org._10ne.grails.windtunnel.model.FlightPlanScript
import org.codehaus.groovy.control.CompilerConfiguration

import java.nio.file.Path

/**
 * @author Noam Y. Tenne.
 */
class FlightPlanScripts {

    FlightPlan evaluate(Path scriptFile) {
        def compilerConfiguration = new CompilerConfiguration()
        compilerConfiguration.scriptBaseClass = FlightPlanScript.name

        def flight = new FlightPlan()
        def binding = new Binding(flight: flight)

        def shell = new GroovyShell(this.class.classLoader, binding, compilerConfiguration)

        shell.evaluate(scriptFile.toFile())

        flight
    }
}
