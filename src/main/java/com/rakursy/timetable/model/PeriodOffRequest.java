package com.rakursy.timetable.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

@Entity
public class PeriodOffRequest implements Serializable {

	private static final long serialVersionUID = 2880725991177421082L;

	@Id
	@GeneratedValue
	private Long id;

	@NotNull
	@ManyToOne
	private Teacher teacher;

	/*
	 * Since Periods are generated when the solver starts planning, it is not
	 * possible to use a Period reference here.
	 */
	@NotNull
	private DayOfWeek dayOfWeek;

	@NotNull
	private Integer schoolHourStartTime;

	@NotNull
	@ManyToOne
	private School school;

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(id)
				.append(dayOfWeek)
				.append(schoolHourStartTime)
				.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (obj instanceof PeriodOffRequest) {
			PeriodOffRequest other = (PeriodOffRequest) obj;
			return new EqualsBuilder()
					.append(id, other.id)
					.append(dayOfWeek, other.dayOfWeek)
					.append(schoolHourStartTime, other.schoolHourStartTime)
					.isEquals();
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		return teacher + " X " + dayOfWeek.name() + ", " + schoolHourStartTime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public DayOfWeek getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(DayOfWeek dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public Integer getSchoolHourStartTime() {
		return schoolHourStartTime;
	}

	public void setSchoolHourStartTime(Integer schoolHourStartTime) {
		this.schoolHourStartTime = schoolHourStartTime;
	}

	public School getSchool() {
		return school;
	}

	public void setSchool(School school) {
		this.school = school;
	}

}
