package ru.maximumdance.passcontrol.model;
import android.os.Parcel;
import android.os.Parcelable;

import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import ru.maximumdance.passcontrol.model.util.DateConverter;

@Entity
@Table(name = "pass")
public class Pass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.MERGE)
    @JoinColumn(name = "course_id", referencedColumnName = "id")
    Course course;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "person_id")

    @JsonBackReference
    private
    Person person;

    @Column
    private
    Integer itemCount;

    @Column
    private
    Integer currentItemCount;

    @Column
    private
    Date launchDate;

    @Column
    private
    Date terminateDate;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "pass_lessons",
            joinColumns = @JoinColumn(name = "pass_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "lesson_id", referencedColumnName = "id"))
    private
    Set<Lesson> lessons  = new HashSet<>();


    public Pass(){};


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Integer getItemCount() {
        return itemCount;
    }

    public void setItemCount(Integer itemCount) {
        this.itemCount = itemCount;
        this.setCurrentItemCount(itemCount);
    }

    public Integer getCurrentItemCount() {
        return currentItemCount;
    }

    public void setCurrentItemCount(Integer currentItemCount) {
        this.currentItemCount = currentItemCount;
    }

    public Date getLaunchDate() {
        return launchDate;
    }

    public void setLaunchDate(Date launchDate) {
        this.launchDate = launchDate;
    }

    public Date getTerminateDate() {
        return terminateDate;
    }

    public void setTerminateDate(Date terminateDate) {
        this.terminateDate = terminateDate;
    }

    public Set<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(Set<Lesson> lessons) {
        this.lessons = lessons;
    }

    public void addLesson(Lesson lesson){
        lessons.add(lesson);
        currentItemCount--;
    }

    @Override
    public String toString() {
      return  getCourse().getName() + " " + getCurrentItemCount() + " / " + getItemCount() + " " + DateConverter.toString(this.launchDate) + "-" + DateConverter.toString(this.terminateDate);
    }
}
