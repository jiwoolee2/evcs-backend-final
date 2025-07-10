package com.example.evcs.common.response;

import org.springframework.stereotype.Component;

import com.example.evcs.common.model.dto.ResponseData;


@Component
public class ResponseUtil {
	
	public ResponseData getResponseData(Object data, String message, String code) {
		ResponseData result = ResponseData.builder()
										  .data(data)
										  .message(message)
										  .code(code)
										  .build();
		return result;
	}
	
	
	public ResponseData getResponseData(String message, String code) {
		ResponseData result = ResponseData.builder()
										  .message(message)
										  .code(code)
										  .build();
		return result;
	}
}
