# A small planer to simplify your week

## The Proposal Summary Panel

When opening the application, you'll see a classical desktop application with a menu bar at the top, two buttons for leaving the application at the bottom and a pane in the middle of the window.

### The GUI
The center of the application is taken by a grid of buttons and text fields:
- The topmost row contains a button labeled *Update*. In order to propose meals that you haven't cooked in a while, the application needs to keep track of what you have cooked. Therefore, before you can make a proposal, you need to update this knowledge until the day of the proposal. This is done by pressing *Update* and entering the meals you have cooked since you last pressed update. You'll find meals of your last proposal already entered, but you can change them if you like. 

The rest of the fields concern a new proposal:
- The text field below the *Update* button asks for the number of days your next proposal should contain.
- You can then decide whether you want to include today in your proposal or start the proposal next week.
- Furthermore, you can add some randomness into the proposal, otherwise it will deterministically determine the next meals according to the rules laid out in the section "How do proposals work?".
- Finally, you can decide to take the default settings for this proposal (more on settings shortly). This will simplify the proposal process and might be reasonable for a usual week.
- In the bottom row, you can find two buttons: The left button lets you edit your default settings, while the right button generates a proposal.

### Making a proposal
If you click the button to make a proposal, you will either directly get a dialog with a proposal, or you will be asked to enter the settings for the day. Here, you can also take the default settings as a basis and make small or large changes from there.

If you did not choose to take the default setting, you will have to specify settings for every day of your proposal. Once you have finished, press *Continue* and your proposal will be generated.

The proposal list contains the name of the meals you should cook and when you should cook them. If you decided to cook a desert or an entry, too, the result will have multiple columns containing entries and/or deserts for those days which you specified. 

If you are unhappy with some of the proposals, you can freely change the meals now and work with that proposal. You can also print your proposal later via the menu bar. 

Once you are happy with your proposal, if you press *Continue* and you have entered some ingredients to your meal, a shopping list will automatically be created for you. The shopping list is organised according to the types of food - e.g. all milk products will be grouped together. This should simplify finding everything in the supermarket. 

## The Meal panel

The second available panel is a panel organising the meal database. Here, you can easily add meals, edit meals and delete meals that you don't want anymore. 

***Note:*** Changing anything requires saving if you want to keep the database. Otherwise, changes will be discarded when closing the application.

Every meal has a few aspects that need to be entered:
- The cooking time: The approximate preparation time (not including waiting time) for the dish, in order to give you the opportunity to plan quick dinners when necessary.
- The utensil: Sometimes, you may want to cook a casserole, because it can be prepared in advance. At other times, you just want to do a quick dish in a pan, which is however not possible when you have a large group of people for dinner. Enter the main utensil to take advantage of that.
- The course type: Most of your meals will be a main dish, but occasionally, you may also want to cook several meals at once.
- The cooking preference: Especially when you have a family with children, they'll have strong opinions about your meals! In that case, you may want to cook some meals more often, while some meals are reserved for special occasions.
- The side dish: While theoretically this could be deduced from the recipe, since you don't have to enter any recipe at all if you don't plan to use the shopping list functionality, it is mandatory to also repeat the main side dish here - this is then used to make sure that you won't have pasta all the time (even if your children would like that).
- A comment: Add any information that you want to record.

In addition, you can also enter a recipe. Recipes usually come with information about how many portions you can expect from a meal, but often, these amounts do not correspond to the actual amount you need for your family. The meal planer is a great way to keep and change recipes. The recipes can then be used to automatically generate shopping lists.
For any recipe, you can choose the corresponding ingredients, which come with the measure of your choice.

## The Ingredients panel

The third available panel is a panel organising the ingredients. Here, you can easily add, remove and edit ingredients and they will be organised in an alphabetical list.

***Note:*** Changing anything requires saving if you want to keep the database. Otherwise, changes will be discarded when closing the application.

While it is always possible to also add ingredients when you enter recipes, it might be helpful to enter a few ingredients up front. Any ingredient consists of three aspects: a name, a measure (e.g. grams or litres) and a type. At the moment, the ingredient type is only relevant for sorting the shopping list, such that fresh fruit are located next to fresh fruit, etc. This will make shopping easier, since it's easier to make sure that you didn't miss anything.

You can also remove ingredients, for example when you later on find out that you already entered the ingredient some time ago. Clearly, removing an ingredient is problematic when you still use it in a meal. But don't worry, you'll get the opportunity to either not remove the ingredient from the list or replace it by some other ingredient from the list. This will then automatically update your meals and recipes.

## How do proposals work?

Proposals are based on the settings you entered and a few extras. Let us start with the settings:

- For every day of the proposal, you can specify which cooking times you prefer: Do you have to cook very fast? Or do you have a lot of time on your hands? The proposal will always satisfy this need if it is at all possible.
- You can then specify the amount of people you cook for. This will be the multiplier for the shopping list later on, but it will also prohibit pan meals, if the number is greater than two, assuming that it becomes infeasible to only cook in a pan for three or more people.
- Furthermore, you can specify whether you want to allow casseroles or not or whether you want to explicitly cook a casserole, because you can prepare it in advance (maybe you are going out before lunch and your children come home hungry?). This setting will also be respected if at all possible.
- In addition, you can specify the preference. Do you want to cook meals that everybody likes? Or do you want to cook something you rarely cook because it's maybe healthy or some friends are coming over? 
- Finally, you can choose to cook more than a main course, maybe add some desert or cook a soup.

If you like your weekly routine, you might want to enter default settings to make it even easier to get a proposal for your weekly needs.

The proposal will then be generated according to the settings you chose:

As a basis for the calculation, it will take the times that meals have not been cooked for, i.e. if you didn't cook something for many weeks, it is more likely to be cooked soon than something you have cooked only recently. Of course, meals that you prefer to be cooked only rarely will have longer time intervals when they are not proposed than meals that are very much preferred, at least if you entered the normal preference settings and didn't explicitly ask for rarely cooked meals.

In addition to times, if you wanted the list to be randomized, a small random number will be added to each meal. This will not completely overturn the list, but it might change the orders that meals might be suggested, sufficiently. 

The last aspect to know about is that the side dishes are taken into account: It's fine to have two times pasta in a row and rarely, it's maybe nice to have it even three times in a row, but generally, you'd want to switch to potatoes, rice or something else after that and this is what you should see with this meal planer.