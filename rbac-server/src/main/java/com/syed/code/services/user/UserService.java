package com.syed.code.services.user;

import com.syed.code.entities.user.User;
import com.syed.code.exceptions.PermissionMissingException;
import com.syed.code.requestsandresponses.user.ChangeUserPasswordRequest;
import com.syed.code.requestsandresponses.user.UserMetadataResponse;
import com.syed.code.requestsandresponses.user.UserResponse;
import com.syed.code.services.baseentity.BaseEntityService;

public interface UserService extends BaseEntityService<User> {
    public UserResponse createUser(User user) throws PermissionMissingException;

    public UserResponse getUser(Long id) throws PermissionMissingException;

    public UserMetadataResponse getUserMetadata();

    public User getVerificationLoadedUser(String username);

    public UserResponse changeUserPassword(ChangeUserPasswordRequest passwordRequest) throws PermissionMissingException;

}
