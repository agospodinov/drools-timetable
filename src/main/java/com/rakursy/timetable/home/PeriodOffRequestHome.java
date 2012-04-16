package com.rakursy.timetable.home;

import java.util.List;

import javax.ejb.Stateful;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import com.rakursy.timetable.model.PeriodOffRequest;
import com.rakursy.timetable.model.Teacher;
import com.rakursy.timetable.model.User;

@Stateful
@ConversationScoped
@Named
public class PeriodOffRequestHome extends ConversationalEntityHome<PeriodOffRequest> {
	
	private static final long serialVersionUID = 7197987990944358898L;

	@Inject
	@Named("loggedInUser")
	private User user;
	
	private Teacher teacher;
	private List<PeriodOffRequest> periodOffRequestsForTeacher;
	
	private Long teacherId;
	
	public Long getTeacherId() {
		return teacherId;
	}
	
	public void setTeacherId(Long teacherId) {
		this.teacherId = teacherId;
	}
	
	public Teacher getTeacher() {
		return teacher;
	}
	
	public List<PeriodOffRequest> getPeriodOffRequestsForTeacher() {
		return periodOffRequestsForTeacher;
	}
	
	@Override
	@Produces
	@Named("periodOffRequest")
	public PeriodOffRequest getInstance() {
		return super.getInstance();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean load() {
		teacher = (Teacher) getEntityManager().createQuery("select t from Teacher t where t.id = :teacherId")
				.setParameter("teacherId", teacherId)
				.getSingleResult();
		
		periodOffRequestsForTeacher = getEntityManager().createQuery("select por from PeriodOffRequest por where por.teacher = :teacher")
				.setParameter("teacher", teacher)
				.getResultList();
		
		return super.load();
	}
	
	@Override
	public boolean persist() {
		getInstance().setSchool(user.getSchool());
		getInstance().setTeacher(teacher);
		return super.persist();
	}
	
}
