package com.rakursy.timetable.home;

import javax.ejb.Stateful;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import com.rakursy.timetable.model.Subject;
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
	@Produces
	@Named("subject")
	public Subject getInstance() {
		return super.getInstance();
	}

	@Override
	public boolean persist() {
		getInstance().setSchool(user.getSchool());
		return super.persist();
	}

}
