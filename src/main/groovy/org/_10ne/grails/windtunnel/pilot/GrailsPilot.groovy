package org._10ne.grails.windtunnel.pilot

import com.google.inject.Inject

import java.nio.file.Path

/**
 * @author Noam Y. Tenne.
 */
interface GrailsPilot {

    @Inject
    void init()

    Path createApp()
    void refreshDependencies()
    void runApp()
}
