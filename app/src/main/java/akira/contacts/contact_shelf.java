package akira.contacts;

import android.support.annotation.Nullable;

import butterknife.Optional;
import io.realm.RealmObject;
import io.realm.annotations.Required;

public class contact_shelf extends RealmObject {


    @Required
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
    public void setSurname(final String surname){
        this.surname = surname;
    }

//@Nullable
    private String phoneNum;

    public String getPhoneNum() {
        return phoneNum;
    }
    public void setPhoneNum(final String phoneNum){
        this.phoneNum = phoneNum;
    }
//@Nullable
    private String bdate;
    public String getBdate(){return bdate;}
    public void setBdate(final String bdate){this.bdate = bdate;}
//@Nullable
    private String gender;
    public String getGender(){return gender;}
    public void setGender(final String gender){this.gender = gender;}

}
