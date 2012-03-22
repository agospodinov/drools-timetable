package com.rakursy.timetable.controller;

import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.select;
import static org.hamcrest.Matchers.equalTo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

import org.jboss.seam.security.annotations.LoggedIn;

import com.rakursy.timetable.model.Grade;
import com.rakursy.timetable.model.Period;
import com.rakursy.timetable.model.SchoolClass;
import com.rakursy.timetable.model.SchoolDay;
import com.rakursy.timetable.model.Timetable;

@Stateful
@Named
@SessionScoped
@LoggedIn
public class OldSolutionsController {
	
	private Map<SchoolDay, List<Period>> oldPeriodsForSchoolDays = new HashMap<SchoolDay,List<Period>>();

	private Timetable oldSolution;
	
	@Produces
	@Named("oldSolution")
	public Timetable getOldSolution() {
		return oldSolution;
	}
	
	public void setOldSolution(Timetable oldSolution) {
		this.oldSolution = oldSolution;
		generatePeriodsForSchoolDays();
	}
	

	public Map<SchoolDay, List<Period>> getOldPeriodsForSchoolDays() {
		return oldPeriodsForSchoolDays;
	}
	
	public void generatePeriodsForSchoolDays() {
		Map<SchoolDay, List<Period>> periodsForSchoolDays = new HashMap<SchoolDay, List<Period>>();
		for (SchoolDay schoolDay : getOldSolution().getSchoolDays()) {
			List<Period> periods = select(getOldSolution().getPeriods(),
					having(on(Period.class).getSchoolDay(), equalTo(schoolDay)));
			periodsForSchoolDays.put(schoolDay, periods);
		}
		this.oldPeriodsForSchoolDays = periodsForSchoolDays;
	}
	
	public List<SchoolClass> getSchoolClassForTimeAndGrade(Period period, Grade grade) {
		List<SchoolClass> schoolClasses = select(getOldSolution().getSchoolClasses(),
				having(on(SchoolClass.class).getPeriod(), equalTo(period))
						.and(having(on(SchoolClass.class).getStudentGroup().getGrade(), equalTo(grade))));
		if (schoolClasses.isEmpty())
			return null;
		return schoolClasses;
	}

}
