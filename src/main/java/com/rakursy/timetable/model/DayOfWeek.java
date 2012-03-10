package com.rakursy.timetable.model;

import java.util.ResourceBundle;

import javax.faces.context.FacesContext;

public enum DayOfWeek {
	// We only care about the ones that have school, so... no Saturday and Sunday
	MON("monday"),
	TUE("tuesday"),
	WED("wednesday"),
	THU("thursday"),
	FRI("friday");

	private String stringValue;

	private DayOfWeek(String stringValue) {
		this.stringValue = stringValue;
	}

	@Override
	public String toString() {
		FacesContext context = FacesContext.getCurrentInstance();
		ResourceBundle bundle = context.getApplication().getResourceBundle(context, "messages");
		return bundle.getString(this.stringValue);
	}
}
