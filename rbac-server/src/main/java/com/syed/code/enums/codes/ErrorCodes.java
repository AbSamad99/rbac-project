package com.syed.code.enums.codes;

public class ErrorCodes {
    public enum Generic {
        E00_001("E00_001", "Error when saving entity"),
        E00_002("E00_002", "User does not have permission to perform this action."),
        E00_003("E00_003", "Error when loading grid data"),
        E00_004("E00_004", "Entity with that name already exists."),
        E00_005("E00_005", "Invalid credentials for login."),
        E00_006("E00_006", "Invalid credentials for login, account has been locked."),
        E00_007("E00_007", "Account disabled, contact admin."),
        E00_008("E00_008", "Password reset code invalid"),
        E00_009("E00_009", "Necessary info/data not provided"),
        E00_010("E00_010", "User verification code invalid");

        public final String code;
        public final String description;

        Generic(String code, String description) {
            this.code = code;
            this.description = description;
        }
    }

    public enum User {
        E03_001("E03_001", "Error loading audit log.");

        public final String code;
        public final String description;

        User(String code, String description) {
            this.code = code;
            this.description = description;
        }
    }

    public enum Role {
        E03_001("E03_001", "Error loading audit log.");

        public final String code;
        public final String description;

        Role(String code, String description) {
            this.code = code;
            this.description = description;
        }
    }

    public enum AuditLog {
        E03_001("E03_001", "Error loading audit log.");

        public final String code;
        public final String description;

        AuditLog(String code, String description) {
            this.code = code;
            this.description = description;
        }
    }
}
