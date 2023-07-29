package com.syed.code.enums.codes;

public class SuccessCodes {
    public enum Generic {
        S00_001("S00_001", "Entity saved"),
        S00_002("S00_002", "Entity updated"),
        S00_003("S00_003", "Entity fetched"),
        S00_004("S00_004", "Grid data loaded"),

        S00_005("S00_005", "Password reset successfully"),
        S00_006("S00_006", "Log in Successful"),

        S00_007("S00_007", "Password reset code valid"),

        S00_008("S00_008", "Verification code valid");

        public final String code;
        public final String description;

        Generic(String code, String description) {
            this.code = code;
            this.description = description;
        }
    }

    public enum User {
        S01_001("S01_001", "Password changed");

        public final String code;
        public final String description;

        User(String code, String description) {
            this.code = code;
            this.description = description;
        }
    }

    public enum Role {
        S01_001("S01_001", "Something");

        public final String code;
        public final String description;

        Role(String code, String description) {
            this.code = code;
            this.description = description;
        }
    }

    public enum AuditLog {
        S03_001("S03_001", "Loaded audit log");

        public final String code;
        public final String description;

        AuditLog(String code, String description) {
            this.code = code;
            this.description = description;
        }
    }
}
