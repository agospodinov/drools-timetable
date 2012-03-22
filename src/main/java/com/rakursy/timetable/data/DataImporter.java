package com.rakursy.timetable.data;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.jboss.logging.Logger;
import org.jboss.solder.logging.Category;

import com.rakursy.timetable.model.Grade;
import com.rakursy.timetable.model.PeriodOffRequest;
import com.rakursy.timetable.model.Room;
import com.rakursy.timetable.model.School;
import com.rakursy.timetable.model.Subject;
import com.rakursy.timetable.model.Teacher;
import com.rakursy.timetable.model.Timetable;
import com.rakursy.timetable.model.User;

@Model
public class DataImporter {

	@Inject
	@Category("example")
	private Logger log;

	@Inject
	private EntityManager em;

	@Inject
	@Named("loggedInUser")
	private User user;

	public void removeAllData() {
		log.info("Deleting all data.");

		School school = user.getSchool();

		removeAllTimetables();

		for (PeriodOffRequest periodOffRequest : school.getPeriodOffRequests()) {
			em.remove(periodOffRequest);
		}
		school.getPeriodOffRequests().clear();

		for (Grade grade : school.getGrades()) {
			em.remove(grade);
		}
		school.getGrades().clear();

		for (Teacher teacher : school.getTeachers()) {
			em.remove(teacher);
		}
		school.getTeachers().clear();

		for (Room room : school.getRooms()) {
			em.remove(room);
		}
		school.getRooms().clear();

		for (Subject subject : school.getSubjects()) {
			em.remove(subject);
		}
		school.getSubjects().clear();

		em.merge(school);
		em.flush();

		log.info("Successfully deleted everything.");
	}

	public void removeAllTimetables() {
		log.info("Deleting all generated timetables.");

		School school = user.getSchool();
		
		for (Timetable timetable : school.getTimetables()) {
			em.remove(timetable);
		}
		school.getTimetables().clear();
		em.merge(school);
		em.flush();

		log.info("Successfully deleted generated timetables.");
	}

	public void importData(String filename) {
		// importSQL(sess., new FileInputStream(filename));

	}

}
