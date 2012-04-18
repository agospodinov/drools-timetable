package com.rakursy.timetable.model;

import java.util.ResourceBundle;

import javax.faces.context.FacesContext;

public enum SubjectWeight {
	
	EASY("easy"),
	MEDIUM("medium"),
	HARD("hard");
	
	private String stringValue;

	private SubjectWeight(String stringValue) {
		this.stringValue = stringValue;
	}

	@Override
	public String toString() {
		FacesContext context = FacesContext.getCurrentInstance();
		ResourceBundle bundle = context.getApplication().getResourceBundle(context, "messages");
		return bundle.getString(this.stringValue);
	}

}
