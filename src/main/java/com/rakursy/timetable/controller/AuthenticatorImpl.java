package com.rakursy.timetable.controller;

import java.util.List;

import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.seam.security.Authenticator;
import org.jboss.seam.security.BaseAuthenticator;
import org.jboss.seam.security.Credentials;
import org.picketlink.idm.impl.api.PasswordCredential;
import org.picketlink.idm.impl.api.model.SimpleUser;

import com.rakursy.timetable.model.User;

@SessionScoped
@Stateful
public class AuthenticatorImpl extends BaseAuthenticator implements Authenticator {

	@Inject
	private Credentials credentials;

	@PersistenceContext
	private EntityManager em;

	@SuppressWarnings("unchecked")
	@Override
	public void authenticate() {
		if (credentials.getCredential() instanceof PasswordCredential) {
			List<User> results = em
					.createQuery("from User u where u.username=:username and u.password=:password")
					.setParameter("username", credentials.getUsername())
					.setParameter("password", ((PasswordCredential) credentials.getCredential()).getValue())
					.getResultList();
			if (results.size() == 1) {
				setStatus(AuthenticationStatus.SUCCESS);
				setUser(new SimpleUser(credentials.getUsername()));
				return;
			}
		}
		setStatus(AuthenticationStatus.FAILURE);
	}
	

}
