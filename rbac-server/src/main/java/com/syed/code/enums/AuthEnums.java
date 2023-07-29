package com.syed.code.enums;

import java.util.HashMap;
import java.util.Map;

public class AuthEnums {
    public enum UserStatus {
        Active(1, "Active"),
        Active_Verification_Pending(2, "Active_Reset_Password"),
        Admin_Disabled(3, "Admin_Disabled"),
        Deleted(4, "Deleted"),
        Locked(5, "Locked");

        public static final Map<Integer, UserStatus> UserStatusMap = new HashMap<>();

        static {
            for (UserStatus userStatus : values())
                UserStatusMap.put(userStatus.statusId, userStatus);
        }

        public final int statusId;
        public final String statusName;

        UserStatus(int id, String statusName) {
            this.statusId = id;
            this.statusName = statusName;
        }

        public static UserStatus getById(int id) {
            return UserStatusMap.get(id);
        }
    }
}
