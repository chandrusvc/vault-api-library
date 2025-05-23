/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.request;

import com.veeva.vault.vapil.api.model.response.*;
import com.veeva.vault.vapil.connector.HttpRequestConnector;
import com.veeva.vault.vapil.connector.HttpRequestConnector.HttpMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Configuration Migration
 *
 * @vapil.apicoverage <a href="https://developer.veevavault.com/api/25.1/#configuration-migration">https://developer.veevavault.com/api/25.1/#configuration-migration</a>
 */
public class ConfigurationMigrationRequest extends VaultRequest<ConfigurationMigrationRequest> {
	private static Logger log = LoggerFactory.getLogger(QueryRequest.class);

	// API Endpoints
	private static final String URL_PACKAGE = "/services/package";
	private static final String URL_PACKAGE_DEPLOY = "/vobject/vault_package__v/{package_id}/actions/deploy";
	private static final String URL_PACKAGE_DEPLOY_RESULTS = "/vobject/vault_package__v/{package_id}/actions/deploy/results";
	private static final String URL_PACKAGE_IMPORT_RESULTS = "/vobject/vault_package__v/{package_id}/actions/import/results";
	private static final String URL_COMPONENT_QUERY = "/query/components";
	private static final String URL_OUTBOUND_PACKAGE_DEPENDENCIES = "/vobjects/outbound_package__v/{package_id}/dependencies";
	private static final String URL_VALIDATE = "/services/package/actions/validate";
	private static final String URL_VALIDATE_INBOUND = "/services/vobject/vault_package__v/{package_id}/actions/validate";
	private static final String URL_VAULT_COMPARE = "/objects/vault/actions/compare";
	private static final String URL_VAULT_CONFIG = "/objects/vault/actions/configreport";
	private static final String URL_VAULT_CONFIG_RESULTS = "/objects/vault/actions/configReport/{job_id}/report";
	private static final String URL_CONFIG_MODE_ENABLE = "/services/configuration_mode/actions/enable";
	private static final String URL_CONFIG_MODE_DISABLE = "/services/configuration_mode/actions/disable";


	// API Request Parameters
	private HttpRequestConnector.BinaryFile binaryFile;
	private List<String> componentTypes;
	private DetailsType detailsType;
	private Boolean generateOutboundPackages;
	private ZonedDateTime includeComponentsModifiedSince;
	private Boolean includeDocBinderTemplates;
	private Boolean includeInactiveComponents;
	private Boolean includeVaultSettings;
	private OutputFormat outputFormat;
	private ResultsType resultsType;
	private Boolean suppressEmptyResults;
	private String inputPath;
	private String outputPath;

	private ConfigurationMigrationRequest() {
	}

	/**
	 * Export Package
	 *
	 * @param packageName name of package to export
	 * @return JobCreateResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/services/package</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/25.1/#export-package' target='_blank'>https://developer.veevavault.com/api/25.1/#export-package</a>
	 * @vapil.request <pre>
	 * JobCreateResponse resp = vaultClient.newRequest(ConfigurationMigrationRequest.class)
	 * 				.exportPackage(packageName);</pre>
	 * @vapil.response <pre>
	 * System.out.println("Status = " + resp.getResponseStatus());
	 * if (resp.isSuccessful()) {
	 *   System.out.println("JobId = " + resp.getJobId());
	 *   System.out.println("Url = " + resp.getUrl());
	 *
	 *   for (JobCreateResponse.Warning warning : resp.getWarnings()) {
	 *     System.out.println("Type = " + warning.getMessage() + ", Message = " + warning.getMessage());
	 *   }
	 * }</pre>
	 */
	public JobCreateResponse exportPackage(String packageName) {
		String url = vaultClient.getAPIEndpoint(URL_PACKAGE);

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addBodyParam("packageName", packageName);

		return send(HttpMethod.POST, request, JobCreateResponse.class);
	}

