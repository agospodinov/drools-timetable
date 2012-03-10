package com.rakursy.timetable.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.rakursy.timetable.model.Teacher;

@FacesConverter("teacherConverter")
public class TeacherConverter implements Converter {

	@Inject
	private EntityManager em;

	public Object getAsObject(final FacesContext facesContext, final UIComponent component, final String id) {
		return em.find(Teacher.class, new Long(id));
	}

	public String getAsString(final FacesContext context, final UIComponent comp, final Object entity) {
		if (entity instanceof Teacher) 
			return ((Teacher) entity).getId().toString();
		return null; 
	}
}
