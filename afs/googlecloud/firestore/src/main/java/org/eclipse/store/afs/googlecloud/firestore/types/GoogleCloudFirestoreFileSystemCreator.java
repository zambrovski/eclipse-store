package org.eclipse.store.afs.googlecloud.firestore.types;

/*-
 * #%L
 * EclipseStore Abstract File System Google Cloud Firestore
 * %%
 * Copyright (C) 2023 MicroStream Software
 * %%
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 * 
 * SPDX-License-Identifier: EPL-2.0
 * #L%
 */

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import com.google.api.gax.core.NoCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;

import org.eclipse.store.afs.blobstore.types.BlobStoreFileSystem;
import org.eclipse.store.configuration.exceptions.ConfigurationException;
import org.eclipse.store.configuration.types.Configuration;
import org.eclipse.store.configuration.types.ConfigurationBasedCreator;
import org.eclipse.serializer.afs.types.AFileSystem;
import org.eclipse.serializer.chars.XChars;

public class GoogleCloudFirestoreFileSystemCreator extends ConfigurationBasedCreator.Abstract<AFileSystem>
{
	private final static String CLASSPATH_PREFIX = "classpath:";
		
	public GoogleCloudFirestoreFileSystemCreator()
	{
		super(AFileSystem.class);
	}
	
	@Override
	public AFileSystem create(
		final Configuration configuration
	)
	{
		final Configuration firestoreConfiguration = configuration.child("googlecloud.firestore");
		if(firestoreConfiguration == null)
		{
			return null;
		}
		
		final FirestoreOptions.Builder optionsBuilder = FirestoreOptions.getDefaultInstance().toBuilder();
				
		firestoreConfiguration.opt("client-lib-token").ifPresent(
			value -> optionsBuilder.setClientLibToken(value)
		);
		
		firestoreConfiguration.opt("database-id").ifPresent(
			value -> optionsBuilder.setDatabaseId(value)
		);
		
		firestoreConfiguration.opt("emulator-host").ifPresent(
			value -> optionsBuilder.setEmulatorHost(value)
		);
		
		firestoreConfiguration.opt("host").ifPresent(
			value -> optionsBuilder.setHost(value)
		);
		
		firestoreConfiguration.opt("project-id").ifPresent(
			value -> optionsBuilder.setProjectId(value)
		);
		
		firestoreConfiguration.opt("quota-project-id").ifPresent(
			value -> optionsBuilder.setQuotaProjectId(value)
		);
				
		this.createCredentials(
			firestoreConfiguration,
			optionsBuilder
		);
		
		final Firestore                     firestore = optionsBuilder.build().getService();
		final boolean                       cache     = configuration.optBoolean("cache").orElse(true);
		final GoogleCloudFirestoreConnector connector = cache
			? GoogleCloudFirestoreConnector.Caching(firestore)
			: GoogleCloudFirestoreConnector.New(firestore)
		;
		return BlobStoreFileSystem.New(connector);
	}

	private void createCredentials(
		final Configuration            configuration,
		final FirestoreOptions.Builder optionsBuilder
	)
	{
		configuration.opt("credentials.type").ifPresent(credentialsProvider ->
		{
			switch(credentialsProvider)
			{
				case "none":
				{
					optionsBuilder.setCredentialsProvider(NoCredentialsProvider.create());
				}
				break;

				case "input-stream":
				{
					final String inputStreamPath = configuration.get("credentials.input-stream");
					if(XChars.isEmpty(inputStreamPath))
					{
						throw new ConfigurationException(
							configuration,
							"googlecloud.firestore.credentials.input-stream must be defined when " +
							"googlecloud.firestore.credentials.type=input-stream"
						);
					}
					
					try
					{
						optionsBuilder.setCredentials(
							GoogleCredentials.fromStream(
								this.createInputStream(
									configuration,
									inputStreamPath
								)
							)
						);
					}
					catch(final IOException e)
					{
						throw new ConfigurationException(configuration, e);
					}
				}
				break;
				
				case "default":
				default:
				{
					try
					{
						optionsBuilder.setCredentials(GoogleCredentials.getApplicationDefault());
					}
					catch(final IOException e)
					{
						throw new ConfigurationException(configuration, e);
					}
				}
				break;
			}
		});
	}
	
	private InputStream createInputStream(
		final Configuration configuration,
		final String        path
	)
	throws IOException
	{
		if(path.toLowerCase().startsWith(CLASSPATH_PREFIX))
		{
			return this.getClass().getResourceAsStream(path.substring(CLASSPATH_PREFIX.length()));
		}
	
		try
		{
			final URL url = new URL(path);
			return url.openStream();
		}
		catch(final MalformedURLException e)
		{
			return new FileInputStream(path);
		}
	}
	
}
