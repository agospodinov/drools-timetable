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
import org.drools.planner.api.domain.entity.PlanningEntity;
import org.drools.planner.api.domain.variable.PlanningVariable;
import org.drools.planner.api.domain.variable.ValueRange;
import org.drools.planner.api.domain.variable.ValueRangeType;

import com.rakursy.timetable.solver.construction.SchoolClassDifficultyFactory;

@Entity
@PlanningEntity(difficultyWeightFactoryClass = SchoolClassDifficultyFactory.class)
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
	
	@NotNull
	@ManyToOne
	private School school;
	
	@Transient
	private UUID uuid;
	
	public SchoolClass() {
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
        } else if (obj instanceof SchoolClass) {
        	SchoolClass other = (SchoolClass) obj;
            return new EqualsBuilder()
                    .append(uuid, other.uuid)
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
		clone.uuid = uuid;
		clone.period = period;
		clone.studentGroup = studentGroup;
		clone.room = room;
		clone.school = school;
		return clone;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public UUID getUuid() {
		return uuid;
	}
	
	public StudentGroup getStudentGroup() {
		return studentGroup;
	}

	public void setStudentGroup(StudentGroup studentGroup) {
		this.studentGroup = studentGroup;
	}
	
	@PlanningVariable
	@ValueRange(type=ValueRangeType.FROM_SOLUTION_PROPERTY, solutionProperty="periods")
	public Period getPeriod() {
		return period;
	}

	public void setPeriod(Period period) {
		this.period = period;
	}

	@PlanningVariable
	@ValueRange(type=ValueRangeType.FROM_SOLUTION_PROPERTY, solutionProperty="rooms")
	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}
	
	public School getSchool() {
		return school;
	}
	
	public void setSchool(School school) {
		this.school = school;
	}
	
	@Transient
	public Subject getSubject() {
		return studentGroup.getSubject();
	}

	@Transient
	public Teacher getTeacher() {
		return studentGroup.getTeacher();
	}
	
	@Transient
	public SchoolDay getSchoolDay() {
		if (period == null) {
			return null;
		}
		return period.getSchoolDay();
	}
	
	@Transient
	public SchoolHour getSchoolHour() {
		if (period == null) {
			return null;
		}
		return period.getSchoolHour();
	}
	
	@Transient
	public Integer getSchoolHourStartTime() {
		if (period == null || period.getSchoolHour() == null) {
			return null;
		}
		return period.getSchoolHour().getActualStartTime();
	}
	
}
