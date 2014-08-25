package org._10ne.grails.windtunnel.pilot

import spock.lang.Specification

import java.nio.file.Files

/**
 * @author Noam Y. Tenne
 */
class GrailsConfigurationInjectorSpec extends Specification {

    def 'Write to a configuration file'() {
        setup:
        def file = Files.createTempFile('temp', 'config')

        GrailsConfigurationInjector injector = new GrailsConfigurationInjector()

        when:
        injector.addConfiguration(file) {
            config {
                key = 'value'
            }
        }

        then:
        file.text == 'config.key=\'value\'\n'
    }
}