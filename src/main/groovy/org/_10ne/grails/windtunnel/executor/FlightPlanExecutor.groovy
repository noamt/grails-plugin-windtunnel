package org._10ne.grails.windtunnel.executor

import com.google.inject.Guice
import com.google.inject.Injector
import org._10ne.grails.windtunnel.model.FlightPlan

import java.nio.file.Path

/**
 * @author Noam Y. Tenne.
 */
class FlightPlanExecutor {

    FlightPlan plan

    def execute() {
        Injector injector = Guice.createInjector(new FlightModule());
        GrailsPilot pilot = injector.getInstance(GrailsPilot.class);
        pilot.init(plan)
        Path appPath = pilot.createApp()
        GrailsPluginDependencyInjector grailsPluginDependencyInjector = new GrailsPluginDependencyInjector()
        def buildConfigPath = appPath.resolve('grails-app').resolve('conf').resolve('BuildConfig.groovy')
        grailsPluginDependencyInjector.addPluginDependency(buildConfigPath, plan.pluginData)
        pilot.refreshDependencies()

        pilot.runApp()
    }
}
