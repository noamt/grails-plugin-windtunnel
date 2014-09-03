package org._10ne.grails.windtunnel.executor

import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.joran.JoranConfigurator
import ch.qos.logback.core.joran.spi.JoranException
import com.google.inject.Guice
import com.google.inject.Injector
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.codehaus.groovy.control.io.NullWriter
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * @author Noam Y. Tenne.
 */
@Slf4j
class Main {

    public static void main(String[] args) {
        def logbackConfig = Paths.get("${System.getProperty('launcher.dir', '')}/etc/logback.xml")
        if (Files.exists(logbackConfig)) {
            LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
            JoranConfigurator configurator = new JoranConfigurator();
            configurator.setContext(context);
            context.reset();
            try {
                configurator.doConfigure(logbackConfig.toFile());
            } catch (JoranException e) {
                LoggerFactory.getLogger(Main).error("Could not configure logging.", e);
            }
        }

        log.info 'Grails Plugin Windtunnel - A blackbox testing framework for Grails plugins'
        log.info ''

        CliBuilder cliBuilder = new CliBuilder(usage: 'gpw <script>', writer: new LoggerWriter(log))
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
            log.error "Unable to find flight plan at: $scriptPath"
            System.exit(1)
        }
        if (!Files.isReadable(scriptPath)) {
            log.error "Unable to read flight plan from: $scriptPath"
            System.exit(1)
        }

        def planEvaluator = new FlightPlanScripts()

        def flightPlan = planEvaluator.evaluate(scriptPath)

        Injector injector = Guice.createInjector(new FlightModule(flightPlan))
        injector.getInstance(FlightPlanExecutor).execute()
    }

    @CompileStatic
    private static class LoggerWriter extends PrintWriter {

        private Logger log

        LoggerWriter(Logger log) {
            super(new NullWriter())
            this.log = log
        }

        @Override
        void println(String x) {
            log.info(x)
        }
    }
}
