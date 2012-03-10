package com.rakursy.timetable.controller;

import javax.ejb.Stateful;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

import com.rakursy.timetable.model.Teacher;

@Stateful
@ConversationScoped
@Named
public class TeacherAction extends EntityManageAction<Teacher> {

	@Produces
	@Named
	public Teacher getNewTeacher() {
		return newEntity;
	}

	@Override
	protected Teacher find() {
		return em.find(Teacher.class, Long.parseLong(idProvider.get()));
	}

	@Override
	protected Teacher create() {
		return new Teacher();
	}
	
}
