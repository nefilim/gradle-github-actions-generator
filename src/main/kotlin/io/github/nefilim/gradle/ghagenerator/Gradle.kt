package io.github.nefilim.gradle.ghagenerator

import org.gradle.api.logging.Logger

private val LogPrefix = "semver: ".bold()

private fun String.coloured(c: String) = "$c$this\u001B[0m"
internal fun String.green() = this.coloured("\u001B[32m")
internal fun String.red() = this.coloured("\u001B[31m")
internal fun String.purple() = this.coloured("\u001B[35m")
internal fun String.yellow() = this.coloured("\u001B[33m")
internal fun String.bold() = this.coloured("\u001B[1m")

internal fun Logger.gha(message: String) = this.lifecycle("gha-gen: ".bold() + message.purple())
internal fun Logger.ghaWarn(message: String) = this.lifecycle("gha-gen: ".bold() + message.yellow())