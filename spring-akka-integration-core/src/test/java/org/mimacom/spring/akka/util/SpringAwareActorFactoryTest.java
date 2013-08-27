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
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mimacom.spring.akka.util.stub.DummyActor;
import org.mimacom.spring.akka.util.stub.DummyService;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import static akka.pattern.Patterns.ask;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class SpringAwareActorFactoryTest {

	@Autowired
	@SuppressWarnings("SpringJavaAutowiringInspection") // Created in SpringAkkaIntegrationBeanDefinitionParser
	private SpringAwareActorFactory springAwareActorFactory;

	@Autowired
	private DummyService dummyService;

	@Test
	public void testServiceInjectionInActor() throws Exception {
		ActorRef dummyActor = springAwareActorFactory.actorOf(DummyActor.class);
		Future<Object> future = ask(dummyActor, new Object(), 3000);
		Object result = Await.result(future, Duration.create("3 seconds"));

		Mockito.verify(dummyService, Mockito.times(1)).doIt();
		Assert.assertEquals("Nothing", result);
	}
}
