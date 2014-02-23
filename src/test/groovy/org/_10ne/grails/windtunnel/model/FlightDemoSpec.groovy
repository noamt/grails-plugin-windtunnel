package org._10ne.grails.windtunnel.model

import com.google.inject.Guice
import com.google.inject.Injector
import org._10ne.grails.windtunnel.executor.FlightModule
import org._10ne.grails.windtunnel.executor.GrailsPilot
import org.codehaus.groovy.control.CompilerConfiguration
import spock.lang.Specification

import java.nio.file.Files
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

    }

    def 'Run app'() {
        setup:
        Injector injector = Guice.createInjector(new FlightModule())

        def flight = new FlightPlan()
        flight.grailsVersion = '2.1.4'
        flight.testDirectory = Files.createTempDirectory('testdir').toString()

        GrailsPilot pilot = injector.getInstance(GrailsPilot)

        pilot.init(flight)
        when:
        Path appPath = pilot.createApp()

        then:
        appPath.toString() == "${flight.testDirectory}/windtunnel-app\n"

        expect:
        pilot.refreshDependencies()
        pilot.runApp()

    }
}