# Gradle GitHub Actions Generator
     
Generates GitHub Action Workflows defined using the [Kotlin Github Actions DSL](https://github.com/nefilim/kotlin-github-actions-dsl).
          
## Usage

```kotlin
import io.github.nefilim.githubactions.dsl.Job
import io.github.nefilim.githubactions.dsl.Step
import io.github.nefilim.githubactions.dsl.actions.CheckoutAction
import io.github.nefilim.githubactions.dsl.actions.GradleBuildAction
import io.github.nefilim.githubactions.dsl.githubRef
import io.github.nefilim.githubactions.dsl.secretRef
import io.github.nefilim.githubactions.dsl.workflow

plugins {
    id("io.github.nefilim.gradle.github-actions-generator-plugin") version "x.x.x"
}

githubActionsGenerator {
    outputDirectory(File(rootProject.rootDir, ".github/workflows"))
    workflows(
        mapOf(
            "local.yaml" to workflow("CI Build") {
                triggers {
                    push {
                        branches = listOf("main")
                        pathsIgnore = listOf("**.md")
                    }
                    pullRequest {
                        branchesIgnore = listOf("renovate/*")
                        pathsIgnore = listOf("**.md")
                    }
                    workflowDispatch {
                        
                    }
                }
                concurrency("ci-build-${githubRef("ref")}")
                env {
                    "NEXUS_USER" to secretRef("NEXUS_USER")
                    "NEXUS_PASS" to secretRef("NEXUS_PASS")
                }
                jobs {
                    "ci-build" to Job(
                        runsOn = listOf("linux", "self-hosted"),
                        steps = listOf(
                            CheckoutAction(
                                path = "source",
                                repository = "nefilim/gradle-github-actions-generator",
                                ref = "main",
                            ).toStep(Step.StepID("my-checkout"), "Checkout Source", CheckoutAction.Uses),
                            GradleBuildAction(
                                buildRootDirectory = "source",
                                arguments = "clean build"
                            ).toStep()
                        ),
                    )
                }
            }
        )
    )
}

```

## Tasks

`generateGithubActions`: Generates the specified workflows in the matching files in the specified output directory