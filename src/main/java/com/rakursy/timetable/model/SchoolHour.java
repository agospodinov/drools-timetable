package com.rakursy.timetable.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

@Entity
public class SchoolHour implements Comparable<SchoolHour>, Serializable {
	
	private static final long serialVersionUID = -2845810134932914137L;

	@Id
	@GeneratedValue
	private Long id;

	@NotNull
	private Integer actualStartTime;

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(id)
				.append(actualStartTime)
//				.append(schoolDay)
				.toHashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj instanceof SchoolHour) {
        	SchoolHour other = (SchoolHour) obj;
            return new EqualsBuilder()
                    .append(id, other.id)
                    .append(actualStartTime, other.actualStartTime)
                    .isEquals();
        } else {
            return false;
        }
	}
	
	@Override
	public int compareTo(SchoolHour other) {
		return actualStartTime - other.actualStartTime;
	}
	
	@Override
	public String toString() {
		return actualStartTime.toString();
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getActualStartTime() {
		return this.actualStartTime;
	}

	public void setActualStartTime(Integer actualStartTime) {
		this.actualStartTime = actualStartTime;
	}

}
