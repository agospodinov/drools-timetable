package com.rakursy.timetable.solver.move.factory;

import java.util.ArrayList;
import java.util.List;

import org.drools.planner.core.move.Move;
import org.drools.planner.core.move.factory.CachedMoveFactory;
import org.drools.planner.core.solution.Solution;

import com.rakursy.timetable.model.SchoolClass;
import com.rakursy.timetable.model.StudentGroup;
import com.rakursy.timetable.model.Timetable;
import com.rakursy.timetable.solver.move.StudentGroupChangeMove;

public class StudentGroupChangeMoveFactory extends CachedMoveFactory {

	@SuppressWarnings("rawtypes")
	@Override
	public List<Move> createCachedMoveList(Solution solution) {
		Timetable timetable = (Timetable) solution;
		List<Move> moves = new ArrayList<Move>();
		for (SchoolClass schoolClass : timetable.getSchoolClasses()) {
			for (StudentGroup studentGroup : schoolClass.getStudentGroup().getGrade().getStudentGroups()) {
				moves.add(new StudentGroupChangeMove(schoolClass, studentGroup));
			}
		}
		return moves;
	}

}
