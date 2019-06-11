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

import ru.maximumdance.passcontrol.model.util.DateConverter;

@Entity
@Table(name = "pass")
public class Pass implements Parcelable {

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


    protected Pass(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        course = in.readParcelable(Course.class.getClassLoader());
        person = in.readParcelable(Person.class.getClassLoader());
        if (in.readByte() == 0) {
            itemCount = null;
        } else {
            itemCount = in.readInt();
        }
        if (in.readByte() == 0) {
            currentItemCount = null;
        } else {
            currentItemCount = in.readInt();
        }

        try {
            launchDate = DateConverter.fromString(in.readString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            terminateDate = DateConverter.fromString(in.readString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        lessons = new HashSet<>(in.createTypedArrayList(Lesson.CREATOR));

    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (id == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(id);
        }
        parcel.writeParcelable(course, i);
        parcel.writeParcelable(person, i);
        if (itemCount == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(itemCount);
        }
        if (currentItemCount == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(currentItemCount);
        }
        parcel.writeString(DateConverter.toString(launchDate));
        parcel.writeString(DateConverter.toString(terminateDate));
        parcel.writeTypedList(new ArrayList<>(lessons));


    }


    public static final Creator<Pass> CREATOR = new Creator<Pass>() {
        @Override
        public Pass createFromParcel(Parcel in) {
            return new Pass(in);
        }

        @Override
        public Pass[] newArray(int size) {
            return new Pass[size];
        }
    };

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
    public int describeContents() {
        return 0;
    }


}
