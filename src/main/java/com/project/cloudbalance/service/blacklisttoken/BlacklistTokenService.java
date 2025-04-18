package com.project.cloudbalance.service.blacklisttoken;

import com.project.cloudbalance.exception.customException.BlackListedTokenException;
import com.project.cloudbalance.jwt.BlacklistToken;
import com.project.cloudbalance.repository.BlacklistTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BlacklistTokenService {

    @Autowired
    private BlacklistTokenRepository blacklistTokenRepository;

    public ResponseEntity<String> blacklistToken(HttpServletRequest request) {
        try {
            String authHeader = request.getHeader("Authorization");

            if (authHeader != null && authHeader.startsWith("Bearer "))
            {
                String token = authHeader.substring(7);
                if (blacklistTokenRepository.existsByToken(token))
                {
                    throw new BlackListedTokenException("Token has already been blacklisted.");
                }
                BlacklistToken blacklistedToken = new BlacklistToken();
                blacklistedToken.setToken(token);
                blacklistTokenRepository.save(blacklistedToken);
                log.info("Token blacklisted successfully");
                return ResponseEntity.ok().body("Token blacklisted successfully");
            }
            else
            {
                throw new BlackListedTokenException("Invalid token format.");
            }

        } catch (Exception e) {

            throw new BlackListedTokenException("Error processing the token for blacklisting: " + e.getMessage());
        }
    }
}
