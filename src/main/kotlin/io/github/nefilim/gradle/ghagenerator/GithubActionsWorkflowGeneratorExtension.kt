package io.github.nefilim.gradle.ghagenerator

import com.charleskorn.kaml.MultiLineStringStyle
import com.charleskorn.kaml.SingleLineStringStyle
import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlConfiguration
import io.github.nefilim.githubactions.dsl.Workflow
import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.MapProperty
import java.io.File
import javax.inject.Inject

private val logger = Logging.getLogger(Logger.ROOT_LOGGER_NAME)

abstract class GithubActionsWorkflowGeneratorExtension @Inject constructor(objects: ObjectFactory, private val project: Project) {
    private val outputDirectory: DirectoryProperty = objects.directoryProperty()
    private val workflows: MapProperty<String, Workflow> = objects.mapProperty(String::class.java, Workflow::class.java)

    fun outputDirectory(directory: File) {
        outputDirectory.set(directory)
        outputDirectory.disallowChanges()
    }

    fun workflows(workflows: Map<String, Workflow>) {
        this.workflows.set(workflows)
    }

    internal fun generateWorkflows() {
        val yaml = Yaml(
            configuration = YamlConfiguration(
                breakScalarsAt = 200,
                multiLineStringStyle = MultiLineStringStyle.Literal,
                singleLineScalarStyle = SingleLineStringStyle.SingleQuoted,
            )
        )

        workflows.get().forEach { (filename, workflow) ->
            logger.gha("generating flow for [${workflow.name}] to [${outputDirectory.get()}/$filename]")
            File("${outputDirectory.get()}/$filename").printWriter().use { out ->
                out.println(yaml.encodeToString(Workflow.serializer(), workflow))
            }
        }
    }

    companion object {
        const val ExtensionName = "githubActionsGenerator"

        internal fun Project.ghaExtension(): GithubActionsWorkflowGeneratorExtension = extensions.create(ExtensionName, GithubActionsWorkflowGeneratorExtension::class.java)
    }
}