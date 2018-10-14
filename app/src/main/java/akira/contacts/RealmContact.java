package akira.contacts;


import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class RealmContact extends RealmObject {


    private String name;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;

    }

    private String surname;

    public String getSurname() {
        return surname;
    }

    public void setSurname(final String surname) {
        this.surname = surname;
    }

    @PrimaryKey
    @Required
    private String phoneNum;

    public String getPhoneNum() {
        return phoneNum;
    }


    private Date bdate;

    public Date getBdate() {
        return bdate;
    }

    public void setBdate(final Date bdate) {
        this.bdate = bdate;
    }


    private String gender;

    public String getGender() {
        return gender;
    }

    public void setGender(final String gender) {
        this.gender = gender;
    }


}
