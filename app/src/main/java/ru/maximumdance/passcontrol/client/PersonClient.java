package ru.maximumdance.passcontrol.client;

//import org.springframework.core.ParameterizedTypeReference;

import android.net.Uri;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import ru.maximumdance.passcontrol.model.Person;

public class PersonClient {


    private String baseURL = "https://passcontrol.herokuapp.com/persons/";

    private RestTemplate restTemplate = new RestTemplate();

    public Person getByCardNumber(String cardNumber) {
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        String url = baseURL + "/select";
        Uri.Builder uriBuilder = Uri.parse(url).buildUpon();
        uriBuilder.appendQueryParameter("cardNumber", cardNumber);
        String req = uriBuilder.build().toString();
        ResponseEntity<Person> responseEntity = restTemplate.getForEntity(req, Person.class);
        return responseEntity.getBody();
    }

    public Person getByName(String name) {
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        String url = baseURL + "/selectByName/"+ name;
        ResponseEntity<Person> responseEntity = restTemplate.getForEntity(url, Person.class);
        return responseEntity.getBody();
    }

    public void updatePerson(Person person) {
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.put(baseURL, Person.class);
    }

    public Person insertPerson(Person person) {
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        ResponseEntity<Person> responseEntity = restTemplate.postForEntity(baseURL, person, Person.class);
        return responseEntity.getBody();
    }



}
