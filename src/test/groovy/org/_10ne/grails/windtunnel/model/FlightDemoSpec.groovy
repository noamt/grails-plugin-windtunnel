package org._10ne.grails.windtunnel.model

import org.codehaus.groovy.control.CompilerConfiguration
import spock.lang.Specification

/**
 * @author Noam Y. Tenne.
 */
class FlightDemoSpec extends Specification {

    def 'Test flight'() {
        setup:
        def compilerConfiguration = new CompilerConfiguration()
        compilerConfiguration.scriptBaseClass = FlightPlanScript.name

        def flight = new FlightPlan()
        def binding = new Binding(flight: flight)

        def shell = new GroovyShell(this.class.classLoader, binding, compilerConfiguration)

        def flightPlan = '''
usingGrails('2.1.1')
testPlugin('/x/y/z')
at('/x/y/z/momo')
'''
        when:
        shell.evaluate(flightPlan)

        then:
        flight.grailsVersion == '2.1.1'
        flight.plugin == '/x/y/z'
        flight.testDirectory == '/x/y/z/momo'

//        and:
//        new FlightPlanExecutor(plan: flight).execute()
    }


}
