package com.rakursy.timetable.controller;

import static ch.lambdaj.Lambda.*;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.drools.planner.config.XmlSolverFactory;
import org.drools.planner.core.Solver;
import org.drools.planner.core.event.BestSolutionChangedEvent;
import org.drools.planner.core.event.SolverEventListener;
import org.drools.planner.core.score.buildin.hardandsoft.DefaultHardAndSoftScore;
import org.drools.planner.core.score.buildin.hardandsoft.HardAndSoftScore;
import org.drools.planner.core.solution.Solution;
import org.jboss.solder.logging.Category;
import org.jboss.solder.logging.Logger;

import com.rakursy.timetable.model.DayOfWeek;
import com.rakursy.timetable.model.Grade;
import com.rakursy.timetable.model.GradeSubject;
import com.rakursy.timetable.model.Period;
import com.rakursy.timetable.model.PeriodOffRequest;
import com.rakursy.timetable.model.Room;
import com.rakursy.timetable.model.SchoolClass;
import com.rakursy.timetable.model.SchoolDay;
import com.rakursy.timetable.model.SchoolHour;
import com.rakursy.timetable.model.StudentGroup;
import com.rakursy.timetable.model.Subject;
import com.rakursy.timetable.model.Teacher;
import com.rakursy.timetable.model.Timetable;

@Stateful
@Named
@SessionScoped
public class TimetableController {

	private static final String SOLVER_CONFIG = "/com/rakursy/timetable/rules/timetableScoreRules.xml";
	
	@Inject
	@Category("timetable")
	private Logger log;
	
	@Inject
	private EntityManager em;
	
	private Map<SchoolDay, List<Period>> periodsForSchoolDays = new HashMap<SchoolDay, List<Period>>();
	
	private volatile Solver solver;
	
	@SuppressWarnings("rawtypes")
	private Solution solution;
	
	public Solver getSolver() {
		return solver;
	}
	
	public Timetable getSolution() {
		return (Timetable) solution;
	}
	
	public HardAndSoftScore getScore() {
		if (solution.getScore() == null) {
			return DefaultHardAndSoftScore.parseScore("-999999hard/-999999soft");
		}
		return (HardAndSoftScore) solution.getScore();
	}
	
	public Map<SchoolDay, List<Period>> getPeriodsForSchoolDays() {
		return periodsForSchoolDays;
	}
	
	// TODO find a way to get the list of broken constraints from the planner's working memory
	public List<String> getBrokenConstraints() {
//		WorkingMemory workingMemory = 
		return null;
	}
	
	public List<SchoolClass> getSchoolClassForTimeAndGrade(Period period, Grade grade) {
		List<SchoolClass> schoolClasses = select(getSolution().getSchoolClasses(), 
				having(on(SchoolClass.class).getPeriod(), equalTo(period))
				.and(having(on(SchoolClass.class).getStudentGroup().getGrade(), equalTo(grade))));
		if (schoolClasses.isEmpty())
			return null;
		return schoolClasses;
	}
	
	public void solve() {
		if (solver != null) {
			terminateEarly();
		}
		
		solver = createSolver();
		solution = createSolution();
		solver.setPlanningProblem(solution);
		
		solver.addEventListener(new SolverEventListener() {
			public void bestSolutionChanged(BestSolutionChangedEvent event) {
				synchronized (solution) {
					solution = event.getNewBestSolution();
				}
				
				for (SchoolDay schoolDay : getSolution().getSchoolDays()) {
					List<Period> periods = select(getSolution().getPeriods(), 
							having(on(Period.class).getSchoolDay(), equalTo(schoolDay)));
					periodsForSchoolDays.put(schoolDay, periods);
				}
			}
		});
		
		log.info("Starting solver " + solver + " with solution " + solution);
		
		// as expected, the interface locks up due to this 
		// method not returning since the solver is working
		new Thread(new Runnable() {
			public void run() {
				solver.solve();
				synchronized (solution) {
					solution = solver.getBestSolution();
				}
				
				log.info("Solver has finished.");
			}
		}).start();
		
	}
	
	public void terminateEarly() {
		if(solver.isSolving()) {
			solver.terminateEarly();
		}
	}
	
