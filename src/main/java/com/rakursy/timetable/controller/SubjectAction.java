package com.rakursy.timetable.controller;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.jboss.logging.Logger;
import org.jboss.solder.logging.Category;
import org.jboss.solder.servlet.http.RequestParam;

import com.rakursy.timetable.model.Subject;
import com.rakursy.timetable.util.qualifiers.Created;

@Model
public class SubjectAction {

	@Inject
	@Category("timetable")
	private Logger log;

	@Inject
	private EntityManager em;

	@Inject
	private Event<Subject> subjectEventSrc;
	
	@Inject
	@RequestParam("id")
	private Instance<String> idProvider;
	
	private Subject newSubject;

	@Produces
	@Named
	public Subject getNewSubject() {
		return newSubject;
	}
	
	@PostConstruct
	public void load() {
		if (idProvider.get() != null) {
			newSubject = em.find(Subject.class, Long.parseLong(idProvider.get()));
		} else {
			newSubject = new Subject();
		}
	}

	@SuppressWarnings("serial")
	public String save() {
		log.info("Saving " + newSubject.getName());
		
		if (em.contains(newSubject)) {
			em.flush();
		} else {
			em.persist(newSubject);
		}
		
		subjectEventSrc.select(new AnnotationLiteral<Created>() {}).fire(newSubject);
		load();
		
		return "success";
	}
	
	public String cancel() {
		load();
		return "success";
	}

}
