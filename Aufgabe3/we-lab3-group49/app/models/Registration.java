package models;

/**
 * Created by Obermair on 05.05.2015.
 */

import at.ac.tuwien.big.we15.lab2.api.Avatar;
import play.data.validation.ValidationError;
import play.db.ebean.*;
import play.data.format.*;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import play.data.validation.Constraints;


@Entity
public class Registration{

    public enum Gender {male, female}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Avatar avatar;
    private String firstName;
    private String lastName;
    @Formats.DateTime(pattern="dd/MM/yyyy")
    private Date birthDate;

    @Constraints.Required
    @Constraints.MinLength(4)
    @Constraints.MaxLength(8)
    private String userName;
    @Constraints.Required
    @Constraints.MinLength(4)
    @Constraints.MaxLength(8)
    private String password;
    private Gender gender;

    public List<ValidationError> validate() {
        List<ValidationError> errors = null;
        if(userName.length()<=8 && userName.length()>=4){
            if(errors== null) {
                errors = new ArrayList<ValidationError>();
            }
            errors.add(new ValidationError("username",
                    "Muss zwischen 4 und 8 zeichen lang sein"));
        }
        if(password.length()<=8 && password.length()>=4){
            if(errors== null) {
                errors = new ArrayList<ValidationError>();
            }
            errors.add(new ValidationError("password",
                    "Muss zwischen 4 und 8 zeichen lang sein"));
        }
        return errors;
    }



    public Registration() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Avatar getAvatar() {
        return avatar;
    }

    public void setAvatar(Avatar avatar) {

        this.avatar = avatar;
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

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getUserName() {

        return userName;
    }

    public void setUserName(String userName) {


    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if(password.length()<=8 && password.length()>= 4) {
            this.password = password;
        }
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }
    public boolean comparePassword(String password){
        if(this.password.equals(password)){
            return true;
        }
        return  false;
    }
}
