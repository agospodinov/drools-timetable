package com.rakursy.timetable.model.solver;

import java.util.List;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.drools.planner.api.domain.variable.PlanningValueStrengthWeightFactory;
import org.drools.planner.core.solution.Solution;

import static ch.lambdaj.Lambda.*;
import static org.hamcrest.Matchers.*;

import com.rakursy.timetable.model.Room;
import com.rakursy.timetable.model.StudentGroup;
import com.rakursy.timetable.model.Timetable;

public class StudentGroupStrengthWeightFactory implements PlanningValueStrengthWeightFactory {

	@Override
	public Comparable createStrengthWeight(Solution solution, Object planningValue) {
        Timetable timetable = (Timetable) solution;
        StudentGroup studentGroup = (StudentGroup) planningValue;
        List<Room> availableRooms = select(timetable.getRooms(), 
        		having(on(Room.class).getPossibleSubjects(), hasItem(studentGroup.getSubject())));
		return new StudentGroupStrengthWeight(studentGroup, availableRooms);
	}
	
    public static class StudentGroupStrengthWeight implements Comparable<StudentGroupStrengthWeight> {

    	private StudentGroup studentGroup;
		private List<Room> availableRooms;

        public StudentGroupStrengthWeight(StudentGroup studentGroup, List<Room> availableRooms) {
			this.studentGroup = studentGroup;
			this.availableRooms = availableRooms;
		}

		public int compareTo(StudentGroupStrengthWeight other) {
            return new CompareToBuilder()
            		.append(availableRooms.size(), other.availableRooms.size())
                    .toComparison();
        }

    }

}
