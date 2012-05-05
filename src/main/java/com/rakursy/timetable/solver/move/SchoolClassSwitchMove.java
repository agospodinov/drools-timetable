package com.rakursy.timetable.solver.move;

import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.drools.planner.core.move.Move;
import org.drools.planner.core.score.director.ScoreDirector;

import com.rakursy.timetable.model.Period;
import com.rakursy.timetable.model.Room;
import com.rakursy.timetable.model.SchoolClass;

public class SchoolClassSwitchMove implements Move {

	private SchoolClass leftSchoolClass;
	private SchoolClass rightSchoolClass;

	public SchoolClassSwitchMove(SchoolClass leftSchoolClass, SchoolClass rightSchoolClass) {
		this.leftSchoolClass = leftSchoolClass;
		this.rightSchoolClass = rightSchoolClass;
	}

	@Override
	public Move createUndoMove(ScoreDirector scoreDirector) {
		return new SchoolClassSwitchMove(rightSchoolClass, leftSchoolClass);
	}

	@Override
	public void doMove(ScoreDirector scoreDirector) {
        Period oldLeftPeriod = leftSchoolClass.getPeriod();
        Period oldRightPeriod = rightSchoolClass.getPeriod();
        Room oldLeftRoom = leftSchoolClass.getRoom();
        Room oldRightRoom = rightSchoolClass.getRoom();
        
        moveSchoolClass(scoreDirector, leftSchoolClass, oldRightPeriod, oldRightRoom);
        moveSchoolClass(scoreDirector, rightSchoolClass, oldLeftPeriod, oldLeftRoom);
	}
	
	private void moveSchoolClass(ScoreDirector scoreDirector, SchoolClass schoolClass, Period period, Room room) {
        scoreDirector.beforeAllVariablesChanged(schoolClass);
        schoolClass.setPeriod(period);
        schoolClass.setRoom(room);
        scoreDirector.afterAllVariablesChanged(schoolClass);
	}

	@Override
	public boolean isMoveDoable(ScoreDirector scoreDirector) {
		return !ObjectUtils.equals(leftSchoolClass, rightSchoolClass);
	}

	@Override
	public Collection<? extends Object> getPlanningEntities() {
		return Arrays.asList(leftSchoolClass, rightSchoolClass);
	}

	@Override
	public Collection<? extends Object> getPlanningValues() {
		return Arrays.asList(leftSchoolClass.getPeriod(), rightSchoolClass.getPeriod(), 
				leftSchoolClass.getRoom(), rightSchoolClass.getRoom());
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

