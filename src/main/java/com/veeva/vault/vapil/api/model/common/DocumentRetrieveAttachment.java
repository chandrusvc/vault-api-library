/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.model.common;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.VaultModel;

import java.util.List;

/**
 * Model for the Document attachment data in Retrieve Document Responses
 */
public class DocumentRetrieveAttachment extends VaultModel {

	@JsonProperty("id")
	public Integer getId() {
		return getInteger("id");
	}

	public void setId(Integer id) {
		this.set("id", id);
	}

	@JsonProperty("url")
	public String getUrl() {
		return getString("url");
	}

	public void setUrl(String url) {
		this.set("url", url);
	}

}
