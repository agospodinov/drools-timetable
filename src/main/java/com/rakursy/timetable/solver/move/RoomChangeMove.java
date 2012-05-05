package com.rakursy.timetable.solver.move;

import java.util.Collection;
import java.util.Collections;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.drools.planner.core.move.Move;
import org.drools.planner.core.score.director.ScoreDirector;

import com.rakursy.timetable.model.Room;
import com.rakursy.timetable.model.SchoolClass;

public class RoomChangeMove implements Move {

	private SchoolClass schoolClass;
	private Room room;
	
	public RoomChangeMove(SchoolClass schoolClass, Room room) {
		this.schoolClass = schoolClass;
		this.room = room;
	}

	@Override
	public boolean isMoveDoable(ScoreDirector scoreDirector) {
		return !ObjectUtils.equals(room, schoolClass.getRoom());
	}

	@Override
	public Move createUndoMove(ScoreDirector scoreDirector) {
		return new RoomChangeMove(schoolClass, schoolClass.getRoom());
	}

	@Override
	public void doMove(ScoreDirector scoreDirector) {
        scoreDirector.beforeAllVariablesChanged(schoolClass);
		schoolClass.setRoom(room);
        scoreDirector.afterAllVariablesChanged(schoolClass);
	}

	@Override
	public Collection<? extends Object> getPlanningEntities() {
		return Collections.singletonList(schoolClass);
	}

	@Override
	public Collection<? extends Object> getPlanningValues() {
		return Collections.singletonList(room);
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
