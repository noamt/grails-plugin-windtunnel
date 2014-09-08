package org._10ne.grails.windtunnel.model

import java.nio.file.Path

/**
 * @author Noam Y. Tenne.
 */
class FlightPlan {

    String grailsVersion
    Path testDirectory

    PluginSource pluginSource

    Closure grailsConfig
}