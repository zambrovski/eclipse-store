package org.eclipse.store.storage.exceptions;

/*-
 * #%L
 * EclipseStore Storage
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

public class StorageExceptionIoWriting extends StorageExceptionIo
{
	///////////////////////////////////////////////////////////////////////////
	// constructors //
	/////////////////

	public StorageExceptionIoWriting()
	{
		super();
	}

	public StorageExceptionIoWriting(final String message)
	{
		super(message);
	}

	public StorageExceptionIoWriting(final Throwable cause)
	{
		super(cause);
	}

	public StorageExceptionIoWriting(final String message, final Throwable cause)
	{
		super(message, cause);
	}

	public StorageExceptionIoWriting(
		final String    message           ,
		final Throwable cause             ,
		final boolean   enableSuppression ,
		final boolean   writableStackTrace
	)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}



}
