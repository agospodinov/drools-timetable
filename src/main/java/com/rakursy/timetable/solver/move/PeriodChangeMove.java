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
import com.rakursy.timetable.model.SchoolClass;

public class PeriodChangeMove implements Move, TabuPropertyEnabled {

	private SchoolClass schoolClass;
	private Period period;

	public PeriodChangeMove(SchoolClass schoolClass, Period period) {
		this.schoolClass = schoolClass;
		this.period = period;
	}

	@Override
	public boolean isMoveDoable(WorkingMemory workingMemory) {
		return !ObjectUtils.equals(period, schoolClass.getPeriod());
	}

	@Override
	public Move createUndoMove(WorkingMemory workingMemory) {
		return new PeriodChangeMove(schoolClass, schoolClass.getPeriod());
	}

	@Override
	public void doMove(WorkingMemory workingMemory) {
		FactHandle schoolClassHandle = workingMemory.getFactHandle(schoolClass);
		schoolClass.setPeriod(period);
		workingMemory.update(schoolClassHandle, schoolClass);
	}

	@Override
	public Collection<? extends Object> getTabuProperties() {
		return Collections.singletonList(Arrays.asList(schoolClass, period));
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
