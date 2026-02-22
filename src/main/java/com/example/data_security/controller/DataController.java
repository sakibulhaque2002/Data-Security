package com.example.data_security.controller;

import com.example.data_security.dto.QRDataDTO;
import com.example.data_security.util.CompressionUtil;
import com.example.data_security.util.JwtUtil;
import com.example.data_security.util.CustomJWTUtil;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/data")
public class DataController {

  private final CustomJWTUtil customJwtUtil;

  public DataController(CustomJWTUtil customJwtUtil) {
    this.customJwtUtil = customJwtUtil;
  }

  @PostMapping("/send")
  public ResponseEntity<Map<String, String>> sendData(@ModelAttribute QRDataDTO qrData) throws IOException {

    Map<String, Object> claims = new HashMap<>();
    claims.put("template", qrData.getTemplate());
    claims.put("data", qrData.getData());
    claims.put("foreground", qrData.getForeground());
    claims.put("background", qrData.getBackground());
    claims.put("scale", qrData.getScale());
    claims.put("shape", qrData.getShape());
    claims.put("shape_scale", qrData.getShape_scale());
    claims.put("error_level", qrData.getError_level());

    // ✅ Process logo image
    MultipartFile logo = qrData.getLogo_image();
    if (logo != null && !logo.isEmpty()) {
      String logoBase64 = Base64.getEncoder().encodeToString(logo.getBytes());
      claims.put("logo_image", logoBase64);
      claims.put("logo_scale", qrData.getLogo_scale());
    }

    Map<String, String> response = new HashMap<>();
    String customJwt = customJwtUtil.generateToken(claims);
    response.put("customJwt", customJwt);

    return ResponseEntity.ok(response);
  }

}
