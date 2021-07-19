package com.example.security;

import com.example.SpringApplicationContext;
import com.example.shared.dto.UserDto;
import com.example.ui.model.request.UserLoginRequestModel;
import com.example.ws.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

//extend Filter to be able to be used in Websecurity configure function
//extend Filter we want to use
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;//Interface with one Method
    //Authentication authenticate(Authentication authentication) throws AuthenticationException;

    public AuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            UserLoginRequestModel creds = new ObjectMapper()
                    .readValue(request.getInputStream(),UserLoginRequestModel.class); //convert request information to java object
            return authenticationManager.authenticate( //takes findByEmail-Method(UserRepository) to find out if data in creds match UserEntity
                    new UsernamePasswordAuthenticationToken(
                    creds.getEmail(),
                    creds.getPassword(),
                    new ArrayList<>()) // collection of authorities
            );
        }catch (IOException e){
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        String username = ((User)authResult.getPrincipal()).getUsername();
        //Json Webtoken is used in AuthorizationFilter
        //when created, it is included in the header
        //request will only be authorized if header has the right Webtoken
        String token = Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.TOKEN_SECRET)
                .compact();


        UserService userService =(UserService)SpringApplicationContext.getBean("userServiceImplementation");
        UserDto userDto = userService.getUser(username);
        response.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token);
        //JWT is added to header as soon as user is authorized via login
        response.addHeader("UserID", userDto.getUserId());
    }


}
