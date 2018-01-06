package com.example.backend.utils;

import com.example.backend.controllersEntities.responses.ResponseWithCode;

import static com.example.utils.ResponseCode.NOT_AUTHENTICATED;

public class ResponseUtils {

  public static ResponseWithCode prepareNotAuthenticatedResponse() {
    return new ResponseWithCode() {
      @Override
      public int getResponseCode() {
        return NOT_AUTHENTICATED;
      }
    };
  }
}
