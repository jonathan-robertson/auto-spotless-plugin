# Auto Spotless Plugin
![](https://github.com/jonathan-robertson/auto-spotless-plugin/workflows/build/badge.svg) ![](https://github.com/jonathan-robertson/auto-spotless-plugin/workflows/publish%20plugin/badge.svg)

*A Spotless Wrapper with embedded configuration*

## Setup
Simply include this plugin in your `build.gradle` by following [this guide](https://plugins.gradle.org/plugin/com.jonathanrobertson.spotless).

## Benefits
1. The fantastic [Spotless Gradle Plugin](https://github.com/diffplug/spotless/tree/master/plugin-gradle) is automatically applied to your project by this plugin
1. Complex formatting rules are applied to Java using the configurations found in these files:
    - `src/main/resources/spotless.eclipseformat.xml`
    - `src/main/resources/spotless.importorder`
1. Simple formatting rules are applied to the following file types:
    - `.gradle`
    - `.md`
    - `.gitignore`
    - `.yml`/`.yaml`

## Usage
So after setting up, you can approach usage in a few different ways:

- I only want the benefit of the Spotless configuration and do not want anything happening automatically for me:
    1. Run `./gradlew build` and will be faced with error messages (preventing build) if your code passes does not pass the formatting requirements.
    1. If you got an error, run `./gradlew spotlessApply build` to format your project files automatically and retry the build.
- I want to use Spotless and this configuration **and also** want `spotlessApply` to be run automatically on build:
    1. Set the environment variable `AUTO_SPOTLESS_ENV` to `dev`. This will cause the `build` task to trigger `spotlessApply` for you automatically any time it is run.
    1. If any files were changed, you will get a `WARNING` indicator with an explanation of exactly which of your tracked files were changed (`git diff --stat` output).
        - NOTE 1: untracked files may been changed as well during this process (just like with running `spotlessApply` directly), but these are not included in the post-analysis since you aren't tracking them... they are not expected to interrupt your push and therefore do not need reminders (or at least that makes sense for me).
        - NOTE 2: I don't recommend using `spotlessApply` directly if you have `AUTO_SPOTLESS_ENV` set to `dev` since it will assume all untracked changes (even pre-existing ones) were caused by Spotless. Other than that, there is no harm in running them separately.

## Etc

### Why does this exist?
As with many things, I was trying to find/build a centralized package to enforce formatting styles on my personal projects without having to copy/paste over all the configs each time.

In the end, I enjoyed the benefits of this approach so much that I built/coded a version of this for my team at work. We've especially enjoyed being able to centralize and version our formatting rules and other helper tasks.

### Customization?
Since this plugin is primarily for my own use, I haven't included support or testing for any customization options in this plugin (which is really a wrapper). Spotless configuration adjustments in your `build.gradle` file may work or may break things.

So if you'd prefer to customize your formatting configuration, I'd encourage you to use [Spotless](https://github.com/diffplug/spotless) directly instead of this plugin... or perhaps even fork this repo to build your own auto-spotless plugin.

A variety of plugins are available directly from the Spotless creators and I believe it's a solid platform for enforcing your preferred formatting standards.

### Anything Weird about this project?
Since Spotless uses the `java.nio.Files` API and it doesn't like absolute paths to files embedded inside jars, I had to copy out the eclipse formatting files to the java system temp directory to provide Spotless with an absolute reference. It seems this is a valid approach, but it does still kind of feel a little gross.

Since this is only performed during build, it shouldn't pose any risks to the security of your project during runtime.
