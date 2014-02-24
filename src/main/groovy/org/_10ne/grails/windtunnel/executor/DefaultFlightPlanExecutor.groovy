package org._10ne.grails.windtunnel.executor

import com.google.inject.Inject
import org._10ne.grails.windtunnel.model.FlightPlan

import java.nio.file.Path

/**
 * @author Noam Y. Tenne.
 */
class DefaultFlightPlanExecutor implements FlightPlanExecutor {

    @Inject
    FlightPlan plan

    @Inject
    GrailsPilot pilot

    def execute() {
        Path appPath = pilot.createApp()
        GrailsPluginDependencyInjector grailsPluginDependencyInjector = new GrailsPluginDependencyInjector()
        def buildConfigPath = appPath.resolve('grails-app').resolve('conf').resolve('BuildConfig.groovy')
        grailsPluginDependencyInjector.addPluginDependency(buildConfigPath, plan.pluginSource)
        pilot.refreshDependencies()

        pilot.runApp()
    }
}
