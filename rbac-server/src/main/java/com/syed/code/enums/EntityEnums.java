package com.syed.code.enums;

import java.util.HashMap;
import java.util.Map;

public class EntityEnums {
    public enum Entity {
        User(1, "User", "User", 1),
        Role(2, "Role", "Role", 1),
        AuditLog(3, "AuditLog", "Audit Log", 0);

        public static final Map<Integer, Entity> EntitiesMap = new HashMap<>();

        static {
            for (Entity entity : values())
                EntitiesMap.put(entity.entityId, entity);
        }

        public final int entityId;
        public final String entityName;
        public final String entityDisplayName;
        public final int isVersioning;

        Entity(int entityId, String entityName, String entityDisplayName, int isVersioning) {
            this.entityId = entityId;
            this.entityName = entityName;
            this.entityDisplayName = entityDisplayName;
            this.isVersioning = isVersioning;
        }

        public static Entity getByEntityId(int entityId) {
            return EntitiesMap.get(entityId);
        }

    }

    public enum DataType {
        Integer(1, "Integer"),
        String(2, "String"),
        Boolean(3, "Boolean"),
        Date(4, "Date"),
        Long(5, "Long");

        public static final Map<Integer, DataType> DataTypeMap = new HashMap<Integer, DataType>();

        static {
            for (DataType dataType : values())
                DataTypeMap.put(dataType.dataTypeId, dataType);
        }

        public final int dataTypeId;
        public final String dataTypeName;

        DataType(int dataTypeId, String dataTypeName) {
            this.dataTypeId = dataTypeId;
            this.dataTypeName = dataTypeName;
        }

        public static DataType getByDataTypeId(int entityId) {
            return DataTypeMap.get(entityId);
        }

    }
}
