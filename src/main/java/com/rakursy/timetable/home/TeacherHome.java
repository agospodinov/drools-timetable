package com.rakursy.timetable.home;

import static ch.lambdaj.Lambda.exists;
import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static org.hamcrest.Matchers.equalTo;

import javax.ejb.Stateful;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.rakursy.timetable.model.Grade;
import com.rakursy.timetable.model.GradeSubject;
import com.rakursy.timetable.model.School;
import com.rakursy.timetable.model.StudentGroup;
import com.rakursy.timetable.model.Teacher;
import com.rakursy.timetable.model.Timetable;
import com.rakursy.timetable.model.User;

@Stateful
@ConversationScoped
@Named
public class TeacherHome extends ConversationalEntityHome<Teacher> {

	private static final long serialVersionUID = 5568996354847592868L;

	@Inject
	@Named("loggedInUser")
	private User user;

	@Override
	public Teacher getInstance() {
		return super.getInstance();
	}

	@Override
	public boolean persist() {
		getInstance().setSchool(user.getSchool());
		return super.persist();
	}
	
	@Override
	public boolean remove() {
		School school = user.getSchool();
		for (Grade grade : school.getGrades()) {
			for (GradeSubject gradeSubject : grade.getSubjects()) {
				if (exists(gradeSubject.getTeachers(), having(on(Teacher.class), equalTo(getInstance())))) {
					return false;
				}
			}
			for (StudentGroup studentGroup : grade.getStudentGroups()){
				if (studentGroup.getTeacher().equals(getInstance())) {
					return false;
				}
			}
		}
		for (Timetable timetable : school.getTimetables()) {
			if (exists(timetable.getTeachers(), having(on(Teacher.class), equalTo(getInstance())))) {
				return false;
			}
		}
		
		return super.remove();
	}

}
