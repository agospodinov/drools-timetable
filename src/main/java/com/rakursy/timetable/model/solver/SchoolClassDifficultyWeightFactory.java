package com.rakursy.timetable.model.solver;

import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.select;
import static org.hamcrest.Matchers.hasItem;

import java.util.List;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.drools.planner.api.domain.entity.PlanningEntityDifficultyWeightFactory;
import org.drools.planner.core.solution.Solution;

import com.rakursy.timetable.model.Room;
import com.rakursy.timetable.model.SchoolClass;
import com.rakursy.timetable.model.Timetable;

public class SchoolClassDifficultyWeightFactory implements PlanningEntityDifficultyWeightFactory {

	@SuppressWarnings({ "rawtypes", "unused" })
	@Override
	public Comparable createDifficultyWeight(Solution solution, Object planningEntity) {
		Timetable timetable = (Timetable) solution;
		SchoolClass schoolClass = (SchoolClass) planningEntity;
		List<Room> availableRooms = select(timetable.getRooms(), 
        		having(on(Room.class).getPossibleSubjects(), hasItem(schoolClass.getSubject())));
		return new SchoolClassDifficultyWeight(schoolClass, availableRooms);
	}
	
	public static class SchoolClassDifficultyWeight implements Comparable<SchoolClassDifficultyWeight> {

		@SuppressWarnings("unused")
		private SchoolClass schoolClass;
		private List<Room> availableRooms;
		
		public SchoolClassDifficultyWeight(SchoolClass schoolClass, List<Room> availableRooms) {
			this.schoolClass = schoolClass;
			this.availableRooms = availableRooms;
		}

		@Override
		public int compareTo(SchoolClassDifficultyWeight other) {
			return new CompareToBuilder()
					.append(other.availableRooms.size(), availableRooms.size())
					.append(schoolClass.getId(), other.schoolClass.getId())
					.toComparison();
		}
		
	}

}
