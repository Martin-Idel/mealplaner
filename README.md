This is a small project for a mealplaner. Based on your settings and your database (simple list with various options), it makes proposition on what next to eat. 

## Features (v0.1.0)

- Internationalization: English and German currently available
- Can propose one meal per day
- Meals can store a comment and all ingredients with amount
- You can define your own settings for every day: How much time do you have? How many people are going to eat? Do you need to prepare the food in advance and want to cook a casserole? Do you rather want to cook special meals or much preferred ones?
- You can store default settings for a weekly routine to quickly get a proposal for next weeks meals.
- The proposals are varied: You won't have pasta only and meals that you haven't cooked for quite a long time will be proposed more often.

## The next version (v0.2.0)

- Since all ingredients can be stored, an ordered shopping list for each proposal can be created and printed

## The future

- The project is under heavy refactoring. The current XML model was implemented such that the backbone can be refactored without losing the database. The goal is to have a more plugin based model that lets you use only those features you really need.
