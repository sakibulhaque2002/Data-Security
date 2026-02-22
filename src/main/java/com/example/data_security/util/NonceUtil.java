package com.example.data_security.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Base64;

public final class NonceUtil {

  private static final String HMAC_ALGO = "HmacSHA256";
  private static final String SECRET_KEY = "super-secret-shared-key"; // use same as JWT secret
  private static final long DEFAULT_NONCE_LIFE_SECONDS = 24 * 60 * 60; // 24h

  private NonceUtil() {}

  /** Generate a new time-based nonce */
  public static String create() {
    long tick = getTick(DEFAULT_NONCE_LIFE_SECONDS);
    return generateNonce(tick);
  }

  /** Verify a nonce */
  public static boolean verify(String nonce) {
    long currentTick = getTick(DEFAULT_NONCE_LIFE_SECONDS);

    // current tick
    if (constantTimeEquals(nonce, generateNonce(currentTick))) return true;

    // previous tick (grace window)
    return constantTimeEquals(nonce, generateNonce(currentTick - 1));
  }

  private static String generateNonce(long tick) {
    try {
      String data = String.valueOf(tick);

      Mac mac = Mac.getInstance(HMAC_ALGO);
      mac.init(new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), HMAC_ALGO));

      byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));

      String encoded = Base64.getUrlEncoder().withoutPadding().encodeToString(hash);

      return encoded.substring(0, 12); // truncate for compactness
    } catch (Exception e) {
      throw new RuntimeException("Error generating nonce", e);
    }
  }

  private static long getTick(long lifeTimeSeconds) {
    long tickLength = lifeTimeSeconds / 2;
    long now = Instant.now().getEpochSecond();
    return now / tickLength;
  }

  private static boolean constantTimeEquals(String a, String b) {
    if (a == null || b == null) return false;
    return MessageDigest.isEqual(a.getBytes(StandardCharsets.UTF_8), b.getBytes(StandardCharsets.UTF_8));
  }
}