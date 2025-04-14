package com.veeva.vault.vapil.extension;

import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.model.common.Document;
import com.veeva.vault.vapil.api.model.response.BinderResponse;
import com.veeva.vault.vapil.api.model.response.DocumentBulkResponse;
import com.veeva.vault.vapil.api.model.response.QueryResponse;
import com.veeva.vault.vapil.api.request.BinderRequest;
import com.veeva.vault.vapil.api.request.QueryRequest;

import java.io.File;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BinderRequestHelper {

    public static final String VAPIL_TEST_DOC_LIFECYCLE_LABEL = "VAPIL Test Doc Lifecycle";
    public static final String VAPIL_TEST_DOC_LIFECYCLE_NAME = "vapil_test_doc_lifecycle__c";
    public static final String VAPIL_TEST_DOC_TYPE_LABEL = "VAPIL Test Doc Type";
    public static final String VAPIL_TEST_DOC_TYPE_NAME = "vapil_test_doc_type__c";
    public static final String VAPIL_TEST_DOC_SUBTYPE_LABEL = "VAPIL Test Doc Subtype";
    public static final String VAPIL_TEST_DOC_SUBTYPE_NAME = "vapil_test_doc_subtype__c";
    public static final String VAPIL_TEST_DOC_CLASSIFICATION_LABEL = "VAPIL Test Doc Classification";
    public static final String VAPIL_TEST_DOC_CLASSIFICATION_NAME = "vapil_test_doc_classification__c";
    public static final String VAPIL_TEST_RECLASSIFY_TYPE_NAME = "vapil_test_reclassify_type__c";
    public static final String VAPIL_TEST_RECLASSIFY_TYPE_LABEL = "VAPIL Test Reclassify Type";
    public static final String PATH_RESOURCES_BINDERS_FOLDER = FileHelper.PATH_RESOURCES_FOLDER + File.separator + "binders";
    private static final int MAJOR_VERSION = 0;
    private static final int MINOR_VERSION = 1;

    public static QueryResponse queryForBinderId(VaultClient vaultClient) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT id, major_version_number__v, minor_version_number__v ");
        query.append("FROM documents ");
        query.append("WHERE binder__v = true ");
        query.append("ORDER BY id ASC ");
        query.append("MAXROWS 1");

        QueryResponse queryResponse = vaultClient.newRequest(QueryRequest.class)
                .query(query.toString());

        assertFalse(queryResponse.isFailure());
        return queryResponse;
    }

    public static BinderResponse createBinder(VaultClient vaultClient) {
        Document binder = new Document();
        String name = "VAPIL Test Create Binder " + ZonedDateTime.now();
        String description = "VAPIL Test";

        binder.setName(name);
        binder.setType(VAPIL_TEST_DOC_TYPE_LABEL);
        binder.setSubtype(VAPIL_TEST_DOC_SUBTYPE_LABEL);
        binder.setClassification(VAPIL_TEST_DOC_CLASSIFICATION_LABEL);
        binder.setLifecycle(VAPIL_TEST_DOC_LIFECYCLE_LABEL);
        BinderResponse createResponse = vaultClient.newRequest(BinderRequest.class)
                .createBinder(binder);

        assertTrue(createResponse.isSuccessful());

        return createResponse;
    }
}
