package com.rakursy.timetable.security;

import org.jboss.seam.faces.rewrite.FacesRedirect;
import org.jboss.seam.faces.security.AccessDeniedView;
import org.jboss.seam.faces.security.LoginView;
import org.jboss.seam.faces.view.config.ViewConfig;
import org.jboss.seam.faces.view.config.ViewPattern;
import org.jboss.seam.security.annotations.LoggedIn;

@ViewConfig
public interface Pages {

	static enum Views {
		
		@FacesRedirect
		@ViewPattern("/login.xhtml")
		LOGIN,

		@FacesRedirect
		@ViewPattern("/private/*")
		@AccessDeniedView("/denied.xhtml")
		@LoginView("/login.xhtml")
		@LoggedIn
		ALL;

	}

}
