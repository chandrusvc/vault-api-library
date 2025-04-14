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
 * Response Model for calls to the Document and Binder User Action endpoints:
 */

// This response class is a Parent class to DocumentActionInitiateResponse and BinderActionInitiateResponse.
// The Request and Response classes in VAPIL for Document User Action and Binder User Action endpoints
// are separated into separate classes for clarity, at the cost of some code duplication.
public class ActionInitiateResponse extends VaultResponse {

    @JsonProperty("id")
    public Integer getId() {
        return this.getInteger("id");
    }

    @JsonProperty("id")
    public void setId(Integer id) {
        this.set("id", id);
    }

    @JsonProperty("workflow_id__v")
    public Integer getWorkFlowId() {
        return this.getInteger("workflow_id__v");
    }

    @JsonProperty("workflow_id__v")
    public void setWorkFlowId(Integer workflowId) {
        this.set("workflow_id__v", workflowId);
    }
}