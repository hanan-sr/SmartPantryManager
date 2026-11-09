\# Application Architecture



Smart Pantry Manager uses a Java-based Android application

structure consisting of Activities, adapters, model classes,

a database helper and a utility class.



\## Main Components



\- MainActivity: Displays the pantry and provides navigation.

\- AddEditIngredientActivity: Handles adding and editing ingredients.

\- SuggestedRecipesActivity: Displays recipes that match the pantry.

\- RecipeDetailActivity: Displays recipe ingredients and instructions.

\- SettingsActivity: Provides application settings.

\- DatabaseHelper: Manages SQLite database operations.

\- PantryAdapter: Displays pantry ingredients in a ListView.

\- RecipeAdapter: Displays suggested recipes in a ListView.

\- IngredientMatcher: Determines whether a recipe can be prepared.



\## Navigation



Explicit Android Intents are used to navigate between Activities.

For example, MainActivity opens AddEditIngredientActivity when

the user chooses to add an ingredient.



The application uses onCreate() to initialise screens and

onResume() in MainActivity to refresh pantry information when

the user returns to the main screen.

