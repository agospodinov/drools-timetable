package com.rakursy.timetable.exceptions;

import java.io.IOException;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.application.ViewExpiredException;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.hibernate.exception.ConstraintViolationException;
import org.jboss.logging.Logger;
import org.jboss.seam.security.AuthorizationException;
import org.jboss.solder.exception.control.CaughtException;
import org.jboss.solder.exception.control.Handles;
import org.jboss.solder.exception.control.HandlesExceptions;
import org.jboss.solder.logging.Category;

@HandlesExceptions
public class ExceptionHandler {

	@Inject
	@Category("exceptions")
	private Logger log;

	@Inject
	private FacesContext facesContext;

	public void handleAuthorizationException(@Handles CaughtException<AuthorizationException> e) {
		log.warn("Handled AuthorizationException!");
		ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "messages");
		facesContext.addMessage(null, new FacesMessage(bundle.getString("accessDenied")));
		redirect("/errors/denied.jsf");
		e.handled();
	}

	public void handleConstraintViolationException(@Handles CaughtException<ConstraintViolationException> e) {
		log.warn("Handled ConstraintViolationException!");
		ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "messages");
		facesContext.addMessage(null, new FacesMessage(bundle.getString("constraintViolation")));
		e.handled();
	}
	
	public void handleViewExpiredException(@Handles CaughtException<ViewExpiredException> e) {
		log.warn("Handled ViewExpiredException!");
		ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "messages");
		facesContext.addMessage(null, new FacesMessage(bundle.getString("sessionExpired")));
		redirect("/errors/sessionExpired.jsf");
		e.handled();
	}

	private void redirect(final String viewId) {
		try {
			String basePath = facesContext.getExternalContext().getRequestContextPath();
			facesContext.getExternalContext().redirect(basePath + viewId);
		} catch (final IOException e) {
			log.warn("Redirect failed", e);
		}
	}

}
