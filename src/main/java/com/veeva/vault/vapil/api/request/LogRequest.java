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

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The Audit APIs retrieve information about audits and audit types
 *
 * @vapil.apicoverage <a href="https://developer.veevavault.com/api/25.1/#logs">https://developer.veevavault.com/api/25.1/#logs</a>
 */
public class LogRequest extends VaultRequest<LogRequest> {
	// API Endpoints
	private static final String URL_AUDIT_TYPES = "/metadata/audittrail";
	private static final String URL_AUDIT_METADATA = "/metadata/audittrail/{audit_trail_type}";
	private static final String URL_AUDIT_DETAILS = "/audittrail/{audit_trail_type}";
	private static final String URL_API_USAGE = "/logs/api_usage";
	private static final String URL_CODE_LOG = "/logs/code/runtime";
	private static final String URL_AUDIT_DOCUMENT = "/objects/documents/{doc_id}/audittrail";
	private static final String URL_AUDIT_OBJECT = "/vobjects/{object_name}/{object_record_id}/audittrail";
	private static final String URL_EMAIL_NOTIFICATION_HISTORY = "/notifications/histories";
	private static final String URL_PROFILING_SESSIONS = "/code/profiler";
	private static final String URL_PROFILING_SESSION = "/code/profiler/{session_name}";
	private static final String URL_PROFILING_SESSION_END = "/code/profiler/{session_name}/actions/end";
	private static final String URL_PROFILING_SESSION_RESULTS = "/code/profiler/{session_name}/results";
	private static final String URL_DEBUG_SESSION = "/logs/code/debug/{id}";
	private static final String URL_DEBUG_SESSIONS = "/logs/code/debug";
	private static final String URL_DEBUG_FILES = "/logs/code/debug/{id}/files";
	private static final String URL_DEBUG_RESET = "/logs/code/debug/{id}/actions/reset";

	// API Request Parameters for audit details request
	private static final String PARAM_START_DATE = "start_date";
	private static final String PARAM_END_DATE = "end_date";
	private static final String PARAM_ALL_DATES = "all_dates";
	private static final String PARAM_FORMAT_RESULT = "format_result";
	private static final String PARAM_LIMIT = "limit";
	private static final String PARAM_EVENT = "events";
	private static final String PARAM_OBJECT = "objects";

	// API Request Parameters for api_usage request
	private static final String PARAM_DATE = "date";
	private static final String PARAM_LOG_FORMAT = "log_format";

	// API Request Parameters for SDK Profiling Session request
	private static final String PARAM_LABEL = "label";
	private static final String PARAM_USER_ID = "user_id";
	private static final String PARAM_DESCRIPTION = "description";

	// API Request Parameters for SDK Debug Session request
	private static final String PARAM_INCLUDE_INACTIVE = "include_inactive";
	private static final String PARAM_NAME = "name";
	private static final String PARAM_LOG_LEVEL = "log_level";
	private static final String PARAM_CLASS_FILTERS = "class_filters";

	// Required date format for audit details request and retrieve email notification history request
	private static final String DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'";

	// Required date format for api_usage request and retrieve email notification history request
	private static final String DATE_PATTERN = "yyyy-MM-dd";

	// Builder Parameters for audit details request
	private ZonedDateTime startDateTime;
	private ZonedDateTime endDateTime;
	private LocalDate startDate;
	private LocalDate endDate;
	private Boolean allDates;
	private String formatResult;
	private Integer limit;

	// Builder Parameters for the api_usage request
	private String logFormat;
	private String outputPath;
	private Set<String> objects;
	private Set<String> events;

	// Builder Parameters for the SDK Profiling request
	private String userId;
	private String description;

	// Builder Parameters for the SDK Debug Log request
	private boolean includeInactive;
	private String logLevel;
	private Set<String> classFilters;

	private LogRequest() {
		// Defaults for the request
		startDateTime = null;
		endDateTime = null;
		startDate = null;
		endDate = null;
		allDates = false;
		formatResult = FormatResultType.JSON.getValue();
		limit = 200;

		// Defaults for api_usage request
		logFormat = LogFormatType.CSV.getValue();
		outputPath = null;
	}

