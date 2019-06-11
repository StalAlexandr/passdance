package ru.maximumdance.passcontrol.model.util;

import ru.maximumdance.passcontrol.model.Person;

public class PersonValidator {

public static void validate(Person person) throws PersonValidationException{

    if ((person.getFirstName()==null)||(person.getFirstName().length()<1)){
        throw  new PersonValidationException("Не задано имя");
    }

    if ((person.getLastName()==null)||person.getLastName().length()<1){
        throw  new PersonValidationException("Не задана фамилия");
    }


    if (person.getCardNumber()==null){
        throw  new PersonValidationException("Не задан номер карты");
    }

}

}
