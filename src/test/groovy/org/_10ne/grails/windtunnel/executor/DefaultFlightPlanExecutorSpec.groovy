package org._10ne.grails.windtunnel.executor

import org._10ne.grails.windtunnel.model.FlightPlan
import org._10ne.grails.windtunnel.model.PluginSource
import org._10ne.grails.windtunnel.pilot.GrailsConfigurationInjector
import org._10ne.grails.windtunnel.pilot.GrailsPilot
import org._10ne.grails.windtunnel.pilot.GrailsPluginDependencyInjector
import spock.lang.Specification

import java.nio.file.Path
import java.nio.file.Paths

/**
 * @author Noam Y. Tenne
 */
class DefaultFlightPlanExecutorSpec extends Specification {

    def 'Execute a flight plan'() {
        def flightPlanExecutor = new DefaultFlightPlanExecutor()

        def configurationInjector = Mock(GrailsConfigurationInjector)
        flightPlanExecutor.grailsConfigurationInjector = configurationInjector

        def dependencyInjector = Mock(GrailsPluginDependencyInjector)
        flightPlanExecutor.grailsPluginDependencyInjector = dependencyInjector

        def grailsPilot = Mock(GrailsPilot)
        flightPlanExecutor.pilot = grailsPilot

        def plan = Mock(FlightPlan) {
            _ * getGrailsConfig() >> { return {} }
            _ * getPluginSource() >> { new PluginSource() }
        }
        flightPlanExecutor.plan = plan

        when:
        flightPlanExecutor.execute()

        then:
        1 * grailsPilot.createApp() >> Paths.get('/tmp')
        1 * configurationInjector.addConfiguration(Paths.get('/tmp/grails-app/conf/Config.groovy'), _ as Closure)
        1 * dependencyInjector.addPluginDependency(Paths.get('/tmp/grails-app/conf/BuildConfig.groovy'), _ as PluginSource)
        1 * grailsPilot.refreshDependencies()
        1 * grailsPilot.runApp()
    }
}