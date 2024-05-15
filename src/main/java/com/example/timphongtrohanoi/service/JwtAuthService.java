package com.example.timphongtrohanoi.service;

import com.example.timphongtrohanoi.domain.common.Const;
import com.example.timphongtrohanoi.domain.entities.User;
import com.example.timphongtrohanoi.domain.model.request.IntrospectRequest;
import com.example.timphongtrohanoi.domain.model.request.SignInRequest;
import com.example.timphongtrohanoi.domain.model.response.IntrospectResponse;
import com.example.timphongtrohanoi.domain.model.response.SignInResponse;
import com.example.timphongtrohanoi.domain.queries.repository.UserRepository;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class JwtAuthService {
    UserRepository userRepository;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        String token = request.getToken();

        JWSVerifier verify = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        boolean verified = signedJWT.verify(verify);
        return IntrospectResponse.builder()
                .valid(verified && expiryTime.after(new Date()))
                .build();
    }

    public SignInResponse authenticate(SignInRequest request) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
        SignInResponse response = new SignInResponse();
        Optional<User> byUser = userRepository.findByUsername(request.getUsername());
        if (byUser.isEmpty()) {
            response.add("SYSTEM", Const.MessageCode.LOGIN_USER_NOT_EXISTED.getMessage());
            return response;
        }

        boolean isMatchPassword = passwordEncoder.matches(request.getPassword(), byUser.get().getPassword());
        if (!isMatchPassword) {
            response.add("SYSTEM", Const.MessageCode.NOT_AUTHENTICATION.getMessage());
            return response;
        }
        String token = generateToken(request.getUsername());

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
        JWSObject jwsObject  = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

}