	/**
	 * <b>Retrieve Audit Types</b>
	 * <p>
	 * Retrieves all available audit types you have permission to access.
	 *
	 * @return AuditTypesResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/metadata/audittrail</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/25.1/#retrieve-audit-types' target='_blank'>https://developer.veevavault.com/api/25.1/#retrieve-audit-types</a>
	 * @vapil.request <pre>
	 * AuditTypesResponse resp = vaultClient.newRequest(LogRequest.class)
	 * 				.retrieveAuditTypes();</pre>
	 * @vapil.response <pre>
	 * for (AuditType a : resp.getAuditTypes()) {
	 *   System.out.println("Name = " + a.getName());
	 *   System.out.println("Label = " + a.getLabel());
	 *   System.out.println("Url = " + a.getUrl());
	 * }</pre>
	 */
	public AuditTypesResponse retrieveAuditTypes() {
		HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_AUDIT_TYPES));

		return send(HttpMethod.GET, request, AuditTypesResponse.class);
	}

	/**
	 * <b>Retrieve Audit Metadata</b>
	 * <p>
	 * Retrieve all fields and their metadata for a specified audit trail or log type.
	 *
	 * @param auditTrailType The name of the specified audit type (The AuditTrailType enum values translate to document_audit_trail, object_audit_trail, etc.).
	 * @return AuditMetadataResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/metadata/audittrail/{audit_trail_type}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/25.1/#retrieve-audit-metadata' target='_blank'>https://developer.veevavault.com/api/25.1/#retrieve-audit-metadata</a>
	 * @vapil.request <pre>
	 * AuditMetadataResponse resp = vaultClient.newRequest(LogRequest.class)
	 * 				.retrieveAuditMetadata(LogRequest.AuditTrailType.DOCUMENT);</pre>
	 * @vapil.response <pre>
	 * AuditMetadata metadata = resp.getData();
	 * System.out.println("Name = " + metadata.getName());
	 * System.out.println("Label = " + metadata.getLabel());
	 *
	 * System.out.println("Fields ****");
	 * for (AuditMetadata.Field f : metadata.getFields()) {
	 *   System.out.println("**** Field **** ");
	 *   System.out.println("Name = " + f.getName());
	 *   System.out.println("Label = " + f.getLabel());
	 *   System.out.println("Type = " + f.getType());
	 * }</pre>
	 */
	public AuditMetadataResponse retrieveAuditMetadata(AuditTrailType auditTrailType) {
		String url = vaultClient.getAPIEndpoint(URL_AUDIT_METADATA)
				.replace("{audit_trail_type}", auditTrailType.getValue());

		HttpRequestConnector request = new HttpRequestConnector(url);

		return send(HttpMethod.GET, request, AuditMetadataResponse.class);
	}

	/**
	 * <b>Retrieve Audit Details</b>
	 * <p>
	 * Retrieve audit details for a specific audit type. This request supports parameters to narrow the results to specific audit events, or a specified date and time within the past 30 days.
	 * <p>
	 * NOTE: Only returns the first page; use retrieveAuditDetailsByPage paginate through previous and next pages
	 *
	 * @param <T>            Response Type class
	 * @param auditTrailType The name of the specified audit type (The AuditTrailType enum values translate to document_audit_trail, object_audit_trail, etc.).
	 * @return Returns one of the following:<br>
	 * SystemAuditResponse when auditTrailType is system_audit_trail<br>
	 * LoginAuditResponse when auditTrailType is login_audit_trail<br>
	 * ObjectAuditResponse when auditTrailType is object_audit_trail<br>
	 * DomainAuditResponse when auditTrailType is domain_audit_trail<br>
	 * DocumentAuditResponse when auditTrailType is document_audit_response
	 * JobCreateResponse when format result is CSV
	 * @vapil.api <pre>
	 * GET /api/{version}/audittrail/{audit_trail_type}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/25.1/#retrieve-audit-details' target='_blank'>https://developer.veevavault.com/api/25.1/#retrieve-audit-details</a>
	 * @vapil.request <pre>
	 * <i>Example 1 - DocumentAuditResponse</i>
	 * DocumentAuditResponse resp = vaultClient.newRequest(LogRequest.class)
	 * 				.setStartDate(ZonedDateTime.now(ZoneId.of("UTC")).minusDays(29))
	 * 				.setEndDate(ZonedDateTime.now(ZoneId.of("UTC")).minusDays(1))
	 * 				.setLimit(4)
	 * 				.setEvents(new HashSet&lt;&gt;(Arrays.asList("UploadDocBulk", "ExportBinder")))
	 * 				.retrieveAuditDetails(LogRequest.AuditTrailType.DOCUMENT);</pre>
	 * @vapil.response <pre>
	 * <i>Example 1 - DocumentAuditResponse</i>
	 * AuditDetailsResponse.ResponseDetails details = resp.getResponseDetails();
	 * System.out.println("Offset = " + details.getOffset());
	 * System.out.println("Limit = " + details.getLimit());
	 * System.out.println("Size = " + details.getSize());
	 * System.out.println("Total = " + details.getTotal());
	 * System.out.println("Object/Name = " + details.getDetailsObject().getName());
	 * System.out.println("Object/Label = " + details.getDetailsObject().getLabel());
	 * System.out.println("Object/Url = " + details.getDetailsObject().getUrl());
	 *
	 * System.out.println("Items ****");
	 * for (DocumentAuditData data : resp.getData()) {
	 *   System.out.println("\n**** Data Item **** ");
	 *   System.out.println("id = " + data.getId());
	 *   System.out.println("timestamp = " + data.getTimestamp());
	 *   System.out.println("UserName = " + data.getUserName());
	 *   System.out.println("Full Name = " + data.getFullName());
	 *   System.out.println("Action = " + data.getAction());
	 *   System.out.println("Item = " + data.getItem());
	 *   System.out.println("Field Name = " + data.getFieldName());
	 *   System.out.println("Workflow Name = " + data.getWorkflowName());
	 *   System.out.println("Task Name = " + data.getTaskName());
	 *   System.out.println("Signature Meaning = " + data.getSignatureMeaning());
	 *   System.out.println("View License = " + data.getViewLicense());
	 *   System.out.println("Job Instance ID = " + data.getJobInstanceId());
	 *   System.out.println("Doc ID = " + data.getDocId());
	 *   System.out.println("Version = " + data.getVersion());
	 *   System.out.println("Document Url = " + data.getDocumentUrl());
	 *   System.out.println("Event Description = " + data.getEventDescription());
	 * }
	 * </pre>
	 * @vapil.request <pre>
	 * <i>Example 3 - DomainAuditResponse</i>
	 * DomainAuditResponse resp = vaultClient.newRequest(LogRequest.class)
	 * 				.setStartDate(ZonedDateTime.now(ZoneId.of("UTC")).minusDays(10))
	 * 				.retrieveAuditDetails(LogRequest.AuditTrailType.DOMAIN);</pre>
	 * @vapil.response <pre>
	 * <i>Example 2 - DomainAuditResponse</i>
	 * AuditDetailsResponse.ResponseDetails details = resp.getResponseDetails();
	 * System.out.println("Offset = " + details.getOffset());
	 * System.out.println("Limit = " + details.getLimit());
	 * System.out.println("Size = " + details.getSize());
	 * System.out.println("Total = " + details.getTotal());
	 * System.out.println("Object/Name = " + details.getDetailsObject().getName());
	 * System.out.println("Object/Label = " + details.getDetailsObject().getLabel());
	 * System.out.println("Object/Url = " + details.getDetailsObject().getUrl());
	 *
	 * System.out.println("Items ****");
	 * for (DomainAuditData data : resp.getData()) {
	 *   System.out.println("\n**** Data Item **** ");
	 *   System.out.println("id = " + data.getId());
	 *   System.out.println("timestamp = " + data.getTimestamp());
	 *   System.out.println("UserId = " + data.getUserId());
	 *   System.out.println("UserName = " + data.getUserName());
	 *   System.out.println("Full Name = " + data.getFullName());
	 *   System.out.println("Action = " + data.getAction());
	 *   System.out.println("Type = " + data.getType());
	 *   System.out.println("Item = " + data.getItem());
	 *   System.out.println("FieldName = " + data.getFieldName());
	 *   System.out.println("NewValue = " + data.getNewValue());
	 *   System.out.println("EventDescription = " + data.getEventDescription());
	 * }
	 * </pre>
	 * @vapil.request <pre>
	 * <i>Example 3 - LoginAuditResponse</i>
	 * LoginAuditResponse resp = vaultClient.newRequest(LogRequest.class)
	 * 				.setStartDate(ZonedDateTime.now(ZoneId.of("UTC")).minusDays(10))
	 * 				.retrieveAuditDetails(LogRequest.AuditTrailType.LOGIN);</pre>
	 * @vapil.response <pre>
	 * <i>Example 3 - LoginAuditResponse</i>
	 * AuditDetailsResponse.ResponseDetails details = resp.getResponseDetails();
	 * System.out.println("Offset = " + details.getOffset());
	 * System.out.println("Limit = " + details.getLimit());
	 * System.out.println("Size = " + details.getSize());
	 * System.out.println("Total = " + details.getTotal());
	 * System.out.println("Object/Name = " + details.getDetailsObject().getName());
	 * System.out.println("Object/Label = " + details.getDetailsObject().getLabel());
	 * System.out.println("Object/Url = " + details.getDetailsObject().getUrl());
	 *
	 * System.out.println("Items ****");
	 * for (LoginAuditData data : resp.getDataw()) {
	 *   System.out.println("\n**** Data Item **** ");
	 *   System.out.println("id = " + data.getId());
	 *   System.out.println("timestamp = " + data.getTimestamp());
	 *   System.out.println("UserName = " + data.getUserName());
	 *   System.out.println("Source IP = " + data.getSourceIp());
	 *   System.out.println("Type = " + data.getType());
	 *   System.out.println("Status = " + data.getStatus());
	 *   System.out.println("Browser = " + data.getBrowser());
	 *   System.out.println("Platform = " + data.getPlatform());
	 *   System.out.println("Vault ID = " + data.getVaultId());
	 * }
	 * </pre>
	 * @vapil.request <pre>
	 * <i>Example 4 - ObjectAuditResponse</i>
	 * ObjectAuditResponse resp = vaultClient.newRequest(LogRequest.class)
	 * 				.setStartDate(ZonedDateTime.now(ZoneId.of("UTC")).minusDays(10))
	 * 				.setEvents(new HashSet&lt;&gt;(Arrays.asList("Create", "Update")))
	 * 				.retrieveAuditDetails(LogRequest.AuditTrailType.OBJECT);</pre>
	 * @vapil.response <pre>
	 * <i>Example 4 - ObjectAuditResponse</i>
	 * AuditDetailsResponse.ResponseDetails details = resp.getResponseDetails();
	 * System.out.println("Offset = " + details.getOffset());
	 * System.out.println("Limit = " + details.getLimit());
	 * System.out.println("Size = " + details.getSize());
	 * System.out.println("Total = " + details.getTotal());
	 * System.out.println("Object/Name = " + details.getDetailsObject().getName());
	 * System.out.println("Object/Label = " + details.getDetailsObject().getLabel());
	 * System.out.println("Object/Url = " + details.getDetailsObject().getUrl());
	 *
	 * System.out.println("Items ****");
	 * for (ObjectAuditData data : resp.getData()) {
	 *   System.out.println("\n**** Data Item **** ");
	 *   System.out.println("id = " + data.getId());
	 *   System.out.println("timestamp = " + data.getTimestamp());
	 *   System.out.println("UserName = " + data.getUserName());
	 *   System.out.println("Full Name = " + data.getFullName());
	 *   System.out.println("Action = " + data.getAction());
	 *   System.out.println("Item = " + data.getItem());
	 *   System.out.println("Record ID = " + data.getRecordId());
	 *   System.out.println("Object Label = " + data.getObjectLabel());
	 *   System.out.println("Workflow Name = " + data.getWorkflowName());
	 *   System.out.println("Task Name = " + data.getTaskName());
	 *   System.out.println("Verdict = " + data.getVerdict());
	 *   System.out.println("Reason = " + data.getReason());
	 *   System.out.println("Capacity = " + data.getCapacity());
	 *   System.out.println("Event Description = " + data.getEventDescription());
	 * }
	 * </pre>
	 * @vapil.request <pre>
	 * <i>Example 5 - JobCreateResponse</i>
	 * JobCreateResponse resp = vaultClient.newRequest(LogRequest.class)
	 * 				.setAllDates(true)
	 * 				.setFormatResult(LogRequest.FormatResultType.CSV)
	 * 				.retrieveAuditDetails(LogRequest.AuditTrailType.DOMAIN);</pre>
	 * @vapil.response <pre>
	 * <i>Example 5 - JobCreateResponse</i>
	 * System.out.println("Response Status = " + resp.getResponseStatus());
	 * System.out.println("Job ID = " + resp.getJobId());
	 * System.out.println("Url = " + resp.getUrl());
	 * </pre>
	 */
	public <T> T retrieveAuditDetails(AuditTrailType auditTrailType) {
		String url = vaultClient.getAPIEndpoint(URL_AUDIT_DETAILS)
				.replace("{audit_trail_type}", auditTrailType.getValue());

		HttpRequestConnector request = new HttpRequestConnector(url);

		if (startDateTime != null) {
			request.addQueryParam(PARAM_START_DATE, getFormattedDate(startDateTime, DATE_TIME_PATTERN));
		}

		if (endDateTime != null) {
			request.addQueryParam(PARAM_END_DATE, getFormattedDate(endDateTime, DATE_TIME_PATTERN));
		}

		if (formatResult != null && !formatResult.isEmpty()) {
			request.addQueryParam(PARAM_FORMAT_RESULT, formatResult);
		}

		if (events != null && !events.isEmpty()) {
			String eventsList = events.stream().collect(Collectors.joining(","));
			request.addQueryParam(PARAM_EVENT, eventsList);
		}

		if (objects != null && !objects.isEmpty()) {
			String objectsList = objects.stream().collect(Collectors.joining(","));
			request.addQueryParam(PARAM_OBJECT, objectsList);
		}

		request.addQueryParam(PARAM_LIMIT, limit);

		//special case, if all dates, then return jobcreateresponse
		if (allDates != null && allDates) {
			request.addQueryParam(PARAM_ALL_DATES, allDates);
			return send(HttpMethod.GET, request, (Class<T>) JobCreateResponse.class);
		}

		//get the first page
		T response = send(HttpMethod.GET, request, auditTrailType.getResponseModel());

		return response;
	}

	/**
	 * <b>Retrieve Audit Details (By Page)</b>
	 * <p>
	 * Retrieve next or previous page of an existing audit details request
	 *
	 * @param <T>            Response Type class
	 * @param auditTrailType type of audit trail
	 * @param pageUrl        The URL from the previous_page or next_page parameter
	 * @return Returns one of the following:<br>
	 * SystemAuditResponse when auditTrailType is system_audit_trail<br>
	 * LoginAuditResponse when auditTrailType is login_audit_trail<br>
	 * ObjectAuditResponse when auditTrailType is object_audit_trail<br>
	 * DomainAuditResponse when auditTrailType is domain_audit_trail<br>
	 * DocumentAuditResponse when auditTrailType is document_audit_response
	 * @vapil.api <pre>
	 * GET /api/{version}/metadata/audittrail/{audit_trail_type}?offset={val}&amp;limit={val}&amp;uuid={val}</pre>
	 */
	public <T> T retrieveAuditDetailsByPage(AuditTrailType auditTrailType, String pageUrl) {
		String url = vaultClient.getPaginationEndpoint(pageUrl);
		HttpRequestConnector request = new HttpRequestConnector(url);
		return send(HttpMethod.GET, request, auditTrailType.getResponseModel());
	}

	/**
	 * <b>Retrieve Complete Audit History for a Single Document</b>
	 * <p>
	 * Retrieve complete audit history for a single document.
	 *
	 * @param docId The Document Id
	 * @return DocumentAuditResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/objects/documents/{doc_id}/audittrail</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/25.1/#retrieve-complete-audit-history-for-a-single-document' target='_blank'>https://developer.veevavault.com/api/25.1/#retrieve-complete-audit-history-for-a-single-document</a>
	 * @vapil.request <pre>
	 * DocumentAuditResponse resp = vaultClient.newRequest(LogRequest.class)
	 * 			.setLimit(4) // Just pull 4 records so the results can be viewed more easily
	 * 			.setFormatResult(LogRequest.FormatResultType.JSON)
	 * 		    .setEvents(new HashSet&lt;&gt;(Arrays.asList("GetDocumentVersion", "UploadDoc")))
	 * 			.retrieveCompleteAuditHistoryForASingleDocument(id);</pre>
	 * @vapil.response <pre>
	 * AuditDetailsResponse.ResponseDetails details = resp.getResponseDetails();
	 * System.out.println("Offset = " + details.getOffset());
	 * System.out.println("Limit = " + details.getLimit());
	 * System.out.println("Size = " + details.getSize());
	 * System.out.println("Total = " + details.getTotal());
	 * System.out.println("Object/Name = " + details.getDetailsObject().getName());
	 * System.out.println("Object/Label = " + details.getDetailsObject().getLabel());
	 * System.out.println("Object/Url = " + details.getDetailsObject().getUrl());
	 *
	 * System.out.println("Items ****");
	 * for (DocumentAuditData data : resp.getData()) {
	 * 	 System.out.println("\n**** Data Item **** ");
	 * 	 System.out.println("id = " + data.getId());
	 * 	 System.out.println("timestamp = " + data.getTimestamp());
	 * 	 System.out.println("UserName = " + data.getUserName());
	 * 	 System.out.println("Full Name = " + data.getFullName());
	 * 	 System.out.println("Action = " + data.getAction());
	 * 	 System.out.println("Item = " + data.getItem());
	 * 	 System.out.println("Field Name = " + data.getFieldName());
	 * 	 System.out.println("New Value = " + data.getNewValue());
	 * 	 System.out.println("Workflow Name = " + data.getWorkflowName());
	 * 	 System.out.println("Task Name = " + data.getTaskName());
	 * 	 System.out.println("Signature Meaning = " + data.getSignatureMeaning());
	 * 	 System.out.println("View License = " + data.getViewLicense());
	 * 	 System.out.println("Job Instance ID = " + data.getJobInstanceId());
	 * 	 System.out.println("Doc ID = " + data.getDocId());
	 * 	 System.out.println("Version = " + data.getVersion());
	 * 	 System.out.println("Document Url = " + data.getDocumentUrl());
	 * 	 System.out.println("Event Description = " + data.getEventDescription());
	 * }</pre>
	 */
	public DocumentAuditResponse retrieveCompleteAuditHistoryForASingleDocument(int docId) {
		String url = vaultClient.getAPIEndpoint(URL_AUDIT_DOCUMENT)
				.replace("{doc_id}", Integer.valueOf(docId).toString());

		HttpRequestConnector request = new HttpRequestConnector(url);

		if (startDateTime != null) {
			request.addQueryParam(PARAM_START_DATE, getFormattedDate(startDateTime, DATE_TIME_PATTERN));
		}

		if (endDateTime != null) {
			request.addQueryParam(PARAM_END_DATE, getFormattedDate(endDateTime, DATE_TIME_PATTERN));
		}

		if (formatResult != null && !formatResult.isEmpty()) {
			request.addQueryParam(PARAM_FORMAT_RESULT, formatResult);
		}

		if (events != null && !events.isEmpty()) {
			String eventsList = events.stream().collect(Collectors.joining(","));
			request.addQueryParam(PARAM_EVENT, eventsList);
		}

		request.addQueryParam(PARAM_LIMIT, limit);

		if (formatResult.equals(FormatResultType.CSV.getValue())) {
			return sendReturnBinary(HttpMethod.GET, request, DocumentAuditResponse.class);
		} else {
			return send(HttpMethod.GET, request, DocumentAuditResponse.class);
		}
	}

	/**
	 * <b>Retrieve Complete Audit History for a Single Object Record</b>
	 * <p>
	 * Retrieve Complete Audit History for a Single Object Record.
	 *
	 * @param objectName Object name
	 * @param recordId   Object record id
	 * @return ObjectAuditResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/vobjects/{object_name}/{object_record_id}/audittrail</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/25.1/#retrieve-complete-audit-history-for-a-single-object-record' target='_blank'>https://developer.veevavault.com/api/25.1/#retrieve-complete-audit-history-for-a-single-object-record</a>
	 * @vapil.request <pre>
	 * ObjectAuditResponse resp = vaultClient.newRequest(LogRequest.class)
	 * 				.setFormatResult(LogRequest.FormatResultType.JSON)
	 * 			    .setEvents(new HashSet&lt;&gt;(Arrays.asList("Create", "Edit")))
	 * 				.retrieveCompleteAuditHistoryForASingleObjectRecord("product__v", "00P000000000601");</pre>
	 * @vapil.response <pre>
	 * if (resp.isSuccessful()) {
	 *   AuditDetailsResponse.ResponseDetails details = resp.getResponseDetails();
	 *   System.out.println("Offset = " + details.getOffset());
	 *   System.out.println("Limit = " + details.getLimit());
	 *   System.out.println("Size = " + details.getSize());
	 *   System.out.println("Total = " + details.getTotal());
	 *   System.out.println("Object/Name = " + details.getDetailsObject().getName());
	 *   System.out.println("Object/Label = " + details.getDetailsObject().getLabel());
	 *   System.out.println("Object/Url = " + details.getDetailsObject().getUrl());
	 *
	 *   System.out.println("Items ****");
	 *   for (ObjectAuditData data : resp.getData()) {
	 *     System.out.println("\n**** Data Item **** ");
	 *     System.out.println("id = " + data.getId());
	 *     System.out.println("timestamp = " + data.getTimestamp());
	 *     System.out.println("UserName = " + data.getUserName());
	 *     System.out.println("Full Name = " + data.getFullName());
	 *     System.out.println("Action = " + data.getAction());
	 *     System.out.println("Item = " + data.getItem());
	 *     System.out.println("Record ID = " + data.getRecordId());
	 *     System.out.println("Object Label = " + data.getObjectLabel());
	 *     System.out.println("Workflow Name = " + data.getWorkflowName());
	 *     System.out.println("Task Name = " + data.getTaskName());
	 *     System.out.println("Verdict = " + data.getVerdict());
	 *     System.out.println("Reason = " + data.getReason());
	 *     System.out.println("Capacity = " + data.getCapacity());
	 *     System.out.println("Event Description = " + data.getEventDescription());
	 *   }
	 * }</pre>
	 */
	public ObjectAuditResponse retrieveCompleteAuditHistoryForASingleObjectRecord(String objectName, String recordId) {
		String url = vaultClient.getAPIEndpoint(URL_AUDIT_OBJECT)
				.replace("{object_name}", objectName)
				.replace("{object_record_id}", recordId);

		HttpRequestConnector request = new HttpRequestConnector(url);

		if (startDateTime != null) {
			request.addQueryParam(PARAM_START_DATE, getFormattedDate(startDateTime, DATE_TIME_PATTERN));
		}

		if (endDateTime != null) {
			request.addQueryParam(PARAM_END_DATE, getFormattedDate(endDateTime, DATE_TIME_PATTERN));
		}

		if (formatResult != null && !formatResult.isEmpty()) {
			request.addQueryParam(PARAM_FORMAT_RESULT, formatResult);
		}

		if (events != null && !events.isEmpty()) {
			String eventsList = events.stream().collect(Collectors.joining(","));
			request.addQueryParam(PARAM_EVENT, eventsList);
		}

		request.addQueryParam(PARAM_LIMIT, limit);

		if (formatResult.equals(FormatResultType.CSV.getValue())) {
			return sendReturnBinary(HttpMethod.GET, request, ObjectAuditResponse.class);
		} else {
			return send(HttpMethod.GET, request, ObjectAuditResponse.class);
		}
	}

	/**
	 * <b>Retrieve Email Notification History</b>
	 * <p>
	 * Retrieve details about the email notifications sent by Vault. Details include the notification date, recipient, subject, and delivery status.
	 * <p>
	 * @param <T>            Response Type class
	 * @return Returns one of the following:<br>
	 * EmailNotificationHistoryResponse
	 * JobCreateResponse when format result is CSV
	 * @vapil.api <pre>
	 * GET /api/{version}/notifications/histories</pre>
	 * @vapil.vaultlink <a href='https://https://developer.veevavault.com/api/25.1/#retrieve-email-notification-histories' target='_blank'>https://developer.veevavault.com/api/25.1/#retrieve-email-notification-histories</a>
	 * @vapil.request <pre>
	 * <i>Example 1 - EmailNotificationHistoryResponse</i>
	 * EmailNotificationHistoryResponse response = vaultClient.newRequest(LogRequest.class)
	 * 					.retrieveEmailNotificationHistory();</pre>
	 * @vapil.response <pre>
	 * <i>Example 1 - EmailNotificationHistoryResponse</i>
	 * System.out.println("Response Status: " + response.getResponseStatus());
	 * System.out.println("Response Message: " + response.getResponse());
	 *
	 * EmailNotificationHistoryResponse.ResponseDetails details = response.getResponseDetails();
	 * System.out.println("Response Details ****");
	 * System.out.println("Offset = " + details.getOffset());
	 * System.out.println("Limit = " + details.getLimit());
	 * System.out.println("Size = " + details.getSize());
	 * System.out.println("Total = " + details.getTotal());
	 *
	 * System.out.println("Items ****");
	 * for (EmailNotification data : response.getData()) {
	 * 	System.out.println("id = " + data.getNotificationId());
	 * 	System.out.println("Send Date = " + data.getSendDate());
	 * 	System.out.println("Recipient Email: " + data.getRecipientEmail());
	 * }
	 * </pre>
	 * @vapil.request <pre>
	 * <i>Example 2 - EmailNotificationHistoryResponse</i>
	 * 	EmailNotificationHistoryResponse response = vaultClient.newRequest(LogRequest.class)
	 * 			.setStartDate(ZonedDateTime.now(ZoneId.of("UTC")).minusDays(29))
	 * 			.setEndDate(ZonedDateTime.now(ZoneId.of("UTC")).minusDays(1))
	 * 			.retrieveEmailNotificationHistory();</pre>
	 * @vapil.response <pre>
	 * <i>Example 2 - EmailNotificationHistoryResponse</i>
	 * System.out.println("Response Status: " + response.getResponseStatus());
	 * System.out.println("Response Message: " + response.getResponse());
	 *
	 * EmailNotificationHistoryResponse.ResponseDetails details = response.getResponseDetails();
	 * System.out.println("Response Details ****");
	 * System.out.println("Offset = " + details.getOffset());
	 * System.out.println("Limit = " + details.getLimit());
	 * System.out.println("Size = " + details.getSize());
	 * System.out.println("Total = " + details.getTotal());
	 *
	 * System.out.println("Items ****");
	 * for (EmailNotification data : response.getData()) {
	 * 	System.out.println("id = " + data.getNotificationId());
	 * 	System.out.println("Send Date = " + data.getSendDate());
	 * 	System.out.println("Recipient Email: " + data.getRecipientEmail());
	 * }
	 * </pre>
	 * @vapil.request <pre>
	 * <i>Example 3 - JobCreateResponse</i>
	 * JobCreateResponse response = vaultClient.newRequest(LogRequest.class)
	 * 				.setAllDates(true)
	 * 				.setFormatResult(LogRequest.FormatResultType.CSV)
	 * 				.retrieveEmailNotificationHistory();</pre>
	 * @vapil.response <pre>
	 * <i>Example 3 - JobCreateResponse</i>
	 * System.out.println("Response Status: " + response.getResponseStatus());
	 * System.out.println("Response Message: " + response.getResponse());
	 * System.out.println("Job ID: " + response.getJobId());
	 * System.out.println("Url = " + resp.getUrl());
	 * </pre>
	 */
	public <T> T retrieveEmailNotificationHistory() {
		String url = vaultClient.getAPIEndpoint(URL_EMAIL_NOTIFICATION_HISTORY);

		HttpRequestConnector request = new HttpRequestConnector(url);

//		API accepts two Date Formats, "yyyy-MM-dd'T'HH:mm:ss'Z'" or "yyyy-MM-dd"
		if (startDateTime != null) {
			request.addQueryParam(PARAM_START_DATE, getFormattedDate(startDateTime, DATE_TIME_PATTERN));
		} else if(startDate != null) {
			request.addQueryParam(PARAM_START_DATE, getFormattedDate(startDate, DATE_PATTERN));
		}

		if (endDateTime != null) {
			request.addQueryParam(PARAM_END_DATE, getFormattedDate(endDateTime, DATE_TIME_PATTERN));
		} else if(endDate != null) {
			request.addQueryParam(PARAM_END_DATE, getFormattedDate(endDate, DATE_PATTERN));
		}

		if (formatResult != null && !formatResult.isEmpty()) {
			request.addQueryParam(PARAM_FORMAT_RESULT, formatResult);
		}

		request.addQueryParam(PARAM_LIMIT, limit);

		//special case, if all dates, then return jobcreateresponse
		if (allDates != null && allDates) {
			request.addQueryParam(PARAM_ALL_DATES, allDates);
			return send(HttpMethod.GET, request, (Class<T>) JobCreateResponse.class);
		}

		//get the first page
		T response = (T) send(HttpMethod.GET, request, EmailNotificationHistoryResponse.class);

		return response;
	}

	/**
	 * <b>Retrieve Daily API Usage</b>
	 * <p>
	 * Retrieve the API Usage Log for a single day, up to 30 days in the past.
	 * The log contains information such as user name, user ID, remaining burst
	 * and daily limits, and the endpoint called. Users with the Admin &gt; Logs &gt;
	 * API Usage Logs permission can access these logs.
	 * <p>
	 * Note that if outputFilePath is set, this method will save the output to the path specified.
	 * If outputFilePath is null or empty, the output will be stored as a byte array in the VaultResponse object.
	 * If outputFilePath is set, it must include the full file path and a file name with a .zip extension
	 *
	 * @param logDate Daily log date
	 * @return VaultResponse On SUCCESS, Vault retrieves the log from the specified date as a .ZIP file.
	 * @vapil.api <pre>
	 * GET /api/{version}/logs/api_usage?date=YYYY-MM-DD</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/25.1/#retrieve-daily-api-usage' target='_blank'>https://developer.veevavault.com/api/25.1/#retrieve-daily-api-usage</a>
	 * @vapil.request <pre>
	 * <i>Example 1 - To file</i>
	 * VaultResponse response = vaultClient.newRequest(LogRequest.class)
	 * 				.setLogDate(date)
	 * 				.setOutputPath(outputFilePath.toString())
	 * 				.retrieveDailyAPIUsage();</pre>
	 * @vapil.request <pre>
	 * <i>Example 2 - To buffer</i>
	 * VaultResponse response = vaultClient.newRequest(LogRequest.class)
	 * 				.setLogDate(date)
	 * 				.setLogFormat(LogRequest.LogFormatType.LOGFILE)
	 * 				.setOutputPath(null)
	 * 				.retrieveDailyAPIUsage();</pre>
	 * @vapil.response <pre>
	 * <i>Example 2 - To buffer</i>
	 * if (response.getResponseStatus().equals(VaultResponse.HTTP_RESPONSE_SUCCESS)) {
	 *   try (OutputStream os = new FileOutputStream(outputFilePath.toString())) {
	 *     os.write(response.getBinaryContent());
	 *     System.out.println("File was saved to: " + outputFilePath.toString());
	 *     }
	 *   catch (IOException ignored){}
	 * }</pre>
	 */


	public VaultResponse retrieveDailyAPIUsage(LocalDate logDate) {
		String url = vaultClient.getAPIEndpoint(URL_API_USAGE);

		HttpRequestConnector request = new HttpRequestConnector(url);

		request.addQueryParam(PARAM_DATE, getFormattedDate(logDate, DATE_PATTERN));

		if (logFormat != null) {
			request.addQueryParam(PARAM_LOG_FORMAT, logFormat);
		}

		if (outputPath != null && !outputPath.isEmpty()) {
			return sendToFile(HttpMethod.GET, request, outputPath, VaultResponse.class);
		} else {
			return sendReturnBinary(HttpMethod.GET, request, VaultResponse.class);
		}
	}

	/**
	 * <b>Download SDK Runtime Log</b>
	 * <p>
	 * Retrieve the SDK Runtime Log for a single day, up to 30 days in the past.
	 * The log contains information such as the timestamp, user ID, execution ID,
	 * class name, depth, log level, and any messages returned. Users with the Admin &gt; Logs &gt;
	 * SDK Runtime Logs permission can access these logs.
	 * <p>
	 * Note that if outputFilePath is set, this method will save the output to the path specified.
	 * If outputFilePath is null or empty, the output will be stored as a byte array in the VaultResponse object.
	 * If outputFilePath is set, it must include the full file path and a file name with a .zip extension
	 *
	 * @param logDate Daily log date
	 * @return VaultResponse On SUCCESS, Vault retrieves the log from the specified date as a .ZIP file.
	 * @vapil.api <pre>
	 * GET /api/{version}/logs/code/runtime?date=YYYY-MM-DD</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/25.1/#download-sdk-runtime-log' target='_blank'>https://developer.veevavault.com/api/25.1/#download-sdk-runtime-log</a>
	 * @vapil.request <pre>
	 * <i>Example 1 - To file</i>
	 * VaultResponse response = vaultClient.newRequest(LogRequest.class)
	 * 				.setOutputPath(outputFilePath.toString())
	 * 				.downloadSdkRuntimeLog(date);</pre>
	 * @vapil.request <pre>
	 * <i>Example 2 - To buffer</i>
	 * VaultResponse response = vaultClient.newRequest(LogRequest.class)
	 * 				.setLogFormat(LogRequest.LogFormatType.LOGFILE)
	 * 				.downloadSdkRuntimeLog(date);</pre>
	 * @vapil.response <pre>
	 * <i>Example 2 - To buffer</i>
	 * if (response.getResponseStatus().equals(VaultResponse.HTTP_RESPONSE_SUCCESS)) {
	 *   try (OutputStream os = new FileOutputStream(outputFilePath.toString())) {
	 *     os.write(response.getBinaryContent());
	 *     System.out.println("File was saved to: " + outputFilePath.toString());
	 *     }
	 *   catch (IOException ignored){}
	 * }</pre>
	 */
	public VaultResponse downloadSdkRuntimeLog(LocalDate logDate) {
		String url = vaultClient.getAPIEndpoint(URL_CODE_LOG);

		HttpRequestConnector request = new HttpRequestConnector(url);

		request.addQueryParam(PARAM_DATE, getFormattedDate(logDate, DATE_PATTERN));

		if (logFormat != null) {
			request.addQueryParam(PARAM_LOG_FORMAT, logFormat);
		}

		if (outputPath != null && !outputPath.isEmpty()) {
			return sendToFile(HttpMethod.GET, request, outputPath, VaultResponse.class);
		} else {
			return sendReturnBinary(HttpMethod.GET, request, VaultResponse.class);
		}
	}

	/**
	 * <b>Retrieve All Profiling Sessions</b>
	 * <p>
	 * List all SDK request profiling sessions in the currently authenticated Vault.
	 *
	 * @return SdkProfilingSessionBulkResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/code/profiler</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/25.1/#retrieve-profiling-session' target='_blank'>https://developer.veevavault.com/api/25.1/#retrieve-profiling-session</a>
	 * @vapil.request <pre>
	 * SdkProfilingSessionBulkResponse response = vaultClient.newRequest(LogRequest.class)
	 * 		.retrieveAllProfilingSessions();
	 * </pre>
	 * @vapil.response <pre>
	 * for (SdkProfilingSession session : response.getData()) {
	 * 		System.out.println(("--------Profiling Session--------"));
	 * 		System.out.println("ID = " + session.getId());
	 * 		System.out.println("Name = " + session.getName());
	 * 		System.out.println("Label = " + session.getLabel());
	 * 		System.out.println("Status = " + session.getStatus());
	 * }
	 * </pre>
	 */
	public SdkProfilingSessionBulkResponse retrieveAllProfilingSessions() {
		HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_PROFILING_SESSIONS));

		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);

		return send(HttpMethod.GET, request, SdkProfilingSessionBulkResponse.class);
	}

	/**
	 * <b>Retrieve Profiling Session</b>
	 * <p>
	 * Retrieve details about a specific SDK request profiling session.
	 *
	 * @param sessionName SDK Profiling Session name
	 * @return SdkProfilingSessionResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/code/profiler/{session_name}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/25.1/#retrieve-profiling-session' target='_blank'>https://developer.veevavault.com/api/25.1/#retrieve-profiling-session</a>
	 * @vapil.request <pre>
	 * SdkProfilingSessionResponse response = vaultClient.newRequest(LogRequest.class)
	 * 		.retrieveProfilingSession(PROFILING_SESSION_NAME);
	 * </pre>
	 * @vapil.response <pre>
	 * System.out.println("ID = " + response.getData().getId());
	 * System.out.println("Name = " + response.getData().getName());
	 * System.out.println("Label = " + response.getData().getLabel());
	 * System.out.println("Status = " + response.getData().getStatus());
	 * </pre>
	 */
	public SdkProfilingSessionResponse retrieveProfilingSession(String sessionName) {
		HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_PROFILING_SESSION)
						.replace("{session_name}", sessionName));

		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);

		return send(HttpMethod.GET, request, SdkProfilingSessionResponse.class);
	}

	/**
	 * <b>Create Profiling Session</b>
	 * <p>
	 * Create a new SDK request profiling session.
	 *
	 * @param label SDK Profiling Session label
	 * @return SdkProfilingSessionCreateResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/code/profiler</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/25.1/#create-profiling-session' target='_blank'>https://developer.veevavault.com/api/25.1/#create-profiling-session</a>
	 * @vapil.request <pre>
	 * SdkProfilingSessionCreateResponse response = vaultClient.newRequest(LogRequest.class)
	 * 		.setUserId(USER_ID)
	 * 		.setDescription("Vapil Test Description")
	 * 		.createProfilingSession("VAPIL Test Profiling Session");
	 * </pre>
	 * @vapil.response <pre>
	 * System.out.println("ID = " + response.getData().getId());
	 * System.out.println("Name = " + response.getData().getName());
	 * </pre>
	 */
	public SdkProfilingSessionCreateResponse createProfilingSession(String label) {
		HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_PROFILING_SESSIONS));
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_XFORM);

		request.addBodyParam(PARAM_LABEL, label);

		if (userId != null) {
			request.addBodyParam(PARAM_USER_ID, userId);
		}

		if (description != null) {
			request.addBodyParam(PARAM_DESCRIPTION, description);
		}

		return send(HttpMethod.POST, request, SdkProfilingSessionCreateResponse.class);
	}

	/**
	 * <b>End Profiling Session</b>
	 * <p>
	 * Terminate a profiling session early. By default, profiling sessions run for either 20 minutes or up to 10,000 SDK requests, whichever comes first.
	 *
	 * @param sessionName SDK Profiling Session name
	 * @return VaultResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/code/profiler/{session_name}/actions/end</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/25.1/#end-profiling-session' target='_blank'>https://developer.veevavault.com/api/25.1/#end-profiling-session</a>
	 * @vapil.request <pre>
	 * VaultResponse response = vaultClient.newRequest(LogRequest.class)
	 * 		.endProfilingSession(sessionName);
	 * </pre>
	 * @vapil.response <pre>
	 * System.out.println("Response Status = " + response.getResponseStatus());
	 * </pre>
	 */
	public VaultResponse endProfilingSession(String sessionName) {
		HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_PROFILING_SESSION_END)
				.replace("{session_name}", sessionName));
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);

		return send(HttpMethod.POST, request, VaultResponse.class);
	}

	/**
	 * <b>Delete Profiling Session</b>
	 * <p>
	 * Delete an inactive profiling session, which deletes the session and all data associated with the session. Inactive sessions have a status of complete__sys.
	 *
	 * @param sessionName SDK Profiling Session name
	 * @return VaultResponse
	 * @vapil.api <pre>
	 * DELETE /api/{version}/code/profiler/{session_name}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/25.1/#delete-profiling-session' target='_blank'>https://developer.veevavault.com/api/25.1/#delete-profiling-session</a>
	 * @vapil.request <pre>
	 * VaultResponse response = vaultClient.newRequest(LogRequest.class)
	 * 		.deleteProfilingSession(sessionName);
	 * </pre>
	 * @vapil.response <pre>
	 * System.out.println("Response Status = " + response.getResponseStatus());
	 * </pre>
	 */
	public VaultResponse deleteProfilingSession(String sessionName) {
		HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_PROFILING_SESSION)
				.replace("{session_name}", sessionName));
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);

		return send(HttpMethod.DELETE, request, VaultResponse.class);
	}

	/**
	 * <b>Download Profiling Session Results</b>
	 * <p>
	 * Download the Profiler Log for a specific profiling session. A profiling session log is a CSV which contains one row for each SDK request, with a maximum of 100,000 rows.
	 *
	 * @param sessionName SDK Profiling Session name
	 * @return VaultResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/code/profiler/{session_name}/results</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/25.1/#download-profiling-session-results' target='_blank'>https://developer.veevavault.com/api/25.1/#download-profiling-session-results</a>
	 * @vapil.request <pre>
	 * <i>Example 1 - Download to file</i>
	 * VaultResponse response = vaultClient.newRequest(LogRequest.class)
	 * 		.setOutputPath(outputPath.toString())
	 * 		.downloadProfilingSessionResults(PROFILING_SESSION_NAME);
	 * <br>
	 * <i>Example 2 - Download to Bytes</i>
	 * VaultResponse response = vaultClient.newRequest(LogRequest.class)
	 * 		.downloadProfilingSessionResults(PROFILING_SESSION_NAME);
	 *
	 * byte[] bytes = response.getBinaryContent();
	 * </pre>
	 * @vapil.response <pre>
	 * System.out.println("Response Status = " + response.getResponseStatus());
	 * </pre>
	 */
	public VaultResponse downloadProfilingSessionResults(String sessionName) {
		HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_PROFILING_SESSION_RESULTS)
				.replace("{session_name}", sessionName));

		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);

		if (outputPath != null && !outputPath.isEmpty()) {
			return sendToFile(HttpMethod.GET, request, outputPath, VaultResponse.class);
		} else {
			return sendReturnBinary(HttpMethod.GET, request, VaultResponse.class);
		}
	}

	/**
	 * <b>Retrieve All Debug Logs</b>
	 * <p>
	 * Retrieve all debug log sessions in the authenticated Vault.
	 *
	 * @return SdkDebugSessionBulkResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/logs/code/debug</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/25.1/#retrieve-all-debug-logs' target='_blank'>https://developer.veevavault.com/api/25.1/#retrieve-all-debug-logs</a>
	 * @vapil.request <pre>
	 * SdkDebugSessionBulkResponse response = vaultClient.newRequest(LogRequest.class)
	 * 		.setUserId(USER_ID)
	 * 		.setIncludeInactive(true)
	 * 		.retrieveAllDebugLogs();
	 * </pre>
	 * @vapil.response <pre>
	 * for (SdkDebugSession session : response.getData()) {
	 * 		System.out.println(("--------Debug Session--------"));
	 * 		System.out.println("ID = " + session.getId());
	 * 		System.out.println("Name = " + session.getName());
	 * 		System.out.println("Status = " + session.getStatus());
	 * }
	 * </pre>
	 */
	public SdkDebugSessionBulkResponse retrieveAllDebugLogs() {
		HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_DEBUG_SESSIONS));

		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);

		if (userId != null) {
			request.addQueryParam(PARAM_USER_ID, userId);
		}

		if (includeInactive) {
			request.addQueryParam(PARAM_INCLUDE_INACTIVE, includeInactive);
		}

		return send(HttpMethod.GET, request, SdkDebugSessionBulkResponse.class);
	}

	/**
	 * <b>Retrieve Single Debug Log</b>
	 * <p>
	 * Given a debug log ID, retrieve details about this debug log.
	 *
	 * @param id SDK Debug Log ID
	 * @return SdkDebugSessionResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/logs/code/debug/{id}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/25.1/#retrieve-single-debug-log' target='_blank'>https://developer.veevavault.com/api/25.1/#retrieve-single-debug-log</a>
	 * @vapil.request <pre>
	 * SdkDebugSessionResponse response = vaultClient.newRequest(LogRequest.class)
	 * 		.retrieveSingleDebugLog(debugLogId);
	 * </pre>
	 * @vapil.response <pre>
	 * SdkDebugSession session = response.getData();
	 * System.out.println("ID = " + session.getId());
	 * System.out.println("Name = " + session.getName());
	 * System.out.println("Status = " + session.getStatus());
	 * </pre>
	 */
	public SdkDebugSessionResponse retrieveSingleDebugLog(String id) {
		HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_DEBUG_SESSION)
				.replace("{id}", id));

		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);

		return send(HttpMethod.GET, request, SdkDebugSessionResponse.class);
	}

	/**
	 * <b>Download Debug Log Files</b>
	 * <p>
	 * Given a debug log ID, download all of this debug log’s files.
	 *
	 * @param id SDK Debug Log ID
	 * @return VaultResponse
	 * @vapil.api <pre>
	 * GET /api/{version}/logs/code/debug/{id}/files</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/25.1/#download-debug-log-files' target='_blank'>https://developer.veevavault.com/api/25.1/#download-debug-log-files</a>
	 * @vapil.request <pre>
	 * <i>Example 1 - Download to file</i>
	 * VaultResponse response = vaultClient.newRequest(LogRequest.class)
	 * 		.setOutputPath(outputPath.toString())
	 * 		.downloadDebugLogFiles(debugSessionId);
	 * <br>
	 * <i>Example 2 - Download to Bytes</i>
	 * VaultResponse response = vaultClient.newRequest(LogRequest.class)
	 * 		.downloadDebugLogFiles(debugSessionId);
	 *
	 * byte[] bytes = response.getBinaryContent();
	 * </pre>
	 * @vapil.response <pre>
	 * System.out.println("Response Status = " + response.getResponseStatus());
	 * </pre>
	 */
	public VaultResponse downloadDebugLogFiles(String id) {
		HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_DEBUG_FILES)
				.replace("{id}", id));

		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);

		if (outputPath != null && !outputPath.isEmpty()) {
			return sendToFile(HttpMethod.GET, request, outputPath, VaultResponse.class);
		} else {
			return sendReturnBinary(HttpMethod.GET, request, VaultResponse.class);
		}
	}

	/**
	 * <b>Create Debug Log</b>
	 * <p>
	 * Create a new debug log session for a user.
	 *
	 * @param name The UI-friendly name for this debug log
	 * @param userId The user who will trigger entries into this debug log
	 * @return SdkDebugSessionCreateResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/logs/code/debug</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/25.1/#create-debug-log' target='_blank'>https://developer.veevavault.com/api/25.1/#create-debug-log</a>
	 * @vapil.request <pre>
	 * SdkDebugSessionCreateResponse createDebugLogResponse = vaultClient.newRequest(LogRequest.class)
	 * 		.setLogLevel("info__sys")
	 * 		.setClassFilters(classFilters)
	 * 		.createDebugLog("VAPIL Test Debug", USER_ID);
	 * </pre>
	 * @vapil.response <pre>
	 * System.out.println("ID = " + createDebugLogResponse.getData().getId());
	 * </pre>
	 */
	public SdkDebugSessionCreateResponse createDebugLog(String name, String userId) {
		HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_DEBUG_SESSIONS));
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_CONTENT_TYPE, HttpRequestConnector.HTTP_CONTENT_TYPE_MULTIPART_FORM);

		request.addBodyParam(PARAM_NAME, name);
		request.addBodyParam(PARAM_USER_ID, userId);

		if (logLevel != null) {
			request.addBodyParam(PARAM_LOG_LEVEL, logLevel);
		}

		if (classFilters != null && !classFilters.isEmpty()) {
			String classFiltersList = classFilters.stream().collect(Collectors.joining(","));
			request.addQueryParam(PARAM_CLASS_FILTERS, classFiltersList);
		}

		return send(HttpMethod.POST, request, SdkDebugSessionCreateResponse.class);
	}

	/**
	 * <b>Reset Debug Log</b>
	 * <p>
	 * Given a debug log ID, delete all existing log files and reset the expiration date to 30 days from today.
	 *
	 * @param id SDK Debug Log ID
	 * @return VaultResponse
	 * @vapil.api <pre>
	 * POST /api/{version}/logs/code/debug/{id}/actions/reset</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/25.1/#reset-debug-log' target='_blank'>https://developer.veevavault.com/api/25.1/#reset-debug-log</a>
	 * @vapil.request <pre>
	 * VaultResponse response = vaultClient.newRequest(LogRequest.class)
	 * 		.resetDebugLog(debugSessionId);
	 * </pre>
	 * @vapil.response <pre>
	 * System.out.println("Response Status = " + response.getResponseStatus());
	 * </pre>
	 */
	public VaultResponse resetDebugLog(String id) {
		HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_DEBUG_RESET)
				.replace("{id}", id));
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);

		return send(HttpMethod.POST, request, VaultResponse.class);
	}

	/**
	 * <b>Delete Debug Log</b>
	 * <p>
	 * Given a debug log ID, delete this debug log and all log files.
	 *
	 * @param id SDK Debug Log ID
	 * @return VaultResponse
	 * @vapil.api <pre>
	 * DELETE /api/{version}/logs/code/debug/{id}</pre>
	 * @vapil.vaultlink <a href='https://developer.veevavault.com/api/25.1/#delete-debug-log' target='_blank'>https://developer.veevavault.com/api/25.1/#delete-debug-log</a>
	 * @vapil.request <pre>
	 * VaultResponse response = vaultClient.newRequest(LogRequest.class)
	 * 		.deleteDebugLog(debugSessionId);
	 * </pre>
	 * @vapil.response <pre>
	 * System.out.println("Response Status = " + response.getResponseStatus());
	 * </pre>
	 */
	public VaultResponse deleteDebugLog(String id) {
		HttpRequestConnector request = new HttpRequestConnector(vaultClient.getAPIEndpoint(URL_DEBUG_SESSION)
				.replace("{id}", id));
		request.addHeaderParam(HttpRequestConnector.HTTP_HEADER_ACCEPT, HttpRequestConnector.HTTP_CONTENT_TYPE_JSON);

		return send(HttpMethod.DELETE, request, VaultResponse.class);
	}

	/**
	 * Converts the date to the proper string format expected by the API
	 *
	 * @param date        The date to convert
	 * @param datePattern DateTimeFormatter pattern
	 * @return Formatted date as string
	 */
	private String getFormattedDate(ZonedDateTime date, String datePattern) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern);
		return date.format(formatter);
	}

	/**
	 * Converts the date to the proper string format expected by the API
	 *
	 * @param date        The date to convert
	 * @param datePattern DateTimeFormatter pattern
	 * @return Formatted date as string
	 */
	private String getFormattedDate(LocalDate date, String datePattern) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern);
		return date.format(formatter);
	}

	/**
	 * Enum for the various audit trail formats
	 */
	public enum AuditTrailType {
		DOCUMENT("document_audit_trail"),
		DOMAIN("domain_audit_trail"),
		LOGIN("login_audit_trail"),
		OBJECT("object_audit_trail"),
		SYSTEM("system_audit_trail");

		private String type;

		AuditTrailType(String type) {
			this.type = type;
		}

		public String getValue() {
			return type;
		}

		private <T> T getResponseModel() {
			switch (this) {
				case DOCUMENT:
					return (T) DocumentAuditResponse.class;
				case DOMAIN:
					return (T) DomainAuditResponse.class;
				case LOGIN:
					return (T) LoginAuditResponse.class;
				case OBJECT:
					return (T) ObjectAuditResponse.class;
				case SYSTEM:
					return (T) SystemAuditResponse.class;
			}
			return null;
		}
	}

	/**
	 * JSON or CSV Result Types
	 */
	public enum FormatResultType {
		JSON("json"),
		CSV("csv");

		private String format;

		FormatResultType(String format) {
			this.format = format;
		}

		public String getValue() {
			return format;
		}
	}

	/**
	 * log_format for the api_usage request
	 */
	public enum LogFormatType {
		CSV("csv"),
		LOGFILE("logfile");

		private String format;

		LogFormatType(String format) {
			this.format = format;
		}

		public String getValue() {
			return format;
		}
	}

	/*
	 *
	 * Request parameter setters
	 *
	 */

	/**
	 * <p>
	 * Specify a start date and time to retrieve audit information or email notification history
	 * <p>
	 * For audit information, this date cannot be more than 30 days ago. If omitted, defaults to the last 30 days.
	 * <p>
	 * For email notification history, this date cannot be more than 2 years ago. If omitted, defaults to the start of the day.
	 *
	 * @param startDate Start date and time for audit information or email notification history log requests.
	 * @return The Request
	 */
	public LogRequest setStartDateTime(ZonedDateTime startDate) {
		this.startDateTime = startDate;
		return this;
	}

	/**
	 * <p>
	 * Specify an end date and time to retrieve audit information or email notification history
	 * <p>
	 * For audit information, this date cannot be more than 30 days ago. If omitted, defaults to the last 30 days.
	 * <p>
	 * For email notification history, this date cannot be more than 30 days away from the specified start date. If an end date has been specified, a start date must also be set.
	 *
	 * @param endDate End date and time for audit information or email notification history log requests.
	 * @return The Request
	 */
	public LogRequest setEndDateTime(ZonedDateTime endDate) {
		this.endDateTime = endDate;
		return this;
	}

	/**
	 * Specify a start date to retrieve email notification history.
	 *
	 * @param startDate This date cannot be more than 2 years ago. If omitted, defaults to the start of the day.
	 * @return The Request
	 */
	public LogRequest setStartDate(LocalDate startDate) {
		this.startDate = startDate;
		return this;
	}

	/**
	 * Specify an end date to retrieve email notification history.
	 *
	 * @param endDate This date cannot be more than 30 days away from the specified start date. If an end date has been specified, a start date must also be set.
	 * @return The Request
	 */
	public LogRequest setEndDate(LocalDate endDate) {
		this.endDate = endDate;
		return this;
	}

	/**
	 * Used to request audit information for all dates
	 *
	 * @param allDates Defaults to false. Set to true to request audit information for all dates. You must leave startDate and endDate blank when requesting an export of a full audit trail.
	 * @return The Request
	 */
	public LogRequest setAllDates(Boolean allDates) {
		this.allDates = allDates;
		return this;
	}

	/**
	 * The format of the returned results (JSON or CSV)
	 *
	 * @param formatResult To request a downloadable CSV file of your audit details, use FormatResultType.CSV. The response contains a jobId to retrieve the job status, which contains a link to download the CSV file. If omitted, the API returns a JSON response and does not start a job. If allDates is true, this parameter is required.
	 * @return The Request
	 */
	public LogRequest setFormatResult(FormatResultType formatResult) {
		this.formatResult = formatResult.getValue();
		return this;
	}

	/**
	 * The maximum number of results to return
	 *
	 * @param limit Limits the number of results (default is 200).  For larger sets, consider using CSV.
	 * @return The Request
	 */
	public LogRequest setLimit(Integer limit) {
		this.limit = limit;
		return this;
	}

	/**
	 * Specify the format to download
	 *
	 * @param logFormat Possible values are csv or logfile. If omitted, defaults to csv. Note that this call always downloads a ZIP file. This parameter only changes the format of the file contained within the ZIP.
	 * @return The Request
	 */
	public LogRequest setLogFormat(LogFormatType logFormat) {
		this.logFormat = logFormat.getValue();
		return this;
	}

	/**
	 * Specify source data in an output file
	 *
	 * @param outputPath Absolute path to the file for the response
	 * @return The Request
	 */
	public LogRequest setOutputPath(String outputPath) {
		this.outputPath = outputPath;
		return this;
	}

	/**
	 * Specify audit events when retrieving object or documents audit trails
	 *
	 * @param events Comma-separated list of one or more audit events
	 * @return The Request
	 */
	public LogRequest setEvents(Set<String> events) {
		this.events = events;
		return this;
	}

	/**
	 * Specify object names when retrieving object audit trails
	 *
	 * @param objects Comma-separated list of one or more objects
	 * @return The Request
	 */
	public LogRequest setObjects(Set<String> objects) {
		this.objects = objects;
		return this;
	}

	/**
	 * Specify the user ID for SDK Profiling and Debug requests
	 *
	 * @param userId User ID
	 * @return The Request
	 */
	public LogRequest setUserId(String userId) {
		this.userId = userId;
		return this;
	}

	/**
	 * Specify an Admin-facing description of the session
	 *
	 * @param description description
	 * @return The Request
	 */
	public LogRequest setDescription(String description) {
		this.description = description;
		return this;
	}

	/**
	 * Specify whether to include inactive sessions in SDK Debug requests
	 *
	 * @param includeInactive Include inactive sessions in the response. Defaults to false.
	 * @return The Request
	 */
	public LogRequest setIncludeInactive(boolean includeInactive) {
		this.includeInactive = includeInactive;
		return this;
	}

	/**
	 * Specify log level for SDK Debug Log session.
	 *
	 * @param logLevel The level of error messages to capture in this log
	 * @return The Request
	 */
	public LogRequest setLogLevel(String logLevel) {
		this.logLevel = logLevel;
		return this;
	}

	/**
	 * Specify class filters when creating SDK debug log sessions
	 *
	 * @param classFilters Set of fully-qualified class names
	 * @return The Request
	 */
	public LogRequest setClassFilters(Set<String> classFilters) {
		this.classFilters = classFilters;
		return this;
	}
}
