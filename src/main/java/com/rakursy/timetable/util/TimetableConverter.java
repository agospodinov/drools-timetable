package com.rakursy.timetable.util;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.rakursy.timetable.model.Timetable;

@FacesConverter(value = "timetableConverter")
public class TimetableConverter implements Converter {

	@Inject
	private EntityManager em;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		if (value == null) {
			return null;
		}
		return em.find(Timetable.class, new Long(value));
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (((Timetable) value).getId() == null) {
			return null;
		}
		return ((Timetable) value).getId().toString();
	}

}
