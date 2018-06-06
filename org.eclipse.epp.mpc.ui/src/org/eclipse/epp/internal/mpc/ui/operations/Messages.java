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
 *     The Eclipse Foundation - initial API and implementation
 *******************************************************************************/
package org.eclipse.epp.internal.mpc.ui.operations;

import org.eclipse.osgi.util.NLS;

class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.epp.internal.mpc.ui.operations.messages"; //$NON-NLS-1$

	public static String CompositeProfileChangeOperation_ChangeRequestError;

	public static String ProfileChangeOperationComputer_unknownOperation;

	public static String ProvisioningOperation_commaSeparator;

	public static String ProvisioningOperation_configuringProvisioningOperation;

	public static String ProvisioningOperation_nothingToUpdate;

	public static String ProvisioningOperation_proceedQuestion;

	public static String ProvisioningOperation_unavailableSolutions;

	public static String ProvisioningOperation_unavailableSolutions_proceedQuestion;

	public static String ProvisioningOperation_unavailableFeatures;

	public static String ProvisioningOperation_unexpectedErrorUrl;

	public static String ResolveFeatureNamesOperation_resolvingFeatures;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
