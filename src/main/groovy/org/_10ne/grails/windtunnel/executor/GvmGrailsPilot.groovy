package org._10ne.grails.windtunnel.executor

import com.google.inject.Inject

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * @author Noam Y. Tenne.
 */
class GvmGrailsPilot extends BaseGrailsPilot {

    @Inject
    @Override
    void init() {
        Path grailsInstallation = Paths.get(System.getProperty('user.home'), '.gvm', 'grails', plan.grailsVersion)
        if (Files.notExists(grailsInstallation)) {
            throw new Exception("Unable to find Grails installation at: $grailsInstallation")
        }
        if (!Files.isReadable(grailsInstallation)) {
            throw new Exception("Unable to access Grails installation at: $grailsInstallation")
        }

        init(grailsInstallation)
    }
}
