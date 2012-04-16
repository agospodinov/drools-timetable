package com.rakursy.timetable.home;

import javax.enterprise.context.Conversation;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.jboss.solder.exception.control.ExceptionToCatch;

import pl.com.it_crowd.seam.framework.EntityHome;


public class ConversationalEntityHome<E> extends EntityHome<E> {
	
	private static final long serialVersionUID = 8080774328223295502L;
	
	@Inject
	private Conversation conversation;
	
	@Inject
	private Event<ExceptionToCatch> event;
	
	public boolean load() {
		if (conversation.isTransient()) {
			conversation.begin();
		}
		return true;
	}
	
	@Override
	public boolean persist() {
		boolean outcome = false; 
		try {
			outcome = super.persist();
		} catch (Exception ex) {
			event.fire(new ExceptionToCatch(ex));
		}

		if (!conversation.isTransient()) {
			conversation.end();
		}
		return outcome;
	}
	
	@Override
	public boolean update() {
		boolean outcome = false; 
		try {
			outcome = super.update();
		} catch (Exception ex) {
			event.fire(new ExceptionToCatch(ex));
		}
		
		if (!conversation.isTransient()) {
			conversation.end();
		}
		return outcome;
	}
	
	@Override
	public boolean remove() {
		boolean outcome = false; 
		try {
			outcome = super.remove();
		} catch (Exception ex) {
			event.fire(new ExceptionToCatch(ex));
		}
		
		if (!conversation.isTransient()) {
			conversation.end();
		}
		return outcome;
	}
	
	public boolean cancel() {
		if (!conversation.isTransient()) {
			conversation.end();
		}
		return true;
	}
	
	protected boolean validate() {
		return true;
	}
	
}
