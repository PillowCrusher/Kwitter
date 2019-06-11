package Controller.Auth;


import Entity.Role;
import Entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@SuppressWarnings("Duplicates")
@AdminSecured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AdminFilter implements ContainerRequestFilter {
    private static final String SECRET_KEY = "BakeMonogatari";

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        // Get the HTTP Authorization header from the request
        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        if(authorizationHeader == null){
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            return;
        }
        if(authorizationHeader.equals(""))
        {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            return;
        }
        // Extract the token from the HTTP Authorization header
        String token = authorizationHeader.substring("Bearer".length()).trim();
        if(token.equals("")){
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }
        try {

            // Validate the token
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            Claims claims =  Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
            ObjectMapper deserializer = new ObjectMapper();
            User user = deserializer.readValue(claims.getSubject(), User.class);
            boolean allowed = false;
            for (Role role : user.getAccount().getRoles()){
                if(role.getRolename().equals("admin")){
                    allowed = true;
                    break;
                }
            }

            if(!allowed){
                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            }

            System.out.println("#### valid token : " + token);

        } catch (Exception e) {
            System.out.println("#### invalid token : " + token);
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }
}
