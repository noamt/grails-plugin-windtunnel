package org._10ne.grails.windtunnel.pilot

import com.google.inject.Inject
import org._10ne.sgvm.Gvm

import java.nio.file.Path

/**
 * @author Noam Y. Tenne.
 */
class GvmGrailsPilot extends BaseGrailsPilot {

    @Inject
    @Override
    void init() {
        Path grailsInstallation = Gvm.use().grails(version: plan.grailsVersion)
        init(grailsInstallation)
    }
}
