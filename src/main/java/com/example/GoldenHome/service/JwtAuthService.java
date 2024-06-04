package com.example.GoldenHome.service;

import com.example.GoldenHome.components.common.Const;
import com.example.GoldenHome.components.common.MessageCode;
import com.example.GoldenHome.components.model.CustomUserDetails;
import com.example.GoldenHome.components.model.request.SignInRequest;
import com.example.GoldenHome.components.model.response.SignInResponse;
import com.example.GoldenHome.components.util.DateTimeUtils;
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
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class JwtAuthService {
    UserService userService;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    public int introspectAccessToken(String accessToken) {
        Date expiryTime;
        if (accessToken == null)
            return Const.JWTStatus.INVALID_AT.getCode();

        try {
            SignedJWT signedJWT = SignedJWT.parse(accessToken);
            expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
            JWSVerifier verify = new MACVerifier(SIGNER_KEY.getBytes());
            boolean tokenVerified = signedJWT.verify(verify);
            if (!tokenVerified) return Const.JWTStatus.ACCEPT_AT.getCode();
            boolean expiryTimeVerified = expiryTime.after(new Date());
            if (!expiryTimeVerified) return Const.JWTStatus.OUT_EXPIRE_AT.getCode();

            return Const.JWTStatus.ACCEPT_AT.getCode();
        } catch (ParseException | JOSEException e) {
            return Const.JWTStatus.INVALID_AT.getCode();
        }

    }

    public int verifyRefreshToken(String refreshToken) {
        if (refreshToken == null) return Const.JWTStatus.INVALID_RT.getCode();
        SignedJWT signedJWT;
        try {
            signedJWT = SignedJWT.parse(refreshToken);
            Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
            if (expiryTime.before(DateTimeUtils.now())) return Const.JWTStatus.INVALID_RT.getCode();

            String username = signedJWT.getJWTClaimsSet().getSubject();
            CustomUserDetails customUserDetails = userService.loadUserByUsername(username);
            String key = SIGNER_KEY + customUserDetails.getPassword();
            JWSVerifier jwsVerifier = new MACVerifier(key.getBytes());
            boolean verifierToken = signedJWT.verify(jwsVerifier);

            if (!verifierToken) return Const.JWTStatus.INVALID_RT.getCode();

            Date nextDay = DateTimeUtils.getTimeAfterStartOfDate(1);
            if (nextDay.after(expiryTime)) return Const.JWTStatus.NEED_RENEW_RT.getCode();

            return Const.JWTStatus.VALID_RT.getCode();
        } catch (ParseException | JOSEException e) {
            return Const.JWTStatus.INVALID_RT.getCode();
        }
    }

    public boolean verifyRefreshToken(String refreshToken, String accessToken) {
        int statusAT = introspectAccessToken(accessToken);
        if (statusAT == Const.JWTStatus.ACCEPT_AT.getCode()) return true;
        int statusRT = verifyRefreshToken(refreshToken);

        return statusRT != Const.JWTStatus.INVALID_RT.getCode();
    }

    public String extractUsername(String accessToken, String refreshToken) throws ParseException {
        SignedJWT signedJWT;
        if (accessToken != null)
            signedJWT = SignedJWT.parse(accessToken);
        else {
            signedJWT = SignedJWT.parse(refreshToken);
        }
        return signedJWT.getJWTClaimsSet().getSubject();
    }

    public SignInResponse authenticate(SignInRequest signInRequest) {
        SignInResponse response = doValidate(signInRequest);

        String accessToken = generateAccessToken(signInRequest.getUsername());
        response.setAccessToken(accessToken);
        if (signInRequest.isRemember()) {
            signInRequest.setPassword(response.getHashPsw());
            String refreshToken = generateRefreshToken(signInRequest);
            response.setRefreshToken(refreshToken);
        }
        response.setStatus(HttpStatus.ACCEPTED);
        return response;

    }

    private SignInResponse doValidate(SignInRequest signInRequest) {
        SignInResponse response = new SignInResponse();
        CustomUserDetails userDetails;
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

        try {
            userDetails = userService.loadUserByUsername(signInRequest.getUsername());
        } catch (UsernameNotFoundException usernameNotFoundException) {
            response.add("result", MessageCode.UN_AUTHENTICATION_USERNAME_OR_PASSWORD);
            response.setStatus(HttpStatus.UNAUTHORIZED);
            return response;
        }

        boolean isMatchPassword = passwordEncoder.matches(signInRequest.getPassword(), userDetails.getPassword());
        if (!isMatchPassword) {
            response.add("result", MessageCode.UN_AUTHENTICATION_USERNAME_OR_PASSWORD);
            response.setStatus(HttpStatus.UNAUTHORIZED);
            return response;
        }

        response.setHashPsw(userDetails.getPassword());
        return response;
    }

    public String generateAccessToken(String username) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(username)
                .jwtID(UUID.randomUUID().toString())
                .issuer("golden-home")
                .expirationTime(new Date(
                        Instant.now().plus(5, ChronoUnit.MINUTES).toEpochMilli()
                ))
                .claim("ROLE", "RENTAL")
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

    private String generateRefreshToken(SignInRequest signInRequest) {
        return generateRefreshToken(signInRequest.getUsername(), signInRequest.getPassword());
    }

    public String generateRefreshToken(String username, String password) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);

        Date expiration = DateTimeUtils.getTimeAfterStartOfDate(2);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(username)
                .jwtID(UUID.randomUUID().toString())
                .expirationTime(expiration)
                .claim("scopes", "REFRESH_TOKEN")
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            String key = SIGNER_KEY + password;
            jwsObject.sign(new MACSigner(key.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }
}
