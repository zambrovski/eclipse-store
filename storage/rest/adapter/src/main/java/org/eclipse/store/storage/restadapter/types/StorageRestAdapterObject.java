package org.eclipse.store.storage.restadapter.types;

/*-
 * #%L
 * EclipseStore Storage REST Adapter
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

public interface StorageRestAdapterObject extends StorageViewDataConverterProvider
{
	public long getDefaultValueLength();

	public void setDefaultValueLength(long defaultValueLength);

	public ViewerObjectDescription getObject(long objectId, long fixedOffset, long fixedLength, long variableOffset,
			long variableLength, long valueLength, boolean resolveReferences);
}
