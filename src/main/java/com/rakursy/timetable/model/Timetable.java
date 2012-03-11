package com.rakursy.timetable.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.drools.planner.api.domain.solution.PlanningEntityCollectionProperty;
import org.drools.planner.core.score.buildin.hardandsoft.HardAndSoftScore;
import org.drools.planner.core.solution.Solution;

public class Timetable implements Solution<HardAndSoftScore> {

	// Problem facts
	private List<Grade> grades;
	private List<GradeSubject> gradeSubjects;
	private List<StudentGroup> studentGroups;
	private List<Subject> subjects;
	private List<Room> rooms;
	private List<Teacher> teachers;
	private List<PeriodOffRequest> periodOffRequests;
	private List<Period> periods;
	
	// Generated facts
	private List<SchoolHour> schoolHours;
	private List<SchoolDay> schoolDays;

	// Planning entity
	private List<SchoolClass> schoolClasses;

	private HardAndSoftScore score;

	public List<Grade> getGrades() {
		return this.grades;
	}

	public void setGrades(List<Grade> grades) {
		this.grades = grades;
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

	public List<Subject> getSubjects() {
		return this.subjects;
	}

	public void setSubjects(List<Subject> subjects) {
		this.subjects = subjects;
	}

	public List<Room> getRooms() {
		return this.rooms;
	}

	public void setRooms(List<Room> rooms) {
		this.rooms = rooms;
	}

	public List<Teacher> getTeachers() {
		return this.teachers;
	}

	public void setTeachers(List<Teacher> teachers) {
		this.teachers = teachers;
	}
	
	public List<PeriodOffRequest> getPeriodOffRequests() {
		return periodOffRequests;
	}

	public void setPeriodOffRequests(List<PeriodOffRequest> periodOffRequests) {
		this.periodOffRequests = periodOffRequests;
	}

	public List<Period> getPeriods() {
		return periods;
	}
	
	public void setPeriods(List<Period> periods) {
		this.periods = periods;
	}

	public List<SchoolHour> getSchoolHours() {
		return this.schoolHours;
	}

	public void setSchoolHours(List<SchoolHour> schoolHours) {
		this.schoolHours = schoolHours;
	}

	public List<SchoolDay> getSchoolDays() {
		return this.schoolDays;
	}

	public void setSchoolDays(List<SchoolDay> schoolDays) {
		this.schoolDays = schoolDays;
	}

	@PlanningEntityCollectionProperty
	public List<SchoolClass> getSchoolClasses() {
		return this.schoolClasses;
	}

	public void setSchoolClasses(List<SchoolClass> schoolClasses) {
		this.schoolClasses = schoolClasses;
	}
	
	@Override
	public Timetable cloneSolution() {
		Timetable clone = new Timetable();
		clone.grades = grades;
		clone.gradeSubjects = gradeSubjects;
		clone.studentGroups = studentGroups;
		clone.rooms = rooms;
		clone.subjects = subjects;
		clone.teachers = teachers;
		clone.schoolHours = schoolHours;
		clone.schoolDays = schoolDays;
		clone.periods = periods;
		clone.periodOffRequests = periodOffRequests;
		List<SchoolClass> clonedSchoolClasses = new ArrayList<SchoolClass>(schoolClasses.size());
		for (SchoolClass schoolClass : schoolClasses) {
			clonedSchoolClasses.add(schoolClass.clone());
		}
		clone.schoolClasses = clonedSchoolClasses;
		clone.score = score;
		return clone;
	}

	@Override
	public Collection<? extends Object> getProblemFacts() {
		List<Object> facts = new ArrayList<Object>();
		facts.addAll(subjects);
		facts.addAll(teachers);
		facts.addAll(grades);
		facts.addAll(studentGroups);
		facts.addAll(gradeSubjects);
		facts.addAll(rooms);
		facts.addAll(periods);
		facts.addAll(periodOffRequests);
		
		facts.addAll(schoolHours);
		facts.addAll(schoolDays);

		return facts;
	}

	@Override
	public HardAndSoftScore getScore() {
		return this.score;
	}

	@Override
	public void setScore(HardAndSoftScore score) {
		this.score = score;
	}
	
//	@Override
//    public boolean equals(Object o) {
//        if (this == o) {
//            return true;
//        }
//        if (!(o instanceof Timetable)) {
//            return false;
//        } else {
//            Timetable other = (Timetable) o;
//            if (schoolClasses.size() != other.schoolClasses.size()) {
//                return false;
//            }
//            for (Iterator<SchoolClass> it = schoolClasses.iterator(), otherIt = other.schoolClasses.iterator(); it.hasNext();) {
//                SchoolClass schoolClass = it.next();
//                SchoolClass otherSchoolClass = otherIt.next();
//                // Notice: we don't use equals()
//                if (!schoolClass.solutionEquals(otherSchoolClass)) {
//                    return false;
//                }
//            }
//            return true;
//        }
//    }
//
//	@Override
//    public int hashCode() {
//        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
//        for (SchoolClass schoolClass : schoolClasses) {
//            // Notice: we don't use hashCode()
//            hashCodeBuilder.append(schoolClass.solutionHashCode());
//        }
//        return hashCodeBuilder.toHashCode();
//    }

}
