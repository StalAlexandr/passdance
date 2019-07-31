package ru.maximumdance.passcontrol.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import ru.maximumdance.passcontrol.model.Course;

public interface CourseApi {

    @GET("/courses/")
    Call<List<Course>> getAll();
}
