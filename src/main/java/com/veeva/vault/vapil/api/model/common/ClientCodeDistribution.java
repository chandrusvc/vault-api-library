/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.model.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.VaultModel;

/**
 * Model for the Client Code Distribution object.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClientCodeDistribution extends VaultModel {

    @JsonProperty("name")
    public String getName() {
        return getString("name");
    }

    public void setName(String name) {
        set("name", name);
    }

    @JsonProperty("checksum")
    public String getChecksum() {
        return getString("checksum");
    }

    public void setChecksum(String checksum) {
        set("checksum", checksum);
    }

    @JsonProperty("size")
    public Integer getSize() {
        return getInteger("size");
    }

    public void setSize(Integer size) {
        set("size", size);
    }


}
