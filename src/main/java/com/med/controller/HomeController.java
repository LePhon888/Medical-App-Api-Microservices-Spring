package com.med.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.logging.Logger;

@RestController
@RequestMapping("/home")
public class HomeController {

   @GetMapping("/users")
    public String getUser() {
       System.out.println("aaa");
       return "Users";
   }

   @GetMapping("/current-user")
    public String getLoggedInUser(Principal p) {
       return p.getName();
   }


}