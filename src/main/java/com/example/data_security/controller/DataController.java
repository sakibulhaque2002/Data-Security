package com.example.data_security.controller;

import com.example.data_security.dto.QRDataDTO;
import com.example.data_security.util.CompressionUtil;
import com.example.data_security.util.JwtUtil;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/data")
public class DataController {

  private final JwtUtil jwtUtil;

  public DataController(JwtUtil jwtUtil) {
    this.jwtUtil = jwtUtil;
  }

  @PostMapping("/send")
  public ResponseEntity<Map<String, String>> sendData(@ModelAttribute QRDataDTO qrData) {

    Map<String, Object> claims = new HashMap<>();
    claims.put("data", qrData.getData());
    claims.put("foreground", qrData.getForeground());
    claims.put("background", qrData.getBackground());
    claims.put("scale", qrData.getScale());
    claims.put("shape", qrData.getShape());
    claims.put("shape_scale", qrData.getShape_scale());
    claims.put("error_level", qrData.getError_level());

    Map<String, String> response = new HashMap<>();

    // 1️⃣ Generate JWT
    String jwt = jwtUtil.generateToken(claims);
    response.put("jwt", jwt);

    // 2️⃣ Compress + Base64URL encode
    String base64Url = CompressionUtil.gzipAndBase64UrlEncode(jwt);
    response.put("payload", base64Url);

    return ResponseEntity.ok(response);
  }

}