	/**
	 * Import Package
	 *
	 * @return JobCreateResponse
	 * @vapil.api <pre>
	 * PUT /api/{version}/services/package</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/25.1/#import-package' target='_blank'>https://developer.veevavault.com/api/25.1/#import-package</a>
	 * @vapil.request <pre>
	 * <i>Example 1 - File</i>
	 * PackageResponse resp = vaultClient.newRequest(ConfigurationMigrationRequest.class)
	 * 				.setInputPath(filePath)
	 * 				.importPackage();</pre>
	 * @vapil.request <pre>
	 * <i>Example 2 - Binary</i>
	 * PackageResponse resp = vaultClient.newRequest(ConfigurationMigrationRequest.class)
	 *   .setBinaryFile("file", Files.readAllBytes(new File(filePath).toPath()))
	 *   .importPackage();</pre>
	 * @vapil.response <pre>
	 * System.out.println("Status = " + resp.getResponseStatus());
	 * if (resp.isSuccessful()) {
	 *   System.out.println("Id = " + resp.getJobId());
	 * 	 System.out.println("Url = " + resp.getUrl());
	 * }</pre>
	 */
	public JobCreateResponse importPackage() {
		String url = vaultClient.getAPIEndpoint(URL_PACKAGE);

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_MULTIPART_FORM);

		if (this.inputPath != null) {
			request.addFileMultiPart("file", inputPath);
		}

