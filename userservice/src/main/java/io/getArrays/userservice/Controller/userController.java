package io.getArrays.userservice.Controller;

import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.getArrays.userservice.Models.User;
import io.getArrays.userservice.Models.Role;
import io.getArrays.userservice.Service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor

@Slf4j
public class userController {
 private final UserService userService;

 @GetMapping("/users")
 public ResponseEntity<List<User>> getUsers(){
     try {

         return ResponseEntity.ok().body(userService.getUsers());
     } catch (Exception e) {
         throw e;
     }
 }

 @PostMapping("/user/save")
 public ResponseEntity<User> saveUser(@RequestBody User user){
   try {

      URI uri =  URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user").toUriString());
      return ResponseEntity.created(uri).body(userService.saveUser(user));

   } catch (Exception e) {
    throw e;
  }
 }


 @PostMapping("/role/save")
 public ResponseEntity<Role> saveRole(@RequestBody Role role){
   try {

       URI uri =  URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/role").toUriString());
       return ResponseEntity.created(uri).body(userService.saveRole(role));

   } catch (Exception e) {
     throw e;
   }
 }

 
 @PostMapping("/role/addtouser")
 public ResponseEntity<?> addRoleToUser(@RequestBody RoleToUserForm form){
   try {
       userService.addRoleToUser(form.getUserName(), form.getRoleName());
       return ResponseEntity.ok().build();

   } catch (Exception e) {
     throw e;
   }
 }

 @GetMapping("/token/refresh")
 public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
     String authorizationHeader = request.getHeader(AUTHORIZATION);
     if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
         try {
             String refreshToken   = authorizationHeader.substring("Bearer ".length());
             Algorithm algorithm   = Algorithm.HMAC256("secret".getBytes());
             JWTVerifier verifier  = JWT.require(algorithm).build();
             DecodedJWT decodedJWT = verifier.verify(refreshToken);

             String username = decodedJWT.getSubject();

             User user = userService.getUser(username);

             String accessToken = JWT.create()
                     .withSubject(user.getUsername())
                     .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                     .withIssuer(request.getRequestURL().toString())
                     .withClaim("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                     .sign(algorithm);

             Map<String, String> tokens = new HashMap<>();
             tokens.put("access_token",  accessToken);
             tokens.put("refresh_token", refreshToken);
             response.setContentType(APPLICATION_JSON_VALUE);
             new ObjectMapper().writeValue(response.getOutputStream(), tokens);

         }catch(Exception exception){
             log.error("Error logging in: {}", exception.getMessage());
             response.setHeader("error", exception.getMessage());
             response.setStatus(FORBIDDEN.value());
             //response.sendError(FORBIDDEN.value())

             Map<String, String> error = new HashMap<>();
             error.put("error_message", exception.getMessage());
             response.setContentType(APPLICATION_JSON_VALUE);
             new ObjectMapper().writeValue(response.getOutputStream(), error);
         }
     }else{
         throw new RuntimeException("Refresh Token Missing");
     }

 }

}



 @Data
 class RoleToUserForm {
  private String userName;
  private String roleName;
 }
