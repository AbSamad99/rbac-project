package com.syed.code.services.navbar;

import com.syed.code.entities.navbar.Navbar;
import com.syed.code.entities.navbar.NavbarItem;
import com.syed.code.entities.user.CustomUserDetails;
import com.syed.code.repositories.NavbarRepository;
import com.syed.code.requestsandresponses.navbar.NavbarResponse;
import com.syed.code.services.loggedinuser.LoggedInUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NavbarServiceImpl implements NavbarService {

    @Autowired
    private LoggedInUserService loggedInUserService;

    @Autowired
    private NavbarRepository navbarRepository;


    @Override
    public NavbarResponse getNavbarInfo() {
        NavbarResponse response = new NavbarResponse();
        List<Navbar> navbars = new ArrayList<>();
        Map<Long, Navbar> navbarMap = new HashMap<>();
        CustomUserDetails userDetails = loggedInUserService.getLoggedInUser();
        List<List<Object>> objects = navbarRepository.getNavbarsByUserId(userDetails.getId());

        for (List<Object> list : objects) {
            Navbar navbar = (Navbar) list.get(0);
            NavbarItem navbarItem = (NavbarItem) list.get(1);
            if (!navbarMap.containsKey(navbar.getId()))
                navbarMap.put(navbar.getId(), navbar);
            navbarMap.get(navbar.getId()).getNavbarItems().add(navbarItem);
        }
        System.out.println(navbarMap);
        System.out.println();

        for (Navbar navbar : navbarMap.values())
            navbars.add(navbar);
        System.out.println(navbars);

        response.setNavbars(navbars);
        response.setStatusCode(HttpStatus.OK);
        return response;
    }
}