		if (this.binaryFile != null) {
			request.addFileBinary("file", binaryFile.getBinaryContent(), binaryFile.getFileName());
		}
		return send(HttpMethod.PUT, request, JobCreateResponse.class);
	}

	/**
	 * Deploy Package
	 *
	 * @param packageId id of package to deploy
	 * @return JobCreateResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/vobject/vault_package__v/{package_id}/actions/deploy</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/25.1/#deploy-package' target='_blank'>https://developer.veevavault.com/api/25.1/#deploy-package</a>
	 * @vapil.request <pre>
	 * JobCreateResponse resp = vaultClient.newRequest(ConfigurationMigrationRequest.class)
	 * 				.deployPackage(packageId);</pre>
	 * @vapil.response <pre>
	 * System.out.println("Status = " + resp.getResponseStatus());
	 * if (resp.isSuccessful()) {
	 *   System.out.println("JobId = " + resp.getJobId());
	 *   System.out.println("Url = " + resp.getUrl());
	 *
	 *   for (JobCreateResponse.Warning warning : resp.getWarnings()) {
	 *     System.out.println("Type = " + warning.getMessage() + ", Message = " + warning.getMessage());
	 *   }
	 * }</pre>
	 */
	public JobCreateResponse deployPackage(String packageId) {
		String url = vaultClient.getAPIEndpoint(URL_PACKAGE_DEPLOY).replace("{package_id}", packageId);

		HttpRequestConnector request = new HttpRequestConnector(url);

		return send(HttpMethod.POST, request, JobCreateResponse.class);
	}

	/**
	 * Retrieve Package Deploy Results
	 *
	 * @param packageId id of package to retrieve results
	 * @return PackageDeploymentResultsResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/vobject/vault_package__v/{package_id}/actions/deploy/results</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/25.1/#retrieve-package-deploy-results' target='_blank'>https://developer.veevavault.com/api/25.1/#retrieve-package-deploy-results</a>
	 * @vapil.request <pre>
	 * PackageDeploymentResultsResponse resp = vaultClient.newRequest(ConfigurationMigrationRequest.class)
	 * 				.retrievePackageDeployResults(packageId);</pre>
	 * @vapil.response <pre>
	 * System.out.println("Status = " + resp.getResponseStatus());
	 * if (resp.isSuccessful()) {
	 *   System.out.println("Total Components = " + resp.getResponseDetails().getTotalComponents());
	 *   System.out.println("Deployment Status = " + resp.getResponseDetails().getDeploymentStatus());
	 * }</pre>
	 */
	public PackageDeploymentResultsResponse retrievePackageDeployResults(String packageId) {
		String url = vaultClient.getAPIEndpoint(URL_PACKAGE_DEPLOY_RESULTS).replace("{package_id}", packageId);

		HttpRequestConnector request = new HttpRequestConnector(url);

		return send(HttpMethod.GET, request, PackageDeploymentResultsResponse.class);
	}

	/**
	 * Retrieve Package Import Results
	 *
	 * <p>
	 *     Retrieve the results of an imported VPK given the package id
	 * </p>
	 * @param packageId id of package to retrieve results
	 * @return PackageImportResultsResponse
	 * @vapil.api <pre>
	 *     GET /api/{version}/vobject/vault_package__v/{package_id}/actions/import/results</pre>
	 * @vapil.request <pre>
	 *     PackageImportResultsResponse resp = vaultClient.newRequest(ConfigurationMigrationRequest.class)
	 *			.retrievePackageImportResults(packageId);
	 * </pre>
	 * @vapil.response <pre>
	 *     if (resp.isSuccessful) {
	 *         System.out.println("Package Name = " + resp.getVaultPackage().getName());
	 *     }
	 * </pre>
	 */
	public PackageImportResultsResponse retrievePackageImportResults(String packageId) {
		String url = vaultClient.getAPIEndpoint(URL_PACKAGE_IMPORT_RESULTS).replace("{package_id}", packageId);

		HttpRequestConnector request = new HttpRequestConnector(url);

		return send(HttpMethod.GET, request, PackageImportResultsResponse.class);
	}

	/**
	 * Retrieve Package Import Results
	 *
	 * <p>
	 *     Retrieve the results of an imported VPK given the href provided in the artifacts section of the
	 *     Job Status response.
	 * </p>
	 * @param href url of the retrieve package import results endpoint
	 * @return PackageImportResultsResponse
	 * @vapil.api <pre>
	 *     GET /api/{version}/vobject/vault_package__v/{package_id}/actions/import/results</pre>
	 * @vapil.request <pre>
	 *     PackageImportResultsResponse resp = vaultClient.newRequest(ConfigurationMigrationRequest.class)
	 *			.retrievePackageImportResults(href);
	 * </pre>
	 * @vapil.response <pre>
	 *     if (resp.isSuccessful) {
	 *         System.out.println("Package Name = " + resp.getVaultPackage().getName());
	 *     }
	 * </pre>
	 */
	public PackageImportResultsResponse retrievePackageImportResultsByHref(String href) {
		return vaultClient.newRequest(JobRequest.class).retrieveJobArtifactByHref(href, PackageImportResultsResponse.class);
	}

	/**
	 * Retrieve Outbound Package Dependencies
	 * <p>
	 *     Retrieve existing dependencies for an outbound package.
	 * </p>
	 * @param packageId id of package to retrieve dependencies
	 * @return OutboundPackageDependenciesResponse
	 * @vapil.api <pre>
	 *     GET /api/{version}/vobject/outbound_package__v/{package_id}/dependencies
	 * </pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/25.1/#retrieve-outbound-package-dependencies' target='_blank'>https://developer.veevavault.com/api/25.1/#retrieve-outbound-package-dependencies</a>
	 * @vapil.request <pre>
	 *     OutboundPackageDependenciesResponse resp = vaultClient.newRequest(ConfigurationMigrationRequest.class)
	 *     					.retrieveOutboundPackageDependencies(packageId);
	 * </pre>
	 * @vapil.response <pre>
	 *     if (resp.isSuccessful) {
	 *     	System.out.println("Total Dependencies = " + resp.getResponseDetails().getTotalDependencies());
	 *     }
	 * </pre>
	 */
	public OutboundPackageDependenciesResponse retrieveOutboundPackageDependencies(String packageId) {
		String url = vaultClient.getAPIEndpoint(URL_OUTBOUND_PACKAGE_DEPENDENCIES).replace("{package_id}", packageId);

		HttpRequestConnector request = new HttpRequestConnector(url);

		return send(HttpMethod.GET, request, OutboundPackageDependenciesResponse.class);
	}

	/**
	 * Vault Compare
	 *
	 * @param targetVaultId target vault id
	 * @return JobCreateResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/objects/vault/actions/compare</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/25.1/#vault-compare' target='_blank'>https://developer.veevavault.com/api/25.1/#vault-compare</a>
	 * @vapil.request <pre>
	 * JobCreateResponse resp = vaultClient.newRequest(ConfigurationMigrationRequest.class)
	 * 				.setResultsType(ConfigurationMigrationRequest.ResultsType.COMPLETE)
	 * 				.setDetailsType(ConfigurationMigrationRequest.DetailsType.COMPLEX)
	 * 				.setIncludeDocBinderTemplates(true)
	 * 				.setIncludeVaultSettings(true)
	 * 				.setComponentsType(componentTypes)
	 * 				.vaultCompare(targetVaultId);</pre>
	 * @vapil.response <pre>
	 * System.out.println("Status = " + resp.getResponseStatus());
	 * if (resp.isSuccessful()) {
	 *   System.out.println("JobId = " + resp.getJobId());
	 *   System.out.println("Url = " + resp.getUrl());
	 *
	 *   for (JobCreateResponse.Warning warning : resp.getWarnings()) {
	 *     System.out.println("Type = " + warning.getMessage() + ", Message = " + warning.getMessage());
	 *   }
	 * }</pre>
	 */
	public JobCreateResponse vaultCompare(int targetVaultId) {
		String url = vaultClient.getAPIEndpoint(URL_VAULT_COMPARE);

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addBodyParam("vault_id", Integer.valueOf(targetVaultId));

		if (resultsType != null) {
			request.addBodyParam("results_type", resultsType.getValue());
		}

		if (detailsType != null) {
			request.addBodyParam("details_type", detailsType.getValue());
		}

		if (includeDocBinderTemplates != null) {
			request.addBodyParam("include_doc_binder_templates", includeDocBinderTemplates);
		}

		if (includeVaultSettings != null) {
			request.addBodyParam("include_vault_settings", includeVaultSettings);
		}

		if (componentTypes != null && !componentTypes.isEmpty()) {
			request.addBodyParam("component_types", String.join(",", componentTypes));
		}

		if (generateOutboundPackages != null) {
			request.addBodyParam("generate_outbound_packages", generateOutboundPackages);
		}

		return send(HttpMethod.POST, request, JobCreateResponse.class);
	}

	/**
	 * Vault Configuration Report
	 *
	 * @return JobCreateResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/objects/vault/actions/configreport</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/25.1/#vault-configuration-report' target='_blank'>https://developer.veevavault.com/api/25.1/#vault-configuration-report</a>
	 * @vapil.request <pre>
	 * JobCreateResponse resp = vaultClient.newRequest(ConfigurationMigrationRequest.class)
	 * 				.setIncludeVaultSettings(true)
	 * 				.setIncludeInactiveComponents(true)
	 * 				.setIncludeComponentsModifiedSince(ZonedDateTime.now().minusDays(14))
	 * 				.setSuppressEmptyResults(true)
	 * 				.setComponentsType(componentTypes)
	 * 				.setOutputFormat(ConfigurationMigrationRequest.OutputFormat.EXCEL)
	 * 				.vaultConfigurationReport();
	 * </pre>
	 * @vapil.response <pre>
	 * System.out.println("Status = " + resp.getResponseStatus());
	 * if (resp.isSuccessful()) {
	 *   System.out.println("JobId = " + resp.getJobId());
	 *   System.out.println("Url = " + resp.getUrl());
	 *
	 *   for (JobCreateResponse.Warning warning : resp.getWarnings()) {
	 *     System.out.println("Type = " + warning.getMessage() + ", Message = " + warning.getMessage());
	 *   }
	 * }
	 * </pre>
	 */
	public JobCreateResponse vaultConfigurationReport() {
		String url = vaultClient.getAPIEndpoint(URL_VAULT_CONFIG);

		HttpRequestConnector request = new HttpRequestConnector(url);

		if (includeVaultSettings != null) {
			request.addBodyParam("include_vault_settings", includeVaultSettings);
		}

		if (includeInactiveComponents != null) {
			request.addBodyParam("include_inactive_components", includeInactiveComponents);
		}

		if (includeComponentsModifiedSince != null) {
			String modifiedDate = includeComponentsModifiedSince.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			request.addBodyParam("include_components_modified_since", modifiedDate);
		}

		if (suppressEmptyResults != null) {
			request.addBodyParam("suppress_empty_results", suppressEmptyResults);
		}

		if (componentTypes != null && !componentTypes.isEmpty()) {
			request.addBodyParam("component_types", String.join(",", componentTypes));
		}

		if (outputFormat != null) {
			request.addBodyParam("output_format", outputFormat.getValue());
		}

		return send(HttpMethod.POST, request, JobCreateResponse.class);
	}

	/**
	 * Retrieve Configuration Report Results
	 *
	 * @param jobId The job id of the configuration report
	 *
	 * @return VaultResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/vault/actions/configreport/{job_id}/report</pre>
	 * @vapil.vaultlink <a href='TODO' target='_blank'>TODO</a>
	 * @vapil.request <pre>
	 * <i>Example 1 - Binary</i>
	 * VaultResponse response = vaultClient.newRequest(ConfigurationMigrationRequest.class)
	 * 		.retrieveConfigurationReportResults(String.valueOf(jobId));
	 *
	 * <i>Example 2 - File</i>
	 * VaultResponse response = vaultClient.newRequest(ConfigurationMigrationRequest.class)
	 * 		.setOutputPath(outputPath)
	 * 		.retrieveConfigurationReportResults(String.valueOf(jobId));
	 * </pre>
	 * @vapil.response <pre>
	 * <i>Example 1 - Binary</i>
	 * System.out.println("Response Status: " + response.getResponseStatus());
	 * byte[] fileContent = response.getBinaryContent();
	 *
	 * <i>Example 2 - To File</i>
	 * System.out.println("Response Status: " + response.getResponseStatus());
	 * System.out.println("Output Path: " + response.getOutputFilePath());
	 * </pre>
	 */
	public VaultResponse retrieveConfigurationReportResults(String jobId) {
		String url = vaultClient.getAPIEndpoint(URL_VAULT_CONFIG_RESULTS)
				.replace("{job_id}", jobId);

		HttpRequestConnector request = new HttpRequestConnector(url);

		if (outputPath != null) {
			return sendToFile(HttpMethod.GET, request, outputPath, VaultResponse.class);
		} else {
			return sendReturnBinary(HttpMethod.GET, request, VaultResponse.class);
		}
	}

	/**
	 * <b>Component Definition Query</b>
	 * <p>
	 * Perform a Vault query request. Subsequent queries and pagination
	 * are needed to retrieve the full result set if the total records returned exceed the "pagesize"
	 * parameter in the response. See {@link #componentDefinitionQueryByPage(String)} (String)}.
	 * <p>
	 * Returned records can be retrieved via the "getData" method in the response.
	 *
	 * @param vql The fully formed query string
	 * @return ComponentQueryResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/query/components</pre>
	 * @vapil.vaultlink <a href='TODO' target='_blank'>TODO</a>
	 * @vapil.request <pre>
	 * <i>Example 1 - Vault Component</i>
	 * String vql = "SELECT id,name__v,component_name__v FROM vault_component__v";
	 * ComponentQueryResponse response = vaultClient.newRequest(ConfigurationMigrationRequest.class)
	 * 		.componentDefinitionQuery(vql);
	 *
	 * <i>Example 2 - Vault Package Component</i>
	 * String vql = "SELECT id,name__v,component_type__v FROM vault_package_component__v";
	 * ComponentQueryResponse response = vaultClient.newRequest(ConfigurationMigrationRequest.class)
	 * 		.componentDefinitionQuery(vql);
	 * </pre>
	 * @vapil.response <pre>
	 * <i>Example 1 - Vault Component</i>
	 * for (ComponentQueryResponse.QueryResult queryResult : response.getData()) {
	 *     System.out.println("----Vault Component----");
	 *     System.out.println("ID: " + queryResult.get("id"));
	 *     System.out.println("Name: " + queryResult.get("name__v"));
	 *     System.out.println("Component Name: " + queryResult.get("component_name__v"));
	 * }
	 *
	 * <i>Example 2 - Vault Package Component</i>
	 * for (ComponentQueryResponse.QueryResult queryResult : response.getData()) {
	 * 		System.out.println("----Vault Package Component----");
	 * 		System.out.println("ID: " + queryResult.get("id"));
	 * 		System.out.println("Name: " + queryResult.get("name__v"));
	 * 		System.out.println("Component Type: " + queryResult.get("component_type__v"));
	 * }
	 * </pre>
	 */
	public ComponentQueryResponse componentDefinitionQuery(String vql) {
		HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_COMPONENT_QUERY));

		request.addBodyParam("q", vql);
		log.info("Query = " + vql);

		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_XFORM);

		return send(HttpMethod.POST, request, ComponentQueryResponse.class);
	}

	/**
	 * <b>Component Definition Query</b>
	 * <p>
	 * Perform a paginated component query based on the URL from a previous query (previous_page or next_page in the response details).
	 * <p>
	 *
	 * @param pageUrl The URL from the previous_page or next_page parameter
	 * @return ComponentQueryResponse
	 * @vapil.api <pre>
	 * <i>Example component query url format</i>
	 * POST /api/v25.1/query/components/xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx?pagesize=1000{@literal &}pageoffset=1000
	 * </pre>
	 * @vapil.vaultlink <a href='TODO' target='_blank'>TODO</a>
	 * @vapil.request <pre>
	 * ComponentQueryResponse componentQueryResponse = vaultClient.newRequest(ConfigurationMigrationRequest.class)
	 * 		.componentDefinitionQuery("SELECT id,name__v,component_name__v FROM vault_component__v");
	 *
	 * ComponentQueryResponse nextPageResponse = vaultClient.newRequest(ConfigurationMigrationRequest.class)
	 * 		.componentDefinitionQueryByPage(componentQueryResponse.getResponseDetails().getNextPage());
	 * </pre>
	 * @vapil.response <pre>
	 * System.out.println("Total Results: " + nextPageResponse.getResponseDetails().getTotal());
	 * System.out.println("Page Offset: " + nextPageResponse.getResponseDetails().getPageOffset());
	 *
	 * for (ComponentQueryResponse.QueryResult queryResult : nextPageResponse.getData()) {
	 * 		System.out.println("----Vault Component----");
	 * 		System.out.println("ID: " + queryResult.get("id"));
	 * 		System.out.println("Name: " + queryResult.get("name__v"));
	 * 		System.out.println("Component Name: " + queryResult.get("component_name__v"));
	 * }
	 * </pre>
	 */
	public ComponentQueryResponse componentDefinitionQueryByPage(String pageUrl) {
		// Manipulate the URL for passing in the exact URL from next_page or previous_page
		String url = vaultClient.getPaginationEndpoint(pageUrl);
		HttpRequestConnector request = new HttpRequestConnector(url);
		return send(HttpMethod.POST, request, ComponentQueryResponse.class);
	}

	/**
	 * <b>Validate Package</b>
	 *
	 * @return ValidatePackageResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/services/package/actions/validate</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/25.1/#validate-package' target='_blank'>https://developer.veevavault.com/api/25.1/#validate-package</a>
	 * @vapil.request <pre>
	 * <i>Example 1 - File</i>
	 * ValidatePackageResponse resp = vaultClient.newRequest(ConfigurationMigrationRequest.class)
	 * 				.setInputPath(filePath)
	 * 				.validatePackage();</pre>
	 * @vapil.request <pre>
	 * <i>Example 2 - Binary</i>
	 * ValidatePackageResponse resp = vaultClient.newRequest(ConfigurationMigrationRequest.class)
	 * 				.setBinaryFile("file", Files.readAllBytes(new File(filePath).toPath()))
	 * 				.validatePackage();</pre>
	 * @vapil.response <pre>
	 * System.out.println("Status = " + resp.getResponseStatus());
	 * if (resp.isSuccessful()) {
	 *   System.out.println("Author = " + resp.getResponseDetails().getAuthor());
	 *   System.out.println("Status = " + resp.getResponseDetails().getPackageStatus());
	 * }</pre>
	 */
	public ValidatePackageResponse validatePackage() {
		String url = vaultClient.getAPIEndpoint(URL_VALIDATE);

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_MULTIPART_FORM);

		if (this.inputPath != null) {
			request.addFileMultiPart("file", inputPath);
		}

		if (this.binaryFile != null) {
			request.addFileBinary("file", binaryFile.getBinaryContent(), binaryFile.getFileName());
		}
		return send(HttpMethod.POST, request, ValidatePackageResponse.class);
	}


	/**
	 * <b>Validate Inbound Package</b>
	 *
	 * @param packageId The id field value of the vault_package__v object record to validate.
	 * @return ValidatePackageResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/services/vobject/vault_package__v/{package_id}/actions/validate</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/25.1/#validate-inbound-package' target='_blank'>https://developer.veevavault.com/api/25.1/#validate-inbound-package</a>
	 * @vapil.request <pre>
	 * <i>Example 1 - File</i>
	 * ValidatePackageResponse resp = vaultClient.newRequest(ConfigurationMigrationRequest.class)
	 * 				.validateInboundPackage();</pre>
	 * @vapil.response <pre>
	 * System.out.println("Status = " + resp.getResponseStatus());
	 * if (resp.isSuccessful()) {
	 *   System.out.println("Author = " + resp.getResponseDetails().getAuthor());
	 *   System.out.println("Status = " + resp.getResponseDetails().getPackageStatus());
	 * }</pre>
	 */
	public ValidatePackageResponse validateInboundPackage(String packageId) {
		String url = vaultClient.getAPIEndpoint(URL_VALIDATE_INBOUND)
				.replace("{package_id}", packageId);

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);

		return send(HttpMethod.POST, request, ValidatePackageResponse.class);
	}

	/**
	 * <b>Enable Configuration Mode</b>
	 *
	 * @return VaultResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/services/configuration_mode/actions/enable</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/25.1/#enable-configuration-mode' target='_blank'>https://developer.veevavault.com/api/25.1/#enable-configuration-mode</a>
	 * @vapil.request <pre>
	 * VaultResponse response = vaultClient.newRequest(ConfigurationMigrationRequest.class)
	 * 		.enableConfigurationMode();
	 * </pre>
	 * @vapil.response <pre>
	 * System.out.println("Response Status = " + response.getResponseStatus());
	 * System.out.println("Response Message= " + response.getResponseMessage());
	 * </pre>
	 */
	public VaultResponse enableConfigurationMode() {
		String url = vaultClient.getAPIEndpoint(URL_CONFIG_MODE_ENABLE);

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);

		return send(HttpMethod.POST, request, VaultResponse.class);
	}

	/**
	 * <b>Disable Configuration Mode</b>
	 *
	 * @return VaultResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/services/configuration_mode/actions/disable</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/25.1/#disable-configuration-mode' target='_blank'>https://developer.veevavault.com/api/25.1/#disable-configuration-mode</a>
	 * @vapil.request <pre>
	 * VaultResponse response = vaultClient.newRequest(ConfigurationMigrationRequest.class)
	 * 		.disableConfigurationMode();
	 * </pre>
	 * @vapil.response <pre>
	 * System.out.println("Response Status = " + response.getResponseStatus());
	 * System.out.println("Response Message= " + response.getResponseMessage());
	 * </pre>
	 */
	public VaultResponse disableConfigurationMode() {
		String url = vaultClient.getAPIEndpoint(URL_CONFIG_MODE_DISABLE);

		HttpRequestConnector request = new HttpRequestConnector(url);
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);

		return send(HttpMethod.POST, request, VaultResponse.class);
	}

	/*
	 *
	 * Request constants
	 *
	 */

	/**
	 * Optional: To show component level details only, set to none.
	 * To include simple attribute-level details, set to simple.
	 * To show all attribute-level details, set to complex.
	 * If omitted, this defaults to simple.
	 */
	public enum DetailsType {
		COMPLEX("complex"),
		NONE("none"),
		SIMPLE("SIMPLE"); //default

		private final String value;

		DetailsType(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	/**
	 * Optional: Output report as either an Excel (XSLX) or Excel_Macro_Enabled (XLSM) file.
	 * If omitted, defaults to Excel_Macro_Enabled.
	 */
	public enum OutputFormat {
		EXCEL("Excel"),
		EXCEL_MACRO_ENABLED("Excel_Macro_Enabled"); //default

		private final String value;

		OutputFormat(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	/**
	 * Optional: To include all configuration values, set this to complete.
	 * To only see the differences between vaults, set to differences.
	 * If omitted, this defaults to differences.
	 */
	public enum ResultsType {
		COMPLETE("complete"),
		DIFFERENCES("differences"); //default

		private final String value;

		ResultsType(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}


	/*
	 *
	 * Request parameter setters
	 *
	 */

	/**
	 * Specify source data in an input file
	 *
	 * @param filename      file name (no path)
	 * @param binaryContent byte array of the file content
	 * @return The Request
	 */
	public ConfigurationMigrationRequest setBinaryFile(String filename, byte[] binaryContent) {
		this.binaryFile = new HttpRequestConnector.BinaryFile(filename, binaryContent);
		return this;
	}

	/**
	 * Set list of component types for compare or config report
	 *
	 * @param componentTypes component types
	 * @return The Request
	 */
	public ConfigurationMigrationRequest setComponentsType(List<String> componentTypes) {
		this.componentTypes = componentTypes;
		return this;
	}

	/**
	 * Set generate outbound packages baed on compare report differences
	 *
	 * @param generateOutboundPackages boolean
	 * @return The Request
	 */
	public ConfigurationMigrationRequest setGenerateOutboundPackages(Boolean generateOutboundPackages) {
		this.generateOutboundPackages = generateOutboundPackages;
		return this;
	}

	/**
	 * Set details type for compare report
	 *
	 * @param detailsType details type
	 * @return The Request
	 */
	public ConfigurationMigrationRequest setDetailsType(DetailsType detailsType) {
		this.detailsType = detailsType;
		return this;
	}

	/**
	 * Set components modified since date in compare report
	 *
	 * @param includeComponentsModifiedSince results type
	 * @return The Request
	 */
	public ConfigurationMigrationRequest setIncludeComponentsModifiedSince(ZonedDateTime includeComponentsModifiedSince) {
		this.includeComponentsModifiedSince = includeComponentsModifiedSince;
		return this;
	}

	/**
	 * Set include binder doc templates in compare report
	 *
	 * @param includeDocBinderTemplates boolean
	 * @return The Request
	 */
	public ConfigurationMigrationRequest setIncludeDocBinderTemplates(Boolean includeDocBinderTemplates) {
		this.includeDocBinderTemplates = includeDocBinderTemplates;
		return this;
	}

	/**
	 * Set include inactive components in config report
	 *
	 * @param includeInactiveComponents boolean
	 * @return The Request
	 */
	public ConfigurationMigrationRequest setIncludeInactiveComponents(Boolean includeInactiveComponents) {
		this.includeInactiveComponents = includeInactiveComponents;
		return this;
	}


	/**
	 * Set include vault settings in compare or config report
	 *
	 * @param includeVaultSettings boolean
	 * @return The Request
	 */
	public ConfigurationMigrationRequest setIncludeVaultSettings(Boolean includeVaultSettings) {
		this.includeVaultSettings = includeVaultSettings;
		return this;
	}

	/**
	 * Specify source data in an input file
	 *
	 * @param inputPath Absolute path to the file for the request
	 * @return The Request
	 */
	public ConfigurationMigrationRequest setInputPath(String inputPath) {
		this.inputPath = inputPath;
		return this;
	}

	/**
	 * Set output format for config report
	 *
	 * @param outputFormat boolean
	 * @return The Request
	 */
	public ConfigurationMigrationRequest setOutputFormat(OutputFormat outputFormat) {
		this.outputFormat = outputFormat;
		return this;
	}

	/**
	 * Set results type for compare report
	 *
	 * @param resultsType results type
	 * @return The Request
	 */
	public ConfigurationMigrationRequest setResultsType(ResultsType resultsType) {
		this.resultsType = resultsType;
		return this;
	}

	/**
	 * Set suppress empty results for config report
	 *
	 * @param suppressEmptyResults boolean
	 * @return The Request
	 */
	public ConfigurationMigrationRequest setSuppressEmptyResults(Boolean suppressEmptyResults) {
		this.suppressEmptyResults = suppressEmptyResults;
		return this;
	}

	/**
	 * Specify source data in an output file
	 *
	 * @param outputPath Absolute path to the file for the response
	 * @return The Request
	 */
	public ConfigurationMigrationRequest setOutputPath(String outputPath) {
		this.outputPath = outputPath;
		return this;
	}

}
