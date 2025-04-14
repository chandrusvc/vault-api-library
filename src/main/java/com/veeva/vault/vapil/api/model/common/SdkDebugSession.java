/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.model.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.VaultModel;

import java.util.List;

public class SdkDebugSession extends VaultModel {
	@JsonProperty("id")
	public String getId() {
		return getString("id");
	}

	public void setId(String id) {
		this.set("id", id);
	}

	@JsonProperty("name")
	public String getName() {
		return getString("name");
	}

	public void setName(String name) {
		this.set("name", name);
	}

	@JsonProperty("user_id")
	public Long getUserId() {
		return getLong("user_id");
	}

	public void setUserId(Long userId) {
		this.set("user_id", userId);
	}

	@JsonProperty("log_level")
	public String getLogLevel() {
		return getString("log_level");
	}

	public void setLogLevel(String logLevel) {
		this.set("log_level", logLevel);
	}

	@JsonProperty("expiration_date")
	public String getExpirationDate() {
		return getString("expiration_date");
	}

	public void setExpirationDate(String expirationDate) {
		this.set("expiration_date", expirationDate);
	}

	@JsonProperty("class_filters")
	public List<ClassFilter> getClassFilters() {
		return (List<ClassFilter>) get("class_filters");
	}

	public void setClassFilters(List<ClassFilter> classFilters) {
		this.set("class_filters", classFilters);
	}

	@JsonProperty("status")
	public String getStatus() {
		return getString("status");
	}

	public void setStatus(String status) {
		this.set("status", status);
	}

	@JsonProperty("created_date")
	public String getCreatedDate() {
		return getString("created_date");
	}

	public void setCreatedDate(String createdDate) {
		this.set("created_date", createdDate);
	}

	public static class ClassFilter extends VaultModel {
		@JsonProperty("name")
		public String getName() {
			return getString("name");
		}

		public void setName(String name) {
			this.set("name", name);
		}

		@JsonProperty("code_type")
		public String getCodeType() {
			return getString("code_type");
		}

		public void setCodeType(String codeType) {
			this.set("code_type", codeType);
		}
	}

}
