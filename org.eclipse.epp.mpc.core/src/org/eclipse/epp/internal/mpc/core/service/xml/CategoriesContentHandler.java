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

import org.eclipse.epp.internal.mpc.core.model.Categories;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * @author David Green
 */
public class CategoriesContentHandler extends UnmarshalContentHandler {

	private Categories model;

	@Override
	public void startElement(String uri, String localName, Attributes attributes) {
		if (localName.equals("categories")) { //$NON-NLS-1$
			model = new Categories();

		} else if (localName.equals("category")) { //$NON-NLS-1$
			org.eclipse.epp.internal.mpc.core.service.xml.CategoryContentHandler childHandler = new org.eclipse.epp.internal.mpc.core.service.xml.CategoryContentHandler();
			childHandler.setParentModel(model);
			childHandler.setParentHandler(this);
			childHandler.setUnmarshaller(getUnmarshaller());
			getUnmarshaller().setCurrentHandler(childHandler);
			childHandler.startElement(uri, localName, attributes);
		}
	}

	@Override
	public boolean endElement(String uri, String localName) throws SAXException {
		if (localName.equals("categories")) { //$NON-NLS-1$
			if (parentModel instanceof org.eclipse.epp.internal.mpc.core.model.Node) {
				((org.eclipse.epp.internal.mpc.core.model.Node) parentModel).setCategories(model);
			}
			getUnmarshaller().setModel(model);
			model = null;
			getUnmarshaller().setCurrentHandler(parentHandler);
			if (parentHandler != null) {
				parentHandler.endElement(uri, localName);
			}
			return true;
		} else if (localName.equals("category")) { //$NON-NLS-1$
			// nothing to do
		}
		return false;
	}

}
