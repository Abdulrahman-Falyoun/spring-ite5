package com.ite5year.authentication.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ite5year.models.ApplicationUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.http.codec.cbor.Jackson2CborEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import io.jsonwebtoken.security.Keys;

import static com.ite5year.authentication.constants.SecurityConstants.*;
import static com.ite5year.utils.GlobalConstants.LOGIN_URL;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    final private AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        setFilterProcessesUrl(LOGIN_URL);
    }

    @Override
    public org.springframework.security.core.Authentication attemptAuthentication(HttpServletRequest req,
                                                                                  HttpServletResponse res) throws AuthenticationException {
        try {
            ApplicationUser applicationUser = new ObjectMapper().readValue(req.getInputStream(), ApplicationUser.class);

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(applicationUser.getUsername(),
                            applicationUser.getPassword(), new ArrayList<>())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
                                            org.springframework.security.core.Authentication auth) throws IOException {

        Date exp = new Date(System.currentTimeMillis() + EXPIRATION_TIME);
        Key key = Keys.hmacShaKeyFor(KEY.getBytes());
        User user = (User) auth.getPrincipal();
        String userName = user.getUsername();
        Claims claims = Jwts.claims().setSubject(userName);
        String token = Jwts.builder().setClaims(claims).signWith(key).setExpiration(exp).compact();
        res.addHeader("token", JWT_TOKEN_PREFIX + " " + token);


        HashMap<String, Object> reply = new HashMap<>();
        reply.put("token", token);
        reply.put("user", user);
        ObjectMapper mapper = new ObjectMapper();
        String clientFilterJson = "";
        try {
            clientFilterJson = mapper.writeValueAsString(reply);
        } catch (IOException e) {
            e.printStackTrace();
        }
        res.getWriter().write(clientFilterJson);
        res.getWriter().flush();
    }
}