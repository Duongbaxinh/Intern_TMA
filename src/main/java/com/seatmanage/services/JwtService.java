package com.seatmanage.services;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.*;
import com.nimbusds.jwt.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.text.ParseException;
import java.time.Instant;
import java.util.*;

@Service
public class JwtService {

    @Value("${jwt.secretKey}")
    private String secretKey;

    private static final long EXPIRATION_TIME = 3600;

    public String generateToken(String username) throws JOSEException {
        Instant now = Instant.now();
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(username)
                .issuer("your-app")
                .issueTime(Date.from(now))
                .expirationTime(Date.from(now.plusSeconds(EXPIRATION_TIME)))
                .build();
        JWSSigner signer = new MACSigner(secretKey.getBytes());

        SignedJWT signedJWT = new SignedJWT( header,claimsSet );

        signedJWT.sign(signer);

        return signedJWT.serialize();
    }


    public boolean validateToken(String token) throws ParseException, JOSEException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        JWSVerifier verifier = new MACVerifier(secretKey);
        return signedJWT.verify(verifier) && isTokenExpired(signedJWT);
    }

    public String extractUsername(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return signedJWT.getJWTClaimsSet().getSubject();
        } catch (ParseException e) {
            return null;
        }
    }


    private boolean isTokenExpired(SignedJWT signedJWT) throws ParseException {
        Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        return expirationTime.before(new Date());
    }
}
