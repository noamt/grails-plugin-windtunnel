package org._10ne.grails.windtunnel.executor

import com.google.inject.Inject
import org._10ne.grails.windtunnel.model.FlightPlan
import org._10ne.grails.windtunnel.pilot.GrailsConfigurationInjector
import org._10ne.grails.windtunnel.pilot.GrailsPilot
import org._10ne.grails.windtunnel.pilot.GrailsPluginDependencyInjector

import java.nio.file.Path

/**
 * @author Noam Y. Tenne.
 */
class DefaultFlightPlanExecutor implements FlightPlanExecutor {

    @Inject
    FlightPlan plan

    @Inject
    GrailsPilot pilot

    GrailsConfigurationInjector grailsConfigurationInjector = new GrailsConfigurationInjector()
    GrailsPluginDependencyInjector grailsPluginDependencyInjector = new GrailsPluginDependencyInjector()

    def execute() {
        Path appPath = pilot.createApp()

        if (plan.grailsConfig) {
            def configPath = appPath.resolve('grails-app').resolve('conf').resolve('Config.groovy')
            grailsConfigurationInjector.addConfiguration(configPath, plan.grailsConfig)
        }

        def buildConfigPath = appPath.resolve('grails-app').resolve('conf').resolve('BuildConfig.groovy')
        grailsPluginDependencyInjector.addPluginDependency(buildConfigPath, plan.pluginSource)
        pilot.refreshDependencies()

        pilot.runApp()
    }
}