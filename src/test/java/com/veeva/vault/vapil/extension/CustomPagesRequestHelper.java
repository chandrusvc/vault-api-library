package com.veeva.vault.vapil.extension;

import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.model.response.QueryResponse;
import com.veeva.vault.vapil.api.request.QueryRequest;

import java.io.File;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CustomPagesRequestHelper {

    public static final String PATH_RESOURCES_CUSTOM_PAGES_FOLDER = FileHelper.PATH_RESOURCES_FOLDER + File.separator + "custom_pages";
    public static final String PATH_TEST_DIST_ZIP = PATH_RESOURCES_CUSTOM_PAGES_FOLDER + File.separator + "vapil_test.zip";

}
