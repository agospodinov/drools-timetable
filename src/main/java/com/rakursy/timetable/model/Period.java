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
public class Period implements Serializable {
	
	private static final long serialVersionUID = 1310687682673346941L;

	@Id
	@GeneratedValue
	private Long id;
	
	@NotNull
	@ManyToOne
	private SchoolDay schoolDay;
	
	@NotNull
	@ManyToOne
	private SchoolHour schoolHour;
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(id)
				.append(schoolDay)
				.append(schoolHour)
				.toHashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj instanceof Period) {
        	Period other = (Period) obj;
            return new EqualsBuilder()
                    .append(id, other.id)
                    .append(schoolDay, other.schoolDay)
                    .append(schoolHour, other.schoolHour)
                    .isEquals();
        } else {
            return false;
        }
	}
	

	@Override
	public String toString() {
		return schoolDay.getDayOfWeek().name() + ", " + schoolHour;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public SchoolDay getSchoolDay() {
		return schoolDay;
	}

	public void setSchoolDay(SchoolDay schoolDay) {
		this.schoolDay = schoolDay;
	}

	public SchoolHour getSchoolHour() {
		return schoolHour;
	}

	public void setSchoolHour(SchoolHour schoolHour) {
		this.schoolHour = schoolHour;
	}
	
}
