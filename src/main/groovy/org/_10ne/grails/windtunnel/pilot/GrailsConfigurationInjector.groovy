package org._10ne.grails.windtunnel.pilot

import java.nio.file.Path

/**
 * @author Noam Y. Tenne.
 */
class GrailsConfigurationInjector {

    void addConfiguration(Path grailsConfigFile, Closure configClosure) {
        def configClosureScript = new ClosureScript(closure: configClosure)
        def slurper = new ConfigSlurper().parse(configClosureScript)
        grailsConfigFile.toFile().withWriterAppend { Writer writer ->
            slurper.writeTo(writer)
        }
    }

    private class ClosureScript extends Script {
        Closure closure

        def run() {
            closure.resolveStrategy = Closure.DELEGATE_FIRST
            closure.delegate = this
            closure.call()
        }
    }
}
