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

import java.math.BigDecimal;
import java.util.List;

/**
 * Response model for the following API calls:
 * <p>
 * POST /api/mdl/execute
 */
public class MdlExecuteResponse extends VaultResponse {
    @JsonProperty("script_execution")
    public ScriptExecution getScriptExecution() {
        return (ScriptExecution) this.get("script_execution");
    }

    public void setScriptExecution(ScriptExecution scriptExecution) {
        this.set("script_execution", scriptExecution);
    }

    @JsonProperty("statement_execution")
    public List<StatementExecution> getStatementExecution() {
		return (List<StatementExecution>) this.get("statement_execution");
	}

	public void setStatementExecution(List<StatementExecution> statementExecution) {
		this.set("statement_execution", statementExecution);
	}


    public static class ScriptExecution extends VaultModel {
        @JsonProperty("code")
        public String getCode() {
            return this.getString("code");
        }

        public void setCode(String code) {
            this.set("code", code);
        }

        @JsonProperty("message")
        public String getMessage() {
            return this.getString("message");
        }

        public void setMessage(String message) {
            this.set("message", message);
        }

        @JsonProperty("warnings")
        public Integer getWarnings() {
            return this.getInteger("warnings");
        }

        public void setWarnings(Integer warnings) {
            this.set("warnings", warnings);
        }

        @JsonProperty("failures")
        public Integer getFailures() {
            return this.getInteger("failures");
        }

        public void setFailures(Integer failures) {
            this.set("failures", failures);
        }

        @JsonProperty("exceptions")
        public Integer getExceptions() {
            return this.getInteger("exceptions");
        }

        public void setExceptions(Integer exceptions) {
            this.set("exceptions", exceptions);
        }

        @JsonProperty("components_affected")
        public Integer getComponentsAffected() {
			return this.getInteger("components_affected");
		}

        public void setComponentsAffected(Integer componentsAffected) {
            this.set("components_affected", componentsAffected);
        }

        @JsonProperty("execution_time")
        public BigDecimal getExecutionTime() {
            return this.getBigDecimal("execution_time");
        }

        public void setExecutionTime(BigDecimal executionTime) {
            this.set("execution_time", executionTime);
        }
    }

    public static class StatementExecution extends VaultModel {
        @JsonProperty("vault")
        public String getVault() {
            return this.getString("vault");
        }

        public void setVault(String vault) {
            this.set("vault", vault);
        }

        @JsonProperty("statement")
        public Integer getStatement() {
            return this.getInteger("statement");
        }

        public void setStatement(Integer statement) {
            this.set("statement", statement);
        }

        @JsonProperty("command")
        public String getCommand() {
            return this.getString("command");
        }

        public void setCommand(String command) {
            this.set("command", command);
        }

        @JsonProperty("component")
        public String getComponent() {
            return this.getString("component");
        }

        public void setComponent(String component) {
            this.set("component", component);
        }

        @JsonProperty("message")
        public String getMessage() {
            return this.getString("message");
        }

        public void setMessage(String message) {
            this.set("message", message);
        }

        @JsonProperty("response")
        public String getResponse() {
            return this.getString("response");
        }

        public void setResponse(String response) {
            this.set("response", response);
        }
    }
}