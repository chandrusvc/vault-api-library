package com.veeva.vault.vapil.extension;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RoleRequestHelper {

    public static final String ASSIGN_ROLES_ON_MULTIPLE_DOCUMENTS_FILE_PATH = FileHelper.PATH_RESOURCES_FOLDER + File.separator + "document_and_binder_roles" + File.separator + "assign_users_and_groups_to_roles_on_multiple_documents.csv";
    public static final String ASSIGN_ROLES_ON_MULTIPLE_BINDERS_FILE_PATH = FileHelper.PATH_RESOURCES_FOLDER + File.separator + "document_and_binder_roles" + File.separator + "assign_users_and_groups_to_roles_on_multiple_binders.csv";

    public static void writeAssignUsersAndGroupsToRolesOnMultipleDocumentsFile(Map<Integer, String> docs) throws IOException {
        List<String[]> updateData = new ArrayList<>();
        updateData.add(new String[]{"id", "editor__v.users"});

        for (int docId : docs.keySet()) {
            updateData.add(new String[]{String.valueOf(docId), docs.get(docId)});
        }

        FileHelper.writeCsvFile(ASSIGN_ROLES_ON_MULTIPLE_DOCUMENTS_FILE_PATH, updateData);
    }

    public static void writeAssignUsersAndGroupsToRolesOnMultipleBindersFile(Map<Integer, String> binders) throws IOException {
        List<String[]> updateData = new ArrayList<>();
        updateData.add(new String[]{"id", "editor__v.users"});

        for (int binderId : binders.keySet()) {
            updateData.add(new String[]{String.valueOf(binderId), binders.get(binderId)});
        }

        FileHelper.writeCsvFile(ASSIGN_ROLES_ON_MULTIPLE_BINDERS_FILE_PATH, updateData);
    }
}
