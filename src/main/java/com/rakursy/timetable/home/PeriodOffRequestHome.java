package com.rakursy.timetable.home;

import static ch.lambdaj.Lambda.exists;
import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static org.hamcrest.Matchers.equalTo;

import java.util.List;

import javax.ejb.Stateful;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.seam.international.status.Messages;
import org.jboss.seam.international.status.builder.BundleKey;

import com.rakursy.timetable.model.PeriodOffRequest;
import com.rakursy.timetable.model.School;
import com.rakursy.timetable.model.Teacher;
import com.rakursy.timetable.model.Timetable;
import com.rakursy.timetable.model.User;

@Stateful
@ConversationScoped
@Named
public class PeriodOffRequestHome extends ConversationalEntityHome<PeriodOffRequest> {
	
	private static final long serialVersionUID = 7197987990944358898L;

	@Inject
	@Named("loggedInUser")
	private User user;

	@Inject
	private Messages messages;
	
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
	
	@Override
	public boolean remove() {
		School school = user.getSchool();
		for (Timetable timetable : school.getTimetables()) {
			if (exists(timetable.getPeriodOffRequests(), having(on(PeriodOffRequest.class), equalTo(getInstance())))) {
				messages.error(new BundleKey("messages", "cannotRemove"));
				return false;
			}
		}
		
		return super.remove();
	}
	
}
