package com.rakursy.timetable.model.solver;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.drools.planner.api.domain.variable.PlanningValueStrengthWeightFactory;
import org.drools.planner.core.solution.Solution;

import com.rakursy.timetable.model.Room;
import com.rakursy.timetable.model.Timetable;

public class RoomStrengthWeightFactory implements PlanningValueStrengthWeightFactory {

    public Comparable createStrengthWeight(Solution solution, Object planningValue) {
//        Timetable timetable = (Timetable) solution;
        Room room = (Room) planningValue;
        return new RoomStrengthWeight(room);
    }

    public static class RoomStrengthWeight implements Comparable<RoomStrengthWeight> {

        private final Room room;

        public RoomStrengthWeight(Room room) {
			this.room = room;
        }

        public int compareTo(RoomStrengthWeight other) {
            return new CompareToBuilder()
                    .append(room.getCapacity(), other.room.getCapacity())
                    .append(room.getId(), other.room.getId())
                    .toComparison();
        }

    }

}
