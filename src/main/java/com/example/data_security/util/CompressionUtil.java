package com.example.data_security.util;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.zip.GZIPOutputStream;

public class CompressionUtil {

  public static String gzipAndBase64UrlEncode(String input) {
    try {
      // 1️⃣ GZIP compress
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      GZIPOutputStream gzip = new GZIPOutputStream(bos);
      gzip.write(input.getBytes(StandardCharsets.UTF_8));
      gzip.close();

      byte[] compressedBytes = bos.toByteArray();

      // 2️⃣ Base64 URL-safe encode (NO padding)
      return Base64.getUrlEncoder()
          .withoutPadding()
          .encodeToString(compressedBytes);

    } catch (Exception e) {
      throw new RuntimeException("Compression failed", e);
    }
  }
}

