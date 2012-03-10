package com.rakursy.timetable.solver.move;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.drools.ClassObjectFilter;
import org.drools.FactHandle;
import org.drools.WorkingMemory;
import org.drools.planner.core.localsearch.decider.acceptor.tabu.TabuPropertyEnabled;
import org.drools.planner.core.move.Move;
import org.drools.planner.core.score.constraint.ConstraintOccurrence;

import com.rakursy.timetable.model.SchoolClass;
import com.rakursy.timetable.model.StudentGroup;

public class StudentGroupChangeMove implements Move, TabuPropertyEnabled {

	private SchoolClass schoolClass;
	private StudentGroup studentGroup;
	
	public StudentGroupChangeMove(SchoolClass schoolClass, StudentGroup studentGroup) {
		this.schoolClass = schoolClass;
		this.studentGroup = studentGroup;
	}

	@Override
	public boolean isMoveDoable(WorkingMemory workingMemory) {
		return !ObjectUtils.equals(studentGroup, schoolClass.getStudentGroup());
	}

	@Override
	public Move createUndoMove(WorkingMemory workingMemory) {
		return new StudentGroupChangeMove(schoolClass, schoolClass.getStudentGroup());
	}

	@Override
	public void doMove(WorkingMemory workingMemory) {
		FactHandle schoolClassHandle = workingMemory.getFactHandle(schoolClass);
		schoolClass.setStudentGroup(studentGroup);
		workingMemory.update(schoolClassHandle, schoolClass);
//		printBrokenConstraints(workingMemory);
	}
	
//	public void printBrokenConstraints(WorkingMemory workingMemory) {
//        Iterator<ConstraintOccurrence> it = (Iterator<ConstraintOccurrence>) workingMemory.iterateObjects(
//                new ClassObjectFilter(ConstraintOccurrence.class));
//        while (it.hasNext()) {
//            ConstraintOccurrence constraintOccurrence = it.next();
//            System.out.println(constraintOccurrence.getRuleId() + " : " + constraintOccurrence.getConstraintType() + " : " + constraintOccurrence.getCauses());
//        }
//    }
	
	@Override
	public Collection<? extends Object> getTabuProperties() {
		return Collections.singletonList(Arrays.asList(schoolClass, studentGroup));
	}
	
	@Override
	public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o instanceof StudentGroupChangeMove) {
        	StudentGroupChangeMove other = (StudentGroupChangeMove) o;
            return new EqualsBuilder()
                    .append(schoolClass, other.schoolClass)
                    .append(studentGroup, other.studentGroup)
                    .isEquals();
        } else {
            return false;
        }
	}
	
	@Override
	public int hashCode() {
        return new HashCodeBuilder()
                .append(schoolClass)
                .append(studentGroup)
                .toHashCode();
	}
	
	@Override
	public String toString() {
		return schoolClass + " => " + studentGroup;
	}

}
