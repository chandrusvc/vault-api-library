/**
 * --------------------------------------------------------------------
 * Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 * This code is based on pre-existing content developed and
 * owned by Veeva Systems Inc. and may only be used in connection
 * with the deliverable with which it was provided to Customer.
 * --------------------------------------------------------------------
 */

package com.veeva.vault.vapil.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.common.SdkDebugSession;
import com.veeva.vault.vapil.api.model.common.SdkProfilingSession;

import java.util.List;

public class SdkDebugSessionBulkResponse extends VaultResponse {

	@JsonProperty("data")
	public List<SdkDebugSession> getData() {
		return (List<SdkDebugSession>) this.get("data");
	}

	public void setData(List<SdkDebugSession> data) {
		this.set("data", data);
	}

}
