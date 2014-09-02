package org._10ne.grails.windtunnel.pilot

import com.google.inject.Inject
import groovy.util.logging.Slf4j
import org._10ne.sgvm.Gvm

import java.nio.file.Path

/**
 * @author Noam Y. Tenne.
 */
@Slf4j
class GvmGrailsPilot extends BaseGrailsPilot {

    @Inject
    @Override
    void init() {
        log.info "Using Grails version ${plan.grailsVersion} (via the GVM-SDK)"
        Path grailsInstallation = Gvm.use.grails(version: plan.grailsVersion, install: true)
        init(grailsInstallation)
    }
}
