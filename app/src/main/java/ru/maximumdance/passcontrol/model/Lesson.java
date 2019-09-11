package ru.maximumdance.passcontrol.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import ru.maximumdance.passcontrol.model.util.DateConverter;

@Entity
@Table(name = "lessons")
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;


    @ManyToOne(fetch = FetchType.LAZY, optional = false,  cascade = CascadeType.MERGE)
    @JoinColumn(name = "courselevel_id", referencedColumnName = "id")
    CourseLevel courselevel;

    @Column
    Date date;

    @Column
    String name="a";

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


    public CourseLevel getCourselevel() {
        return courselevel;
    }

    public void setCourselevel(CourseLevel courselevel) {
        this.courselevel = courselevel;
    }


    @Override
    public String toString() {
        return  courselevel.getName() + " " + DateConverter.toString(date);
    }
}
