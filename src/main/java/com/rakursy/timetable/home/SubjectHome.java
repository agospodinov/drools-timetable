package com.rakursy.timetable.home;

import static ch.lambdaj.Lambda.*;
import static org.hamcrest.Matchers.*;

import javax.ejb.Stateful;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.rakursy.timetable.model.Grade;
import com.rakursy.timetable.model.Room;
import com.rakursy.timetable.model.School;
import com.rakursy.timetable.model.Subject;
import com.rakursy.timetable.model.Teacher;
import com.rakursy.timetable.model.User;

@Stateful
@ConversationScoped
@Named
public class SubjectHome extends ConversationalEntityHome<Subject> {

	private static final long serialVersionUID = -1414340316676457189L;

	@Inject
	@Named("loggedInUser")
	private User user;
	
	@Override
	public Subject getInstance() {
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
		for (Teacher teacher : school.getTeachers()) {
			if (exists(teacher.getSubjects(), having(on(Subject.class), equalTo(getInstance())))) {
				return false;
			}
		}
		for (Room room : school.getRooms()) {
			if (exists(room.getPossibleSubjects(), having(on(Subject.class), equalTo(getInstance())))) {
				return false;
			}
		}
		for (Grade grade : school.getGrades()) {
			if (exists(grade.getSubjectList(), having(on(Subject.class), equalTo(getInstance())))) {
				return false;
			}
		}
		
		return super.remove();
	}
}
