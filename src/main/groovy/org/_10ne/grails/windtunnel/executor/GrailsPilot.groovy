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
    private static APP_NAME = 'windtunnel-app'

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

        def validator = {String output ->
            output.contains('Created Grails Application at')
        }

        def commandOutput = runCommand("${grailsExec} create-app ${APP_NAME}", validator,  new File(plan.testDirectory))
        int index = commandOutput.indexOf('Created Grails Application at')
        Paths.get(commandOutput.substring(index + 30))
    }

    void refreshDependencies() {
        def validator = {String output ->
            output.contains('Dependencies refreshed')
        }
        runCommand("${grailsExec} refresh-dependencies", validator, new File("${plan.testDirectory}${File.separator}${APP_NAME}"))
    }

    void runApp() {
        def validator = {String output ->
            output.contains('Server running')
        }
        runCommand("${grailsExec} run-app", validator, new File("${plan.testDirectory}${File.separator}${APP_NAME}"))
    }


    static def runCommand(String command, Closure validator, File dir=null) {
        //grails non interactive mode
        //make sure that we are running from the correct place
        def commandOutput = new StringBuilder()
        def commandError = new StringBuilder()
        Process createGrailsWindtunnelApp = command.execute(["JAVA_HOME=${System.getProperty('java.home')}"], dir)
        println("Running command: ${command}")
        if(!validator.call(commandOutput)){
            throw new Exception("Error running: ${command}, command output: ${commandOutput}")
        }
        createGrailsWindtunnelApp.waitFor();
        createGrailsWindtunnelApp.consumeProcessOutput(commandOutput, commandError)
        createGrailsWindtunnelApp.consumeProcessErrorStream(commandError)
        println "Comman output: ${commandOutput}"
        if(commandError){
            println "Command error output: ${commandError}"
        }
        commandOutput
    }

}
