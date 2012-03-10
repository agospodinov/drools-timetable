package com.rakursy.timetable.data;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
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

@RequestScoped
public class EntityListProducer implements Serializable {

	private static final long serialVersionUID = -6649938298097056651L;

	@Inject
	private EntityManager em;

	private List<Grade> grades;
	private List<Room> rooms;
	private List<Subject> subjects;
	private List<Teacher> teachers;
	
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
	
	@SuppressWarnings("unchecked")
	@PostConstruct
	public void retrieveEntites() {
		grades = em.createQuery("select g from Grade g").getResultList();
		rooms = em.createQuery("select r from Room r").getResultList();
		subjects = em.createQuery("select s from Subject s").getResultList();
		teachers = em.createQuery("select t from Teacher t").getResultList();
	}
	
}
