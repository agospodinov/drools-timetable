package com.rakursy.timetable.solver.move;

import java.util.Collection;
import java.util.Collections;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.drools.FactHandle;
import org.drools.WorkingMemory;
import org.drools.planner.core.localsearch.decider.acceptor.tabu.TabuPropertyEnabled;
import org.drools.planner.core.move.Move;

import com.rakursy.timetable.model.Room;
import com.rakursy.timetable.model.SchoolClass;

public class RoomChangeMove implements Move, TabuPropertyEnabled {

	private SchoolClass schoolClass;
	private Room room;
	
	public RoomChangeMove(SchoolClass schoolClass, Room room) {
		this.schoolClass = schoolClass;
		this.room = room;
	}

	@Override
	public boolean isMoveDoable(WorkingMemory workingMemory) {
		return !ObjectUtils.equals(room, schoolClass.getRoom())
				// and is the move necessary?
				&& (!room.getPossibleSubjects().contains(schoolClass.getSubject())
						|| room == schoolClass.getStudentGroup().getGrade().getClassRoom());
	}

	@Override
	public Move createUndoMove(WorkingMemory workingMemory) {
		return new RoomChangeMove(schoolClass, schoolClass.getRoom());
	}

	@Override
	public void doMove(WorkingMemory workingMemory) {
		FactHandle schoolClassHandle = workingMemory.getFactHandle(schoolClass);
		schoolClass.setRoom(room);
		workingMemory.update(schoolClassHandle, schoolClass);
	}
	
	@Override
	public Collection<? extends Object> getTabuProperties() {
		return Collections.singletonList(schoolClass);
	}
	
	@Override
	public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o instanceof RoomChangeMove) {
        	RoomChangeMove other = (RoomChangeMove) o;
            return new EqualsBuilder()
                    .append(schoolClass, other.schoolClass)
                    .append(room, other.room)
                    .isEquals();
        } else {
            return false;
        }
	}
	
	@Override
	public int hashCode() {
        return new HashCodeBuilder()
                .append(schoolClass)
                .append(room)
                .toHashCode();
	}
	
	@Override
	public String toString() {
		return schoolClass + " => " + room;
	}

}
