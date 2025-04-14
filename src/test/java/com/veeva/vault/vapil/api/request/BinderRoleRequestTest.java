package com.veeva.vault.vapil.api.request;

import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.model.response.*;
import com.veeva.vault.vapil.extension.BinderRequestHelper;
import com.veeva.vault.vapil.extension.RoleRequestHelper;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@Tag("BinderRoleRequestTest")
@Tag("SmokeTest")
@ExtendWith(VaultClientParameterResolver.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Binder Role Request request should")
public class BinderRoleRequestTest {
    private static final String ASSIGN_ROLES_ON_MULTIPLE_BINDERS_FILE_PATH =
            RoleRequestHelper.ASSIGN_ROLES_ON_MULTIPLE_BINDERS_FILE_PATH;
	
    private static List<Integer> binderIds = new ArrayList<>();
    private static VaultClient vaultClient;

    @BeforeAll
    static void setup(VaultClient client) {
        vaultClient = client;
        Assertions.assertTrue(vaultClient.getAuthenticationResponse().isSuccessful());
        QueryResponse queryResponse = BinderRequestHelper.queryForBinderId(vaultClient);
        binderIds.add(queryResponse.getData().get(0).getInteger("id"));
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully retrieve all binder roles on a binder")
    class TestRetrieveAllBinderRoles {
        BinderRoleRetrieveResponse response = null;

        @Test
        @Order(1)
        public void testRequest() {
            response = vaultClient.newRequest(BinderRoleRequest.class)
                    .retrieveAllBinderRoles(binderIds.get(0));
            assertNotNull(response);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(response.isSuccessful());
            assertNull(response.getErrorCodes());
            assertNull(response.getErrorType());
            List<BinderRoleRetrieveResponse.Role> docRoles = response.getRoles();
            assertNotNull(docRoles);
            for (BinderRoleRetrieveResponse.Role docRole : docRoles) {
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
    @DisplayName("successfully retrieve roles on a binder")
    class TestRetrieveBinderRole {
        BinderRoleRetrieveResponse response = null;

        @Test
        @Order(1)
        public void testRequest() {
            response = vaultClient.newRequest(BinderRoleRequest.class)
                    .retrieveBinderRole(binderIds.get(0), "owner__v");
            assertNotNull(response);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(response.isSuccessful());
            assertNull(response.getErrorCodes());
            assertNull(response.getErrorType());
            List<BinderRoleRetrieveResponse.Role> docRoles = response.getRoles();
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
    @DisplayName("successfully assign users and groups to roles on a single binder")
    class TestAssignUsersAndGroupsToRolesOnASingleBinder {
        BinderRoleChangeResponse response = null;
        int binderId;
        String userId;

        @BeforeAll
        void setup() {
            BinderResponse createResponse = BinderRequestHelper.createBinder(vaultClient);
            binderId = createResponse.getDocument().getId();

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
            BinderResponse deleteResponse = vaultClient.newRequest(BinderRequest.class)
                    .deleteBinder(binderId);
            assertTrue(deleteResponse.isSuccessful());
        }

        @Test
        @Order(1)
        public void testRequest() {
            response = vaultClient.newRequest(BinderRoleRequest.class)
                    .setBodyParams(Collections.singletonMap("editor__v.users", userId))
                    .assignUsersAndGroupsToRolesOnASingleBinder(binderId);
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
    @DisplayName("successfully remove users and groups from roles on a single binder")
    class TestRemoveUsersAndGroupsFromRolesOnASingleBinder {
        BinderRoleChangeResponse response = null;
        int binderId;
        String userId;

        @BeforeAll
        void setup() {
//            Create a binder
            BinderResponse createResponse = BinderRequestHelper.createBinder(vaultClient);
            binderId = createResponse.getDocument().getId();

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
            BinderRoleChangeResponse assignResponse = vaultClient.newRequest(BinderRoleRequest.class)
                    .setBodyParams(Collections.singletonMap("editor__v.users", userId))
                    .assignUsersAndGroupsToRolesOnASingleBinder(binderId);
            assertTrue(assignResponse.isSuccessful());
        }

        @AfterAll
        void teardown() {
            BinderResponse deleteResponse = vaultClient.newRequest(BinderRequest.class)
                    .deleteBinder(binderId);
            assertTrue(deleteResponse.isSuccessful());
        }

        @Test
        @Order(1)
        public void testRequest() {
            response = vaultClient.newRequest(BinderRoleRequest.class)
                    .removeUsersAndGroupsFromRolesOnASingleBinder(binderId,
                            "editor__v",
                            BinderRoleRequest.MemberType.USER,
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
    @DisplayName("successfully assign users and groups to roles on multiple binders")
    class TestAssignUsersAndGroupsToRolesOnMultipleBinders {
        BinderRoleChangeBulkResponse response = null;
        List<Integer> binderIds = new ArrayList<>();
        String userId;

        @BeforeAll
        void setup() throws IOException {
//            Create 2 binders
            BinderResponse createResponse1 = BinderRequestHelper.createBinder(vaultClient);
            binderIds.add(createResponse1.getDocument().getId());

            BinderResponse createResponse2 = BinderRequestHelper.createBinder(vaultClient);
            binderIds.add(createResponse2.getDocument().getId());

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

//            Write to CSV. Test hardcoded to assign single user to editor role on each binder
            Map<Integer, String> binderIdToRole = new HashMap<>();
            for (Integer binderId : binderIds) {
                binderIdToRole.put(binderId, userId);
            }
            RoleRequestHelper.writeAssignUsersAndGroupsToRolesOnMultipleBindersFile(binderIdToRole);
        }

        @AfterAll
        void teardown() {
            for (int binderId : binderIds) {
                BinderResponse deleteResponse = vaultClient.newRequest(BinderRequest.class)
                        .deleteBinder(binderId);
                assertTrue(deleteResponse.isSuccessful());
            }
        }

        @Test
        @Order(1)
        public void testRequest() {
            response = vaultClient.newRequest(BinderRoleRequest.class)
                    .setContentTypeCsv()
                    .setInputPath(ASSIGN_ROLES_ON_MULTIPLE_BINDERS_FILE_PATH)
                    .assignUsersAndGroupsToRolesOnMultipleBinders();
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
    @DisplayName("successfully remove users and groups from roles on multiple binders")
    class TestRemoveUsersAndGroupsFromRolesOnMultipleBindersCsv {
        BinderRoleChangeBulkResponse response = null;
        List<Integer> binderIds = new ArrayList<>();
        String userId;

        @BeforeAll
        void setup() throws IOException {
//            Create 2 binders
            BinderResponse createResponse1 = BinderRequestHelper.createBinder(vaultClient);
            binderIds.add(createResponse1.getDocument().getId());

            BinderResponse createResponse2 = BinderRequestHelper.createBinder(vaultClient);
            binderIds.add(createResponse2.getDocument().getId());

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
            Map<Integer, String> binderIdToRole = new HashMap<>();
            for (Integer docId : binderIds) {
                binderIdToRole.put(docId, userId);
            }
            RoleRequestHelper.writeAssignUsersAndGroupsToRolesOnMultipleBindersFile(binderIdToRole);

//            Assign user to Editor role
            BinderRoleChangeBulkResponse response = vaultClient.newRequest(BinderRoleRequest.class)
                    .setContentTypeCsv()
                    .setInputPath(ASSIGN_ROLES_ON_MULTIPLE_BINDERS_FILE_PATH)
                    .assignUsersAndGroupsToRolesOnMultipleBinders();
            assertTrue(response.isSuccessful());
        }

        @AfterAll
        void teardown() {
            for (int binderId : binderIds) {
                BinderResponse deleteResponse = vaultClient.newRequest(BinderRequest.class)
                        .deleteBinder(binderId);
                assertTrue(deleteResponse.isSuccessful());
            }
        }

        @Test
        @Order(1)
        public void testRequest() {
            response = vaultClient.newRequest(BinderRoleRequest.class)
                    .setContentTypeCsv()
                    .setInputPath(ASSIGN_ROLES_ON_MULTIPLE_BINDERS_FILE_PATH)
                    .removeUsersAndGroupsFromRolesOnMultipleBinders();
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
