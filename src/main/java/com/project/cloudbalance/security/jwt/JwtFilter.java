package com.project.cloudbalance.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.cloudbalance.repository.BlacklistTokenRepository;
import com.project.cloudbalance.security.AuthService;
import com.project.cloudbalance.security.UserDetailsImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;
    private final AuthService authService;
    private final BlacklistTokenRepository blacklistTokenRepository;

    public JwtFilter(JwtUtils jwtUtils, @Lazy AuthService authService, BlacklistTokenRepository blacklistTokenRepository) {
        this.jwtUtils = jwtUtils;
        this.authService = authService;
        this.blacklistTokenRepository=blacklistTokenRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        logger.info("token "+ authHeader);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = authHeader.substring(7);
        try {
            if (blacklistTokenRepository.existsByToken(token)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                logger.info("Token has been Blacklisted Already");
                return;
            }
            String username = jwtUtils.extractUsername(token);
            String role = jwtUtils.extractRole(token);
            logger.info(role+username);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetailsImpl userDetails = (UserDetailsImpl) authService.loadUserByUsername(username);

                if (jwtUtils.validateToken(token, userDetails.getUsername())) {
                    Authentication auth = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    SecurityContextHolder.getContext().setAuthentication(auth);
                    logger.info(SecurityContextHolder.getContext());
                }
            }
        }
        catch (ExpiredJwtException | SignatureException e)
        {
            logger.info("Token Expired");
            ObjectMapper mapper = new ObjectMapper();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            Map<String, String> res = new HashMap<>();
            res.put("status", String.valueOf(HttpStatus.UNAUTHORIZED.value()));
            res.put("message", "Token Invalid");
            response.getWriter().write(mapper.writeValueAsString(res));
            return;
        }
        filterChain.doFilter(request, response);
    }

}
