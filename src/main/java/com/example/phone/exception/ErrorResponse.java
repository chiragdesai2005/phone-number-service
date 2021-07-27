package com.example.phone.exception;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ErrorResponse {

  private Integer status;
  private String error;
  private String message;
  private String requestId; // Use sleuth trace id
  private String path;
}
