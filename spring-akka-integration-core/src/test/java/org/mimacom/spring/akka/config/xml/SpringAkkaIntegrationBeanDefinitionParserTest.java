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

package org.mimacom.spring.akka.config.xml;


import akka.actor.ActorSystem;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.parsing.BeanDefinitionParsingException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringAkkaIntegrationBeanDefinitionParserTest {

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Test
	public void testDefaultActorSystem() throws Exception {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(getClass().getSimpleName() + "-context.xml", getClass());
		ActorSystem actorSystem = applicationContext.getBean(ActorSystem.class);
		Assert.assertNotNull(actorSystem);
	}

	@Test
	public void testActorSystemWithName() throws Exception {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(getClass().getSimpleName() + "-WithName-context.xml", getClass());
		ActorSystem actorSystem = applicationContext.getBean(ActorSystem.class);
		Assert.assertNotNull(actorSystem);
		Assert.assertEquals("myActorSystem", actorSystem.name());
	}

	@Test
	public void testMultipleActorSystemError() throws Exception {
		this.expectedException.expect(BeanDefinitionParsingException.class);
		this.expectedException.expectMessage("Only one ActorSystem is allowed per application");
		new ClassPathXmlApplicationContext(getClass().getSimpleName() + "-WithTwoActorSystem-context.xml", getClass());
	}

	// TODO (alsa): Test with config and classLoader
}
