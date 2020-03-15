# Contributing

If you'd like to add a feature or fix a bug, great! If it is a bug, just create a pull request, if it is a feature, please first create an issue for discussion.

## Building from Source

- The project uses Gradle to allow simple building. Install Gradle (using the gradle wrapper, any version is sufficient) and add it to `PATH`
- Clone the repository into a folder of your choice
- On a command line, run `./gradlew build` and wait for completion
- A complete version is now available under "mealplaner/build/distributions". 
It contains a zipped folder and a zipped tarball
- The project uses log4j (Apache 2.0), slf4j (MIT) for logging and swingx (LGPL3) for autocompletion as well as jaxb (CDDL) for xml serialization and de-serialization. 
The libraries will be downloaded into `build/libs/lib`. 
If you want to provide your own versions of these libraries, you need to adapt the classpath in the `manifest.MF` in "Mealplaner.jar" or copy your versions into the "lib" folder.

## Testing

- New code should always be accompanied by unit tests (junit 5 with Mockito and AssertJ assertions)
- New GUI-features should have also be accompanied by a smoke end-to-end test. To build the end to end tests, run `./gradlew build -Drune2etests=True`. The end to end tests use AssertJ-Swing to click through the Swing-GUI

## Code-Style

- The project uses mostly IntelliJ defaults, a lot of them enforced by the checkstyle plugin. New contributions must not have checkstyle warnings.
