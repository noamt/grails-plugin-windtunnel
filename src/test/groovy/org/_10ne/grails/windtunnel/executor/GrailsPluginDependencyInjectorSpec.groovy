package org._10ne.grails.windtunnel.executor

import org._10ne.grails.windtunnel.model.PluginSource
import spock.lang.Shared
import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * @author Noam Y. Tenne.
 */
class GrailsPluginDependencyInjectorSpec extends Specification {

    @Shared
    Path buildConfig

    @Shared
    GrailsPluginDependencyInjector dependencyInjector = new GrailsPluginDependencyInjector()

    def setup() {
        buildConfig = buildConfigTemplate()
    }

    def 'Inject a default plugin'() {
        setup:
        def pluginSource = new PluginSource(artifactId: 'plugin-id', version: '1.0')

        when:
        dependencyInjector.addPluginDependency(buildConfig, pluginSource)

        then:
        def lines = buildConfig.toFile().readLines()
        def pluginsClosureLineIndex = lines.findIndexOf { it.contains('plugins {') }
        def pluginLineIndex = lines.findIndexOf { it.contains('compile \':plugin-id:1.0\'') }

        pluginLineIndex != -1
        pluginsClosureLineIndex == (pluginLineIndex - 1)
    }

    def 'Inject a plugin with a non-default group ID'() {
        setup:
        def pluginSource = new PluginSource(groupId: 'org.bob.plugins', artifactId: 'plugin-id', version: '1.0')

        when:
        dependencyInjector.addPluginDependency(buildConfig, pluginSource)

        then:
        def lines = buildConfig.toFile().readLines()
        def pluginsClosureLineIndex = lines.findIndexOf { it.contains('plugins {') }
        def pluginLineIndex = lines.findIndexOf { it.contains('compile \'org.bob.plugins:plugin-id:1.0\'') }

        pluginLineIndex != -1
        pluginsClosureLineIndex == (pluginLineIndex - 1)
    }

    def 'Inject a plugin with a non-default repository'() {
        setup:
        def pluginSource = new PluginSource(repositoryUrl: 'http://customRepo.com', artifactId: 'plugin-id', version: '1.0')

        when:
        dependencyInjector.addPluginDependency(buildConfig, pluginSource)

        then:
        def lines = buildConfig.toFile().readLines()

        def repositoriesClosureLineIndex = lines.findIndexOf { it.contains('repositories {') }
        def repositoryLineIndex = lines.findIndexOf { it.contains('mavenRepo \'http://customRepo.com\'') }

        repositoryLineIndex != -1
        repositoriesClosureLineIndex == (repositoryLineIndex - 1)

        def pluginsClosureLineIndex = lines.findIndexOf { it.contains('plugins {') }
        def pluginLineIndex = lines.findIndexOf { it.contains('compile \':plugin-id:1.0\'') }

        pluginLineIndex != -1
        pluginsClosureLineIndex == (pluginLineIndex - 1)
    }

    private Path buildConfigTemplate() {
        def buildConfigTemplate = Paths.get(this.class.getResource('/org/_10ne/grails/windtunnel/executor/BuildConfig.gscript').toURI())
        def tempBuildConfig = Files.createTempFile('BuildConfig', 'groovy')
        Files.delete(tempBuildConfig)
        Files.copy(buildConfigTemplate, tempBuildConfig)
        tempBuildConfig
    }
}
