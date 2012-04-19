package com.rakursy.timetable.home;

import javax.ejb.Stateful;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.rakursy.timetable.model.Teacher;
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

}
