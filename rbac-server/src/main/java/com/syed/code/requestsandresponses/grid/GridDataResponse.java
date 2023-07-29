package com.syed.code.requestsandresponses.grid;

import com.syed.code.requestsandresponses.base.BaseResponse;
import lombok.Data;

import java.util.List;

@Data
public class GridDataResponse extends BaseResponse {

    List<Object> gridData;
}
