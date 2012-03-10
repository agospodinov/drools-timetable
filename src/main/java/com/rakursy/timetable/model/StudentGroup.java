package com.rakursy.timetable.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

@Entity
public class StudentGroup implements Serializable {
	
	private static final long serialVersionUID = 6720302928630877945L;

	@Id
	@GeneratedValue
	private Long id;
	
	@NotNull
	@ManyToOne
	private Grade grade;
	
	@NotNull
	@ManyToOne
	private Subject subject;
	
	@NotNull
	@ManyToOne
	private Teacher teacher;
	
	@NotNull
	@Min(value=1)
	private Integer studentCount;
	
	@NotNull
	private Boolean wholeGrade;
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(id)
				.append(studentCount)
				.append(wholeGrade)
				.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj instanceof StudentGroup) {
        	StudentGroup other = (StudentGroup) obj;
            return new EqualsBuilder()
                    .append(id, other.id)
                    .append(studentCount, other.studentCount)
                    .append(wholeGrade, other.wholeGrade)
                    .isEquals();
        } else {
            return false;
        }
	}
	
	@Override
	public String toString() {
		return grade + " : " + subject + " : " + teacher;
	}
	
	public StudentGroup() {
	}

	public StudentGroup(Subject subject, Teacher teacher) {
		this.subject = subject;
		this.teacher = teacher;
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

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public Integer getStudentCount() {
		return studentCount;
	}

	public void setStudentCount(Integer studentCount) {
		this.studentCount = studentCount;
	}

	public Boolean getWholeGrade() {
		return wholeGrade;
	}

	public void setWholeGrade(Boolean wholeGrade) {
		this.wholeGrade = wholeGrade;
	}
	
}
