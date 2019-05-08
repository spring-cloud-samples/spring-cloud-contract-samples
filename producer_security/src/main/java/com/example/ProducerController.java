package com.example;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.security.core.userdetails.UserDetails;

@RestController
public class ProducerController {

    private final PersonCheckingService personCheckingService;

    public ProducerController(PersonCheckingService personCheckingService) {
        this.personCheckingService = personCheckingService;
    }

    @RequestMapping(value = "/check", method = RequestMethod.POST, produces = "application/json")
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

class PersonCheckingService {

    Boolean shouldGetBeer(UserDetails userDetails) {
        return userDetails.getAge() >= 20;
    }
}

class Response {

    public BeerCheckStatus status;

    Response(BeerCheckStatus status) {
        this.status = status;
    }
}

enum BeerCheckStatus {
        OK, NOT_OK
}
