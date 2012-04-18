package com.rakursy.timetable.security;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.jboss.seam.security.AuthorizationException;
import org.jboss.solder.exception.control.CaughtException;
import org.jboss.solder.exception.control.Handles;
import org.jboss.solder.exception.control.HandlesExceptions;

@HandlesExceptions
public class ExceptionHandler {
	
	@Inject
	private FacesContext facesContext;
	
	public void handleAuthorizationException(@Handles CaughtException<AuthorizationException> e) {
		facesContext.addMessage(null, new FacesMessage("You don't have the necessary permissions to perform that operation"));
		e.handled();
	}
	
}
