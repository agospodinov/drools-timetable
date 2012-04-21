package com.rakursy.timetable.util;


import javax.enterprise.context.ConversationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import org.jboss.logging.Logger;
import org.jboss.seam.international.locale.DefaultLocale;
import org.jboss.solder.core.ExtensionManaged;

/**
 * This class uses CDI to alias Java EE resources, such as the persistence
 * context, to CDI beans
 * 
 * <p>
 * Example injection on a managed bean field:
 * </p>
 * 
 * <pre>
 * &#064;Inject
 * private EntityManager em;
 * </pre>
 */
public class Resources {
	
	@SuppressWarnings("unused")
	@Produces
	@DefaultLocale
	private String defaultLocaleKey = "bg_BG";
	
	@SuppressWarnings("unused")
	@ExtensionManaged
	@Produces
	@PersistenceUnit
	@ConversationScoped
	private EntityManagerFactory emf;
	
	@Produces
	public Logger produceLog(InjectionPoint injectionPoint) {
		return Logger.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
	}
	
}
