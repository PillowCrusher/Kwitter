package Controller.Auth;

import Entity.Account;
import Entity.User;
import Service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.security.Key;
import java.util.Date;


@Path("/authentication")
public class AuthenticationEndpoint {
    private static final String SECRET_KEY = "BakeMonogatari";
    @Inject
    UserService userService;


    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response authenticateUser(String json){
        ObjectMapper objectMapper = new ObjectMapper();
        Account account;
        try {
             account = objectMapper.readValue(json,Account.class);
        } catch (IOException e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
       User user = userService.authenticate(account);
       if(user==null) {
           return Response.status(Response.Status.FORBIDDEN).build();
       }
        try{
            String token = createJWT("KwitterID","KwitterBackend",user);
            user.getAccount().setToken(token);
            return Response.ok(user).build();
        }catch (Exception e){
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    public static String createJWT(String id, String issuer, User subject) throws JsonProcessingException {
        ObjectMapper obj = new ObjectMapper();

        //The JWT signature algorithm we will be using to sign the token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //We will sign our JWT with our ApiKey secret
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        String a=obj.writeValueAsString(subject);
        //Let's set the JWT Claims
        JwtBuilder builder = Jwts.builder().setId(id)
                .setIssuedAt(now)
                .setSubject(a)
                .setIssuer(issuer)
                .signWith(signatureAlgorithm, signingKey);


        //Builds the JWT and serializes it to a compact, URL-safe string
        return builder.compact();
    }

}
