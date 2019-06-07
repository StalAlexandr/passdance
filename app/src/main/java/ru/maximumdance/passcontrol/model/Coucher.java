package ru.maximumdance.passcontrol.model;
import javax.persistence.*;
@Entity
@Table(name = "couchers")
public class Coucher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column
    String firstName;

    @Column
    String lastName;

    @Column
    String midName;

    @Column
    String phoneNumber;

}
