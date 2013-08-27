/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.mimacom.spring.akka;

import akka.actor.Actor;
import akka.actor.IndirectActorProducer;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * An actor producer that lets Spring create the Actor instances.
 */
class SpringActorProducer implements IndirectActorProducer, ApplicationContextAware {

	private ApplicationContext applicationContext;
	private final Class<?> actorBeanType;

	public SpringActorProducer(ApplicationContext applicationContext, Class<?> actorBeanType) {
		this.applicationContext = applicationContext;
		this.actorBeanType = actorBeanType;
	}

	@Override
	public Actor produce() {
		if (this.actorBeanType != null) {
			return (Actor) applicationContext.getBean(this.actorBeanType);
		} else {
			throw new RuntimeException("No bean type is defined");
		}
	}

	@Override
	public Class<? extends Actor> actorClass() {
		if (this.actorBeanType != null && Actor.class.isAssignableFrom(this.actorBeanType)) {
			return this.actorBeanType.asSubclass(Actor.class);
		} else {
			throw new RuntimeException("No bean name or type is defined");
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}