package com.rakursy.timetable.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.NotBlank;

@Entity
public class Subject implements Serializable {

	private static final long serialVersionUID = -4339521594499114421L;

	@Id
	@GeneratedValue
	private Long id;

	@NotNull
	@NotBlank
	private String name;

	@NotNull
	private SubjectWeight weight;

	@NotNull
	@ManyToOne
	private School school;

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(id)
				.append(name)
				.append(weight)
				.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (obj instanceof Subject) {
			Subject other = (Subject) obj;
			return new EqualsBuilder()
					.append(id, other.id)
					.append(name, other.name)
					.append(weight, other.weight)
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

	public SubjectWeight getWeight() {
		return weight;
	}

	public void setWeight(SubjectWeight weight) {
		this.weight = weight;
	}

	public School getSchool() {
		return school;
	}

	public void setSchool(School school) {
		this.school = school;
	}

}
