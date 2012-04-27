package com.rakursy.timetable.solver.move;

import java.util.Collection;
import java.util.Collections;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.drools.planner.core.move.Move;
import org.drools.planner.core.score.director.ScoreDirector;

import com.rakursy.timetable.model.Period;
import com.rakursy.timetable.model.SchoolClass;

public class PeriodChangeMove implements Move {

	private SchoolClass schoolClass;
	private Period period;

	public PeriodChangeMove(SchoolClass schoolClass, Period period) {
		this.schoolClass = schoolClass;
		this.period = period;
	}

	@Override
	public boolean isMoveDoable(ScoreDirector scoreDirector) {
		return !ObjectUtils.equals(period, schoolClass.getPeriod());
	}

	@Override
	public Move createUndoMove(ScoreDirector scoreDirector) {
		return new PeriodChangeMove(schoolClass, schoolClass.getPeriod());
	}

	@Override
	public void doMove(ScoreDirector scoreDirector) {
        scoreDirector.beforeAllVariablesChanged(schoolClass);
		schoolClass.setPeriod(period);
        scoreDirector.afterAllVariablesChanged(schoolClass);
	}
	
	@Override
	public Collection<? extends Object> getPlanningEntities() {
		return Collections.singletonList(schoolClass);
	}

	@Override
	public Collection<? extends Object> getPlanningValues() {
		return Collections.singletonList(period);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		} else if (o instanceof PeriodChangeMove) {
			PeriodChangeMove other = (PeriodChangeMove) o;
			return new EqualsBuilder()
					.append(schoolClass, other.schoolClass)
					.append(period, other.period)
					.isEquals();
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(schoolClass)
				.append(period)
				.toHashCode();
	}

	@Override
	public String toString() {
		return schoolClass + " => " + period;
	}

}
