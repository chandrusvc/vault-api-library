package com.veeva.vault.vapil.extension;

import com.veeva.vault.vapil.api.client.VaultClient;
import com.veeva.vault.vapil.api.model.response.QueryResponse;
import com.veeva.vault.vapil.api.request.QueryRequest;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MetadataRequestHelper {

    public static final String PATH_RESOURCES_MDL_FOLDER = FileHelper.PATH_RESOURCES_FOLDER + File.separator + "mdl";
    public static final String PATH_MDL_RECREATE_SCRIPT = PATH_RESOURCES_MDL_FOLDER + File.separator + "recreate_script.txt";
    public static final String PATH_MDL_ALTER_SCRIPT = PATH_RESOURCES_MDL_FOLDER + File.separator + "alter_script.txt";
}
