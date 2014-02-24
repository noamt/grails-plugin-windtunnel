package org._10ne.grails.windtunnel.executor

import com.google.inject.Guice
import com.google.inject.Injector

import java.nio.file.Paths

/**
 * @author Noam Y. Tenne.
 */
class Main {

    public static void main(String[] args) {
        CliBuilder cliBuilder = new CliBuilder(usage: 'gpw <script>')
        OptionAccessor optionAccessor = cliBuilder.parse(args)
        String scriptPath = optionAccessor.arguments().first()

        def planEvaluator = new FlightPlanScripts()
        def flightPlan = planEvaluator.evaluate(Paths.get(scriptPath).toFile())

        Injector injector = Guice.createInjector(new FlightModule(flightPlan))
        injector.getInstance(FlightPlanExecutor).execute()
    }
}
