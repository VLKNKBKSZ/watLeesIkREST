package nl.watleesik.controller;

import nl.watleesik.api.ApiResponse;

public interface IApiResponse {

	default public <T> ApiResponse<T> createResponse(int statusCode, String message, T object) {
		return new ApiResponse<T>(statusCode, message, object);
	}
	
	default public <T> ApiResponse<T> createResponse(int statusCode, String message) {
		return createResponse(statusCode, message, null);
	}
}
