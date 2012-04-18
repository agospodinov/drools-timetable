package com.rakursy.timetable.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.logging.Logger;
import org.jboss.solder.logging.Category;

import com.rakursy.timetable.model.School;

@ApplicationScoped
@Stateful
@Named
public class SolverPoolManager {

	@Inject
	@Category("timetable")
	private Logger log;

	private Map<School, Integer> schoolSolverLocks;

	private ThreadPoolExecutor solverThreadPool;
	
	@SuppressWarnings("unused")
	@PostConstruct
	private void initialize() {
		schoolSolverLocks = new HashMap<School, Integer>();
		solverThreadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
	}

	public Boolean isSchoolLocked(School school) {
		if (!schoolSolverLocks.containsKey(school)) {
			return false;
		}
		log.info("Current locks: " + schoolSolverLocks.get(school));
		return schoolSolverLocks.get(school) != 0;
	}

	public void addLock(School school) {
		if (schoolSolverLocks.containsKey(school)) {
			Integer currentLocks = schoolSolverLocks.get(school);
			schoolSolverLocks.put(school, currentLocks + 1);
		} else {
			schoolSolverLocks.put(school, 1);
		}
		log.info("Adding lock for " + school + "; current locks: " + schoolSolverLocks.get(school));
	}

	public void removeLock(School school) {
		if (schoolSolverLocks.containsKey(school) && schoolSolverLocks.get(school) > 0) {
			Integer currentLocks = schoolSolverLocks.get(school);
			schoolSolverLocks.put(school, currentLocks - 1);
		}
		log.info("Removing lock for " + school + "; current locks: " + schoolSolverLocks.get(school));
	}

	public Future<?> submitSolver(Runnable runnable) {
		log.info("A solver has been queued.");
		return solverThreadPool.submit(runnable);
	}

	public Integer getAvailableThreadCount() {
		return solverThreadPool.getMaximumPoolSize() - solverThreadPool.getActiveCount();
	}

}