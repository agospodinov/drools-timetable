package com.rakursy.timetable.controller;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import org.jboss.logging.Logger;
import org.jboss.solder.logging.Category;
import org.jboss.solder.servlet.http.RequestParam;

import com.rakursy.timetable.util.qualifiers.Created;
import com.rakursy.timetable.util.qualifiers.Updated;

@Dependent
public abstract class EntityManageAction<T> {

	@Inject
	@Category("timetable")
	protected Logger log;
	
	@PersistenceContext(type=PersistenceContextType.EXTENDED)
	protected EntityManager em;
	
	@Inject
	protected BeanManager beanManager;
	
	@Inject
	@RequestParam("id")
	protected Instance<String> idProvider;
	
	@Inject
	protected Conversation conversation;
	
	protected T newEntity;
	
	protected abstract T find();
	
	protected abstract T create();
	
	public String load() {
		if (conversation.isTransient()) {
			conversation.begin();
		}
		
		if (idProvider.get() != null) {
			newEntity = find();
		} else {
			newEntity = create();
		}
		
		return "success";
	}

	@SuppressWarnings("serial")
	public String save() {
		log.info("Saving " + newEntity);
		
		if (em.contains(newEntity)) {
			newEntity = em.merge(newEntity);
			beanManager.fireEvent(newEntity, new AnnotationLiteral<Updated>() {});
		} else {
			em.persist(newEntity);
			beanManager.fireEvent(newEntity, new AnnotationLiteral<Created>() {});
		}
		
		
		if (!conversation.isTransient()) {
			conversation.end();
		}
		return "success";
	}
	
	public String cancel() {
		if (!conversation.isTransient()) {
			conversation.end();
		}
		return "success";
	}
	
}
