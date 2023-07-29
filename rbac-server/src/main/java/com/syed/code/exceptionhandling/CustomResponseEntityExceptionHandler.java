package com.syed.code.exceptionhandling;

import com.syed.code.enums.EntityEnums;
import com.syed.code.enums.codes.ErrorCodes;
import com.syed.code.exceptions.PermissionMissingException;
import com.syed.code.requestsandresponses.base.BaseResponse;
import com.syed.code.requestsandresponses.base.MessageVariable;
import com.syed.code.requestsandresponses.exception.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({PermissionMissingException.class})
    public ResponseEntity<BaseResponse> handlePermissionMissingError(PermissionMissingException e, WebRequest webRequest) {
        ExceptionResponse response = new ExceptionResponse();
        MessageVariable messageVariable = getMessageVariable(e.getEntity());
        response.getMessageVariables().add(messageVariable);
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(response, response.getStatusCode());
    }

    private MessageVariable getMessageVariable(EntityEnums.Entity entity) {
        MessageVariable messageVariable = new MessageVariable();
        messageVariable.setEntity(entity.entityName);
        messageVariable.setEntityId(entity.entityId);
        messageVariable.setApplicationCode(ErrorCodes.Generic.E00_002.code);
        return messageVariable;
    }
}
