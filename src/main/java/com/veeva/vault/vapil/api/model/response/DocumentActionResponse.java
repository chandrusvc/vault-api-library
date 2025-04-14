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
import com.veeva.vault.vapil.api.model.response.VaultResponse;

import java.util.List;

/**
 * Response model for the following API calls:
 * <p>
 * GET /api/{version}/objects/documents/{id}/versions/{major_version}/{minor_version}/lifecycle_actions
 * PUT /api/{version}/objects/{documents_or_binders}/{id}/versions/{major_version}/{minor_version}/lifecycle_actions/{name__v}
 * POST /api/{version}/objects/{documents_or_binders}/lifecycle_actions
 */
public class DocumentActionResponse extends ActionResponse {


}