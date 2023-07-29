package com.syed.code.controllers;

import com.syed.code.entities.user.User;
import com.syed.code.exceptions.PermissionMissingException;
import com.syed.code.requestsandresponses.base.BaseResponse;
import com.syed.code.requestsandresponses.user.ChangeUserPasswordRequest;
import com.syed.code.requestsandresponses.user.UserMetadataResponse;
import com.syed.code.requestsandresponses.user.UserResponse;
import com.syed.code.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/create-user/", method = RequestMethod.POST)
    public ResponseEntity<BaseResponse> createUser(@RequestBody User user) throws PermissionMissingException {
        UserResponse response = userService.createUser(user);
        return new ResponseEntity<>(response, response.getStatusCode());
    }

    @RequestMapping(value = "/change-user-password/", method = RequestMethod.POST)
    public ResponseEntity<BaseResponse> changeUserPassword(@RequestBody ChangeUserPasswordRequest passwordRequest) throws PermissionMissingException {
        UserResponse response = userService.changeUserPassword(passwordRequest);
        return new ResponseEntity<>(response, response.getStatusCode());
    }

    @RequestMapping(value = "/get-user/{id}", method = RequestMethod.GET)
    public ResponseEntity<BaseResponse> getUser(@PathVariable("id") Long id) throws PermissionMissingException {
        UserResponse response = userService.getUser(id);
        return new ResponseEntity<>(response, response.getStatusCode());
    }

    @RequestMapping(value = "/get-user-metadata", method = RequestMethod.GET)
    public ResponseEntity<BaseResponse> getUserMetadata() {
        UserMetadataResponse response = userService.getUserMetadata();
        return new ResponseEntity<>(response, response.getStatusCode());
    }
}
