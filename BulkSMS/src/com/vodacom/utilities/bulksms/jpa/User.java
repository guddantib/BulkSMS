package com.vodacom.utilities.bulksms.jpa;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The persistent class for the USER_LOGIN database table.
 * 
 */
@Entity
@Table(name = "USER_LOGIN")
@NamedQuery(name = "User.findAll", query = "SELECT u FROM User u")
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "USER_LOGIN_USERID_GENERATOR", sequenceName = "USER_LOGIN_SEQ")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_LOGIN_USERID_GENERATOR")
	@Column(name = "USER_ID")
	private long userId;

	@Column(name = "COMPANY")
	private String company;

	@Column(name = "EMAIL")
	private String email;

	@Column(name = "PASSWORD")
	private String password;

	@Column(name = "USER_NAME")
	private String userName;

	public User() {
	}

	public boolean isLoggedIn() {
		return true;
	}
	 
	/*public boolean isLoggedIn() {
	return user != null;
     }  */

	public long getUserId() {
		return this.userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getCompany() {
		return this.company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}