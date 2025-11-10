package com.todo.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtService {
    @Value("${spring.jwt.secret}")
    private String SECRET_KEY;
    @Value("${spring.jwt.expiration}")
    private long JWT_EXPIRATION;

    /** Extracts username from token and checks if it is a valid token**/
    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /** Extracts subject - username from the token**/
    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /** Generic method to extract a specific claim (user details - username, expiration)**/
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     *  This is mostly deprecated
     *  Parses the jwt token and retrieves all the claims (payload data - user info)
    private Claims extractAllClaims(String token) {
        return Jwts.parser() // Create a jwt parser
                .setSigningKey(getSignInKey())// Set sing in a key to verify signature
                .build()
                .parseClaimsJws(token) // Parses the token(jws) to jwt
                .getBody(); // Get body - payload
    }
    **/

    // Parses the jwt token and retrieves all the claims (payload data - user info)
    private Claims extractAllClaims(String token) {
        return Jwts.parser() // Create a jwt parser
                .verifyWith(getSignInKey())// Set sing in a key to verify signature
                .build()
                .parseSignedClaims(token) // Parses the token(jws) to jwt
                .getPayload(); // Get body - payload
    }

    /**
     * Generate a new JWT token
     * setClaims()... and other setting methods are deprecated, so instead of setClaims, we can use claims() method
     * also singWith(KEY, SignatureAlgorithm.HS256) is deprecated as well, instead it takes only takes KEY parameter - singWith(KEY)
     */
    @Override
    public String generateToken(Map<String, Object> claims, UserDetails userDetails) {
        return Jwts.builder()
                .claims(claims) // Claims are the additional information to the token
                .subject(userDetails.getUsername()) // Add a username, as the subject
                .issuedAt(new Date(System.currentTimeMillis())) // Token issue time
                .expiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION)) // Token expiration
                .signWith(getSignInKey()) //Sign in with given Sign In Key and decode with HS256
                .compact(); // Build and return the final JWT string
    }

    /**
     * Converts Base64 encoded secret key string into a cryptic Key object
     * This is a deprecated use
     * In the previous version, it does return Key class - object; but in the newer version, we need to use SecretKey to use it
     **/
    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY); // Decoding the secret to bytes
        return Keys.hmacShaKeyFor(keyBytes); // Creating a Key from bytes
    }
}
