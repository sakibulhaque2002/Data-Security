package com.example.data_security.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QRDataDTO {
  private String template;
  private String data;
  private String foreground;
  private String background;
  private Integer scale;
  private String shape;
  private Double shape_scale;
  private String error_level;
  private MultipartFile logo_image;
  private Double logo_scale;
}