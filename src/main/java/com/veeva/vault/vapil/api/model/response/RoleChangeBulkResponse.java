/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

/**
 * Response Model for calls to the Document and Binder Single Role assign endpoints:
 */

// This response class is a Parent class to DocumentRoleChangeBulkResponse and BinderRoleChangeBulkResponse.
// In the Vault API, Document Role and Binder Role endpoints are not clearly separated.
// The Request and Response classes in VAPIL for Document Role and Binder Role endpoints
// are separated into separate classes for clarity, at the cost of some code duplication.
public class RoleChangeBulkResponse extends VaultResponse {

    @JsonProperty("data")
    public List<RoleChange> getData() {
        return (List<RoleChange>) this.get(("data"));
    }

    public void setData(List<RoleChange> data) {
        this.set("data", data);
    }

    public static class RoleChange extends VaultResponse {

        @JsonProperty("id")
        public Integer getId() {
            return this.getInteger("id");
        }

        public void setId(Integer id) {
            this.set("id", id);
        }

        @JsonProperty("responseStatus")
        public String getResponseStatus() {
            return this.getString("responseStatus");
        }

        public void setResponseStatus(String responseStatus) {
            this.set("responseStatus", responseStatus);
        }
    }
}