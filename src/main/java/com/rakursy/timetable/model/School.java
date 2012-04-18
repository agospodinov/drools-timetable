package com.rakursy.timetable.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.NotBlank;

@Entity
public class School implements Serializable {

	private static final long serialVersionUID = -2692684586022338364L;

	@Id
	@GeneratedValue
	private Long id;

	@NotBlank
	private String name;

	@OneToMany(orphanRemoval = true, mappedBy = "school")
	private List<Grade> grades;

	@OneToMany(orphanRemoval = true, mappedBy = "school")
	private List<Room> rooms;

	@OneToMany(orphanRemoval = true, mappedBy = "school")
	private List<Subject> subjects;

	@OneToMany(orphanRemoval = true, mappedBy = "school")
	private List<Teacher> teachers;

	@OneToMany(orphanRemoval = true, mappedBy = "school")
	private List<PeriodOffRequest> periodOffRequests;

	@OneToMany(orphanRemoval = true, mappedBy = "school")
	private List<Timetable> timetables;

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(id)
				.append(name)
				.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (obj instanceof School) {
			School other = (School) obj;
			return new EqualsBuilder()
					.append(id, other.id)
					.append(name, other.name)
					.isEquals();
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		return name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Grade> getGrades() {
		return grades;
	}

	public void setGrades(List<Grade> grades) {
		this.grades = grades;
	}

	public List<Room> getRooms() {
		return rooms;
	}

	public void setRooms(List<Room> rooms) {
		this.rooms = rooms;
	}

	public List<Subject> getSubjects() {
		return subjects;
	}

	public void setSubjects(List<Subject> subjects) {
		this.subjects = subjects;
	}

	public List<Teacher> getTeachers() {
		return teachers;
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

	public List<Timetable> getTimetables() {
		return timetables;
	}

	public void setTimetables(List<Timetable> timetables) {
		this.timetables = timetables;
	}

}
