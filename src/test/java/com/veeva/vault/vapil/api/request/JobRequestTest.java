/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.request;

import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.model.common.Job;
import com.veeva.vault.vapil.api.model.response.*;
import com.veeva.vault.vapil.extension.BinderRequestHelper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import com.veeva.vault.vapil.extension.VaultClientParameterResolver;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@Disabled("Jobs are set to Inactive after the sandbox is refreshed. " +
		"Verify that the VAPIL Test Doc Job is active before running tests.")
@Tag("JobRequestTest")
@ExtendWith(VaultClientParameterResolver.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Job Request should")
public class JobRequestTest {

	private static final String JOB_NAME = "VAPIL Test Doc Job";
	private static int jobId;
	private static VaultClient vaultClient;

	@BeforeAll
	static void setup(VaultClient client) {
		vaultClient = client;
		Assertions.assertTrue(vaultClient.getAuthenticationResponse().isSuccessful());
	}

	@Test
	@Order(1)
	@DisplayName("successfully retrieve monitors for jobs which have not yet completed in the authenticated Vault")
	public void testRetrieveJobMonitors() {
		JobMonitorResponse response = vaultClient.newRequest(JobRequest.class)
				.retrieveJobMonitors();
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getJobs());
		Assertions.assertNotEquals(0, response.getJobs().size());
		for (Job job : response.getJobs()) {
			if (job.getTitle().equals(JOB_NAME)) {
				jobId = job.getJobId();
				break;
			}
		}

		if (response.isPaginated()) {
			JobMonitorResponse paginatedResponse = vaultClient.newRequest(JobRequest.class)
					.retrieveJobMonitorsByPage(response.getResponseDetails().getNextPage());
			Assertions.assertTrue(paginatedResponse.isSuccessful());
		}
	}

	@Test
	@Order(2)
	@DisplayName("successfully move up a scheduled job instance to start immediately")
	public void testStartJob() {
		JobCreateResponse response = vaultClient.newRequest(JobRequest.class)
				.startJob(jobId);
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getJobId());
		Assertions.assertEquals(jobId, response.getJobId());

	}

	@Test
	@Order(3)
	@DisplayName("successfully retrieve the status of a previously submitted job request")
	public void testRetrieveJobStatus() {
		String jobStatus = "";
		boolean jobCompleted = false;
		for (int i = 0; i < 30; i++) {
			if (jobStatus.equals("SUCCESS")) break;
			JobStatusResponse jobStatusResponse = vaultClient.newRequest(JobRequest.class)
					.retrieveJobStatus(jobId);
			jobStatus = jobStatusResponse.getData().getStatus();
			switch (jobStatus) {
				case "SUCCESS":
					jobCompleted = true;
					break;
				default:
					try {
						Thread.sleep(10000);
						break;
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
			}
		}
		Assertions.assertTrue(jobCompleted);
		Assertions.assertEquals("SUCCESS", jobStatus);
	}

	@Test
	@Order(4)
	@DisplayName("successfully retrieve a history of all completed jobs in the authenticated Vault")
	public void testRetrieveJobHistories() {
		JobHistoryResponse response = vaultClient.newRequest(JobRequest.class).retrieveJobHistories();
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertNotNull(response.getJobs());
		Assertions.assertNotEquals(0, response.getJobs().size());

		for(Job job : response.getJobs()) {
			if(job.getJobId() == jobId) {
				Assertions.assertEquals("SUCCESS", job.getStatus());
				break;
			}
		}

		if (response.isPaginated()) {
			JobHistoryResponse paginatedResponse = vaultClient.newRequest(JobRequest.class)
					.retrieveJobHistoriesByPage(response.getResponseDetails().getNextPage());
			Assertions.assertTrue(paginatedResponse.isSuccessful());
		}
	}

	@Nested
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@DisplayName("successfully retrieve the tasks associated with an SDK job")
	@Disabled("Test Manually. May be automated in the future")
	class TestRetrieveSdkJobTasks {
		JobTaskResponse response = null;
		int jobId = 282871;

		@Test
		@Order(1)
		public void testRequest() {
			response = vaultClient.newRequest(JobRequest.class)
							.retrieveSdkJobTasks(jobId);
			assertNotNull(response);
		}

		@Test
		@Order(2)
		public void testResponse() {
			assertTrue(response.isSuccessful());
			assertNotNull(response.getUrl());
			assertNotNull(response.getJobId());
			assertNotNull(response.getTasks());
			for (JobTaskResponse.JobTask task : response.getTasks()) {
				assertNotNull(task.getId());
				assertNotNull(task.getState());
			}
		}
	}
}
