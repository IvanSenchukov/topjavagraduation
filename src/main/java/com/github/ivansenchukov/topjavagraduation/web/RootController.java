package com.github.ivansenchukov.topjavagraduation.web;

import com.github.ivansenchukov.topjavagraduation.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServlet;

@Controller
public class RootController extends HttpServlet {

    final RestaurantService restaurantService;

    @Autowired
    public RootController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @RequestMapping(path = "/")
    public ModelAndView root() {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index.jsp");

        return modelAndView;
    }
}
