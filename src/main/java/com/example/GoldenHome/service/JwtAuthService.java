package com.example.GoldenHome.service;

import com.example.GoldenHome.domain.common.Const;
import com.example.GoldenHome.domain.model.CustomUserDetails;
import com.example.GoldenHome.domain.model.request.SignInRequest;
import com.example.GoldenHome.domain.model.response.SignInResponse;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Objects;

@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class JwtAuthService {
    UserService userService;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    public boolean introspect(String token, CustomUserDetails userDetails) throws JOSEException, ParseException {
        JWSVerifier verify = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);
        final String username = signedJWT.getJWTClaimsSet().getSubject();

        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        boolean tokenVerified = signedJWT.verify(verify);
        boolean usernameVerified = Objects.equals(username, userDetails.getUsername());
        boolean expiryTimeVerified = expiryTime.after(new Date());
        return tokenVerified && usernameVerified && expiryTimeVerified;
    }

    public String extractUsername(String token) throws ParseException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        return signedJWT.getJWTClaimsSet().getSubject();
    }

    public SignInResponse authenticate(SignInRequest signInRequest) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
        SignInResponse response = new SignInResponse();
        CustomUserDetails userDetails;
        try {
            userDetails = userService.loadUserByUsername(signInRequest.getUsername());
        } catch (UsernameNotFoundException usernameNotFoundException) {
            response.add("SYSTEM", Const.MessageCode.LOGIN_USER_NOT_EXISTED.getMessage());
            response.setStatus(404);
            return response;
        }


        boolean isMatchPassword = passwordEncoder.matches(signInRequest.getPassword(), userDetails.getPassword());
        if (!isMatchPassword) {
            response.add("SYSTEM", Const.MessageCode.NOT_AUTHENTICATION.getMessage());
            response.setStatus(401);
            return response;
        }
        String token = generateToken(signInRequest.getUsername());
        response.setStatus(202);
        response.setToken(token);
        return response;
    }


    private String generateToken(String username) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(username)
                .issuer("golden-home")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                ))
                .claim("customClaim", "claim")
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }
}
