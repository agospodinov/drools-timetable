package com.rakursy.timetable.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.drools.planner.api.domain.entity.PlanningEntity;
import org.drools.planner.api.domain.variable.PlanningVariable;
import org.drools.planner.api.domain.variable.ValueRangeFromSolutionProperty;

@Entity
@PlanningEntity
public class SchoolClass implements Serializable {

	private static final long serialVersionUID = 2923161934264247932L;

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne
	private StudentGroup studentGroup;

	@ManyToOne
	private Period period;

	@ManyToOne
	private Room room;

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(id)
				.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj instanceof SchoolClass) {
        	SchoolClass other = (SchoolClass) obj;
            return new EqualsBuilder()
                    .append(id, other.id)
                    .isEquals();
        } else {
            return false;
        }
	}
	
	@Override
	public String toString() {
		return studentGroup.getSubject() + " : " + studentGroup.getTeacher() + " : " + room;
	}

	@Override
	public SchoolClass clone() {
		SchoolClass clone = new SchoolClass();
		clone.id = id;
		clone.period = period;
		clone.studentGroup = studentGroup;
		clone.room = room;
		return clone;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public StudentGroup getStudentGroup() {
		return studentGroup;
	}

	public void setStudentGroup(StudentGroup studentGroup) {
		this.studentGroup = studentGroup;
	}
	
	@PlanningVariable
	@ValueRangeFromSolutionProperty(propertyName="periods")
	public Period getPeriod() {
		return period;
	}

	public void setPeriod(Period period) {
		this.period = period;
	}

	@PlanningVariable
	@ValueRangeFromSolutionProperty(propertyName="rooms")
	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}
	
	@Transient
	public Subject getSubject() {
		return studentGroup.getSubject();
	}

	@Transient
	public Teacher getTeacher() {
		return studentGroup.getTeacher();
	}


}
