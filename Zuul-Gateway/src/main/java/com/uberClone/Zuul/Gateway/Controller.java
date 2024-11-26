package com.uberClone.Zuul.Gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class Controller {



    @GetMapping("/test")
    public String testEndPoint()
    {
        System.out.println("Test endpoint hit!");
        return "Hello!!";
    }


   /* @GetMapping("/routes")
    public Map<String, String> getRoutes() {
        Map<String, String> routes = new HashMap<>();
        routeLocator.getRoutes().forEach(route -> {
            routes.put(route.getPath(), route.getLocation());
        });
        return routes;
    }*/

}
