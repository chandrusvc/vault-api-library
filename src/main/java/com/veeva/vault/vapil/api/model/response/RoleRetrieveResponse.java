/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.VaultModel;

import java.util.List;

/**
 * Response Model for calls to the Document and Binder Role Retrieve endpoints:
 */

// This response class is a Parent class to DocumentRetrieveResponse and BinderRetrieveResponse.
// In the Vault API, Document Role and Binder Role endpoints are not clearly separated.
// The Request and Response classes in VAPIL for Document Role and Binder Role endpoints
// are separated into separate classes for clarity, at the cost of some code duplication.
public class RoleRetrieveResponse extends VaultResponse {

//	Note the getter is "getRoles"
	@JsonProperty("documentRoles")
	public List<Role> getRoles() {
		return (List<Role>) this.get("documentRoles");
	}

//	Note the setter is "setRoles"
	public void setRoles(List<Role> documentRoles) {
		this.set("documentRoles", documentRoles);
	}

	@JsonProperty("errorType")
	public String getErrorType() {
		return this.getString("errorType");
	}

	public void setErrorType(String errorType) {
		this.set("errorType", errorType);
	}

	@JsonProperty("errorCodes")
	public String getErrorCodes() {
		return this.getString("errorCodes");
	}

	public void setErrorCodes(String errorCodes) {
		this.set("errorCodes", errorCodes);
	}

	public static class Role extends VaultModel {
		@JsonProperty("name")
		public String getName() {
			return this.getString("name");
		}

		public void setName(String name) {
			this.set("name", name);
		}

		@JsonProperty("label")
		public String getLabel() {
			return this.getString("label");
		}

		public void setLabel(String label) {
			this.set("label", label);
		}

		@JsonProperty("assignedUsers")
		public List<Long> getAssignedUsers() {
			return (List<Long>) this.get("assignedUsers");
		}

		public void setAssignedUsers(List<Long> assignedUsers) {
			this.set("assignedUsers", assignedUsers);
		}

		@JsonProperty("assignedGroups")
		public List<Long> getAssignedGroups() {
			return (List<Long>) this.get("assignedGroups");
		}

		public void setAssignedGroups(List<Long> assignedGroups) {
			this.set("assignedGroups", assignedGroups);
		}

		@JsonProperty("availableUsers")
		public List<Long> getAvailableUsers() {
			return (List<Long>) this.get("availableUsers");
		}

		public void setAvailableUsers(List<Long> availableUsers) {
			this.set("availableUsers", availableUsers);
		}

		@JsonProperty("availableGroups")
		public List<Long> getAvailableGroups() {
			return (List<Long>) this.get("availableGroups");
		}

		public void setAvailableGroups(List<Long> availableGroups) {
			this.set("availableGroups", availableGroups);
		}

		@JsonProperty("defaultUsers")
		public List<Long> getDefaultUsers() {
			return (List<Long>) this.get("defaultUsers");
		}

		public void setDefaultUsers(List<Long> defaultUsers) {
			this.set("defaultUsers", defaultUsers);
		}

		@JsonProperty("defaultGroups")
		public List<Long> getDefaultGroups() {
			return (List<Long>) this.get("defaultGroups");
		}

		public void setDefaultGroups(List<Long> defaultGroups) {
			this.set("defaultGroups", defaultGroups);
		}
	}
}