package org._10ne.grails.windtunnel.pilot

import com.google.inject.Inject
import groovy.util.logging.Slf4j
import org._10ne.grails.windtunnel.model.FlightPlan

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * @author Ophir Hordan
 */
@Slf4j
abstract class BaseGrailsPilot implements GrailsPilot {

    @Inject
    FlightPlan plan

    private Path grailsExec
    private appName
    private static CREATE_APP_COMMAND = 'create-app'
    private static REFRESH_DEPENDENCIES_COMMAND = ' refresh-dependencies'
    private static RUN_APP_COMMAND = 'run-app'

    protected void init(Path grailsInstallation) {
        grailsExec = grailsInstallation.resolve('bin').resolve('grails')
        if (!Files.isExecutable(grailsExec)) {
            throw new Exception("Unable to find Grails executable at: $grailsExec")
        }

        appName = "test-app-${System.currentTimeMillis()}"
    }

    Path createApp() {
        def validator = { String output ->
            output.contains('Created Grails Application at')
        }
        runCommand([grailsExec, CREATE_APP_COMMAND, appName, '--non-interactive'], validator, plan.testDirectory)
        plan.testDirectory.resolve(appName)
    }

    void refreshDependencies() {
        runRefreshDependenciesCommand([grailsExec, REFRESH_DEPENDENCIES_COMMAND, '--non-interactive'],
                plan.testDirectory.resolve(appName))
    }

    void runApp() {
        runStartAppCommand([grailsExec, RUN_APP_COMMAND, '--non-interactive'], plan.testDirectory.resolve(appName)) }

    private void runCommand(List<String> command, Closure validator, Path dir) {
        def commandOutput = new StringBuilder()
        def commandError = new StringBuilder()
        Process windtunnelAppProcess = command.execute([javaHomeEnvironmentVariable()], dir.toFile())
        log.info "Running command: $command"

        windtunnelAppProcess.waitForProcessOutput(commandOutput, commandError)
        if (!validator.call(commandOutput.toString())) {
            throw new Exception("Error running: ${command}, command output: ${commandOutput}")
        }
        printOutput(commandOutput, commandError)
        commandOutput
    }

    private void runStartAppCommand(List<String> command, Path dir) {
        Process createGrailsWindtunnelApp = command.execute([javaHomeEnvironmentVariable()], dir.toFile())
        log.info "Running command: $command"
        def out = createGrailsWindtunnelApp.getInputStream()

        boolean stop = false
        try {
            while (!stop) {
                byte[] bytes = new byte[out.available()]
                out.read(bytes)
                String read = new String(bytes)
                if (read) {
                    log.info read
                    if (read.contains('Server running')) {
                        stop = true
                    }
                    if (read.contains('Error')) {
                        throw new Exception(read)
                    }
                }
            }
        } finally {
            out.close()
        }
        createGrailsWindtunnelApp.destroy()
    }

    private void runRefreshDependenciesCommand(List<String> command, Path dir) {
        Process createGrailsWindtunnelApp = command.execute([javaHomeEnvironmentVariable()], dir.toFile())
        log.info "Running command: $command"
        def out = createGrailsWindtunnelApp.getInputStream()

        boolean stop = false
        try {
            while (!stop) {
                byte[] bytes = new byte[out.available()]
                out.read(bytes)
                String read = new String(bytes)
                if (read) {
                    log.info read
                    if (read.contains('Dependencies refreshed')) {
                        stop = true
                    }
                    if (read.contains('Error')) {
                        throw new Exception(read)
                    }
                }
            }
        } finally {
            out.close()
        }
        createGrailsWindtunnelApp.destroy()
    }

    private void printOutput(commandOutput, commandError) {
        log.info "Comman output: $commandOutput"
        if (commandError) {
            log.info "Command error output: $commandError"
        }
    }

    private String javaHomeEnvironmentVariable() {
        if (javaHomeFromSystemEnv()) {
            return "JAVA_HOME=${javaHomeFromSystemEnv()}"
        }
        def javaHomeProperty = System.properties['java.home']
        javaHomeProperty = javaHomeProperty.substring(0, javaHomeProperty.indexOf('jre') - 1)
        "JAVA_HOME=${javaHomeProperty}"
    }

    private String javaHomeFromSystemEnv() {
        System.getenv().get('JAVA_HOME')
    }
}
