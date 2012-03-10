package com.rakursy.timetable.controller;

import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

import com.rakursy.timetable.model.PeriodOffRequest;

@Model
public class PeriodOffRequestAction extends EntityManageAction<PeriodOffRequest> {
	
	@Produces
	@Named
	public PeriodOffRequest getNewPeriodOffRequest() {
		return newEntity;
	}

	@Override
	protected PeriodOffRequest find() {
		return em.find(PeriodOffRequest.class, idProvider.get());
	}

	@Override
	protected PeriodOffRequest create() {
		return new PeriodOffRequest();
	}
	
}
