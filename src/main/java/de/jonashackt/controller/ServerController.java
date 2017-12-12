package de.jonashackt.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/restexamples")
public class ServerController {

    public static final String RESPONSE = "Hello Rest-User!";

    @RequestMapping(path="/hello", method=RequestMethod.GET)
    public String helloWorld() {
        System.out.println("Rocking REST!");
    	return RESPONSE;
    }
}
