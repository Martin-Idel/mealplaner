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
- New GUI-features should also be accompanied by a smoke end-to-end test. Run end-to-end tests with `./gradlew :mealplaner-e2e:test`
- Run all native GUI tests: `./gradlew :mealplaner-e2e:test --tests "*nativeguitests*"`
- Run single test: `./gradlew :mealplaner-e2e:test --tests "*YourTestName*"`
- Run test with debug info: `./gradlew :mealplaner-e2e:test --tests "*YourTestName*" --info --stacktrace`
- For Java debugging with breakpoints: `./gradlew :mealplaner-e2e:test --tests "*YourTestName*" -Dorg.gradle.jvmargs=-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=5005`, then attach IDE debugger to `localhost:5005`

## Code-Style

- The project uses mostly IntelliJ defaults, a lot of them enforced by the checkstyle plugin. New contributions must not have checkstyle warnings.
