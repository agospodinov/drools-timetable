package com.rakursy.timetable.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
public class PeriodOffRequest implements Serializable {

	private static final long serialVersionUID = 2880725991177421082L;

	@Id
	@GeneratedValue
	private Long id;
	
	@NotNull
	@ManyToOne
	private Teacher teacher;
	
	@NotNull
	@ManyToOne
	private Period period;

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

	public Period getPeriod() {
		return period;
	}

	public void setPeriod(Period period) {
		this.period = period;
	}

}
