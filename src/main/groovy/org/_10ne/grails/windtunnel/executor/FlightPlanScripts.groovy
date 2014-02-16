package org._10ne.grails.windtunnel.executor

import org._10ne.grails.windtunnel.model.FlightPlan
import org._10ne.grails.windtunnel.model.FlightPlanScript
import org.codehaus.groovy.control.CompilerConfiguration

/**
 * @author Noam Y. Tenne.
 */
class FlightPlanScripts {

    FlightPlan evaluate(File scriptFile) {
        def compilerConfiguration = new CompilerConfiguration()
        compilerConfiguration.scriptBaseClass = FlightPlanScript.name

        def flight = new FlightPlan()
        def binding = new Binding(flight: flight)

        def shell = new GroovyShell(this.class.classLoader, binding, compilerConfiguration)

        shell.evaluate(scriptFile)

        flight
    }
}
