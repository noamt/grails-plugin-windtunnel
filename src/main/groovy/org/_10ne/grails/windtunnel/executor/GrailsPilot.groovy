package org._10ne.grails.windtunnel.executor

import org._10ne.grails.windtunnel.model.FlightPlan

import java.nio.file.Path

/**
 * @author Noam Y. Tenne.
 */
interface GrailsPilot {

    void init(FlightPlan plan)
    Path createApp()
    void refreshDependencies()
    void runApp()
}
