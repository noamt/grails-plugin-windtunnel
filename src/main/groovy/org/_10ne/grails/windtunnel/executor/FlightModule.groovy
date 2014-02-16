package org._10ne.grails.windtunnel.executor

import com.google.inject.AbstractModule

/**
 * Created by ophirh on 2/16/14.
 */
class FlightModule extends AbstractModule{
    @Override
    protected void configure() {
        bind(GrailsPilot.class).to(RealGrailsPilot.class);
    }
}
