package com.vodacom.utilities.bulksms;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import org.primefaces.context.RequestContext;

import com.vodacom.utilities.bulksms.jpa.User;
import com.vodacom.utilities.bulksms.UserQuery;

@ManagedBean(name = "loginBean")
@SessionScoped
public class LoginBean implements Serializable, HttpSessionBindingListener {

	private static final long serialVersionUID = 1L;
	private static Map<LoginBean, HttpSession> logins = new HashMap<LoginBean, HttpSession>();
	@Inject
	HttpSession session;

	@Inject
	public UserQuery query;

	private User user = new User();
	
	public LoginBean(){
		
	}
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String userName;

	public String password;

	public Date currentDate;

	@PostConstruct
	public void init() {
//		user = new User();
		currentDate = new Date(System.currentTimeMillis());
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(Date currentDate) {
		this.currentDate = currentDate;
	}

	public String redirectToIndex() {
		return "/index.xhtml?faces-redirect=true";
	}

	public String toLogin() {
		return "/login.xhtml";
	}
	
public String login() throws Exception, CustomException {
		RequestContext context = RequestContext.getCurrentInstance();
		boolean loggedIn = false;

		User login = query.getUser(userName, password);

		if (login != null) {// user is valid and validated from database etc...
			System.out.println("create user: " + login.getUserName() + ", saving user object to session ");
			loggedIn = true;
		//	HttpSession session = Util.getSession();
			session.setAttribute("userName", userName);// this will invoke valueBound method of LoginBean.
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("userName", userName);
			if (login.isLoggedIn()) {
				session.removeAttribute("userName"); // this will invoke valueUnbound method of LoginDto
				throw new CustomException("You are already logged in from a different session. Please logout first.");
			}
			FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
		} else {
			loggedIn = false;
			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_WARN, "Incorrect Login Credentials", null));
			return toLogin();
		}
		context.addCallbackParam("Logged In", loggedIn);
		return "";
	}

	public void valueBound(HttpSessionBindingEvent event) {
		// Removing old session of user and let the user log-in from new session.
		System.out.println("valueBound:" + event.getName() + " session:" + event.getSession().getId());
		session = logins.remove(this);
		if (session != null) {
			session.invalidate();
		}
		logins.put(this, event.getSession());
		// System.out.println("session saved:" + logins.put(this, event.getSession()));
	}

	@Override
	public void valueUnbound(HttpSessionBindingEvent event) {
		System.out.println("valueUnbound:" + event.getName() + " session:" + event.getSession().getId());
		logins.remove(this);
	}

	public String logout() {
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		return redirectToIndex();
	}
}