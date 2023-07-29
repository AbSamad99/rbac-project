package com.syed.code.services.audit.details;

import com.syed.code.entities.audit.AuditDetails;
import com.syed.code.entities.audit.AuditLog;
import com.syed.code.entities.baseentity.BaseEntity;
import com.syed.code.entities.role.GenericPermissions;
import com.syed.code.entities.user.User;
import com.syed.code.enums.EntityEnums.Entity;
import com.syed.code.enums.PermissionEnums.GenericPerms;
import com.syed.code.repositories.RoleRepository;
import com.syed.code.repositories.UserRepository;
import com.syed.code.services.objecthashmapper.ObjectHashMapperServiceImpl;
import com.syed.code.utils.EntityClassMapUtil;
import jakarta.persistence.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Primary
public class GenericAuditDetailsServiceImpl<T extends BaseEntity> implements GenericAuditDetailsService<T> {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private EntityClassMapUtil classMapUtil;

    @Autowired
    private ObjectHashMapperServiceImpl objectHashMapperService;

    protected static final String TAB = "\t";
    protected static final String NEWLINE = "\n";
    protected static final String SPACE = " ";
    protected static final String SEPARATOR = "@@";
    protected static final String NULL_VALUE_String = "-";
    protected static final String COMMA = ",";

    @Override
    public AuditDetails performAudit(AuditLog auditLog) {
        Entity ent = Entity.getByEntityId(auditLog.getEntityId());
        if (ent == null) return null;
        String queryString = "select e from " + ent.entityName + " e where e.id = :entityId";
        T entity = null;
        T previousEntity = null;
        User user = null;
        try {

            Session session = sessionFactory.openSession();

            Query query1 = session.createQuery(queryString);
            query1.setParameter("entityId", auditLog.getNewId());
            List<T> list1 = query1.getResultList();
            entity = list1.get(0);

            user = userRepository.getUserById(auditLog.getPerformedBy());

            if (auditLog.getPreviousId() != null) {
                Query query2 = session.createQuery(queryString);
                query2.setParameter("entityId", auditLog.getPreviousId());
                List<T> list2 = query2.getResultList();
                previousEntity = list2.get(0);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        GenericPerms genericPerms = GenericPerms.getGenericPermissionById(auditLog.getPermissionId());
        GenericPermissions genericPermissions = roleRepository.getGenericPermissionById(Integer.toUnsignedLong(auditLog.getPermissionId()));

        AuditDetails auditDetails = new AuditDetails();
        auditDetails.setGenericPermissions(genericPermissions);
        auditDetails.setEntity(ent);
        if (genericPerms.equals(GenericPerms.Edit)) handleEditDetails(entity, previousEntity, auditDetails);
        auditDetails.setPerformedBy(user);
        auditDetails.setPerformedAt(auditLog.getPerformedAt());

        return auditDetails;
    }

    @Override
    public void handleEditDetails(T entity, T previousEntity, AuditDetails auditDetails) {
        HashMap<String, String> editDetails = new HashMap<>();
        Class clazz = classMapUtil.entityClassMap.get(auditDetails.getEntity());
        if (clazz != null) {
            try {
                Map<String, Object> entityMap = objectHashMapperService.getHashMapFromObject(clazz, entity);
                Map<String, Object> previousEntityMap = objectHashMapperService.getHashMapFromObject(clazz, previousEntity);
                for (String key : entityMap.keySet()) {
                    StringBuilder stringBuilder = new StringBuilder("");
                    stringBuilder.append(previousEntityMap.get(key) != null ? previousEntityMap.get(key) : NULL_VALUE_String);
                    stringBuilder.append(SEPARATOR);
                    stringBuilder.append(entityMap.get(key) != null ? entityMap.get(key) : NULL_VALUE_String);
                    editDetails.put(camelCaseToSentence(key), stringBuilder.toString());
                }
                auditDetails.setEditDetails(editDetails);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public String camelCaseToSentence(String key) {
        StringBuilder sentence = new StringBuilder();
        sentence.append(Character.toUpperCase(key.charAt(0)));
        for (int i = 1; i < key.length(); i++) {
            char ch = key.charAt(i);
            if (Character.isUpperCase(ch)) sentence.append(" ");
            sentence.append(ch);
        }
        return sentence.toString();
    }
}
