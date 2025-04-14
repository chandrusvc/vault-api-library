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
import java.util.Map;

/**
 * Response Model for calls to the Document and Binder User Action endpoints:
 */

// This response class is a Parent class to DocumentActionResponse and BinderActionResponse.
// The Request and Response classes in VAPIL for Document User Action and Binder User Action endpoints
// are separated into separate classes for clarity, at the cost of some code duplication.
public class ActionResponse extends VaultResponse {

    @JsonProperty("lifecycle_actions__v")
    public List<LifecycleAction> getLifecycleActions() {
        return (List<LifecycleAction>) this.get("lifecycle_actions__v");
    }

    @JsonProperty("lifecycle_actions__v")
    public void setLifecycleActions(List<LifecycleAction> lifecycleActions) {
        this.set("lifecycle_actions__v", lifecycleActions);
    }

    public static class LifecycleAction extends VaultModel {

        @JsonProperty("entry_requirements__v")
        public String getEntryRequirements() {
            return this.getString("entry_requirements__v");
        }

        @JsonProperty("entry_requirements__v")
        public void setEntryRequirements(String entryRequirements) {
            this.set("entry_requirements__v", entryRequirements);
        }

        @JsonProperty("label__v")
        public String getLabel() {
            return this.getString("label__v");
        }

        @JsonProperty("label__v")
        public void setLabel(String label) {
            this.set("label__v", label);
        }

        @JsonProperty("lifecycle__v")
        public String getLifecycle() {
            return this.getString("lifecycle__v");
        }

        @JsonProperty("lifecycle__v")
        public void setLifecycle(String lifecycle) {
            this.set("lifecycle__v", lifecycle);
        }

        @JsonProperty("lifecycle_action_type__v")
        public String getLifecycleActionType() {
            return this.getString("lifecycle_action_type__v");
        }

        @JsonProperty("lifecycle_action_type__v")
        public void setLifecycleActionType(String lifecycleActionType) {
            this.set("lifecycle_action_type__v", lifecycleActionType);
        }

        @JsonProperty("name__v")
        public String getName() {
            return this.getString("name__v");
        }

        @JsonProperty("name__v")
        public void setName(String name) {
            this.set("name__v", name);
        }

        @JsonProperty("state__v")
        public String getState() {
            return this.getString("state__v");
        }

        @JsonProperty("state__v")
        public void setState(String state) {
            this.set("state__v", state);
        }

        @JsonProperty("executable__v")
        public Boolean getExecutable() {
            return this.getBoolean("executable__v");
        }

        @JsonProperty("executable__v")
        public void setExecutable(Boolean executable) {
            this.set("executable__v", executable);
        }
    }
}