/**********************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors: 
 * IBM - Initial API and implementation
 **********************************************************************/
package org.eclipse.cdt.managedbuilder.internal.scannerconfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.cdt.managedbuilder.scannerconfig.IManagedScannerInfoCollector;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;

/**
 * Implementation class for gathering the built-in compiler settings for 
 * GCC-based targets. The assumption is that the tools will answer path 
 * information in POSIX format and that the Scanner will be able to search for 
 * files using this format.
 * 
 * @since 2.0
 */
public class DefaultGCCScannerInfoCollector implements IManagedScannerInfoCollector {
	protected Map definedSymbols;
	protected static final String EQUALS = "=";	//$NON-NLS-1$
	protected List includePaths;
	protected IProject project;
	
	/**
	 * 
	 */
	public DefaultGCCScannerInfoCollector() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.cdt.make.core.scannerconfig.IScannerInfoCollector#contributeToScannerConfig(org.eclipse.core.resources.IResource, java.util.List, java.util.List, java.util.List)
	 */
	public void contributeToScannerConfig(IResource resource, List includes,
			List symbols, List targetSpecificOptions) {

		// This method will be called by the parser each time there is a new value
		Iterator pathIter = includes.listIterator();
		while (pathIter.hasNext()) {
			String path = (String) pathIter.next();
			getIncludePaths().add(path);
		}
		
		// Now add the macros
		Iterator symbolIter = symbols.listIterator();
		while (symbolIter.hasNext()) {
			// See if it has an equals
			String[] macroTokens = ((String)symbolIter.next()).split(EQUALS);
			String macro = macroTokens[0].trim();
			String value = (macroTokens.length > 1) ? macroTokens[1].trim() : new String();
			getDefinedSymbols().put(macro, value);
		}	
	}

	/* (non-Javadoc)
	 * @see org.eclipse.cdt.core.parser.IScannerInfo#getDefinedSymbols()
	 */
	public Map getDefinedSymbols() {
		if (definedSymbols == null) {
			definedSymbols = new HashMap();
		}
		return definedSymbols;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.cdt.managedbuilder.scannerconfig.IManagedScannerInfoCollector#getIncludePaths()
	 */
	public List getIncludePaths() {
		if (includePaths == null) {
			includePaths = new ArrayList();
		}
		return includePaths;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.cdt.managedbuilder.scannerconfig.IManagedScannerInfoCollector#setProject(org.eclipse.core.resources.IProject)
	 */
	public void setProject(IProject project) {
		this.project = project;
		
	}
}
