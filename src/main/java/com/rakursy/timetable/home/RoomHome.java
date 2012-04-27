package com.rakursy.timetable.home;

import static ch.lambdaj.Lambda.exists;
import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static org.hamcrest.Matchers.equalTo;

import javax.ejb.Stateful;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.seam.international.status.Messages;
import org.jboss.seam.international.status.builder.BundleKey;

import com.rakursy.timetable.model.Grade;
import com.rakursy.timetable.model.Room;
import com.rakursy.timetable.model.School;
import com.rakursy.timetable.model.Timetable;
import com.rakursy.timetable.model.User;

@Stateful
@ConversationScoped
@Named
public class RoomHome extends ConversationalEntityHome<Room> {

	private static final long serialVersionUID = 541622376092918608L;

	@Inject
	@Named("loggedInUser")
	private User user;
	
	@Inject
	private Messages messages;

	@Override
	public Room getInstance() {
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
			if (grade.getClassRoom().equals(getInstance())) {
				messages.error(new BundleKey("messages", "cannotRemove"));
				return false;
			}
		}
		for (Timetable timetable : school.getTimetables()) {
			if (exists(timetable.getRooms(), having(on(Room.class), equalTo(getInstance())))) {
				messages.error(new BundleKey("messages", "cannotRemove"));
				return false;
			}
		}
		
		return super.remove();
	}

}
