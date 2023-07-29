package com.syed.code.enums;

import java.util.HashMap;
import java.util.Map;

public class PermissionEnums {
    public enum GenericPerms {
        Create(1, "Create"),
        View(2, "View"),
        Edit(3, "Edit"),
        Delete(4, "Delete"),
        Copy(5, "Copy"),
        ChangePassword(11, "Change Password");

        public static final Map<Integer, GenericPerms> genericPermissionMap = new HashMap<>();

        static {
            for (GenericPerms genericPerms : values())
                genericPermissionMap.put(genericPerms.permissionId, genericPerms);
        }

        public final int permissionId;
        public final String permissionName;

        GenericPerms(int permissionId, String permissionName) {
            this.permissionId = permissionId;
            this.permissionName = permissionName;
        }

        public static GenericPerms getGenericPermissionById(int permissionId) {
            return genericPermissionMap.get(permissionId);
        }
    }
}
