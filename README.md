This is a small project for a mealplaner. Based on your settings and your database (simple list with various options), it makes proposition on what next to eat. 

## Features (v2.1.1)

Get a proposal for what to cook in a few seconds:

- Enter your settings (do you want to cook long? A casserole which may take some time in the oven?)
- Get a proposal and print the shopping list adjusted to the amount of people to cook for - the shopping list will be ordered according to the type of ingredient (i.e. meat will be ordered with meat)
- If you have a routine, just enter default settings once and use them always

![Basic demo of mealplaner](demos/basic_mealplaner.gif)

Use your own database of meals: 

- Enter ingredients if you like to get a shopping list
- Enter meals with various settings
- Define up to three courses per meal

![Meal creation demo](demos/mealplaner_create_database.gif)

- Internationalization: English and German currently available
- If you don't like the settings, implement your own plugins: You can add additional bits of information by meal or ingredient, add GUI-elements to input them and change the way the proposal is made. Just drop in the JAR into the lib folder!
- A number of plugins are active by default:
  * Enter the sidedish per meal: Ensure that you'll never eat pasta all week long
  * Enter a comment per meal - maybe store some information on where to find the recipe?
  * Distinguish meals by cooking time: sometimes you just want to cook something quick, othertimes something fancy
  * Distinguish meals that need to cook in the oven: they can be prepared in advance but may need to cook for hours without your intervention
  * Add a preference to your meals: You'll probably have a few meals you want to cook regularly, while others only rarely

## Installation

Grab the zip file from the [release page](https://github.com/Martin-Idel/mealplaner/releases/tag/v2.1.1) and unzip it wherever you want. Then just call `mealplaner` (Mac or Linux) or `mealplaner.bat` (Windows) in the `bin` folder to start the application.

## Contributing

If you'd like to help, yes please! See [the guidelines](https://github.com/Martin-Idel/mealplaner/blob/master/CONTRIBUTING.md) for details and feel free to contact me.
