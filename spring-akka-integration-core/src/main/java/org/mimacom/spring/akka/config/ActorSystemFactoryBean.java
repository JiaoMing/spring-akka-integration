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

package org.mimacom.spring.akka.config;

import akka.actor.ActorSystem;
import com.typesafe.config.Config;
import org.mimacom.spring.akka.SpringExtension;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;

public class ActorSystemFactoryBean extends AbstractFactoryBean<ActorSystem> implements ApplicationContextAware {

	private String name;
	private Config config;
	private ClassLoader classLoader;
	private ApplicationContext applicationContext;

	@SuppressWarnings("UnusedDeclaration") // Is used by the bean definition parser
	public void setClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

	@SuppressWarnings("UnusedDeclaration") // Is used by the bean definition parser
	public void setConfig(Config config) {
		this.config = config;
	}

	@SuppressWarnings("UnusedDeclaration") // Is used by the bean definition parser
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Class<?> getObjectType() {
		return ActorSystem.class;
	}

	@Override
	protected ActorSystem createInstance() throws Exception {
		ActorSystem actorSystem;

		if (this.name != null && this.config == null && this.classLoader == null) {
			actorSystem = ActorSystem.create(this.name);
		} else if (this.name != null && this.config != null && this.classLoader == null) {
			actorSystem = ActorSystem.create(this.name, this.config);
		} else //noinspection ConstantConditions
			if (this.name != null && this.config != null && this.classLoader != null) {
				actorSystem = ActorSystem.create(this.name, this.config, this.classLoader);
			} else {
				actorSystem = ActorSystem.create();
			}

		createSpringAwareActorFactory(actorSystem);

		return actorSystem;
	}

	private void createSpringAwareActorFactory(ActorSystem actorSystem) {
		Assert.notNull(actorSystem, "actorSystem cannot be null");
		SpringExtension.SpringExtProvider.get(actorSystem).initialize(this.applicationContext);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
