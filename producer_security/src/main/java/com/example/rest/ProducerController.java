package com.example.rest;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.security.core.userdetails.UserDetails;
import com.example.service.PersonCheckingService;

@RestController
public class ProducerController {

    private final PersonCheckingService personCheckingService;

    public ProducerController() {
        this.personCheckingService = new PersonCheckingService();
    }

    @PostMapping(path = "/check", produces = APPLICATION_JSON_UTF8_VALUE)
    public Response check(Authentication authentication) {
        // remove::start[]
        if (this.personCheckingService.shouldGetBeer(currentUserDetails(authentication))) {
            return new Response(BeerCheckStatus.OK);
        }
        return new Response(BeerCheckStatus.NOT_OK);
        // remove::end[return]
    }

    /**
     * <p>Method to receive current user details.</p>
     * 
     * @param authentication authentication token information
     * @return UserDetails
     */
    private UserDetails currentUserDetails(Authentication authentication) {
        if (authentication == null) {
            return null;
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        if (userDetails == null) {
            return null;
        }
        return userDetails;
    }

}
