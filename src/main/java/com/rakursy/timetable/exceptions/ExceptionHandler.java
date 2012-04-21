package com.rakursy.timetable.exceptions;

import java.io.IOException;

import javax.faces.application.ViewExpiredException;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.hibernate.exception.ConstraintViolationException;
import org.jboss.logging.Logger;
import org.jboss.seam.international.status.Messages;
import org.jboss.seam.international.status.builder.BundleKey;
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
	private Messages messages;

	@Inject
	private FacesContext facesContext;

	public void handleAuthorizationException(@Handles CaughtException<AuthorizationException> e) {
		log.warn("Handled AuthorizationException!");
		messages.error(new BundleKey("messages", "accessDenied"));
		redirect("/errors/denied.jsf");
		e.handled();
	}

	public void handleConstraintViolationException(@Handles CaughtException<ConstraintViolationException> e) {
		log.warn("Handled ConstraintViolationException!");
		messages.error(new BundleKey("messages", "cannotRemove"));
		e.handled();
	}
	
	public void handleViewExpiredException(@Handles CaughtException<ViewExpiredException> e) {
		log.warn("Handled ViewExpiredException!");
		messages.error(new BundleKey("messages", "sessionExpired"));
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
