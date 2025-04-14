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

public class SdkDebugSessionResponse extends VaultResponse {

	@JsonProperty("data")
	public SdkDebugSession getData() {
		return (SdkDebugSession) this.get("data");
	}

	public void setData(SdkDebugSession data) {
		this.set("data", data);
	}

}
