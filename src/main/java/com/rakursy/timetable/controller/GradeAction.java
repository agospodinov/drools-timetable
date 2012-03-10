package com.rakursy.timetable.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.jboss.logging.Logger;
import org.jboss.solder.logging.Category;
import org.jboss.solder.servlet.http.RequestParam;

import com.rakursy.timetable.model.Grade;
import com.rakursy.timetable.model.GradeSubject;
import com.rakursy.timetable.model.StudentGroup;
import com.rakursy.timetable.model.Subject;
import com.rakursy.timetable.model.Teacher;
import com.rakursy.timetable.util.qualifiers.Created;

@Stateful
@ConversationScoped
@Named
public class GradeAction {
	
	@Inject
	@Category("timetable")
	private Logger log;
	
	@Inject
	private Conversation conversation;

	@Inject
	private EntityManager em;

	@Inject
	private Event<Grade> gradeEventSrc;
	
	@Inject
	@RequestParam("id")
	private Instance<String> idProvider;
	
	private Grade newGrade;

	private List<Subject> subjects = new ArrayList<Subject>();
	private List<GradeSubject> gradeSubjects = new ArrayList<GradeSubject>();
	private List<StudentGroup> studentGroups = new ArrayList<StudentGroup>();
	
	public List<Subject> getSubjects() {
		return subjects;
	}

	public void setSubjects(List<Subject> subjects) {
		this.subjects = subjects;
	}

	public List<GradeSubject> getGradeSubjects() {
		return gradeSubjects;
	}

	public void setGradeSubjects(List<GradeSubject> gradeSubjects) {
		this.gradeSubjects = gradeSubjects;
	}
	
	public List<StudentGroup> getStudentGroups() {
		return studentGroups;
	}

	public void setStudentGroups(List<StudentGroup> studentGroups) {
		this.studentGroups = studentGroups;
	}

	@SuppressWarnings("unchecked")
	public List<Teacher> getTeachersForSubject(Subject subject) {
		return em.createQuery("select t from Teacher t where :subject member of t.subjects")
				.setParameter("subject", subject)
				.getResultList();
	}

	@Produces
	@Named
	public Grade getNewGrade() {
		return newGrade;
	}
	
	public String generateMaps() {
		for (Subject subject : subjects) {
			GradeSubject gradeSubject = new GradeSubject();
			gradeSubject.setGrade(newGrade);
			gradeSubject.setSubject(subject);
			gradeSubjects.add(gradeSubject);
		}
		
		return "next";
		
//		return "gradeSubjects.xhtml";
	}
	
	public String splitIntoGroups() {
		for (GradeSubject gradeSubject : gradeSubjects) {
			if (gradeSubject.getTeachers().size() > 1) {
				for (Teacher teacher : gradeSubject.getTeachers()) {
					StudentGroup studentGroup = new StudentGroup(gradeSubject.getSubject(), teacher);
					studentGroup.setGrade(newGrade);
					studentGroup.setWholeGrade(false);
					studentGroups.add(studentGroup);
				}
			}
		}
		
		if (studentGroups.size() == 0) {			
			// There's nothing extra to do...
			return save();
		}
		
		return "next";
		
//		return "gradeGroups.xhtml";
	}
	
	@PostConstruct
	public void load() {
		conversation.begin();
		
		if (idProvider.get() != null) {
			newGrade = em.find(Grade.class, Long.parseLong(idProvider.get()));
		} else {
			newGrade = new Grade();
		}
	}

	@SuppressWarnings("serial")
	public String save() {
		log.info("Saving " + newGrade.getName());
		
		newGrade.setSubjects(gradeSubjects);
		
		// Don't forget the ones that are the whole grades
		for (GradeSubject gradeSubject : gradeSubjects) {
			if (gradeSubject.getTeachers().size() == 1) {
				StudentGroup studentGroup = new StudentGroup(gradeSubject.getSubject(), gradeSubject.getTeachers().get(0));
				studentGroup.setGrade(newGrade);
				studentGroup.setStudentCount(newGrade.getStudentCount());
				studentGroup.setWholeGrade(true);
				studentGroups.add(studentGroup);
			}
		}
		
		newGrade.setStudentGroups(studentGroups);
		
		if (em.contains(newGrade)) {
			em.merge(newGrade);
		} else {
			em.persist(newGrade);
		}
		
		gradeEventSrc.select(new AnnotationLiteral<Created>() {}).fire(newGrade);
		
		conversation.end();
//		load();
		
		return "success";
	}
	
	public String cancel() {
		conversation.end();
//		load();
		return "success";
	}

}
