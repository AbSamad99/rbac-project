package com.syed.code.enums;

import java.util.HashMap;
import java.util.Map;

public class AuditEnums {

    public enum AttemptStatus {
        Success(1, "Success","Success", "Action performed successfully."),
        FailedError(2, "FailedError","Failed - Error", "Action not complete due to error."),
        FailedMissingPerms(3, "FailedMissingPerms","Failed - Missing Perms", "Action not complete due to user missing permissions."),
        FailedBadRequest(3, "FailedBadRequest","Failed - Bad Request", "Action not complete due to improper request.");

        public static final Map<Integer, AttemptStatus> AttempStatusMap = new HashMap<>();

        static {
            for (AttemptStatus attemptStatus : values())
                AttempStatusMap.put(attemptStatus.statusId, attemptStatus);
        }

        public int statusId;
        public String statusName;
        public String statusDisplayName;
        public String statusDescription;

        AttemptStatus(int statusId, String statusName, String statusDisplayName, String statusDescription) {
            this.statusId = statusId;
            this.statusName = statusName;
            this.statusDisplayName = statusDisplayName;
            this.statusDescription = statusDescription;
        }

        public static AttemptStatus getAttemptStatusById(int id) {
            return AttempStatusMap.get(id);
        }

    }
}
