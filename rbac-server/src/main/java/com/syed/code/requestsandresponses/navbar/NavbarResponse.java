package com.syed.code.requestsandresponses.navbar;

import com.syed.code.entities.navbar.Navbar;
import com.syed.code.requestsandresponses.base.BaseResponse;
import lombok.Data;

import java.util.List;

@Data
public class NavbarResponse extends BaseResponse {

    List<Navbar> navbars;
}
