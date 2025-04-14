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

/**
 * Model for the following API calls responses:
 * <p>
 * GET /api/{version}/uicode/distributions
 */
public class CustomPagesDistributionResponse extends VaultResponse {

    @JsonProperty("data")
    public ClientCodeDistributionUpdate getData() {
        return (ClientCodeDistributionUpdate) this.get("data");
    }

    public void setData(ClientCodeDistributionUpdate data) {
        this.set("data", data);
    }

    public static class ClientCodeDistributionUpdate extends VaultModel {
        @JsonProperty("name")
        public String getName() {
            return getString("name");
        }

        public void setName(String name) {
            this.set("name", name);
        }

        @JsonProperty("updateType")
        public String getUpdateType() {
            return getString("updateType");
        }

        public void setUpdateType(String updateType) {
            this.set("updateType", updateType);
        }

        @JsonProperty("checksum")
        public String getChecksum() {
            return getString("checksum");
        }

        public void setChecksum(String checksum) {
            this.set("checksum", checksum);
        }
    }
}