	public Solver createSolver() {
		XmlSolverFactory configurer = new XmlSolverFactory();
		configurer.configure(SOLVER_CONFIG);
		return configurer.buildSolver();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Solution createSolution() {
		Timetable timetable = new Timetable();
		
		// Get the actual data from the database
		List<Grade> grades = em.createQuery("select g from Grade g").getResultList();
		List<GradeSubject> gradeSubjects = em.createQuery("select gs from GradeSubject gs").getResultList();
		List<StudentGroup> studentGroups = em.createQuery("select sg from StudentGroup sg").getResultList();
		List<Teacher> teachers = em.createQuery("select t from Teacher t").getResultList();
		List<PeriodOffRequest> periodOffRequests = em.createQuery("select por from PeriodOffRequest por").getResultList();
		List<Subject> subjects = em.createQuery("select s from Subject s").getResultList();
		List<Room> rooms = em.createQuery("select r from Room r").getResultList();
		
		// Get rid of older generated data
		List<SchoolDay> schoolDays = em.createQuery("select sd from SchoolDay sd").getResultList();
		List<SchoolHour> schoolHours = em.createQuery("select sh from SchoolHour sh").getResultList();
		List<SchoolClass> schoolClasses = em.createQuery("select sc from SchoolClass sc").getResultList();
		List<Period> periods = em.createQuery("select p from Period p").getResultList();
		
		// Hibernate faces a limitation that it can't cascade on bulk update and delete operations...
		for (SchoolDay schoolDay : schoolDays) {
			em.remove(schoolDay);
		}
		
		for (SchoolHour schoolHour : schoolHours) {
			em.remove(schoolHour);
		}
		
		for (SchoolClass schoolClass : schoolClasses) {
			em.remove(schoolClass);
		}
		
		for (Period period : periods) {
			em.remove(period);
		}
		
		// Regenerate said data
		schoolDays = new ArrayList<SchoolDay>();
		schoolHours = new ArrayList<SchoolHour>();
		schoolClasses = new ArrayList<SchoolClass>();
		periods = new ArrayList<Period>();
		
		
		for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
			SchoolDay schoolDay = new SchoolDay();
			schoolDay.setDayOfWeek(dayOfWeek);
			schoolDay.setMinAllowedWeight(3);
			schoolDay.setMaxAllowedWeight(140);

			schoolDays.add(schoolDay);
			em.persist(schoolDay);
		}
		
		for (int i = 1; i <= 8; i++) {
			SchoolHour schoolHour = new SchoolHour();
			schoolHour.setActualStartTime(i);
			
			schoolHours.add(schoolHour);
			em.persist(schoolHour);
		}
		
		for (SchoolDay schoolDay : schoolDays) {
			for (SchoolHour schoolHour : schoolHours) {
				Period period = new Period();
				period.setSchoolDay(schoolDay);
				period.setSchoolHour(schoolHour);
				
				periods.add(period);
				em.persist(period);
			}
		}
		
		for (StudentGroup studentGroup : studentGroups) {
			Grade grade = studentGroup.getGrade();
			
			Integer classCount = grade.getClassCountForSubjectWithTeacher(studentGroup.getSubject(), studentGroup.getTeacher());
			for (int i = 0; i < classCount; i++) {
				SchoolClass schoolClass = new SchoolClass();
				schoolClass.setStudentGroup(studentGroup);
				// Period and room for each school class are added
				// accordingly by the construction heuristic of Drools planner
				
				schoolClasses.add(schoolClass);
				em.persist(schoolClass);
			}
		}
		
		timetable.setGrades(grades);
		timetable.setTeachers(teachers);
		timetable.setSubjects(subjects);
		timetable.setRooms(rooms);
		timetable.setGradeSubjects(gradeSubjects);
		timetable.setStudentGroups(studentGroups);
		timetable.setSchoolDays(schoolDays);
		timetable.setSchoolHours(schoolHours);
		timetable.setPeriods(periods);
		timetable.setPeriodOffRequests(periodOffRequests);
		timetable.setSchoolClasses(schoolClasses);
		return timetable;
	}

}
