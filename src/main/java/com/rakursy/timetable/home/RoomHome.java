package com.rakursy.timetable.home;

import javax.ejb.Stateful;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.rakursy.timetable.model.Room;
import com.rakursy.timetable.model.User;

@Stateful
@ConversationScoped
@Named
public class RoomHome extends ConversationalEntityHome<Room> {

	private static final long serialVersionUID = 541622376092918608L;

	@Inject
	@Named("loggedInUser")
	private User user;

	@Override
	public Room getInstance() {
		return super.getInstance();
	}

	@Override
	public boolean persist() {
		getInstance().setSchool(user.getSchool());
		return super.persist();
	}

}
