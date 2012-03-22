package com.rakursy.timetable.controller;

import javax.ejb.Stateful;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

import com.rakursy.timetable.model.Room;

@Stateful
@ConversationScoped
@Named
public class RoomAction extends EntityManageAction<Room> {

	@Produces
	@Named
	public Room getNewRoom() {
		return newEntity;
	}
	
	@Override
	public String save() {
		newEntity.setSchool(user.getSchool());
		return super.save();
	}

	@Override
	protected Room find() {
		return em.find(Room.class, Long.parseLong(idProvider.get()));
	}

	@Override
	protected Room create() {
		return new Room();
	}
	
}
