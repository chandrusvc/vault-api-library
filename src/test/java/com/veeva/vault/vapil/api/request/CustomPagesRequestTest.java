/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.request;

import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.model.common.ClientCodeDistribution;
import com.veeva.vault.vapil.api.model.response.CustomPagesDistributionBulkResponse;
import com.veeva.vault.vapil.api.model.response.CustomPagesDistributionResponse;
import com.veeva.vault.vapil.api.model.response.CustomPagesDistributionRetrieveResponse;
import com.veeva.vault.vapil.api.model.response.VaultResponse;
import com.veeva.vault.vapil.extension.CustomPagesRequestHelper;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("CustomPagesRequestTest")
@Tag("SmokeTest")
@ExtendWith(VaultClientParameterResolver.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Custom Pages request should")
public class CustomPagesRequestTest {

    private static final String PATH_TEST_DIST_ZIP = CustomPagesRequestHelper.PATH_TEST_DIST_ZIP;
    private static final String PATH_DOWNLOAD_DIST_ZIP = CustomPagesRequestHelper.PATH_RESOURCES_CUSTOM_PAGES_FOLDER + File.separator + "vapil_test_download.zip";
    private static final String DIST_NAME = "vapil_test__c";
    private static VaultClient vaultClient;

    @BeforeAll
    static void setup(VaultClient client) {
        vaultClient = client;
        Assertions.assertTrue(vaultClient.getAuthenticationResponse().isSuccessful());
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully retrieve all client code distributions in the vault and their metadata")
    class TestRetrieveAllClientCodeDistributionMetadata {
        CustomPagesDistributionBulkResponse response = null;

        @Test
        @Order(1)
        public void testRequest() {
            response = vaultClient.newRequest(CustomPagesRequest.class)
                    .retrieveAllClientCodeDistributionMetadata();

            assertNotNull(response);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(response.isSuccessful());
            for (ClientCodeDistribution dist : response.getData()) {
                assertNotNull(dist.getName());
                assertNotNull(dist.getSize());
                assertNotNull(dist.getChecksum());
            }
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully retrieve single client code distribution and metadata")
    class TestRetrieveSingleClientCodeDistributionMetadata {
        CustomPagesDistributionRetrieveResponse response = null;
        String distributionName = null;

        @BeforeAll
        public void setup() {
            CustomPagesDistributionBulkResponse retrieveResponse = vaultClient.newRequest(CustomPagesRequest.class)
                            .retrieveAllClientCodeDistributionMetadata();
            assertTrue(retrieveResponse.isSuccessful());
            distributionName = retrieveResponse.getData().get(0).getName();
        }

        @Test
        @Order(1)
        public void testRequest() {
            response = vaultClient.newRequest(CustomPagesRequest.class)
                    .retrieveSingleClientCodeDistributionMetadata(distributionName);

            assertNotNull(response);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(response.isSuccessful());
            assertNotNull(response.getData().getName());
            assertNotNull(response.getData().getSize());
            assertNotNull(response.getData().getChecksum());
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully download a single client code distribution as bytes")
    class TestDownloadSingleClientCodeDistributionBinary {
        VaultResponse response = null;
        String distributionName = null;

        @BeforeAll
        public void setup() {
            CustomPagesDistributionBulkResponse retrieveResponse = vaultClient.newRequest(CustomPagesRequest.class)
                    .retrieveAllClientCodeDistributionMetadata();
            assertTrue(retrieveResponse.isSuccessful());
            distributionName = retrieveResponse.getData().get(0).getName();
        }

        @Test
        @Order(1)
        public void testRequest() {
            response = vaultClient.newRequest(CustomPagesRequest.class)
                    .downloadSingleClientCodeDistribution(distributionName);

            assertNotNull(response);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(response.isSuccessful());
            assertNotNull(response.getBinaryContent());
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully download a single client code distribution as bytes")
    class TestDownloadSingleClientCodeDistributionToFile {
        VaultResponse response = null;
        String distributionName = null;

        @BeforeAll
        public void setup() {
            CustomPagesDistributionBulkResponse retrieveResponse = vaultClient.newRequest(CustomPagesRequest.class)
                    .retrieveAllClientCodeDistributionMetadata();
            assertTrue(retrieveResponse.isSuccessful());
            distributionName = retrieveResponse.getData().get(0).getName();
        }

        @Test
        @Order(1)
        public void testRequest() {
            response = vaultClient.newRequest(CustomPagesRequest.class)
                    .setOutputPath(PATH_DOWNLOAD_DIST_ZIP)
                    .downloadSingleClientCodeDistribution(distributionName);

            assertNotNull(response);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(response.isSuccessful());
            assertNotNull(response.getOutputFilePath());
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully add or replace a client code distribution in the vault from input path")
    class TestAddOrReplaceSingleClientCodeDistributionFromInput {
        CustomPagesDistributionResponse response = null;

        @AfterAll
        public void teardown() {
            VaultResponse response = vaultClient.newRequest(CustomPagesRequest.class)
                    .deleteSingleClientCodeDistribution(DIST_NAME);
            assertTrue(response.isSuccessful());
        }

        @Test
        @Order(1)
        public void testRequest() {
            response = vaultClient.newRequest(CustomPagesRequest.class)
                    .setInputPath(PATH_TEST_DIST_ZIP)
                    .addOrReplaceSingleClientCodeDistribution();

            assertNotNull(response);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(response.isSuccessful());
            assertNotNull(response.getData().getName());
            assertNotNull(response.getData().getUpdateType());
            assertNotNull(response.getData().getChecksum());
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully add or replace a client code distribution in the vault from binary file")
    class TestAddOrReplaceSingleClientCodeDistributionFromBinary {
        CustomPagesDistributionResponse response = null;

        @AfterAll
        public void teardown() {
            VaultResponse response = vaultClient.newRequest(CustomPagesRequest.class)
                    .deleteSingleClientCodeDistribution(DIST_NAME);
            assertTrue(response.isSuccessful());
        }

        @Test
        @Order(1)
        public void testRequest() throws IOException {
            File inputFile = new File(PATH_TEST_DIST_ZIP);
            byte[] fileContent = Files.readAllBytes(inputFile.toPath());
            response = vaultClient.newRequest(CustomPagesRequest.class)
                    .setFile("vapil_test_dist.zip", fileContent)
                    .addOrReplaceSingleClientCodeDistribution();

            assertNotNull(response);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(response.isSuccessful());
            assertNotNull(response.getData().getName());
            assertNotNull(response.getData().getUpdateType());
            assertNotNull(response.getData().getChecksum());
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("successfully delete a client code distribution in the vault")
    class TestDeleteSingleClientCodeDistribution {
        VaultResponse response = null;

        @BeforeAll
        public void setup() {
            CustomPagesDistributionResponse response = vaultClient.newRequest(CustomPagesRequest.class)
                    .setInputPath(PATH_TEST_DIST_ZIP)
                    .addOrReplaceSingleClientCodeDistribution();
            assertTrue(response.isSuccessful());
        }

        @Test
        @Order(1)
        public void testRequest() {
            response = vaultClient.newRequest(CustomPagesRequest.class)
                    .deleteSingleClientCodeDistribution(DIST_NAME);

            assertNotNull(response);
        }

        @Test
        @Order(2)
        public void testResponse() {
            assertTrue(response.isSuccessful());
        }
    }
}
