package za.co.smartpantry.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import za.co.smartpantry.R;
import za.co.smartpantry.model.Recipe;
import za.co.smartpantry.model.RecipeIngredient;
import java.util.List;

public class RecipeAdapter extends ArrayAdapter<Recipe> {
    public RecipeAdapter(Context context, List<Recipe> recipes) { super(context, 0, recipes); }
    @Override public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) row = LayoutInflater.from(getContext()).inflate(R.layout.row_recipe, parent, false);
        Recipe recipe = getItem(position);
        ((TextView) row.findViewById(R.id.recipeName)).setText(recipe.getName());
        StringBuilder summary = new StringBuilder();
        for (RecipeIngredient ingredient : recipe.getIngredients()) {
            if (summary.length() > 0) summary.append(" • ");
            summary.append(ingredient.getName());
        }
        ((TextView) row.findViewById(R.id.recipeSummary)).setText(summary.toString());
        return row;
    }
}
