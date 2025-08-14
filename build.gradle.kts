// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
  alias(libs.plugins.android.application) apply false
  alias(libs.plugins.kotlin.android) apply false
  alias(libs.plugins.ktlint) apply true
}

ktlint {
  verbose.set(true)
  android.set(true)
  outputToConsole.set(true)
  ignoreFailures.set(false)
}
