package com.syed.code.controllers;

import com.syed.code.entities.role.Role;
import com.syed.code.exceptions.PermissionMissingException;
import com.syed.code.requestsandresponses.base.BaseResponse;
import com.syed.code.requestsandresponses.role.RoleMetadataResponse;
import com.syed.code.requestsandresponses.role.RoleResponse;
import com.syed.code.services.role.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @RequestMapping(value = "/create-role/", method = RequestMethod.POST)
    public ResponseEntity<BaseResponse> createRole(@RequestBody Role role) throws PermissionMissingException {
        RoleResponse response = roleService.createRole(role);
        return new ResponseEntity<>(response, response.getStatusCode());
    }

    @RequestMapping(value = "/get-role/{id}", method = RequestMethod.GET)
    public ResponseEntity<BaseResponse> getRole(@PathVariable("id") Long id) throws PermissionMissingException {
        RoleResponse response = roleService.getRole(id);
        return new ResponseEntity<>(response, response.getStatusCode());
    }

    @RequestMapping(value = "/get-role-metadata", method = RequestMethod.GET)
    public ResponseEntity<BaseResponse> getRoleMetadata() {
        RoleMetadataResponse response = roleService.getRoleMetadata();
        return new ResponseEntity<>(response, response.getStatusCode());
    }
}
