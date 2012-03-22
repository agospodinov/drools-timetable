package com.rakursy.timetable.model;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

@Entity
public class SchoolDay implements Serializable {

	private static final long serialVersionUID = -7246028417466244556L;

	@Id
	@GeneratedValue
	private Long id;

	@NotNull
	private Integer minAllowedWeight;

	@NotNull
	private Integer maxAllowedWeight;

	@NotNull
	private DayOfWeek dayOfWeek;

	@NotNull
	@ManyToOne
	private School school;

	@Transient
	private UUID uuid;

	public SchoolDay() {
		uuid = UUID.randomUUID();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(uuid)
				.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (obj instanceof SchoolDay) {
			SchoolDay other = (SchoolDay) obj;
			return new EqualsBuilder()
					.append(uuid, other.uuid)
					.isEquals();
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		return dayOfWeek.toString();
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getMinAllowedWeight() {
		return this.minAllowedWeight;
	}

	public void setMinAllowedWeight(Integer minAllowedWeight) {
		this.minAllowedWeight = minAllowedWeight;
	}

	public Integer getMaxAllowedWeight() {
		return this.maxAllowedWeight;
	}

	public void setMaxAllowedWeight(Integer maxAllowedWeight) {
		this.maxAllowedWeight = maxAllowedWeight;
	}

	public DayOfWeek getDayOfWeek() {
		return this.dayOfWeek;
	}

	public void setDayOfWeek(DayOfWeek dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public School getSchool() {
		return school;
	}

	public void setSchool(School school) {
		this.school = school;
	}

}
