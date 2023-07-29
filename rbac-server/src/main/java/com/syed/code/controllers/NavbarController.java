package com.syed.code.controllers;

import com.syed.code.requestsandresponses.base.BaseResponse;
import com.syed.code.requestsandresponses.navbar.NavbarResponse;
import com.syed.code.services.navbar.NavbarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/navbar")
public class NavbarController {

    @Autowired
    private NavbarService navbarService;

    @RequestMapping(value = "/get-navbar-info", method = RequestMethod.GET)
    public ResponseEntity<BaseResponse> getNavbarInfo() {
        NavbarResponse response = navbarService.getNavbarInfo();
        return new ResponseEntity<>(response, response.getStatusCode());
    }
}
