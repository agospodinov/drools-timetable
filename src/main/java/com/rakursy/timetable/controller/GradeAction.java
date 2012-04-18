package com.rakursy.timetable.controller;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateful;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

import com.rakursy.timetable.model.Grade;
import com.rakursy.timetable.model.GradeSubject;
import com.rakursy.timetable.model.StudentGroup;
import com.rakursy.timetable.model.Subject;
import com.rakursy.timetable.model.Teacher;

@Stateful
@ConversationScoped
@Named
public class GradeAction extends EntityManageAction<Grade> {
	
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
		return newEntity;
	}
	
	public String generateMaps() {
		for (Subject subject : subjects) {
			GradeSubject gradeSubject = new GradeSubject();
			gradeSubject.setGrade(newEntity);
			gradeSubject.setSubject(subject);
			gradeSubject.setSchool(user.getSchool());
			gradeSubjects.add(gradeSubject);
		}
		
		return "next";
	}
	
	public String splitIntoGroups() {
		for (GradeSubject gradeSubject : gradeSubjects) {
			if (gradeSubject.getTeachers().size() > 1) {
				for (Teacher teacher : gradeSubject.getTeachers()) {
					StudentGroup studentGroup = new StudentGroup(gradeSubject.getSubject(), teacher);
					studentGroup.setGrade(newEntity);
					studentGroup.setWholeGrade(false);
					studentGroup.setSchool(user.getSchool());
					studentGroups.add(studentGroup);
				}
			}
		}
		
		if (studentGroups.size() == 0) {			
			// There's nothing extra to do...
			return save();
		}
		
		return "next";
	}

	@Override
	public String save() {
		newEntity.setSchool(user.getSchool());
		newEntity.setSubjects(gradeSubjects);
		
		// Don't forget the ones that are the whole grades
		for (GradeSubject gradeSubject : gradeSubjects) {
			if (gradeSubject.getTeachers().size() == 1) {
				StudentGroup studentGroup = new StudentGroup(gradeSubject.getSubject(), gradeSubject.getTeachers().get(0));
				studentGroup.setGrade(newEntity);
				studentGroup.setStudentCount(newEntity.getStudentCount());
				studentGroup.setWholeGrade(true);
				studentGroup.setSchool(user.getSchool());
				studentGroups.add(studentGroup);
			}
		}
		
		newEntity.setStudentGroups(studentGroups);
		
		return super.save();
	}

	@Override
	protected Grade find() {
		return em.find(Grade.class, Long.parseLong(idProvider.get()));
	}

	@Override
	protected Grade create() {
		return new Grade();
	}

}
