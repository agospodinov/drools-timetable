package com.rakursy.timetable.controller;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.jboss.logging.Logger;
import org.jboss.solder.logging.Category;
import org.jboss.solder.servlet.http.RequestParam;

import com.rakursy.timetable.model.Room;
import com.rakursy.timetable.util.qualifiers.Created;

@Model
public class RoomAction {
	
	@Inject
	@Category("timetable")
	private Logger log;

	@Inject
	private EntityManager em;

	@Inject
	private Event<Room> roomEventSrc;
	
	@Inject
	@RequestParam("id")
	private Instance<String> idProvider;
	
	private Room newRoom;

	@Produces
	@Named
	public Room getNewRoom() {
		return newRoom;
	}
	
	@PostConstruct
	public void load() {
		if (idProvider.get() != null) {
			newRoom = em.find(Room.class, Long.parseLong(idProvider.get()));
		} else {
			newRoom = new Room();
		}
	}

	@SuppressWarnings("serial")
	public String save() {
		log.info("Saving " + newRoom.getNumber());
		
		if (em.contains(newRoom)) {
			em.flush();
		} else {
			em.persist(newRoom);
		}
		
		roomEventSrc.select(new AnnotationLiteral<Created>() {}).fire(newRoom);
		load();
		
		return "success";
	}
	
	public String cancel() {
		load();
		return "success";
	}
	
}
