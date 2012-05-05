package com.rakursy.timetable.solver.construction;

import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.select;
import static org.hamcrest.Matchers.hasItem;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.drools.planner.api.domain.entity.PlanningEntityDifficultyWeightFactory;
import org.drools.planner.core.solution.Solution;

import com.rakursy.timetable.model.Room;
import com.rakursy.timetable.model.SchoolClass;
import com.rakursy.timetable.model.Subject;
import com.rakursy.timetable.model.Teacher;
import com.rakursy.timetable.model.Timetable;

public class SchoolClassDifficultyFactory implements PlanningEntityDifficultyWeightFactory {

	@SuppressWarnings("rawtypes")
	@Override
	public Comparable createDifficultyWeight(Solution solution, Object planningEntity) {
		Timetable timetable = (Timetable) solution;
		SchoolClass schoolClass = (SchoolClass) planningEntity;
		Subject subject = schoolClass.getSubject();
		Integer possibleRooms = select(timetable.getRooms(), having(on(Room.class).getPossibleSubjects(), hasItem(subject))).size();
		Integer possibleTeachers = select(timetable.getTeachers(), having(on(Teacher.class).getSubjects(), hasItem(subject))).size();
		return new SchoolClassDifficultyWeight(schoolClass, possibleRooms, possibleTeachers);
	}
	
    public static class SchoolClassDifficultyWeight implements Comparable<SchoolClassDifficultyWeight> {
    	
    	private final SchoolClass schoolClass;
        private final Integer possibleRooms;
        private final Integer possibleTeachers;

		public SchoolClassDifficultyWeight(SchoolClass schoolClass, Integer possibleRooms, Integer possibleTeachers) {
			this.schoolClass = schoolClass;
			this.possibleRooms = possibleRooms;
			this.possibleTeachers = possibleTeachers;
		}
		
		public int compareTo(SchoolClassDifficultyWeight other) {
            return new CompareToBuilder()
            		.append(3 * other.possibleRooms, 3 * possibleRooms)
            		.append(other.possibleTeachers, possibleTeachers)
            		.append(schoolClass.getUuid(), other.schoolClass.getUuid())
                    .toComparison();
        }
		
    }

}
