/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.model.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.veeva.vault.vapil.api.model.VaultModel;

import java.util.List;

/**
 * Response model for the Component Definition Query
 * <p>
 * POST /api/query/components
 */
public class ComponentQueryResponse extends VaultResponse {
    @JsonProperty("data")
    public List<QueryResult> getData() {
        return (List<QueryResult>) this.get("data");
    }

    public void setData(List<QueryResult> data) {
        this.set("data", data);
    }

    @JsonIgnore
    public boolean isPaginated() {
        if (getResponseDetails() != null) {
            if (getResponseDetails().getPreviousPage() != null || getResponseDetails().getNextPage() != null) {
                return true;
            }

            if (getResponseDetails().getSize() != getResponseDetails().getTotal()) {
                return true;
            }
        }
        return false;
    }

    public static class QueryResult extends VaultModel {

    }

    @JsonProperty("responseDetails")
    public ResponseDetails getResponseDetails() {
        return (ResponseDetails) this.get("responseDetails");
    }

    public void setResponseDetails(ResponseDetails responseDetails) {
        this.set("responseDetails", responseDetails);
    }

    public class ResponseDetails extends VaultModel {

        @JsonProperty("next_page")
        public String getNextPage() {
            return this.getString("next_page");
        }

        public void setNextPage(String nextPage) {
            this.set("next_page", nextPage);
        }

        @JsonProperty("pageoffset")
        public Integer getPageOffset() {
            return this.getInteger("pageoffset");
        }

        public void setPageOffset(Integer pageOffset) {
            this.set("pageoffset", pageOffset);
        }

        @JsonProperty("pagesize")
        public Integer getPageSize() {
            return this.getInteger("pagesize");
        }

        public void setPageSize(Integer pageSize) {
            this.set("pagesize", pageSize);
        }

        @JsonProperty("previous_page")
        public String getPreviousPage() {
            return this.getString("previous_page");
        }

        public void setPreviousPage(String previousPage) {
            this.set("previous_page", previousPage);
        }

        @JsonProperty("size")
        public Integer getSize() {
            return this.getInteger("size");
        }

        public void setSize(Integer size) {
            this.set("size", size);
        }

        @JsonProperty("total")
        public Integer getTotal() {
            return this.getInteger("total");
        }

        public void setTotal(Integer total) {
            this.set("total", total);
        }

        /**
         * Determine if a next page exists for pagination
         *
         * @return true if a next page exists
         */
        @JsonIgnore
        public boolean hasNextPage() {
            return getNextPage() != null && !getNextPage().isEmpty();
        }

        /**
         * Determine if a previous page exists for pagination
         *
         * @return true if a previous page exists
         */
        @JsonIgnore
        public boolean hasPreviousPage() {
            return getPreviousPage() != null && !getPreviousPage().isEmpty();
        }
    }
}