package org.eclipse.store.integrations.spring.boot.types;

/*-
 * #%L
 * EclipseStore Integrations SpringBoot
 * %%
 * Copyright (C) 2023 - 2024 MicroStream Software
 * %%
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * #L%
 */

import org.eclipse.store.integrations.spring.boot.types.configuration.EclipseStoreProperties;
import org.eclipse.store.integrations.spring.boot.types.factories.EmbeddedStorageFoundationFactory;
import org.eclipse.store.integrations.spring.boot.types.factories.EmbeddedStorageManagerFactory;
import org.eclipse.store.storage.embedded.types.EmbeddedStorageFoundation;
import org.eclipse.store.storage.embedded.types.EmbeddedStorageManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Supplier;

@Configuration
@AutoConfigureAfter(EclipseStoreSpringBoot.class)
public class DefaultEclipseStoreConfiguration {

  /**
   * <p>This means is annotated with {@code @ConfigurationProperties},
   * which means its properties are bound to the "org.eclipse.store" prefix in the configuration files.</p>
   */
  @Bean
  @Qualifier("defaultEclipseStoreProperties")
  @ConfigurationProperties(prefix = "org.eclipse.store")
  public EclipseStoreProperties defaultEclipseStoreProperties() {
    return new EclipseStoreProperties();
  }

  /**
   * Creates a supplier for embedded storage foundation.
   *
   * @param eclipseStoreProperties properties.
   * @param foundationFactory      embedded foundation factory.
   * @return embedded foundation factory supplier with provided properties.
   */
  @Bean
  @Qualifier("defaultEclipseStore")
  @ConditionalOnMissingBean
  public Supplier<EmbeddedStorageFoundation<?>> embeddedStorageFoundationSupplier(
      @Qualifier("defaultEclipseStoreProperties") EclipseStoreProperties eclipseStoreProperties,
      EmbeddedStorageFoundationFactory foundationFactory
  ) {
    return () -> foundationFactory.createStorageFoundation(eclipseStoreProperties);
  }

  /**
   * Creates embedded storage manager based on a single configuration.
   *
   * @param embeddedStorageManagerFactory     embedded storage manager factory.
   * @param eclipseStoreProperties            properties used for construction.
   * @param embeddedStorageFoundationSupplier embedded storage foundation factory supplier.
   * @return storage manager.
   */
  @Bean
  @Qualifier("defaultEclipseStore")
  @ConditionalOnMissingBean
  public EmbeddedStorageManager embeddedStorageManager(
      EmbeddedStorageManagerFactory embeddedStorageManagerFactory,
      @Qualifier("defaultEclipseStoreProperties") EclipseStoreProperties eclipseStoreProperties,
      @Qualifier("defaultEclipseStore")
      Supplier<EmbeddedStorageFoundation<?>> embeddedStorageFoundationSupplier
  ) {
    return embeddedStorageManagerFactory.createStorage(
        embeddedStorageFoundationSupplier.get(),
        eclipseStoreProperties.isAutoStart()
    );
  }

}
