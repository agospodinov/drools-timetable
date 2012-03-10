package com.rakursy.timetable.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

@Entity
public class GradeSubject implements Serializable {
	
	private static final long serialVersionUID = -6497661457784023861L;

	@Id
	@GeneratedValue
	private Long id;
	
	@NotNull
	@ManyToOne(cascade = CascadeType.ALL)
	private Grade grade;
	
	@NotNull
	@ManyToOne
	private Subject subject;
	
	@NotNull
	@ManyToMany
	private List<Teacher> teachers;
	
	@NotNull
	private Integer classCountPerWeek;
	
	public GradeSubject() {
	}
	
	public GradeSubject(Subject subject, List<Teacher> teachers, Integer classCountPerWeek) {
		this.subject = subject;
		this.teachers = teachers;
		this.classCountPerWeek = classCountPerWeek;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(id)
				.append(classCountPerWeek)
				.toHashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj instanceof GradeSubject) {
        	GradeSubject other = (GradeSubject) obj;
            return new EqualsBuilder()
                    .append(id, other.id)
                    .append(classCountPerWeek, other.classCountPerWeek)
                    .isEquals();
        } else {
            return false;
        }
	}
	
	@Override
	public String toString() {
		return grade + " : " + subject + " : " + teachers;
	}

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Grade getGrade() {
		return grade;
	}
	
	public void setGrade(Grade grade) {
		this.grade = grade;
	}
	
	public Subject getSubject() {
		return subject;
	}
	
	public void setSubject(Subject subject) {
		this.subject = subject;
	}
	
	public List<Teacher> getTeachers() {
		return teachers;
	}
	
	public void setTeachers(List<Teacher> teachers) {
		this.teachers = teachers;
	}
	
	public Integer getClassCountPerWeek() {
		return classCountPerWeek;
	}
	
	public void setClassCountPerWeek(Integer classCountPerWeek) {
		this.classCountPerWeek = classCountPerWeek;
	}
	
}
