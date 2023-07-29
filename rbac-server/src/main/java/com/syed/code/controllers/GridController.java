package com.syed.code.controllers;

import com.syed.code.exceptions.PermissionMissingException;
import com.syed.code.requestsandresponses.base.BaseResponse;
import com.syed.code.requestsandresponses.grid.GridDataResponse;
import com.syed.code.requestsandresponses.grid.GridMetadataResponse;
import com.syed.code.requestsandresponses.grid.GridRequest;
import com.syed.code.services.grid.GridService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/grid")
public class GridController {

    @Autowired
    private GridService gridService;

    @RequestMapping(value = "/get-grid-data", method = RequestMethod.POST)
    public ResponseEntity<BaseResponse> getGridData(@RequestBody GridRequest gridRequest) throws PermissionMissingException {
        GridDataResponse response = gridService.getGridData(gridRequest);
        return new ResponseEntity<>(response, response.getStatusCode());
    }

    @RequestMapping(value = "/get-grid-metadata/{id}", method = RequestMethod.GET)
    public ResponseEntity<BaseResponse> getGridMetadata(@PathVariable("id") Integer id) {
        GridMetadataResponse response = gridService.getGridMetadata(id);
        return new ResponseEntity<>(response, response.getStatusCode());
    }

    @RequestMapping(value = "/delete-grid-entries", method = RequestMethod.POST)
    public ResponseEntity<BaseResponse> deleteGridEntries() {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
