package com.rakursy.timetable.data;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import com.rakursy.timetable.model.Grade;
import com.rakursy.timetable.model.Room;
import com.rakursy.timetable.model.SchoolClass;
import com.rakursy.timetable.model.SchoolDay;
import com.rakursy.timetable.model.SchoolHour;
import com.rakursy.timetable.model.Subject;
import com.rakursy.timetable.model.Teacher;
import com.rakursy.timetable.util.qualifiers.Created;
import com.rakursy.timetable.util.qualifiers.Deleted;
import com.rakursy.timetable.util.qualifiers.Updated;

@RequestScoped
public class EntityListProducer implements Serializable {

	private static final long serialVersionUID = -6649938298097056651L;

	@Inject
	private EntityManager em;

	private List<Grade> grades;
	private List<Room> rooms;
	private List<Subject> subjects;
	private List<Teacher> teachers;
	private List<SchoolClass> schoolClasses;
	private List<SchoolHour> schoolHours;
	private List<SchoolDay> schoolDays;
	
	@Produces
	@Named
	public List<Grade> getGrades() {
		return grades;
	}
	
	@Produces
	@Named
	public List<Room> getRooms() {
		return rooms;
	}
	
	@Produces
	@Named
	public List<Subject> getSubjects() {
		return subjects;
	}
	
	@Produces
	@Named
	public List<Teacher> getTeachers() {
		return teachers;
	}
	
	@Produces
	@Named
	public List<SchoolClass> getSchoolClasses() {
		return schoolClasses;
	}
	
	@Produces
	@Named
	public List<SchoolHour> getSchoolHours() {
		return schoolHours;
	}
	
	@Produces
	@Named
	public List<SchoolDay> getSchoolDays() {
		return schoolDays;
	}
	
	public void entityCreated(@Observes @Created Object o) {
		retrieveEntites();
	}
	
	public void entityUpdated(@Observes @Updated Object o) {
		retrieveEntites();
	}
	
	public void entityDeleted(@Observes @Deleted Object o) {
		retrieveEntites();
	}
	
	@SuppressWarnings("unchecked")
	@PostConstruct
	public void retrieveEntites() {
		grades = em.createQuery("select g from Grade g").getResultList();
		rooms = em.createQuery("select r from Room r").getResultList();
		subjects = em.createQuery("select s from Subject s").getResultList();
		teachers = em.createQuery("select t from Teacher t").getResultList();
		schoolClasses = em.createQuery("select sc from SchoolClass sc").getResultList();
		schoolHours = em.createQuery("select sh from SchoolHour sh").getResultList();
		schoolDays = em.createQuery("select sd from SchoolDay sd").getResultList();
	}
	
}
