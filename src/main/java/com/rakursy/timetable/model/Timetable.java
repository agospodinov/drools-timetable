package com.rakursy.timetable.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.drools.planner.api.domain.solution.PlanningEntityCollectionProperty;
import org.drools.planner.core.score.buildin.hardandsoft.HardAndSoftScore;
import org.drools.planner.core.solution.Solution;

@Entity
public class Timetable implements Solution<HardAndSoftScore>, Serializable {

	private static final long serialVersionUID = -1133029484237774537L;

	@Id
	@GeneratedValue
	private Long id;

	@NotNull
	@ManyToOne
	private School school;

	// @NotNull
	@Size(max = 300)
	private String description;

	@Basic
	@Temporal(TemporalType.DATE)
	private Date creationDate;

	// Problem facts

	@ManyToMany
	private List<Grade> grades;

	@ManyToMany
	private List<GradeSubject> gradeSubjects;

	@ManyToMany
	private List<StudentGroup> studentGroups;

	@ManyToMany
	private List<Subject> subjects;

	@ManyToMany
	private List<Room> rooms;

	@ManyToMany
	private List<Teacher> teachers;

	@ManyToMany
	private List<PeriodOffRequest> periodOffRequests;

	// Generated facts

	@OneToMany(orphanRemoval = true)
	private List<Period> periods;

	@OneToMany(orphanRemoval = true)
	private List<SchoolHour> schoolHours;

	@OneToMany(orphanRemoval = true)
	private List<SchoolDay> schoolDays;

	// Planning entity

	@OneToMany(orphanRemoval = true)
	private List<SchoolClass> schoolClasses;

	@Transient
	private HardAndSoftScore score;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public School getSchool() {
		return school;
	}

	public void setSchool(School school) {
		this.school = school;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

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

	@SuppressWarnings("unused")
	@PrePersist
	private void prePersist() {
		this.creationDate = new Date();
	}

	@Override
	public Timetable cloneSolution() {
		Timetable clone = new Timetable();
		clone.school = school;
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
	@Transient
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
	@Transient
	public HardAndSoftScore getScore() {
		return this.score;
	}

	@Override
	public void setScore(HardAndSoftScore score) {
		this.score = score;
	}
	
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (id == null || !(o instanceof Timetable)) {
            return false;
        } else {
            Timetable other = (Timetable) o;
            if (schoolClasses.size() != other.schoolClasses.size()) {
                return false;
            }
            for (Iterator<SchoolClass> it = schoolClasses.iterator(), otherIt = other.schoolClasses.iterator(); it.hasNext();) {
                SchoolClass schoolClass = it.next();
                SchoolClass otherSchoolClass = otherIt.next();
                if (!schoolClass.equals(otherSchoolClass)) {
                    return false;
                }
            }
            return true;
        }
    }

    public int hashCode() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        for (SchoolClass schoolClass : schoolClasses) {
            hashCodeBuilder.append(schoolClass.hashCode());
        }
        return hashCodeBuilder.toHashCode();
    }

}
