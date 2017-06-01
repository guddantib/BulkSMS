package com.vodacom.utilities.bulksms;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import com.vodacom.utilities.bulksms.jpa.User;

public class UserQuery {

	@PersistenceContext
	public EntityManager em;

	public User getUser(String userName, String password) {
		try {
			User user = (User) em
					.createQuery("SELECT u FROM User u WHERE u.userName=:userName AND u.password=:password")
					.setParameter("userName", userName).setParameter("password", password).getSingleResult();
			return user;
		} catch (NoResultException e) {
			return null;
		}
	}
}