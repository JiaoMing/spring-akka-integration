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

package org.mimacom.spring.akka.util;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import org.mimacom.spring.akka.SpringExtension;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author Alain Sahli
 * @since 1.0
 */
public class SpringAwareActorFactory implements ApplicationContextAware {

	private ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public ActorRef actorOf(Class<?> actorType) {
		ActorSystem actorSystem = applicationContext.getBean(ActorSystem.class);
		return actorSystem.actorOf(SpringExtension.SpringExtProvider.get(actorSystem).props(actorType));
	}
}
