\# Code Walkthrough



\## Database Management

DatabaseHelper.java manages SQLite database creation and

pantry CRUD operations.



\## Custom Adapters

PantryAdapter.java extends ArrayAdapter and uses getView()

to display pantry ingredient information in a custom row layout.



RecipeAdapter.java performs a similar role for recipes.



\## Recipe Matching

IngredientMatcher.java contains the canMake() method.

It checks whether all required recipe ingredients exist

in the pantry with sufficient quantities and compatible units.



\## Navigation

MainActivity.java uses explicit Intents to open the

AddEditIngredientActivity, SuggestedRecipesActivity and

SettingsActivity screens.

