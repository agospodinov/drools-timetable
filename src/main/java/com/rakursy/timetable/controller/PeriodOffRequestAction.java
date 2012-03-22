package com.rakursy.timetable.controller;

import java.util.List;

import javax.ejb.Stateful;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.solder.servlet.http.RequestParam;

import com.rakursy.timetable.model.PeriodOffRequest;
import com.rakursy.timetable.model.Teacher;

@Stateful
@ConversationScoped
@Named
public class PeriodOffRequestAction extends EntityManageAction<PeriodOffRequest> {
	
	private Teacher teacher;
	private List<PeriodOffRequest> periodOffRequestsForTeacher;
	
	@Inject
	@RequestParam("teacherId")
	private Instance<String> teacherId;
	
	public Teacher getTeacher() {
		return teacher;
	}
	
	public List<PeriodOffRequest> getPeriodOffRequestsForTeacher() {
		return periodOffRequestsForTeacher;
	}
	
	@Produces
	@Named
	public PeriodOffRequest getNewPeriodOffRequest() {
		return newEntity;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String load() {
		teacher = (Teacher) em.createQuery("select t from Teacher t where t.id = :teacherId")
				.setParameter("teacherId", Long.parseLong(teacherId.get()))
				.getSingleResult();
		
		periodOffRequestsForTeacher = em.createQuery("select por from PeriodOffRequest por where por.teacher = :teacher")
				.setParameter("teacher", teacher)
				.getResultList();
		
		return super.load();
	}
	
	@Override
	public String save() {
		newEntity.setSchool(user.getSchool());
		newEntity.setTeacher(teacher);
		return super.save();
	}

	@Override
	protected PeriodOffRequest find() {
		return em.find(PeriodOffRequest.class, idProvider.get());
	}

	@Override
	protected PeriodOffRequest create() {
		return new PeriodOffRequest();
	}
	
}