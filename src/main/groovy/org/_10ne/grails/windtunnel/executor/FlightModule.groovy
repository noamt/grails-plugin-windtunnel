package org._10ne.grails.windtunnel.executor

import com.google.inject.AbstractModule
import org._10ne.grails.windtunnel.model.FlightPlan
import org._10ne.grails.windtunnel.pilot.GrailsPilot
import org._10ne.grails.windtunnel.pilot.GvmGrailsPilot

/**
 * Created by ophirh on 2/16/14.
 */
class FlightModule extends AbstractModule {

    private FlightPlan plan

    FlightModule(FlightPlan plan) {
        this.plan = plan
    }

    @Override
    protected void configure() {
        bind(FlightPlan).toInstance(plan)
        bind(GrailsPilot).to(GvmGrailsPilot)
        bind(FlightPlanExecutor).to(DefaultFlightPlanExecutor)
    }
}
