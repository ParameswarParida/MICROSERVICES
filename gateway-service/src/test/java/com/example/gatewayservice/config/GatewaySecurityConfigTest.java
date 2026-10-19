package com.example.gatewayservice.config;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;

import reactor.test.StepVerifier;

class GatewaySecurityConfigTest {

    private static final byte[] SECRET_BYTES =
            "0123456789abcdef0123456789abcdef".getBytes(StandardCharsets.UTF_8);
    private ReactiveJwtDecoder jwtDecoder;

    @BeforeEach
    void setUp() {
        jwtDecoder = new GatewaySecurityConfig().jwtDecoder(Base64.getEncoder().encodeToString(SECRET_BYTES));
    }

    @Test
    void acceptsValidSignedToken() throws Exception {
        String token = createToken(
                "\"sub\":\"alice\",\"exp\":" + Instant.now().plusSeconds(60).getEpochSecond(),
                SECRET_BYTES);

        StepVerifier.create(jwtDecoder.decode(token))
                .assertNext(jwt -> assertEquals("alice", jwt.getSubject()))
                .verifyComplete();
    }

    @Test
    void rejectsExpiredToken() throws Exception {
        String token = createToken(
                "\"sub\":\"alice\",\"exp\":" + Instant.now().minusSeconds(60).getEpochSecond(),
                SECRET_BYTES);

        StepVerifier.create(jwtDecoder.decode(token))
                .expectError()
                .verify();
    }

    @Test
    void rejectsTokenWithoutExpiration() throws Exception {
        String token = createToken("\"sub\":\"alice\"", SECRET_BYTES);

        StepVerifier.create(jwtDecoder.decode(token))
                .expectError()
                .verify();
    }

    @Test
    void rejectsInvalidSignature() throws Exception {
        byte[] differentKey = "abcdef0123456789abcdef0123456789".getBytes(StandardCharsets.UTF_8);
        String token = createToken(
                "\"sub\":\"alice\",\"exp\":" + Instant.now().plusSeconds(60).getEpochSecond(),
                differentKey);

        StepVerifier.create(jwtDecoder.decode(token))
                .expectError()
                .verify();
    }

    private static String createToken(String claims, byte[] signingKey) throws Exception {
        Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();
        String header = encoder.encodeToString("{\"alg\":\"HS256\",\"typ\":\"JWT\"}"
                .getBytes(StandardCharsets.UTF_8));
        String payload = encoder.encodeToString(("{" + claims + "}").getBytes(StandardCharsets.UTF_8));
        String content = header + "." + payload;

        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(signingKey, "HmacSHA256"));
        return content + "." + encoder.encodeToString(mac.doFinal(content.getBytes(StandardCharsets.US_ASCII)));
    }
}
