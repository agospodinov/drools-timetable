package com.rakursy.timetable.solver.move;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.drools.FactHandle;
import org.drools.WorkingMemory;
import org.drools.planner.core.localsearch.decider.acceptor.tabu.TabuPropertyEnabled;
import org.drools.planner.core.move.Move;

import com.rakursy.timetable.model.Period;
import com.rakursy.timetable.model.Room;
import com.rakursy.timetable.model.SchoolClass;

public class SchoolClassSwitchMove implements Move, TabuPropertyEnabled {

	private SchoolClass leftSchoolClass;
	private SchoolClass rightSchoolClass;

	public SchoolClassSwitchMove(SchoolClass leftSchoolClass, SchoolClass rightSchoolClass) {
		this.leftSchoolClass = leftSchoolClass;
		this.rightSchoolClass = rightSchoolClass;
	}

	@Override
	public Move createUndoMove(WorkingMemory workingMemory) {
		return new SchoolClassSwitchMove(rightSchoolClass, leftSchoolClass);
	}

	@Override
	public void doMove(WorkingMemory workingMemory) {
        Period oldLeftPeriod = leftSchoolClass.getPeriod();
        Period oldRightPeriod = rightSchoolClass.getPeriod();
        Room oldLeftRoom = leftSchoolClass.getRoom();
        Room oldRightRoom = rightSchoolClass.getRoom();
        
        moveSchoolClass(workingMemory, leftSchoolClass, oldRightPeriod, oldRightRoom);
        moveSchoolClass(workingMemory, rightSchoolClass, oldLeftPeriod, oldLeftRoom);
	}
	
	private void moveSchoolClass(WorkingMemory workingMemory, SchoolClass schoolClass, Period period, Room room) {
        FactHandle factHandle = workingMemory.getFactHandle(schoolClass);
        schoolClass.setPeriod(period);
        schoolClass.setRoom(room);
        workingMemory.update(factHandle, schoolClass);
	}

	@Override
	public boolean isMoveDoable(WorkingMemory workingMemory) {
		return !ObjectUtils.equals(leftSchoolClass, rightSchoolClass);
	}

	@Override
	public Collection<? extends Object> getTabuProperties() {
		return Collections.singletonList(Arrays.asList(leftSchoolClass, rightSchoolClass));
	}
	
	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o instanceof SchoolClassSwitchMove) {
        	SchoolClassSwitchMove other = (SchoolClassSwitchMove) o;
            return new EqualsBuilder()
                    .append(leftSchoolClass, other.leftSchoolClass)
                    .append(rightSchoolClass, other.rightSchoolClass)
                    .isEquals();
        } else {
            return false;
        }
    }
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(leftSchoolClass)
				.append(rightSchoolClass)
				.toHashCode();
	}
	
	@Override
	public String toString() {
		return leftSchoolClass + " <=> " + rightSchoolClass;
	}

}

