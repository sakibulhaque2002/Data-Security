package com.example.data_security.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;


import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Map;

@Component
public class CustomJWTUtil {

  // Shared symmetric key between Spring Boot and FastAPI
  private static final String SECRET_KEY = "super-secret-shared-key"; // store securely

  private static final String HMAC_ALGO = "HmacSHA256";
  private final ObjectMapper objectMapper = new ObjectMapper();

  /**
   * Generate custom token: payload + "." + signature
   */
  public String generateToken(Map<String, Object> claims){

    // 1. Add a nonce automatically
    String nonce = NonceUtil.create();
    claims.put("nonce", nonce);

    // 2. Convert claims to JSON
    String jsonPayload = objectMapper.writeValueAsString(claims);

    // 3. Base64 encode payload
    String payload = Base64.getUrlEncoder().withoutPadding().encodeToString(jsonPayload.getBytes());

    // 4. Sign payload with HMAC
    String signature = signData(payload);

    // 5. Return JWT
    return payload + "." + signature;
  }

  /**
   * Generate HMAC-SHA256 signature from payload
   */
  private String signData(String payload) {

    try {
      Mac mac = Mac.getInstance(HMAC_ALGO);
      SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(), HMAC_ALGO);
      mac.init(keySpec);

      byte[] sigBytes = mac.doFinal(payload.getBytes());

      return Base64.getUrlEncoder()
          .withoutPadding()
          .encodeToString(sigBytes)
          .substring(0, 16); // Truncate to 16 chars for compactness
    }

    catch (Exception e) {
      throw new RuntimeException("Failed to sign payload", e);
    }
  }
}
