package org._10ne.grails.windtunnel.executor

import com.google.inject.Guice
import com.google.inject.Injector
import org._10ne.grails.windtunnel.model.FlightPlan

import java.nio.file.Path

/**
 * @author Noam Y. Tenne.
 */
class FlightPlanExecutor {

    FlightPlan plan

    def execute() {
        Injector injector = Guice.createInjector(new FlightModule());
        GrailsPilot pilot = injector.getInstance(GrailsPilot.class);
        pilot.init(plan)
        Path app = pilot.createApp()
//        def testDir = Files.createTempDirectory('tests')
//        def tempEnvFile = Files.createTempFile('temp', 'env').toFile()

//        def executor = new DefaultExecutor()

//        def commandLine = new CommandLine("bash -c source ${tempEnvFile.absolutePath} ; source ${System.getProperty('user.home')}/.gvm/bin/gvm-init.sh ; gvm use grails ${plan.grailsVersion}")

//        System.getenv().each {key, value ->
//            tempEnvFile << "$key=\"$value\"\n"
//        }
//        Process process = "bash -c 'source ${System.getProperty('user.home')}/.gvm/bin/gvm-init.sh' ; gvm use grails ${plan.grailsVersion}".execute([], testDir.toFile())
//        Process process = ['bash', '-c', "source ${tempEnvFile.absolutePath} ; source ${System.getProperty('user.home')}/.gvm/bin/gvm-init.sh ; gvm use grails ${plan.grailsVersion}"].execute([], testDir.toFile())
//        def standardOutput = new StringWriter()
//        def errorOutput = new StringWriter()

//        process.consumeProcessOutput(standardOutput, errorOutput)

//        def errorOut = errorOutput.toString()
//        if (!errorOut.isEmpty()) {
//            throw new Exception("Error selecting Grails version ${plan.grailsVersion} via GVM: ${errorOut}")
//        }

//        def standardOut = standardOutput.toString()
//        if (standardOut.contains('Using')) {
//            println 'Great success!'
//        } else if (standardOut.contains('not installed')) {
//            process.outputStream.write('y'.bytes)
//            process.outputStream.write('\r'.bytes)
//            process.outputStream.flush()
//        }

        true
    }
}
