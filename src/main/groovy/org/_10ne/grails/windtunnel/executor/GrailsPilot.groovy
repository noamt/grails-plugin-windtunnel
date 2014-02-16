package org._10ne.grails.windtunnel.executor

import org._10ne.grails.windtunnel.model.FlightPlan

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * @author Noam Y. Tenne.
 */
class GrailsPilot {
    private FlightPlan plan
    private Path grailsInstallation
    private Path grailsExec

    GrailsPilot(FlightPlan plan) {
        this.plan = plan
        grailsInstallation = Paths.get(System.getProperty('user.home'), '.gvm', 'grails', plan.grailsVersion)
        if (Files.notExists(grailsInstallation)) {
            grailsInstallation = Paths.get(plan.alternativeGrailsDir)
            if (Files.notExists(Paths.get(plan.alternativeGrailsDir))){
                throw new Exception("Unable to find Grails installation at: ${grailsInstallation}")
            }
        }
        if (!Files.isReadable(grailsInstallation)) {
            throw new Exception("Unable to access Grails installation at: ${grailsInstallation}")
        }
        grailsExec = grailsInstallation.resolve('bin').resolve('grails')
        if (!Files.isExecutable(grailsExec)) {
            throw new Exception("Unable to find Grails executable at: ${grailsExec}")
        }
    }

    Path createApp() {
        //grails non interactive mode
        //make sure that we are running from the correct place
//        def commandOutput = new StringBuffer()
//        def commandError = new StringBuffer()
//        Process createGrailsWindtunnelApp = "${grailsExec} create-app windtunnel-app".execute(["JAVA_HOME=${System.getProperty('java.home')}"], new File(plan.testDirectory))
//        createGrailsWindtunnelApp.waitFor();
//        createGrailsWindtunnelApp.consumeProcessOutput(commandOutput, commandError)
//        createGrailsWindtunnelApp.consumeProcessErrorStream(commandError)
//        println 'create-app output' + commandOutput
//        println 'create-apperror output: ' + commandError
//
//        int index = commandOutput.indexOf('Created Grails Application at')
//        Paths.get(commandOutput.substring(index + 30))

        def commandOutput = runCommand("${grailsExec} create-app windtunnel-app", new File(plan.testDirectory))
        int index = commandOutput.indexOf('Created Grails Application at')
        Paths.get(commandOutput.substring(index + 30))

    }


    static def runCommand(String command, File dir=null) {
        //grails non interactive mode
        //make sure that we are running from the correct place
        def commandOutput = new StringBuffer()
        def commandError = new StringBuffer()
        Process createGrailsWindtunnelApp = command.execute(["JAVA_HOME=${System.getProperty('java.home')}"], dir)
        createGrailsWindtunnelApp.waitFor();
        createGrailsWindtunnelApp.consumeProcessOutput(commandOutput, commandError)
        createGrailsWindtunnelApp.consumeProcessErrorStream(commandError)
        println 'comman output' + commandOutput
        println 'command error output: ' + commandError

        commandOutput
//        int index = commandOutput.indexOf('Created Grails Application at')
//        Paths.get(commandOutput.substring(index + 30))

    }

}
