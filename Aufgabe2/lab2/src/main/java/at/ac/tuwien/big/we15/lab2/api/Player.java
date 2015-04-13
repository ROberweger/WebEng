package at.ac.tuwien.big.we15.lab2.api;

import java.util.Date;

public class Player extends User {
	
	private String username;
	private String passwd;
	private String firstname;
	private String lastname;
	private Date birthdate;
	private EGender gender;
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPasswd() {
		return passwd;
	}
	
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	
	public String getFirstname() {
		return firstname;
	}
	
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	
	public String getLastname() {
		return lastname;
	}
	
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	
	public Date getBirthdate() {
		return birthdate;
	}
	
	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}
	
	public EGender getGender() {
		return gender;
	}
	
	public void setGender(EGender gender) {
		this.gender = gender;
	}

	public enum EGender{
		male,
		female,
		;
	}
}
