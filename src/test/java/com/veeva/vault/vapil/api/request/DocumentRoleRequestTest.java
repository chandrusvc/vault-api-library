package com.veeva.vault.vapil.api.request;

import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.model.response.*;
import com.veeva.vault.vapil.extension.DocumentRequestHelper;
import com.veeva.vault.vapil.extension.RoleRequestHelper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@Tag("DocumentRoleRequestTest")
@Tag("SmokeTest")
@ExtendWith(VaultClientParameterResolver.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Document Role Request request should")
public class DocumentRoleRequestTest {

    private static final String ASSIGN_ROLES_ON_MULTIPLE_DOCUMENTS_FILE_PATH =
            RoleRequestHelper.ASSIGN_ROLES_ON_MULTIPLE_DOCUMENTS_FILE_PATH;
	private static final String OWNER_ROLE = "owner__v";
    private static final String VIEWER_ROLE = "viewer__v";
    private static final Long ALL_INTERNAL_USERS_ID = 1L;
    private static final Long USER_ID = 8701969L;
    private static final String TEST_CSV = "testRoleCsv.csv";
    private static final int DOC_ID = 5;
    private static Integer BINDER_ID = 1;

    private static List<Integer> docIds = new ArrayList<>();
    private static VaultClient vaultClient;

    @BeforeAll
    static void setup(VaultClient client) {
        vaultClient = client;
        Assertions.assertTrue(vaultClient.getAuthenticationResponse().isSuccessful());
        QueryResponse queryResponse = DocumentRequestHelper.queryForDocId(vaultClient);
        docIds.add(queryResponse.getData().get(0).getInteger("id"));
    }

    @Test
    @Disabled
    public void testAssignUsersAndGroupsToRolesOnMultipleDocumentsBodyParams(VaultClient vaultClient) {
        DocumentRoleRequest documentRoleRequest = vaultClient.newRequest(DocumentRoleRequest.class);
        documentRoleRequest.setContentTypeXForm();
        Map<String, Object> bodyParams = new HashMap<>();
        bodyParams.put("docIds", Integer.toString(DOC_ID));
        bodyParams.put(VIEWER_ROLE + ".users", USER_ID);
        bodyParams.put(VIEWER_ROLE + ".groups", ALL_INTERNAL_USERS_ID);
        documentRoleRequest.setBodyParams(bodyParams);
        DocumentRoleChangeBulkResponse response = documentRoleRequest.assignUsersAndGroupsToRolesOnMultipleDocuments();
        Assertions.assertTrue(response.isSuccessful());
        Assertions.assertNotNull(response.getData());
    }

    @Test
    @Disabled
    public void testRemoveUsersAndGroupsFromRolesOnMultipleDocumentsBodyParams(VaultClient vaultClient) {
        DocumentRoleRequest documentRoleRequest = vaultClient.newRequest(DocumentRoleRequest.class);
        documentRoleRequest.setContentTypeXForm();
        Map<String, Object> bodyParams = new HashMap<>();
        bodyParams.put("docIds", Integer.toString(DOC_ID));
        bodyParams.put(VIEWER_ROLE + ".users", USER_ID);
        bodyParams.put(VIEWER_ROLE + ".groups", ALL_INTERNAL_USERS_ID);
        documentRoleRequest.setBodyParams(bodyParams);
        DocumentRoleChangeBulkResponse response = documentRoleRequest.removeUsersAndGroupsFromRolesOnMultipleDocuments();
        Assertions.assertTrue(response.isSuccessful());
        Assertions.assertNotNull(response.getData());
    }

    @Test
    @Disabled
    public void testAssignUsersAndGroupsToRolesOnMultipleDocumentsBinaryContent(VaultClient vaultClient) throws IOException {
        DocumentRoleRequest documentRoleRequest = vaultClient.newRequest(DocumentRoleRequest.class);
        documentRoleRequest.setContentTypeCsv();
        byte[] csv = Files.readAllBytes(new File(TEST_CSV).toPath());
        documentRoleRequest.setBinaryFile(TEST_CSV, csv);
        DocumentRoleChangeBulkResponse response = documentRoleRequest.assignUsersAndGroupsToRolesOnMultipleDocuments();
        Assertions.assertTrue(response.isSuccessful());
        Assertions.assertNotNull(response.getData());
    }

    @Test
    @Disabled
    public void testRemoveUsersAndGroupsFromRolesOnMultipleDocumentsBinaryContent(VaultClient vaultClient) throws IOException {
        DocumentRoleRequest documentRoleRequest = vaultClient.newRequest(DocumentRoleRequest.class);
        documentRoleRequest.setContentTypeCsv();
        byte[] csv = Files.readAllBytes(new File(TEST_CSV).toPath());
        documentRoleRequest.setBinaryFile(TEST_CSV, csv);
        DocumentRoleChangeBulkResponse response = documentRoleRequest.removeUsersAndGroupsFromRolesOnMultipleDocuments();
        Assertions.assertTrue(response.isSuccessful());
        Assertions.assertNotNull(response.getData());
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully retrieve all document roles on a document")
    class TestRetrieveAllDocumentRoles {
        DocumentRoleRetrieveResponse response = null;

        @Test
        @Order(1)
        public void testRequest() {
            response = vaultClient.newRequest(DocumentRoleRequest.class)
                    .retrieveAllDocumentRoles(docIds.get(0));
            assertNotNull(response);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(response.isSuccessful());
            assertNull(response.getErrorCodes());
            assertNull(response.getErrorType());
            List<DocumentRoleRetrieveResponse.Role> docRoles = response.getRoles();
            assertNotNull(docRoles);
            for (DocumentRoleRetrieveResponse.Role docRole : docRoles) {
                assertNotNull(docRole.getName());
                assertNotNull(docRole.getLabel());
                assertNotNull(docRole.getAssignedUsers());
                assertNotNull(docRole.getAssignedGroups());
                assertNotNull(docRole.getAvailableUsers());
                assertNotNull(docRole.getAvailableGroups());
                assertNotNull(docRole.getDefaultUsers());
                assertNotNull(docRole.getDefaultGroups());
            }
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully retrieve a document role on a document")
    class TestRetrieveDocumentRole {
        DocumentRoleRetrieveResponse response = null;

        @Test
        @Order(1)
        public void testRequest() {
            response = vaultClient.newRequest(DocumentRoleRequest.class)
                    .retrieveDocumentRole(docIds.get(0), "owner__v");
            assertNotNull(response);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(response.isSuccessful());
            assertNull(response.getErrorCodes());
            assertNull(response.getErrorType());
            List<DocumentRoleRetrieveResponse.Role> docRoles = response.getRoles();
            assertNotNull(docRoles);
            assertNotNull(docRoles.get(0).getName());
            assertNotNull(docRoles.get(0).getLabel());
            assertNotNull(docRoles.get(0).getAssignedUsers());
            assertNotNull(docRoles.get(0).getAssignedGroups());
            assertNotNull(docRoles.get(0).getAvailableUsers());
            assertNotNull(docRoles.get(0).getAvailableGroups());
            assertNotNull(docRoles.get(0).getDefaultUsers());
            assertNotNull(docRoles.get(0).getDefaultGroups());
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully assign users and groups to roles on a single document")
    class TestAssignUsersAndGroupsToRolesOnASingleDocument {
        DocumentRoleChangeResponse response = null;
        List<Integer> docIds = new ArrayList<>();
        String userId;

        @BeforeAll
        void setup() {
            DocumentBulkResponse createResponse = DocumentRequestHelper.createMultipleDocuments(vaultClient, 1);
            docIds.add(createResponse.getData().get(0).getDocument().getId());

            StringBuilder query = new StringBuilder();
            query.append("SELECT id ");
            query.append("FROM user__sys ");
            query.append("WHERE name__v ");
            query.append("CONTAINS('Vapil Test (developersupport.com)')");
            QueryResponse queryResponse = vaultClient.newRequest(QueryRequest.class)
                    .query(query.toString());
            assertFalse(queryResponse.isFailure());
            userId = queryResponse.getData().get(0).getString("id");
        }

        @AfterAll
        void teardown() {
            DocumentBulkResponse deleteResponse = DocumentRequestHelper.deleteDocuments(vaultClient, docIds);
            assertTrue(deleteResponse.isSuccessful());
        }

        @Test
        @Order(1)
        public void testRequest() {
            response = vaultClient.newRequest(DocumentRoleRequest.class)
                    .setBodyParams(Collections.singletonMap("editor__v.users", userId))
                    .assignUsersAndGroupsToRolesOnASingleDocument(docIds.get(0));
            assertNotNull(response);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(response.isSuccessful());
            assertEquals(String.valueOf(response.getUpdatedRoles().get("editor__v").get("users").get(0)), userId);
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully remove users and groups from roles on a single document")
    class TestRemoveUsersAndGroupsFromRolesOnASingleDocument {
        DocumentRoleChangeResponse response = null;
        List<Integer> docIds = new ArrayList<>();
        String userId;

        @BeforeAll
        void setup() {
//            Create a Document
            DocumentBulkResponse createResponse = DocumentRequestHelper.createMultipleDocuments(vaultClient, 1);
            docIds.add(createResponse.getData().get(0).getDocument().getId());

//            Get a User Id to assign/remove
            StringBuilder query = new StringBuilder();
            query.append("SELECT id ");
            query.append("FROM user__sys ");
            query.append("WHERE name__v ");
            query.append("CONTAINS('Vapil Test (developersupport.com)')");
            QueryResponse queryResponse = vaultClient.newRequest(QueryRequest.class)
                    .query(query.toString());
            assertFalse(queryResponse.isFailure());
            userId = queryResponse.getData().get(0).getString("id");

//            Assign user to Editor role
            DocumentRoleChangeResponse assignResponse = vaultClient.newRequest(DocumentRoleRequest.class)
                    .setBodyParams(Collections.singletonMap("editor__v.users", userId))
                    .assignUsersAndGroupsToRolesOnASingleDocument(docIds.get(0));
            assertTrue(assignResponse.isSuccessful());
        }

        @AfterAll
        void teardown() {
            DocumentBulkResponse deleteResponse = DocumentRequestHelper.deleteDocuments(vaultClient, docIds);
            assertTrue(deleteResponse.isSuccessful());
        }

        @Test
        @Order(1)
        public void testRequest() {
            response = vaultClient.newRequest(DocumentRoleRequest.class)
                    .removeUsersAndGroupsFromRolesOnASingleDocument(docIds.get(0),
                            "editor__v",
                            DocumentRoleRequest.MemberType.USER,
                            Long.parseLong(userId));
            assertNotNull(response);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(response.isSuccessful());
            assertEquals(String.valueOf(response.getUpdatedRoles().get("editor__v").get("users").get(0)), userId);
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully assign users and groups to roles on multiple documents")
    class TestAssignUsersAndGroupsToRolesOnMultipleDocumentsCsv {
        DocumentRoleChangeBulkResponse response = null;
        List<Integer> docIds = new ArrayList<>();
        String userId;

        @BeforeAll
        void setup() throws IOException {
//            Create 2 docs
            DocumentBulkResponse createResponse = DocumentRequestHelper.createMultipleDocuments(vaultClient, 2);
            for (DocumentResponse documentResponse : createResponse.getData()) {
                docIds.add(documentResponse.getDocument().getId());
            }

//            Get a User Id to assign to roles
            StringBuilder query = new StringBuilder();
            query.append("SELECT id ");
            query.append("FROM user__sys ");
            query.append("WHERE name__v ");
            query.append("CONTAINS('Vapil Test (developersupport.com)')");
            QueryResponse queryResponse = vaultClient.newRequest(QueryRequest.class)
                    .query(query.toString());
            assertFalse(queryResponse.isFailure());
            userId = queryResponse.getData().get(0).getString("id");

//            Write to CSV. Test hardcoded to assign single user to editor role on each document
            Map<Integer, String> docIdToRole = new HashMap<>();
            for (Integer docId : docIds) {
                docIdToRole.put(docId, userId);
            }
            RoleRequestHelper.writeAssignUsersAndGroupsToRolesOnMultipleDocumentsFile(docIdToRole);
        }

        @AfterAll
        void teardown() {
            DocumentBulkResponse deleteResponse = DocumentRequestHelper.deleteDocuments(vaultClient, docIds);
            assertTrue(deleteResponse.isSuccessful());
        }

        @Test
        @Order(1)
        public void testRequest() {
            response = vaultClient.newRequest(DocumentRoleRequest.class)
                    .setContentTypeCsv()
                    .setInputPath(ASSIGN_ROLES_ON_MULTIPLE_DOCUMENTS_FILE_PATH)
                    .assignUsersAndGroupsToRolesOnMultipleDocuments();
            assertNotNull(response);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(response.isSuccessful());
            assertNotNull(response.getData());
            for (RoleChangeBulkResponse.RoleChange docResponse : response.getData()) {
                assertTrue(docResponse.getResponseStatus().equalsIgnoreCase("SUCCESS"));
                assertNotNull(docResponse.getId());
                for (int id : docResponse.getListInteger("editor__v.users")) {
                    assertEquals(id, Integer.valueOf(userId));
                }
            }
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully remove users and groups from roles on multiple documents")
    class TestRemoveUsersAndGroupsFromRolesOnMultipleDocumentsCsv {
        DocumentRoleChangeBulkResponse response = null;
        List<Integer> docIds = new ArrayList<>();
        String userId;

        @BeforeAll
        void setup() throws IOException {
//            Create 2 docs
            DocumentBulkResponse createResponse = DocumentRequestHelper.createMultipleDocuments(vaultClient, 2);
            for (DocumentResponse documentResponse : createResponse.getData()) {
                docIds.add(documentResponse.getDocument().getId());
            }

//            Get a User Id to assign to roles
            StringBuilder query = new StringBuilder();
            query.append("SELECT id ");
            query.append("FROM user__sys ");
            query.append("WHERE name__v ");
            query.append("CONTAINS('Vapil Test (developersupport.com)')");
            QueryResponse queryResponse = vaultClient.newRequest(QueryRequest.class)
                    .query(query.toString());
            assertFalse(queryResponse.isFailure());
            userId = queryResponse.getData().get(0).getString("id");

//            Write to CSV. Test hardcoded to assign single user to editor role on each document
            Map<Integer, String> docIdToRole = new HashMap<>();
            for (Integer docId : docIds) {
                docIdToRole.put(docId, userId);
            }
            RoleRequestHelper.writeAssignUsersAndGroupsToRolesOnMultipleDocumentsFile(docIdToRole);

//            Assign user to Editor role
            DocumentRoleChangeBulkResponse response = vaultClient.newRequest(DocumentRoleRequest.class)
                    .setContentTypeCsv()
                    .setInputPath(ASSIGN_ROLES_ON_MULTIPLE_DOCUMENTS_FILE_PATH)
                    .assignUsersAndGroupsToRolesOnMultipleDocuments();
            assertTrue(response.isSuccessful());
        }

        @AfterAll
        void teardown() {
            DocumentBulkResponse deleteResponse = DocumentRequestHelper.deleteDocuments(vaultClient, docIds);
            assertTrue(deleteResponse.isSuccessful());
        }

        @Test
        @Order(1)
        public void testRequest() {
            response = vaultClient.newRequest(DocumentRoleRequest.class)
                    .setContentTypeCsv()
                    .setInputPath(ASSIGN_ROLES_ON_MULTIPLE_DOCUMENTS_FILE_PATH)
                    .removeUsersAndGroupsFromRolesOnMultipleDocuments();
            assertNotNull(response);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(response.isSuccessful());
            assertNotNull(response.getData());
            for (RoleChangeBulkResponse.RoleChange docResponse : response.getData()) {
                assertTrue(docResponse.getResponseStatus().equalsIgnoreCase("SUCCESS"));
                assertNotNull(docResponse.getId());
                for (int id : docResponse.getListInteger("editor__v.users")) {
                    assertEquals(id, Integer.valueOf(userId));
                }
            }
        }
    }
}
