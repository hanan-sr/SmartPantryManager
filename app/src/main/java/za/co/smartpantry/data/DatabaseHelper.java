package za.co.smartpantry.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import za.co.smartpantry.model.PantryItem;
import za.co.smartpantry.model.Recipe;
import za.co.smartpantry.model.RecipeIngredient;

import java.util.ArrayList;
import java.util.List;

/** Creates the local database and provides pantry CRUD and recipe queries. */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "smart_pantry.db";
    private static final int DB_VERSION = 1;

    public DatabaseHelper(Context context) { super(context, DB_NAME, null, DB_VERSION); }

    @Override public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE pantry (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL COLLATE NOCASE UNIQUE, quantity REAL NOT NULL, unit TEXT NOT NULL, expiry_date TEXT)");
        db.execSQL("CREATE TABLE recipes (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, method TEXT NOT NULL)");
        db.execSQL("CREATE TABLE recipe_ingredients (id INTEGER PRIMARY KEY AUTOINCREMENT, recipe_id INTEGER NOT NULL, name TEXT NOT NULL, quantity REAL NOT NULL, unit TEXT NOT NULL, FOREIGN KEY(recipe_id) REFERENCES recipes(id) ON DELETE CASCADE)");
        seedRecipes(db);
    }

    @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS recipe_ingredients");
        db.execSQL("DROP TABLE IF EXISTS recipes");
        db.execSQL("DROP TABLE IF EXISTS pantry");
        onCreate(db);
    }

    public long insertPantryItem(String name, double quantity, String unit, String expiry) {
        ContentValues v = new ContentValues();
        v.put("name", name.trim()); v.put("quantity", quantity); v.put("unit", unit);
        v.put("expiry_date", expiry == null ? "" : expiry.trim());
        return getWritableDatabase().insert("pantry", null, v);
    }

    public int updatePantryItem(int id, String name, double quantity, String unit, String expiry) {
        ContentValues v = new ContentValues();
        v.put("name", name.trim()); v.put("quantity", quantity); v.put("unit", unit);
        v.put("expiry_date", expiry == null ? "" : expiry.trim());
        return getWritableDatabase().update("pantry", v, "id=?", new String[]{String.valueOf(id)});
    }

    public int deletePantryItem(int id) {
        return getWritableDatabase().delete("pantry", "id=?", new String[]{String.valueOf(id)});
    }

    public List<PantryItem> getAllPantryItems() {
        List<PantryItem> items = new ArrayList<>();
        try (Cursor c = getReadableDatabase().rawQuery("SELECT id,name,quantity,unit,expiry_date FROM pantry ORDER BY name COLLATE NOCASE", null)) {
            while (c.moveToNext()) items.add(new PantryItem(c.getInt(0), c.getString(1), c.getDouble(2), c.getString(3), c.getString(4)));
        }
        return items;
    }

    public PantryItem getPantryItem(int id) {
        try (Cursor c = getReadableDatabase().rawQuery("SELECT id,name,quantity,unit,expiry_date FROM pantry WHERE id=?", new String[]{String.valueOf(id)})) {
            if (c.moveToFirst()) return new PantryItem(c.getInt(0), c.getString(1), c.getDouble(2), c.getString(3), c.getString(4));
        }
        return null;
    }

    public List<Recipe> getAllRecipes() {
        List<Recipe> recipes = new ArrayList<>();
        try (Cursor c = getReadableDatabase().rawQuery("SELECT id,name,method FROM recipes ORDER BY name", null)) {
            while (c.moveToNext()) recipes.add(getRecipe(c.getInt(0)));
        }
        return recipes;
    }

    public Recipe getRecipe(int id) {
        SQLiteDatabase db = getReadableDatabase();
        String name = "", method = "";
        try (Cursor c = db.rawQuery("SELECT name,method FROM recipes WHERE id=?", new String[]{String.valueOf(id)})) {
            if (!c.moveToFirst()) return null;
            name = c.getString(0); method = c.getString(1);
        }
        List<RecipeIngredient> ingredients = new ArrayList<>();
        try (Cursor c = db.rawQuery("SELECT name,quantity,unit FROM recipe_ingredients WHERE recipe_id=? ORDER BY name", new String[]{String.valueOf(id)})) {
            while (c.moveToNext()) ingredients.add(new RecipeIngredient(c.getString(0), c.getDouble(1), c.getString(2)));
        }
        return new Recipe(id, name, method, ingredients);
    }

    /** Adds a recipe and all its required ingredients as one transaction. */
    private void addRecipe(SQLiteDatabase db, String name, String method, String[][] ingredients) {
        ContentValues recipe = new ContentValues();
        recipe.put("name", name); recipe.put("method", method);
        long recipeId = db.insert("recipes", null, recipe);
        for (String[] ingredient : ingredients) {
            ContentValues v = new ContentValues();
            v.put("recipe_id", recipeId); v.put("name", ingredient[0]);
            v.put("quantity", Double.parseDouble(ingredient[1])); v.put("unit", ingredient[2]);
            db.insert("recipe_ingredients", null, v);
        }
    }

    private void seedRecipes(SQLiteDatabase db) {
        db.beginTransaction();
        try {
            addRecipe(db, "Tomato and Egg Scramble", "1. Chop the tomatoes. 2. Beat the eggs. 3. Cook tomatoes in a pan, add eggs and stir until set.", new String[][]{{"tomato","2","pcs"},{"egg","2","pcs"},{"salt","2","g"}});
            addRecipe(db, "Cheese Omelette", "1. Beat the eggs. 2. Pour into a warm pan. 3. Add cheese and fold when cooked.", new String[][]{{"egg","2","pcs"},{"cheese","30","g"},{"salt","2","g"}});
            addRecipe(db, "Garlic Tomato Pasta", "1. Cook pasta. 2. Fry garlic and tomato. 3. Mix together and season.", new String[][]{{"pasta","150","g"},{"tomato","2","pcs"},{"garlic","2","cloves"},{"salt","2","g"}});
            addRecipe(db, "Banana Pancakes", "1. Mash banana. 2. Mix with egg and flour. 3. Cook small pancakes on both sides.", new String[][]{{"banana","1","pcs"},{"egg","1","pcs"},{"flour","100","g"},{"milk","100","ml"}});
            addRecipe(db, "Simple Vegetable Soup", "1. Dice vegetables. 2. Simmer in water until tender. 3. Season and serve.", new String[][]{{"carrot","2","pcs"},{"potato","2","pcs"},{"onion","1","pcs"},{"water","500","ml"},{"salt","2","g"}});
            addRecipe(db, "Cheesy Potato Bake", "1. Slice potatoes. 2. Layer with milk and cheese. 3. Bake until golden and tender.", new String[][]{{"potato","3","pcs"},{"cheese","80","g"},{"milk","100","ml"},{"salt","2","g"}});
            addRecipe(db, "Chicken Fried Rice", "1. Cook rice. 2. Cook chicken fully in a pan. 3. Add peas and rice and stir-fry.", new String[][]{{"rice","200","g"},{"chicken","150","g"},{"peas","50","g"},{"soy sauce","15","ml"}});
            addRecipe(db, "Peanut Banana Smoothie", "1. Add all ingredients to a blender. 2. Blend until smooth.", new String[][]{{"banana","1","pcs"},{"milk","250","ml"},{"peanut butter","20","g"}});
            addRecipe(db, "Spinach and Cheese Toast", "1. Cook spinach until wilted. 2. Place with cheese on bread. 3. Toast until warm.", new String[][]{{"spinach","40","g"},{"cheese","30","g"},{"bread","2","slices"}});
            addRecipe(db, "Carrot Fritters", "1. Grate carrots. 2. Mix with flour and egg. 3. Pan-fry spoonfuls until cooked.", new String[][]{{"carrot","2","pcs"},{"flour","60","g"},{"egg","1","pcs"},{"salt","2","g"}});
            addRecipe(db, "Garlic Butter Pasta", "1. Cook pasta. 2. Melt butter and gently cook garlic. 3. Toss pasta through the sauce.", new String[][]{{"pasta","150","g"},{"butter","25","g"},{"garlic","2","cloves"}});
            addRecipe(db, "Apple Oat Porridge", "1. Cook oats with milk. 2. Dice and add apple. 3. Simmer until creamy.", new String[][]{{"oats","60","g"},{"milk","200","ml"},{"apple","1","pcs"}});
            addRecipe(db, "Tuna Sweetcorn Sandwich", "1. Drain tuna. 2. Mix with sweetcorn. 3. Fill the bread slices.", new String[][]{{"tuna","1","can"},{"sweetcorn","50","g"},{"bread","2","slices"}});
            addRecipe(db, "Cucumber Tomato Salad", "1. Chop cucumber and tomato. 2. Combine and season. 3. Serve chilled.", new String[][]{{"cucumber","1","pcs"},{"tomato","2","pcs"},{"salt","2","g"}});
            addRecipe(db, "Lemon Garlic Chicken", "1. Mix lemon juice and garlic. 2. Coat chicken. 3. Cook chicken fully before serving.", new String[][]{{"chicken","200","g"},{"lemon","1","pcs"},{"garlic","2","cloves"}});
            addRecipe(db, "Sweet Potato Mash", "1. Peel and cube sweet potato. 2. Boil until soft. 3. Mash with butter and salt.", new String[][]{{"sweet potato","2","pcs"},{"butter","20","g"},{"salt","2","g"}});
            addRecipe(db, "Bean and Tomato Stew", "1. Cook onion until soft. 2. Add beans and tomato. 3. Simmer and season.", new String[][]{{"beans","200","g"},{"tomato","2","pcs"},{"onion","1","pcs"},{"salt","2","g"}});
            addRecipe(db, "Mushroom Omelette", "1. Slice and cook mushrooms. 2. Add beaten eggs. 3. Fold when set.", new String[][]{{"egg","2","pcs"},{"mushroom","80","g"},{"salt","2","g"}});
            addRecipe(db, "Yoghurt Fruit Bowl", "1. Spoon yoghurt into a bowl. 2. Slice banana and apple. 3. Arrange fruit on top.", new String[][]{{"yoghurt","150","g"},{"banana","1","pcs"},{"apple","1","pcs"}});
            addRecipe(db, "Corn and Potato Cakes", "1. Mash cooked potato. 2. Mix with sweetcorn and flour. 3. Shape and pan-fry.", new String[][]{{"potato","2","pcs"},{"sweetcorn","60","g"},{"flour","40","g"}});
            db.setTransactionSuccessful();
        } finally { db.endTransaction(); }
    }
}
