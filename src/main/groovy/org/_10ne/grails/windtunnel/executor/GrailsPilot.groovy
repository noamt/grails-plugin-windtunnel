package org._10ne.grails.windtunnel.executor

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
