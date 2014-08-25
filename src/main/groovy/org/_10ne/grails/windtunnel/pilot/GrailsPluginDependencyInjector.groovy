package org._10ne.grails.windtunnel.pilot

import org._10ne.grails.windtunnel.model.PluginSource

import java.nio.file.Path

/**
 * @author Noam Y. Tenne.
 */
class GrailsPluginDependencyInjector {

    void addPluginDependency(Path buildConfigFile, PluginSource pluginSource) {
        File buildConfig = buildConfigFile.toFile()
        def buildConfigLines = buildConfig.readLines()

        if (pluginSource.repositoryUrl) {
            def repositoriesClosureLineIndex = buildConfigLines.findIndexOf { it.contains('repositories {') }
            def scheme = new URI(pluginSource.repositoryUrl).scheme
            String repo
            if (['http', 'https'].contains(scheme)) {
                repo = 'mavenRepo'
            } else {
                repo = 'mavenLocal'
            }
            buildConfigLines.add((repositoriesClosureLineIndex + 1), "$repo '${pluginSource.repositoryUrl}'")
        }

        def pluginsClosureLineIndex = buildConfigLines.findIndexOf { it.contains('plugins {') }
        buildConfigLines.add((pluginsClosureLineIndex + 1), "compile '${pluginSource.groupId ?: ''}:${pluginSource.artifactId}:${pluginSource.version}'")

        buildConfig.withWriter { out ->
            buildConfigLines.each { out.println(it) }
        }
    }
}
