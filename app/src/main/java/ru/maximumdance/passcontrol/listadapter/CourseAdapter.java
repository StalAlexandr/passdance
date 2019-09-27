package ru.maximumdance.passcontrol.listadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import ru.maximumdance.passcontrol.R;
import ru.maximumdance.passcontrol.model.Course;

public class CourseAdapter extends ArrayAdapter<Course> {

    private static class ViewHolder {
        private TextView itemView;
    }

    int resource;


    public CourseAdapter(@NonNull Context context, @NonNull Course[] objects) {
        super(context, R.layout.course_item, objects);
        resource = R.layout.course_item;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext())
                    .inflate(resource, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.itemView = (TextView) convertView.findViewById(R.id.courseView);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Course item = getItem(position);
        if (item != null) {
            viewHolder.itemView.setText(String.format("%s", item.getName()));
        }

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }

}
