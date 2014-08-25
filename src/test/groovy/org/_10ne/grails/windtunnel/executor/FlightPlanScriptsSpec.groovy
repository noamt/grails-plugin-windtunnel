package org._10ne.grails.windtunnel.executor

import spock.lang.Specification

import java.nio.file.Files

/**
 * @author Noam Y. Tenne
 */
class FlightPlanScriptsSpec extends Specification {

    def 'Evaluate a flight plan script'() {
        setup:
        def temp = Files.createTempFile('flight', 'plan')
        temp.withWriterAppend {
            it << '''
                usingGrails '2.1.1'
                testPlugin artifactId: 'artifact', version: '1.0'
                at '/x/y/z/momo'
            '''
        }
        def scripts = new FlightPlanScripts()

        when:
        def flightPlan = scripts.evaluate(temp)

        then:
        flightPlan.grailsVersion == '2.1.1'
        flightPlan.testDirectory == '/x/y/z/momo'
        !flightPlan.grailsConfig
        flightPlan.pluginSource.artifactId == 'artifact'
        flightPlan.pluginSource.version == '1.0'
    }
}
