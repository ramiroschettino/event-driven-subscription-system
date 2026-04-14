package com.rappi.payment.infrastructure.adapter.out.rest;

import com.rappi.payment.domain.port.out.UserValidationPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class MembershipUserValidationAdapter implements UserValidationPort {

    private static final Logger log = LoggerFactory.getLogger(MembershipUserValidationAdapter.class);
    private final RestTemplate restTemplate;
    private final String membershipApiUrl;

    public MembershipUserValidationAdapter(RestTemplate restTemplate, 
                                           @Value("${membership-api.url:http://localhost:8080}") String membershipApiUrl) {
        this.restTemplate = restTemplate;
        this.membershipApiUrl = membershipApiUrl;
    }

    @Override
    public boolean userExists(String userId) {
        try {
            String url = membershipApiUrl + "/api/users/" + userId;
            restTemplate.getForEntity(url, Void.class);
            return true;
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                log.warn("User {} not found in Membership API", userId);
                return false;
            }
            log.error("HTTP error validating user", e);
            throw e;
        } catch (Exception e) {
            log.error("Unknown error communicating with Membership API", e);
            throw new RuntimeException("Could not validate user existence", e);
        }
    }
}
