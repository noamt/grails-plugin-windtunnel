package org._10ne.grails.windtunnel.model

import com.google.inject.Guice
import com.google.inject.Injector
import org._10ne.grails.windtunnel.executor.FlightModule
import org._10ne.grails.windtunnel.pilot.GrailsPilot
import org.codehaus.groovy.control.CompilerConfiguration
import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

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
                usingGrails '2.1.1'
                testPlugin artifactId: 'artifact', version: '1.0'
                at '/x/y/z/momo'
            '''
        when:
        shell.evaluate(flightScript)

        then:
        flight.grailsVersion == '2.1.1'
        flight.pluginSource.artifactId == 'artifact'
        flight.pluginSource.version == '1.0'
        flight.testDirectory == '/x/y/z/momo'
    }

    def 'Run app'() {
        setup:
        def flight = new FlightPlan()
        flight.grailsVersion = '2.1.4'
        flight.testDirectory = Files.createTempDirectory('testdir').toString()
        def flightModule = new FlightModule(flight)
        Injector injector = Guice.createInjector(flightModule)

        GrailsPilot pilot = injector.getInstance(GrailsPilot)

        pilot.init()
        when:
        Path appPath = pilot.createApp()

        then:
        appPath.parent == Paths.get(flight.testDirectory)

        expect:
        pilot.refreshDependencies()
        pilot.runApp()

    }
}