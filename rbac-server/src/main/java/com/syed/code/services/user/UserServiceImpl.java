package com.syed.code.services.user;

import com.syed.code.entities.email.Email;
import com.syed.code.entities.role.EntityPermissions;
import com.syed.code.entities.role.LiteRole;
import com.syed.code.entities.role.Role;
import com.syed.code.entities.role.UserRoleMapping;
import com.syed.code.entities.user.CustomUserDetails;
import com.syed.code.entities.user.User;
import com.syed.code.entities.user.UserVerificationInfo;
import com.syed.code.enums.AuditEnums;
import com.syed.code.enums.AuthEnums;
import com.syed.code.enums.PermissionEnums;
import com.syed.code.enums.codes.ErrorCodes;
import com.syed.code.enums.codes.SuccessCodes;
import com.syed.code.exceptions.PermissionMissingException;
import com.syed.code.repositories.RoleRepository;
import com.syed.code.repositories.UserRepository;
import com.syed.code.repositories.UserVerificationInfoRepository;
import com.syed.code.requestsandresponses.base.BaseResponse;
import com.syed.code.requestsandresponses.base.MessageVariable;
import com.syed.code.requestsandresponses.user.ChangeUserPasswordRequest;
import com.syed.code.requestsandresponses.user.UserMetadataResponse;
import com.syed.code.requestsandresponses.user.UserResponse;
import com.syed.code.services.baseentity.BaseEntityServiceImpl;
import com.syed.code.services.email.EmailService;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl extends BaseEntityServiceImpl<User> implements UserService {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserVerificationInfoRepository userVerificationInfoRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder encoder;

    private final String ACCOUNT_CREATION_TEMPLATE = "user-creation-email.ftl";

    @Override
    public void clearFieldsForCopy(User entity) {
        entity.setKey(null);
        entity.setUserVerificationInfo(null);
    }

    @Override
    public Boolean saveEntity(User entity, User previousEntity, Session session, BaseResponse response, PermissionEnums.GenericPerms genericPerm) throws PermissionMissingException {
        Boolean res = saveEntityInternal(entity, previousEntity, session, response, genericPerm);
        if (res == true) {
            if (entity.getRoles() != null && !entity.getRoles().isEmpty()) {
                List<UserRoleMapping> userRoleMappings = new ArrayList<>();
                for (Role role : entity.getRoles()) {
                    UserRoleMapping userRoleMapping = new UserRoleMapping();
                    userRoleMapping.setUserId(entity.getId());
                    userRoleMapping.setRoleKey(role.getKey());
                    userRoleMappings.add(userRoleMapping);
                    session.persist(userRoleMapping);
                }
                entity.setUserRoleMappings(userRoleMappings);
            }
            if (entity.getVerificationInfoId() == null) {
                if (entity.getPassword() == null || entity.getPassword().isEmpty()) {
                    MessageVariable messageVariable = getEntityPopulatedMessageVariable();
                    messageVariable.setApplicationCode(ErrorCodes.Generic.E00_009.code);
                    response.getMessageVariables().add(messageVariable);
                    return false;
                }
                UserVerificationInfo verificationInfo = new UserVerificationInfo();
                verificationInfo.setUserKey(entity.getKey());
                verificationInfo.setHash(encoder.encode(entity.getPassword()));
                verificationInfo.setStatus(AuthEnums.UserStatus.Active.statusId);
                verificationInfo.setLockCount(0);
                session.persist(verificationInfo);
                entity.setVerificationInfoId(verificationInfo.getId());
            } else {
                entity.setVerificationInfoId(previousEntity.getVerificationInfoId());
            }
            session.update(entity);
            handleNewUserEmail(entity);
        }
        return res;
    }

    private void handleNewUserEmail(User user) {
        CustomUserDetails userDetails = getLoggedInUserService().getLoggedInUser();
        Email email = new Email();
        Map<String, Object> contentMap = new HashMap<>();

        email.setTemplateName(ACCOUNT_CREATION_TEMPLATE);
        email.setFrom(userDetails.getUsername());
        email.setTo(user.getEmail());
        email.setSubject("Account Created");

        contentMap.put("firstName", user.getFirstName());
        contentMap.put("lastName", user.getLastName());
        contentMap.put("link", user.getFirstName());
        email.setContentMap(contentMap);

        Thread thread = new Thread(() -> {
            emailService.sendEmail(email);
        });
        thread.start();
    }

    @Override
    public UserResponse createUser(User user) throws PermissionMissingException {
        UserResponse response = new UserResponse();
        Boolean res = saveEntity(user, response);
        return response;
    }

    @Override
    public UserResponse getUser(Long id) throws PermissionMissingException {
        UserResponse response = new UserResponse();
        PermissionEnums.GenericPerms genericPerms = PermissionEnums.GenericPerms.View;

        User user = userRepository.getUserById(id);
        if (!getLoggedInUserService().isActionAllowed(getEnt(), genericPerms)) {
            getAuditService().performLog(getEnt(), genericPerms, user, null, AuditEnums.AttemptStatus.FailedMissingPerms);
            throw new PermissionMissingException(getEnt(), "View user perms missing");
        }

        if (user != null) {
            loadRoles(user);
//            loadEntityPermissions(user);
        }
        MessageVariable messageVariable = getEntityPopulatedMessageVariable();
        messageVariable.setApplicationCode(SuccessCodes.Generic.S00_003.code);
        response.getMessageVariables().add(messageVariable);
        response.setStatusCode(HttpStatus.OK);
        response.setUser(user);

        return response;
    }

    @Override
    public UserMetadataResponse getUserMetadata() {
        UserMetadataResponse response = new UserMetadataResponse();
        List<LiteRole> roles = roleRepository.getActiveLiteRoles();

        response.setRoles(roles);
        response.setStatusCode(HttpStatus.OK);
        return response;
    }

    @Override
    public User getVerificationLoadedUser(String username) {
        User user = null;
        List<User> users = userRepository.getExistingUserByUsername(username);
        if (users.size() == 1)
            user = users.get(0);
        if (user != null) {
            loadUserVerificationInfo(user);
            loadRoles(user);
        }
        return user;
    }

    @Override
    public UserResponse changeUserPassword(ChangeUserPasswordRequest passwordRequest) throws PermissionMissingException {
        UserResponse response = new UserResponse();
        MessageVariable messageVariable = getEntityPopulatedMessageVariable();
        PermissionEnums.GenericPerms genericPerms = PermissionEnums.GenericPerms.ChangePassword;

        User user = userRepository.getUserById(passwordRequest.getUserId());
        if (!getLoggedInUserService().isActionAllowed(getEnt(), genericPerms)) {
            getAuditService().performLog(getEnt(), genericPerms, user, null, AuditEnums.AttemptStatus.FailedMissingPerms);
            throw new PermissionMissingException(getEnt(), "Change user password perms missing");
        }

        if (user == null || passwordRequest.getPassword() == null || passwordRequest.getPassword().isEmpty()) {
            response.setStatusCode(HttpStatus.BAD_REQUEST);
            return response;
        }

        loadUserVerificationInfo(user);
        UserVerificationInfo verificationInfo = user.getUserVerificationInfo();
        verificationInfo.setHash(encoder.encode(passwordRequest.getPassword()));
        userVerificationInfoRepository.save(verificationInfo);

        messageVariable.setApplicationCode(SuccessCodes.User.S01_001.code);
        response.getMessageVariables().add(messageVariable);
        response.setStatusCode(HttpStatus.OK);
        return response;
    }

    @Override
    public Boolean isValid(User entity, User previousEntity) {
        if (previousEntity != null && previousEntity.getKey() == 1) return false;
        List<User> users = userRepository.getExistingUserByUsername(entity.getUsername());
        if (users.size() != 0 && previousEntity == null) return false;
        for (User user : users)
            if (!user.getKey().equals(entity.getKey())) return false;

        return true;
    }

    @Override
    public User getPreviousEntity(User entity) {
        User previousUser = null;
        if (entity.getKey() != null) {
            previousUser = userRepository.getUserByKey(entity.getKey());
            if (previousUser != null) {
                loadUserRoleMappings(previousUser);
            }
        }
        return previousUser;
    }

    private void loadUserRoleMappings(User user) {
        List<UserRoleMapping> userRoleMappings = userRepository.getUserRoleMappingsByUserId(user.getId());
        user.setUserRoleMappings(userRoleMappings);
    }

    private void loadRoles(User user) {
        loadUserRoleMappings(user);
        if (user.getUserRoleMappings() == null || user.getUserRoleMappings().isEmpty()) return;
        List<Long> roleKeys = user.getUserRoleMappings().stream().map(userRoleMapping -> userRoleMapping.getRoleKey()).toList();
        List<Role> roles = roleRepository.getRolesByRoleKeys(roleKeys);
        user.setRoles(roles);
    }

    private void loadEntityPermissions(User user) {
        List<EntityPermissions> entityPermissions = roleRepository.getEntityPermissionsByUserId(user.getId());
        user.setEntityPermissions(entityPermissions);
    }

    private void loadUserVerificationInfo(User user) {
        UserVerificationInfo userVerificationInfo = userVerificationInfoRepository.getUserVerificationInfoByUserKey(user.getKey());
        user.setUserVerificationInfo(userVerificationInfo);
    }
}
