package za.co.smartpantry.util;

import za.co.smartpantry.model.PantryItem;
import za.co.smartpantry.model.Recipe;
import za.co.smartpantry.model.RecipeIngredient;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/** Strict matcher: every recipe ingredient must exist in sufficient quantity. */
public final class IngredientMatcher {
    private IngredientMatcher() {}

    public static String normaliseName(String name) {
        String value = name == null ? "" : name.trim().toLowerCase(Locale.ROOT);
        // Small, deliberate plural normalisation for common English ingredient names.
        if (value.endsWith("ies") && value.length() > 4) value = value.substring(0, value.length()-3) + "y";
        else if (value.endsWith("oes") && value.length() > 4) value = value.substring(0, value.length()-2);
        else if (value.endsWith("s") && !value.endsWith("ss") && value.length() > 3) value = value.substring(0, value.length()-1);
        return value;
    }

    public static boolean canMake(Recipe recipe, List<PantryItem> pantry) {
        Map<String, PantryItem> available = new HashMap<>();
        for (PantryItem item : pantry) available.put(normaliseName(item.getName()), item);

        for (RecipeIngredient required : recipe.getIngredients()) {
            PantryItem owned = available.get(normaliseName(required.getName()));
            if (owned == null) return false;
            double availableAmount = convertToBase(owned.getQuantity(), owned.getUnit());
            double requiredAmount = convertToBase(required.getQuantity(), required.getUnit());
            if (availableAmount < 0 || requiredAmount < 0 || !sameDimension(owned.getUnit(), required.getUnit())) return false;
            if (availableAmount + 0.000001 < requiredAmount) return false;
        }
        return true;
    }

    private static boolean sameDimension(String a, String b) {
        return dimension(a).equals(dimension(b));
    }

    private static String dimension(String unit) {
        String u = unit.toLowerCase(Locale.ROOT);
        if (u.equals("g") || u.equals("kg")) return "mass";
        if (u.equals("ml") || u.equals("l")) return "volume";
        if (u.equals("pcs") || u.equals("piece") || u.equals("pieces") || u.equals("slices") || u.equals("cloves") || u.equals("can")) return u;
        return "unknown";
    }

    private static double convertToBase(double amount, String unit) {
        String u = unit.toLowerCase(Locale.ROOT);
        if (u.equals("kg")) return amount * 1000.0;
        if (u.equals("l")) return amount * 1000.0;
        if (dimension(u).equals("mass") || dimension(u).equals("volume") ||
                dimension(u).equals("pcs") || dimension(u).equals("piece") ||
                dimension(u).equals("pieces") || dimension(u).equals("slices") ||
                dimension(u).equals("cloves") || dimension(u).equals("can")) return amount;
        return -1;
    }
}
