/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.cdt.core.lrparser.xlc;

import java.util.Map;

import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;
import org.eclipse.cdt.core.dom.lrparser.IParser;
import org.eclipse.cdt.core.dom.lrparser.LRParserProperties;
import org.eclipse.cdt.core.dom.lrparser.gnu.GPPLanguage;
import org.eclipse.cdt.core.dom.parser.IScannerExtensionConfiguration;
import org.eclipse.cdt.core.index.IIndex;
import org.eclipse.cdt.core.lrparser.xlc.preferences.XlcLanguagePreferences;
import org.eclipse.cdt.core.lrparser.xlc.preferences.XlcPreferenceKeys;
import org.eclipse.cdt.core.model.ICLanguageKeywords;
import org.eclipse.cdt.core.parser.IScanner;
import org.eclipse.cdt.internal.core.lrparser.xlc.cpp.XlcCPPParser;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;

/**
 * 
 * @author Mike Kucera
 */
public class XlcCPPLanguage extends GPPLanguage {

	public static final String ID = "org.eclipse.cdt.core.lrparser.xlc.cpp"; //$NON-NLS-1$ 

	private static XlcCPPLanguage DEFAULT = new XlcCPPLanguage();
	
	public static XlcCPPLanguage getDefault() {
		return DEFAULT;
	}

	public static boolean supportVectors(Map<String,String> properties) {
		String path = properties.get(LRParserProperties.TRANSLATION_UNIT_PATH);
		System.out.println("path: " + path);
		IFile[] file = ResourcesPlugin.getWorkspace().getRoot().findFilesForLocation(new Path(path));
		
		IProject project = null;
		if(file != null && file.length > 0) {
			project = file[0].getProject();
		}
		
		return Boolean.valueOf(XlcLanguagePreferences.getPreference(XlcPreferenceKeys.KEY_SUPPORT_VECTOR_TYPES, project));
	}
	
	
	@Override
	protected IParser<IASTTranslationUnit> getParser(IScanner scanner, IIndex index, Map<String,String> properties) {
		boolean supportVectors = supportVectors(properties);
		XlcCPPParser parser = new XlcCPPParser(scanner, new XlcCPPTokenMap(supportVectors), getBuiltinBindingsProvider(), index, properties);
		return parser;
	}
	
	public String getId() {
		return ID;
	}
	
	@Override
	protected IScannerExtensionConfiguration getScannerExtensionConfiguration() {
		return XlcScannerExtensionConfiguration.getInstance();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public Object getAdapter(Class adapter) {
		if(ICLanguageKeywords.class.equals(adapter))
			return XlcKeywords.CPP;
		
		return super.getAdapter(adapter);
	}
	
}