package za.co.smartpantry.model;

import java.util.List;

public class Recipe {
    private final int id;
    private final String name;
    private final String method;
    private final List<RecipeIngredient> ingredients;

    public Recipe(int id, String name, String method, List<RecipeIngredient> ingredients) {
        this.id = id; this.name = name; this.method = method; this.ingredients = ingredients;
    }
    public int getId() { return id; }
    public String getName() { return name; }
    public String getMethod() { return method; }
    public List<RecipeIngredient> getIngredients() { return ingredients; }
}
