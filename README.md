This is a small project for a mealplaner. Based on your settings and your database (simple list with various options), it makes proposition on what next to eat. 

## Features (v2.1.0)

- Internationalization: English and German currently available
- Can propose one menu with up to three meals per day
- Meals can store a comment and all ingredients with amounts
- You can define your own settings for every day: How much time do you have? How many people are going to eat? Do you need to prepare the food in advance and want to cook a casserole? Do you rather want to cook special meals or much preferred ones?
- You can store default settings for a weekly routine to quickly get a proposal for next week's meals.
- The proposals are varied: You won't have pasta only and meals that you haven't cooked for quite a long time will be proposed more often.
- An ordered shopping list for each proposal will be created and can be printed.
- If you don't like the settings, implement your own plugins: You can add additional bits of information by meal or ingredient, add GUI-elements to input them and change the way the proposal is made. Just drop in the JAR into the lib folder

## Installation

Grab the zip file from the [release page](https://github.com/Martin-Idel/mealplaner/releases/tag/v2.0.1) and unzip it wherever you want. Then just call `mealplaner` (Mac or Linux) or `mealplaner.bat` (Windows) in the `bin` folder to start the application.

## Building from Source

- The project uses Gradle to allow simple building. Install Gradle (using the gradle wrapper, any version is sufficient) and add it to PATH.
- Clone the repository into a folder of your choice.
- On a command line, run "./gradlew build" and wait
- A complete version is now available under "mealplaner/build/distributions". It contains a zipped folder and a zipped tarball.
- The project uses log4j (Apache 2.0), slf4j (MIT) for logging and swingx (LGPL3) for autocompletion as well as jaxb (CDDL) for xml serialization and de-serialization. The libraries will be downloaded into "build/libs/lib". If you want to provide your own versions of these libraries, you need to adapt the classpath in the manifest.MF in "Mealplaner.jar" or copy your versions into the "lib" folder.
