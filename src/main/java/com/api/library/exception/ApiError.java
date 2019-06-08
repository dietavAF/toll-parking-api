package com.api.library.exception;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "Details about the error thrown by the API")
public class ApiError {
	
	@ApiModelProperty(notes = "The code of the error")
	private String code;
	
	@ApiModelProperty(notes = "The messages linked to the error code")
	private String message;
	
	public ApiError() {
		// default constructor
	}

	public String getMessage() {
		return message;
	}

	public String getCode() {
		return code;
	}

	public ApiError(String message, String code) {
		this.message = message;
		this.code = code;
	}
}
