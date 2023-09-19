package com.example.websocketchat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FrontController {
    @GetMapping
    public String redirectToChatView(){
        return "redirect:chat/room";
    }
}
