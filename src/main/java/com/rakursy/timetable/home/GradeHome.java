package com.rakursy.timetable.home;

import static ch.lambdaj.Lambda.exists;
import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static org.hamcrest.Matchers.equalTo;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateful;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.seam.international.status.Messages;
import org.jboss.seam.international.status.builder.BundleKey;

import com.rakursy.timetable.model.Grade;
import com.rakursy.timetable.model.GradeSubject;
import com.rakursy.timetable.model.School;
import com.rakursy.timetable.model.StudentGroup;
import com.rakursy.timetable.model.Subject;
import com.rakursy.timetable.model.Teacher;
import com.rakursy.timetable.model.Timetable;
import com.rakursy.timetable.model.User;

@Stateful
@ConversationScoped
@Named
public class GradeHome extends ConversationalEntityHome<Grade> {

	private static final long serialVersionUID = 8253694804456554182L;

	@Inject
	@Named("loggedInUser")
	private User user;
	
	@Inject
	private Messages messages;

	private List<Subject> subjects = new ArrayList<Subject>();
	private List<GradeSubject> gradeSubjects = new ArrayList<GradeSubject>();
	private List<StudentGroup> studentGroups = new ArrayList<StudentGroup>();

	public List<Subject> getSubjects() {
		return subjects;
	}

	public void setSubjects(List<Subject> subjects) {
		this.subjects = subjects;
	}

	public List<GradeSubject> getGradeSubjects() {
		return gradeSubjects;
	}

	public void setGradeSubjects(List<GradeSubject> gradeSubjects) {
		this.gradeSubjects = gradeSubjects;
	}

	public List<StudentGroup> getStudentGroups() {
		return studentGroups;
	}

	public void setStudentGroups(List<StudentGroup> studentGroups) {
		this.studentGroups = studentGroups;
	}

	@SuppressWarnings("unchecked")
	public List<Teacher> getTeachersForSubject(Subject subject) {
		return getEntityManager()
				.createQuery("select t from Teacher t where :subject member of t.subjects")
				.setParameter("subject", subject)
				.getResultList();
	}

	@Override
	public Grade getInstance() {
		return super.getInstance();
	}

	public boolean generateMaps() {
		gradeSubjects = new ArrayList<GradeSubject>();
		for (Subject subject : subjects) {
			GradeSubject gradeSubject = new GradeSubject();
			gradeSubject.setGrade(getInstance());
			gradeSubject.setSubject(subject);
			gradeSubject.setSchool(user.getSchool());
			gradeSubjects.add(gradeSubject);
		}

		return true;
	}

	public boolean splitIntoGroups() {
		studentGroups = new ArrayList<StudentGroup>();
		for (GradeSubject gradeSubject : gradeSubjects) {
			if (gradeSubject.getTeachers().size() > 1) {
				for (Teacher teacher : gradeSubject.getTeachers()) {
					StudentGroup studentGroup = new StudentGroup(gradeSubject.getSubject(), teacher);
					studentGroup.setGrade(getInstance());
					studentGroup.setWholeGrade(false);
					studentGroup.setSchool(user.getSchool());
					studentGroups.add(studentGroup);
				}
			}
		}

		if (studentGroups.size() == 0) {
			// There's nothing extra to do...
			persist();
			return false;
		}

		return true;
	}

	@Override
	public boolean persist() {
		getInstance().setSchool(user.getSchool());
		getInstance().setSubjects(gradeSubjects);

		// Don't forget the ones that are the whole grades
		for (GradeSubject gradeSubject : gradeSubjects) {
			if (gradeSubject.getTeachers().size() == 1) {
				StudentGroup studentGroup = new StudentGroup(gradeSubject.getSubject(), gradeSubject.getTeachers().get(0));
				studentGroup.setGrade(getInstance());
				studentGroup.setStudentCount(getInstance().getStudentCount());
				studentGroup.setWholeGrade(true);
				studentGroup.setSchool(user.getSchool());
				studentGroups.add(studentGroup);
			}
		}

		getInstance().setStudentGroups(studentGroups);

		return super.persist();
	}

	@Override
	public boolean remove() {
		School school = user.getSchool();
		for (Timetable timetable : school.getTimetables()) {
			if (exists(timetable.getGrades(), having(on(Grade.class), equalTo(getInstance())))) {
				messages.error(new BundleKey("messages", "cannotRemove"));
				return false;
			}
		}
		
		return super.remove();
	}
	
}
