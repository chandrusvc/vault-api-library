/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.common.ClientCodeDistribution;

import java.util.List;

/**
 * Model for the following API calls responses:
 * <p>
 * GET /api/{version}/uicode/distributions
 */
public class CustomPagesDistributionBulkResponse extends VaultResponse {

    @JsonProperty("data")
    public List<ClientCodeDistribution> getData() {
        return (List<ClientCodeDistribution>) this.get("data");
    }

    public void setData(List<ClientCodeDistribution> data) {
        this.set("data", data);
    }

}