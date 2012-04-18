package com.rakursy.timetable.controller;

import javax.ejb.Stateful;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

import com.rakursy.timetable.model.Subject;

@Stateful
@ConversationScoped
@Named
public class SubjectAction extends EntityManageAction<Subject> {

	@Produces
	@Named
	public Subject getNewSubject() {
		return newEntity;
	}
	
	@Override
	public String save() {
		newEntity.setSchool(user.getSchool());
		return super.save();
	}

	@Override
	protected Subject find() {
		return em.find(Subject.class, Long.parseLong(idProvider.get()));
	}

	@Override
	protected Subject create() {
		return new Subject();
	}

}
