package ru.maximumdance.passcontrol.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

import ru.maximumdance.passcontrol.model.util.DateConverter;

@Entity
@Table(name = "persons")
public class Person  implements Parcelable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(unique = true)
    Integer cardNumber;

    @Column
    String firstName;

    @Column
    String lastName;

    @Column
    String midName;

    @Column
    String phoneNumber;

    @Column
    String comment;

    @Column
    Date birthDate;

    @Column
    Date regDate;

    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            mappedBy = "person")
    List<Pass> passes = new ArrayList<>();

   // @Version
   // private Long version;

    public  Person(){}


    protected Person(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        if (in.readByte() == 0) {
            cardNumber = null;
        } else {
            cardNumber = in.readInt();
        }
        firstName = in.readString();
        lastName = in.readString();
        midName = in.readString();
        phoneNumber = in.readString();
        comment = in.readString();
        try {
            birthDate = DateConverter.fromString(in.readString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        passes = in.createTypedArrayList(Pass.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        if (cardNumber == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(cardNumber);
        }
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(midName);
        dest.writeString(phoneNumber);
        dest.writeString(comment);
        dest.writeString(DateConverter.toString(birthDate));
        dest.writeTypedList(passes);
    }

    public static final Creator<Person> CREATOR = new Creator<Person>() {
        @Override
        public Person createFromParcel(Parcel in) {
            return new Person(in);
        }

        @Override
        public Person[] newArray(int size) {
            return new Person[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(Integer cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMidName() {
        return midName;
    }

    public void setMidName(String midName) {
        this.midName = midName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Date getRegDate() {
        return regDate;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }

    public List<Pass> getPasses() {
        return passes;
    }

    public void setPasses(List<Pass> passes) {
        this.passes = passes;
    }

    public void addPass(Pass pass){
        passes.add(pass);
        pass.setPerson(this);
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", cardNumber=" + cardNumber +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", midName='" + midName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", comment='" + comment + '\'' +
                ", birthDate=" + birthDate +
                ", regDate=" + regDate +
                ", passes=" + passes +
                '}';
    }


}
