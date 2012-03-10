package com.rakursy.timetable.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.rakursy.timetable.model.Room;

@FacesConverter("roomConverter")
public class RoomConverter implements Converter {

	@Inject
	private EntityManager em;

	public Object getAsObject(final FacesContext facesContext, final UIComponent component, final String id) {
		return em.find(Room.class, new Long(id));
	}

	public String getAsString(final FacesContext context, final UIComponent comp, final Object entity) {
		if (entity instanceof Room) 
			return ((Room) entity).getId().toString();
		return null; 
	}
}
