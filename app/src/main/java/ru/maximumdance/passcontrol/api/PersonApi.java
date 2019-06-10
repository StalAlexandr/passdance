package ru.maximumdance.passcontrol.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import ru.maximumdance.passcontrol.model.Person;

public interface PersonApi {


    @GET("/persons/")
    Call<List<Person>> getAll();

    @GET("/persons/selectByName/{name}")
    Call<List<Person>>  getByName(@Path("name") String name);

    @GET("/persons/{id}")
    Call<Person> getById(@Path("id") Integer id);

    @POST("/persons/")
    Call<Person> create(@Body Person person);
}
