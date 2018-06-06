/*******************************************************************************
 * Copyright (c) 2010, 2018 The Eclipse Foundation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *      The Eclipse Foundation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.epp.internal.mpc.core.service.xml;


import org.eclipse.epp.internal.mpc.core.model.Market;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;


/**
 * @author David Green
 */
public class MarketContentHandler extends UnmarshalContentHandler {

	private static final String NS_URI = ""; //$NON-NLS-1$

	private Market model;

	@Override
	public void startElement(String uri, String localName, Attributes attributes) {
		if (localName.equals("market")) { //$NON-NLS-1$
			model = new Market();

			model.setId(attributes.getValue(NS_URI,"id")); //$NON-NLS-1$
			model.setName(attributes.getValue(NS_URI,"name")); //$NON-NLS-1$
			model.setUrl(toUrlString(attributes.getValue(NS_URI, "url"))); //$NON-NLS-1$
		} else if (localName.equals("category")) { //$NON-NLS-1$
			org.eclipse.epp.internal.mpc.core.service.xml.CategoryContentHandler childHandler = new org.eclipse.epp.internal.mpc.core.service.xml.CategoryContentHandler();
			childHandler.setParentModel(model);
			childHandler.setParentHandler(this);
			childHandler.setUnmarshaller(getUnmarshaller());
			getUnmarshaller().setCurrentHandler(childHandler);
			childHandler.startElement(uri,localName,attributes);
		}
	}

	@Override
	public boolean endElement(String uri, String localName) throws SAXException {
		if (localName.equals("market")) { //$NON-NLS-1$
			if (parentModel instanceof org.eclipse.epp.internal.mpc.core.model.Marketplace) {
				((org.eclipse.epp.internal.mpc.core.model.Marketplace)parentModel).getMarket().add(model);
			}
			getUnmarshaller().setModel(model);
			model = null;
			getUnmarshaller().setCurrentHandler(parentHandler);
			if (parentHandler != null) {
				parentHandler.endElement(uri,localName);
			}
			return true;
		} else if (localName.equals("category")) { //$NON-NLS-1$
			// nothing to do
		}
		return false;
	}

}
