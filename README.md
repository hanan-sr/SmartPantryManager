# Smart Pantry Manager

A Java Android application that helps reduce food waste by tracking pantry ingredients and suggesting recipes only when every required ingredient is available in a sufficient quantity.

## Technology
- Java and Android Studio
- SQLite using `SQLiteOpenHelper`
- Android `ListView` with custom `ArrayAdapter` classes
- Activities and Intents for navigation
- XML layouts

## Database choice
SQLite was selected because the application stores a relatively small amount of structured data locally. It works offline, requires no separate server, and supports persistent Create, Read, Update and Delete operations.

## Features
- Add, edit and delete pantry ingredients
- Store ingredient name, quantity, unit and optional expiry date
- View pantry items in a custom ListView
- 20 recipes seeded into SQLite on first database creation
- Strict recipe matching: all ingredients and required quantities must be present
- Basic singular/plural ingredient-name normalisation
- Recipe detail screen with ingredients and preparation steps
- Settings preference for expiring-soon reminders

## Run the project
1. Install Android Studio with an Android SDK (API 35 recommended).
2. Open the `SmartPantryManager` project folder in Android Studio.
3. Allow Gradle to sync. The Android Gradle Plugin may need to be downloaded on the first sync.
4. Select an emulator (API 23 or later) or connect an Android device with USB debugging enabled.
5. Press **Run**.

## Test the strict matching rule
1. Add `tomato`, quantity `2`, unit `pcs`.
2. Add `egg`, quantity `2`, unit `pcs`.
3. Add `salt`, quantity `2`, unit `g`.
4. Open Suggested Recipes. `Tomato and Egg Scramble` should appear.
5. Edit the tomato quantity to `1` or delete salt.
6. Reopen Suggested Recipes. `Tomato and Egg Scramble` must no longer appear.

## GitHub development
Create a public GitHub repository and commit genuine development stages as you build and test. Do not invent earlier commit dates or claim work was completed before it was.

Suggested stages:
1. Create Android Studio project structure
2. Add XML screen layouts
3. Add pantry model classes
4. Implement SQLite database tables
5. Seed the recipe collection
6. Implement pantry CRUD operations
7. Add custom pantry adapter
8. Add strict recipe matching
9. Add recipe list and detail screens
10. Add settings and validation
11. Test persistence and fix issues
12. Complete README and final verification

## Important matching notes
The matcher supports g/kg and ml/l conversion. Piece-based units such as `pcs`, `slices`, `cloves` and `can` are treated as separate unit types. It does not assume that a gram can be converted to a millilitre or that unlike piece units are interchangeable. Recipe quantities are minimum quantities: having more than the required amount is acceptable.
