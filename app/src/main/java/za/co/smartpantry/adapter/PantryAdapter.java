package za.co.smartpantry.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import za.co.smartpantry.R;
import za.co.smartpantry.model.PantryItem;
import java.util.List;

public class PantryAdapter extends ArrayAdapter<PantryItem> {
    public PantryAdapter(Context context, List<PantryItem> items) { super(context, 0, items); }
    @Override public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) row = LayoutInflater.from(getContext()).inflate(R.layout.row_ingredient, parent, false);
        PantryItem item = getItem(position);
        ((TextView) row.findViewById(R.id.ingredientName)).setText(item.getName());
        ((TextView) row.findViewById(R.id.ingredientQuantity)).setText(format(item.getQuantity()) + " " + item.getUnit());
        TextView expiry = row.findViewById(R.id.ingredientExpiry);
        expiry.setText(item.getExpiryDate().isEmpty() ? "No expiry date" : "Expires: " + item.getExpiryDate());
        return row;
    }
    private String format(double value) { return value == Math.floor(value) ? String.valueOf((long)value) : String.valueOf(value); }
}
