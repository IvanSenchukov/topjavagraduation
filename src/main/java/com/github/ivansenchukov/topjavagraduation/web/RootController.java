package com.github.ivansenchukov.topjavagraduation.web;

import com.github.ivansenchukov.topjavagraduation.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServlet;

@Controller
public class RootController extends HttpServlet {

    final RestaurantService restaurantService;

    @Autowired
    public RootController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @RequestMapping(path = "/")
    public String root() {
        return "Congrats!";
    }
}
