package com.rakursy.timetable.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class Teacher implements Serializable {

	private static final long serialVersionUID = 1041393892001229350L;

	@Id
	@GeneratedValue
	private Long id;

	@NotNull
	@NotBlank
	private String name;

	@NotNull
	@NotEmpty
	@ManyToMany
	private List<Subject> subjects;

	@NotNull
	@ManyToOne
	private School school;

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(id).append(name).toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (obj instanceof Teacher) {
			Teacher other = (Teacher) obj;
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
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Subject> getSubjects() {
		return this.subjects;
	}

	public void setSubjects(List<Subject> subjects) {
		this.subjects = subjects;
	}

	public School getSchool() {
		return school;
	}

	public void setSchool(School school) {
		this.school = school;
	}

}
