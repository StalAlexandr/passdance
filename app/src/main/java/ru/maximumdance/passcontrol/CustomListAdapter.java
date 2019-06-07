package ru.maximumdance.passcontrol;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import ru.alexandrstal.passdance.R;

public class CustomListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] itemName;
    private final Integer[] imgId;

    CustomListAdapter(Activity context, String[] itemName, Integer[] imgId) {
        super(context, R.layout.menulist, itemName);
        this.context = context;
        this.itemName = itemName;
        this.imgId = imgId;
    }

    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.menulist, null, true);
        rowView.setForegroundGravity(Gravity.CENTER_HORIZONTAL);
        TextView txtTitle = rowView.findViewById(R.id.item);
        ImageView imageView = rowView.findViewById(R.id.icon);
        txtTitle.setText(itemName[position]);
        imageView.setImageResource(imgId[position]);
        return rowView;
    }
}