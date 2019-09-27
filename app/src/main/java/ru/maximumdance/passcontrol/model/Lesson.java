package ru.maximumdance.passcontrol.model;

import javax.persistence.*;

import java.util.Date;

import ru.maximumdance.passcontrol.model.util.DateConverter;

@Entity
@Table(name = "lessons")
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;


    @ManyToOne(fetch = FetchType.LAZY, optional = false,  cascade = CascadeType.MERGE)
    @JoinColumn(name = "courselevel_id", referencedColumnName = "id")
    CourseLevel courseLevel;

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


    public CourseLevel getCourseLevel() {
        return courseLevel;
    }

    public void setCourseLevel(CourseLevel courseLevel) {
        this.courseLevel = courseLevel;
    }


    @Override
    public String toString() {
        return  courseLevel.getName() + " " + DateConverter.toString(date);
    }
}
