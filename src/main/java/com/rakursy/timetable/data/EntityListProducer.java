package com.rakursy.timetable.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.jboss.seam.security.Identity;

import com.rakursy.timetable.model.DayOfWeek;
import com.rakursy.timetable.model.Grade;
import com.rakursy.timetable.model.PeriodOffRequest;
import com.rakursy.timetable.model.Room;
import com.rakursy.timetable.model.Subject;
import com.rakursy.timetable.model.SubjectWeight;
import com.rakursy.timetable.model.Teacher;
import com.rakursy.timetable.model.Timetable;
import com.rakursy.timetable.model.User;

@RequestScoped
public class EntityListProducer implements Serializable {

	private static final long serialVersionUID = -6649938298097056651L;

	@Inject
	private EntityManager em;
	
	@Inject
	private Identity identity;
	
	private User user;

	private List<Grade> grades;
	private List<Room> rooms;
	private List<Subject> subjects;
	private List<Teacher> teachers;
	private List<PeriodOffRequest> periodOffRequests;
	
	private List<Timetable> timetables;
	
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
	public List<PeriodOffRequest> getPeriodOffRequests() {
		return periodOffRequests;
	}
	
	@Produces
	@Named
	public List<DayOfWeek> getDaysOfWeek() {
		return Arrays.asList(DayOfWeek.values());
	}
	
	@Produces
	@Named
	public List<Integer> getSchoolHourStartTimes() {
		return new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8));
	}
	
	@Produces
	@Named
	public List<SubjectWeight> getSubjectWeights() {
		return Arrays.asList(SubjectWeight.values());
	}
	
	@Produces
	@Named
	public List<Timetable> getTimetables() {
		return timetables;
	}
	
	@Produces
	@Named
	public User getLoggedInUser() {
		return user;
	}
	
	@SuppressWarnings("unchecked")
	@PostConstruct
	public void retrieveEntites() {
		user = (User) em.createQuery("select u from User u where u.username = :username")
				.setParameter("username", identity.getUser().getKey()).getSingleResult();
		
		grades = em.createQuery("select g from Grade g where g.school = :school").setParameter("school", user.getSchool()).getResultList();
		rooms = em.createQuery("select r from Room r where r.school = :school").setParameter("school", user.getSchool()).getResultList();
		subjects = em.createQuery("select s from Subject s where s.school = :school").setParameter("school", user.getSchool()).getResultList();
		teachers = em.createQuery("select t from Teacher t where t.school = :school").setParameter("school", user.getSchool()).getResultList();
		periodOffRequests = em.createQuery("select por from PeriodOffRequest por where por.school = :school").setParameter("school", user.getSchool()).getResultList();
		
		timetables = em.createQuery("select tt from Timetable tt where tt.school = :school").setParameter("school", user.getSchool()).getResultList();
	}
	
}
