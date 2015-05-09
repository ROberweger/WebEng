package models;

import play.data.validation.Constraints.Required;

public class Login {
	public String username;
	public String password;
	
	public String validate() {
		//TODO: validate username and password
		return null;
	}
}
