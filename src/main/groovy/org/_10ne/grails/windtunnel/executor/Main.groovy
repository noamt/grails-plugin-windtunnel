package org._10ne.grails.windtunnel.executor

import com.google.inject.Guice
import com.google.inject.Injector

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * @author Noam Y. Tenne.
 */
class Main {

    public static void main(String[] args) {
        println 'Grails Plugin Windtunnel - A blackbox testing framework for Grails plugins\n'

        CliBuilder cliBuilder = new CliBuilder(usage: 'gpw <script>')
        cliBuilder.setFooter('https://github.com/noamt/grails-plugin-windtunnel')

        OptionAccessor optionAccessor = cliBuilder.parse(args)
        List<String> providedArguments = optionAccessor.arguments()
        if (providedArguments.empty) {
            cliBuilder.usage()
            System.exit(1)
        }

        String givenScriptPath = providedArguments.first()
        Path scriptPath = Paths.get(givenScriptPath)
        if (Files.notExists(scriptPath)) {
            throw new Exception("Unable to find flight plan at: $scriptPath")
        }
        if (!Files.isReadable(scriptPath)) {
            throw new Exception("Unable to read flight plan from: $scriptPath")
        }

        def planEvaluator = new FlightPlanScripts()

        def flightPlan = planEvaluator.evaluate(scriptPath)

        Injector injector = Guice.createInjector(new FlightModule(flightPlan))
        injector.getInstance(FlightPlanExecutor).execute()
    }
}
