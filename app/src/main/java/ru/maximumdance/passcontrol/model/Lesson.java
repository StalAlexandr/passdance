package ru.maximumdance.passcontrol.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import ru.maximumdance.passcontrol.model.util.DateConverter;

@Entity
@Table(name = "lessons")
public class Lesson implements Parcelable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false,  cascade = CascadeType.MERGE)
    @JoinColumn(name = "course_id", referencedColumnName = "id")
    Course course;

    @Column
    Date date;

    @Column
    String name;

    @JsonBackReference
    @ManyToMany(mappedBy = "lessons")
    Set<Pass> passes = new HashSet<>();


    protected Lesson(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        try {
            date = DateConverter.fromString(in.readString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        name = in.readString();
        course = in.readParcelable(Course.class.getClassLoader());
        passes = new HashSet(in.createTypedArrayList(Pass.CREATOR));
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (id == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(id);
        }
        parcel.writeString(DateConverter.toString(date));
        parcel.writeString(name);
        parcel.writeParcelable(course, i);
        parcel.writeTypedList(new ArrayList<>(passes));

    }

    public static final Creator<Lesson> CREATOR = new Creator<Lesson>() {
        @Override
        public Lesson createFromParcel(Parcel in) {
            return new Lesson(in);
        }

        @Override
        public Lesson[] newArray(int size) {
            return new Lesson[size];
        }
    };

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Set<Pass> getPasses() {
        return passes;
    }

    public void setPasses(Set<Pass> passes) {
        this.passes = passes;
    }


    @Override
    public int describeContents() {
        return 0;
    }


}
