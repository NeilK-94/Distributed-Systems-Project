package ie.gmit.ds;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {

    private int userId;
    private String userName;
    private String email;
    private String password;


    public User() {
        // Needed for Jackson deserialisation
    }

    public User(int userId, String userName, String email, String password) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    @JsonProperty
	public int getUserId() {
		return userId;
	}

    @JsonProperty
	public String getUserName() {
		return userName;
	}

    @JsonProperty
	public String getEmail() {
		return email;
	}

    @JsonProperty
	public String getPassword() {
		return password;
	}
}
