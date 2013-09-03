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

import akka.actor.Actor;
import akka.actor.ActorSystem;
import akka.testkit.TestActorRef;
import org.mimacom.spring.akka.SpringExtension;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;

@SuppressWarnings("WeakerAccess") // Must be accessible for other packages because it's a test utility class.
public class SpringAwareTestActorFactory {

	/**
	 * Creates a test {@code TestActorRef} with dependency injection enabled. In order to work a spring-akka
	 * {@code ApplicationContext} must have been initialized.
	 *
	 * @param actorType          The type of the actor to return. Cannot be <code>null</code>
	 * @param applicationContext The current {@code ApplicationContext}. Cannot be <code>null</code>
	 * @return A test reference to the actor
	 */
	public static <T extends Actor> TestActorRef<T> create(Class<T> actorType, ApplicationContext applicationContext) {
		Assert.notNull(actorType, "actorType cannot be null.");
		Assert.notNull(applicationContext, "applicationContext cannot be null.");
		ActorSystem actorSystem = applicationContext.getBean(ActorSystem.class);
		return TestActorRef.create(actorSystem, SpringExtension.SpringExtProvider.get(actorSystem).props(actorType));
	}
}
