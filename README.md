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
    - `.yml`/`.yaml`

## Usage
So after setting up, you can approach usage in a few different ways:

- You can take the same steps you'd normally take or encounter with Spotless:
    1. Run `./gradlew build` and will be faced with error messages (preventing build) if your code passes does not pass the formatting requirements.
    1. If you got an error, run `./gradlew spotlessApply build` to format your project files automatically and retry the build.
- Or you can add a shortcut to make things a *little* more automated:
    1. Set the environment variable `AUTO_SPOTLESS_ENV` to `dev` if you don't like manually writing `spotlessApply`. This will cause the `build` task to trigger `spotlessApply` for you automatically any time it is run.
    1. Be sure to check for formatting changes. A reminder for this will appear as well as the output of `git status -s -uno` (if you're in a git repository) to show what's changed in your tracked files (hopefully this helps to speed things up for you).

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
