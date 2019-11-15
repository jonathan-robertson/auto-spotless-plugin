# Auto Spotless Plugin
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

## Usage
You'd use this in the same way that you'd use Spotless directly, just without the need to add your own configuration.

So after setting up, you can run `./gradlew build` and will be faced with error messages (preventing build) unless your code passes the formatting requirements.

Run `./gradlew spotlessApply` to format your project files automatically and then your build should work again.

*TODO: I have plans to automatically trigger the `spotlessApply` task if a certain environment variable is set, but that will be added for the `v1.1.0` release.*

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
