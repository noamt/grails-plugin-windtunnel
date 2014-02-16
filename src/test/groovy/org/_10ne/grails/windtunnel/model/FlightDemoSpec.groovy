package org._10ne.grails.windtunnel.model

import org._10ne.grails.windtunnel.executor.GrailsPilot
import org.codehaus.groovy.control.CompilerConfiguration
import spock.lang.IgnoreRest
import spock.lang.Specification

import java.nio.file.Path

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

        def flightScript = '''
                usingGrails('2.1.1')
                testPlugin('/x/y/z')
                at('/x/y/z/momo')
            '''
        when:
        shell.evaluate(flightScript)

        then:
        flight.grailsVersion == '2.1.1'
        flight.plugin == '/x/y/z'
        flight.testDirectory == '/x/y/z/momo'

//        and:
//        new FlightPlanExecutor(plan: flight).execute()
    }


    @IgnoreRest
    def 'Run app'() {
        setup:
//        def compilerConfiguration = new CompilerConfiguration()
//        compilerConfiguration.scriptBaseClass = FlightPlanScript.name

        def flight = new FlightPlan()
        flight.grailsVersion = '2.1.4'
        flight.testDirectory = '/home/liatn/test-grails'

        GrailsPilot pilot = new GrailsPilot(flight)

        when:
        Path appPath = pilot.createApp()

        then:
        appPath.toString() == "${flight.testDirectory}/windtunnel-app\n"

    }


    def 'Run app win'() {
        setup:
//        def compilerConfiguration = new CompilerConfiguration()
//        compilerConfiguration.scriptBaseClass = FlightPlanScript.name

        def flight = new FlightPlan()
        flight.grailsVersion = '2.1.4'
        flight.testDirectory = 'c:\\test-grails'
        flight.alternativeGrailsDir = 'C:\\grails-2.1.4'

        GrailsPilot pilot = new GrailsPilot(flight)

        expect:
        pilot.createApp()

    }

}
