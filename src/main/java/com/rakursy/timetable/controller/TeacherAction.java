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

import com.rakursy.timetable.model.Teacher;
import com.rakursy.timetable.util.qualifiers.Created;

@Model
public class TeacherAction {
	
	@Inject
	@Category("timetable")
	private Logger log;

	@Inject
	private EntityManager em;

	@Inject
	private Event<Teacher> teacherEventSrc;
	
	@Inject
	@RequestParam("id")
	private Instance<String> idProvider;
	
	private Teacher newTeacher;

	@Produces
	@Named
	public Teacher getNewTeacher() {
		return newTeacher;
	}
	
	@PostConstruct
	public void load() {
		if (idProvider.get() != null) {
			newTeacher = em.find(Teacher.class, Long.parseLong(idProvider.get()));
		} else {
			newTeacher = new Teacher();
		}
	}

	@SuppressWarnings("serial")
	public String save() {
		log.info("Saving " + newTeacher.getName());
		
		if (em.contains(newTeacher)) {
			em.flush();
		} else {
			em.persist(newTeacher);
		}
		
		teacherEventSrc.select(new AnnotationLiteral<Created>() {}).fire(newTeacher);
		load();
		
		return "success";
	}
	
	public String cancel() {
		load();
		return "success";
	}
	
}
