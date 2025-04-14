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

// This response class is a Parent class to DocumentRoleChangeResponse and BinderRoleChangeResponse.
// In the Vault API, Document Role and Binder Role endpoints are not clearly separated.
// The Request and Response classes in VAPIL for Document Role and Binder Role endpoints
// are separated into separate classes for clarity, at the cost of some code duplication.
public class RoleChangeResponse extends VaultResponse {

    @JsonProperty("updatedRoles")
    public Map<String, Map<String, List<Long>>> getUpdatedRoles() {
        return (Map<String, Map<String, List<Long>>>) this.get("updatedRoles");
    }

    public void setUpdatedRoles(Map<String, Map<String, List<Long>>> updatedRoles) {
        this.set("updatedRoles", updatedRoles);
    }
}