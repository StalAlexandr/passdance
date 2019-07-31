package ru.maximumdance.passcontrol.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Spinner;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.maximumdance.passcontrol.R;
import ru.maximumdance.passcontrol.listadapter.CourseAdapter;
import ru.maximumdance.passcontrol.model.Course;
import ru.maximumdance.passcontrol.model.CourseLevel;

public class LessonReduceDialog extends Dialog {

    Context context;

    @BindView(R.id.courseLevels)
    Spinner courseLevels;

    private CourseLevel[] levels;

    public LessonReduceDialog(@NonNull Context context, CourseLevel[] levels) {
        super(context);
        this.levels = levels;
        this.context = context;

        View view = View.inflate(getContext(), R.layout.lesson_reduce_dialog, null);
        ButterKnife.bind(this, view);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.lesson_reduce_dialog);

        /*
        CourseLevelAdapter adapter = new CourseLevelAdapter(context,levels);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseLevels.setAdapter(adapter);
*/
        Course[] courses = new Course[1];
        courses[0] = new Course();
        CourseAdapter adapter = new CourseAdapter(context,courses);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseLevels.setAdapter(adapter);
    }




}
