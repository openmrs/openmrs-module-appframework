/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.appframework.fragment.controller;

import java.util.List;

import org.openmrs.EncounterType;
import org.openmrs.api.context.Context;
import org.openmrs.module.appframework.fragment.controller.base.ManageMetadataFragmentController;


public class AdministerEncounterTypesFragmentController extends ManageMetadataFragmentController<EncounterType> {
	
	@Override
	protected List<EncounterType> getAllMetadata() {
		return Context.getEncounterService().getAllEncounterTypes(true);
	}
	
	@Override
	protected String getTitleCode() {
		return "Encounter Type";
	}
	
	@Override
	protected EncounterType createNew() {
		return new EncounterType();
	}
	
	@Override
	protected EncounterType getExisting(String uniqueId) {
		return Context.getEncounterService().getEncounterType(Integer.valueOf(uniqueId));
	}
	
	@Override
	protected void saveMetadata(EncounterType metadata) {
		Context.getEncounterService().saveEncounterType(metadata);
	}
	
	@Override
	protected void retireMetadata(EncounterType metadata, String reason) {
		Context.getEncounterService().retireEncounterType(metadata, reason);
	}
	
	@Override
	protected void unretireMetadata(EncounterType metadata) {
		Context.getEncounterService().unretireEncounterType(metadata);
	}
	
}
