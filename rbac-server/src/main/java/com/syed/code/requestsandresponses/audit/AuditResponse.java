package com.syed.code.requestsandresponses.audit;

import com.syed.code.entities.audit.AuditDetails;
import com.syed.code.requestsandresponses.base.BaseResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class AuditResponse extends BaseResponse {

    AuditDetails auditDetails;
}
