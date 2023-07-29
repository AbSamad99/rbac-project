package com.syed.code.controllers;

import com.syed.code.exceptions.PermissionMissingException;
import com.syed.code.requestsandresponses.audit.AuditResponse;
import com.syed.code.requestsandresponses.base.BaseResponse;
import com.syed.code.services.audit.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/audit")
public class AuditController {

    @Autowired
    private AuditService auditService;

    @RequestMapping(value = "/get-audit-details/{id}", method = RequestMethod.GET)
    public ResponseEntity<BaseResponse> getAuditDetails(@PathVariable("id") Long id) throws PermissionMissingException {
        AuditResponse response = auditService.getAuditDetails(id);
        return new ResponseEntity<>(response, response.getStatusCode());
    }
}
