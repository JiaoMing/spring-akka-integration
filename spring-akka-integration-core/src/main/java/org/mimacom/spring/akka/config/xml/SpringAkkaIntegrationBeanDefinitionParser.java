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

import org.mimacom.spring.akka.config.ActorSystemFactoryBean;
import org.mimacom.spring.akka.util.SpringAwareActorFactory;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

class SpringAkkaIntegrationBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

	private static final String ACTOR_SYSTEM_BEAN_NAME = "akkaActorSystem";
	private static final String SPRING_AWARE_ACTOR_FACTORY = "springAwareActorFactory";

	@Override
	protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
		if (!parserContext.getRegistry().containsBeanDefinition(ACTOR_SYSTEM_BEAN_NAME)) {
			createActorSystemBean(element, parserContext, builder);
		} else {
			parserContext.getReaderContext().error("Only one ActorSystem is allowed per application", element);
		}

		if (!parserContext.getRegistry().containsBeanDefinition(SPRING_AWARE_ACTOR_FACTORY)) {
			createSpringAwareActorFactory(parserContext);
		} else {
			parserContext.getReaderContext().error("Only one SpringAwareActorFactory is allowed per application", element);
		}
	}

	private void createSpringAwareActorFactory(ParserContext parserContext) {
		BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(SpringAwareActorFactory.class);
		parserContext.getRegistry().registerBeanDefinition(SPRING_AWARE_ACTOR_FACTORY, beanDefinitionBuilder.getBeanDefinition());
	}

	private void createActorSystemBean(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
		String name = element.getAttribute("name");
		String config = element.getAttribute("config");
		String classLoader = element.getAttribute("class-loader");

		if (StringUtils.hasText(name)) {
			builder.addPropertyValue("name", name);
		}
		if (StringUtils.hasText(config)) {
			builder.addPropertyReference("config", config);
		}
		if (StringUtils.hasText(classLoader)) {
			builder.addPropertyReference("classLoader", classLoader);
		}

		parserContext.getRegistry().registerBeanDefinition(ACTOR_SYSTEM_BEAN_NAME, builder.getBeanDefinition());
	}

	@Override
	protected String resolveId(Element element, AbstractBeanDefinition definition, ParserContext parserContext) throws BeanDefinitionStoreException {
		return ACTOR_SYSTEM_BEAN_NAME;
	}

	@Override
	protected Class<?> getBeanClass(Element element) {
		return ActorSystemFactoryBean.class;
	}
}
