package io.github.nefilim.gradle.ghagenerator

import io.github.nefilim.gradle.ghagenerator.GithubActionsWorkflowGeneratorExtension.Companion.ghaExtension
import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.get

public class GithubActionsWorkflowGeneratorPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        target.ghaExtension() // impure! but needed to create and register the extension with the project so that we can use it in the tasks below

        target.tasks.register("generateGithubActions", GenerateGithubActionsTask::class.java)
    }
}

open class GenerateGithubActionsTask: DefaultTask() {
    @TaskAction
    fun generateVersionFile() {
        val extension = (project.extensions[GithubActionsWorkflowGeneratorExtension.ExtensionName] as GithubActionsWorkflowGeneratorExtension)
        extension.generateWorkflows()
    }
}