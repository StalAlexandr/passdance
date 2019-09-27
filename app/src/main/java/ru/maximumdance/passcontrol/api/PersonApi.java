package ru.maximumdance.passcontrol.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import ru.maximumdance.passcontrol.model.Pass;
import ru.maximumdance.passcontrol.model.Person;

public interface PersonApi {


    @GET("/persons/")
    Call<List<Person>> getAll();

    @GET("/persons/selectByName/{name}")
    Call<List<Person>> getByName(@Path("name") String name);

    @GET("/persons/{id}")
    Call<Person> getById(@Path("id") Integer id);

    @GET("/persons/select")
    Call<Person> getByCardNumber(@Query("cardNumber") Integer cardNumber);

    @POST("/persons/")
    Call<Person> create(@Body Person person);

    @PUT("/persons/")
    Call<Person> update(@Body Person person);

    @POST("/persons/{id}/pass/")
    Call<Person> addPass(@Path("id") Integer id, @Body Pass person);

    @PUT("/persons/{id}/pass/")
    Call<Person> updatePass(@Path("id") Integer id, @Body Pass person);

    @POST("/pass/{id}/lesson/")
    Call<Person> addLesson(@Path("id") Integer id, @Body Object lesson);

    @DELETE("/lessons/{id}/")
    Call<Person> removeLesson(@Path("id") Long id);

    @DELETE("/persons/{id}/pass/{passId}")
    Call<Person> deletePass(@Path("id") Integer id,  @Path("passId") Integer passId);
}
