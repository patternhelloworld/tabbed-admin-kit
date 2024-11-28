package io.github.patternhelloworld.common.config.response.error.dto;

import io.github.patternhelloworld.common.config.response.TimestampUtil;
import io.github.patternhelloworld.common.util.CustomUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.ToString;

import java.util.Date;
import java.util.Map;

@ToString
public class CommonErrorResponsePayload {

	private Date timestamp;

	// Never to be returned to clients, but must be logged.
	//@JsonIgnore
	private String message;
	private String details;
	private String userMessage;
	private Map<String, String> userValidationMessage;

	@JsonIgnore
	private String stackTrace;
	@JsonIgnore
	private String cause;


	public CommonErrorResponsePayload(CommonErrorMessages commonErrorMessages, Exception e, String details, String stackTrace, String userMessage, Map<String, String> userValidationMessage) {
		this.timestamp = TimestampUtil.getPayloadTimestamp();
		this.message = !CustomUtils.isEmpty(commonErrorMessages.getMessage()) ? commonErrorMessages.getMessage() : e.getMessage() ;
		this.details = details;
		this.userMessage = !CustomUtils.isEmpty(commonErrorMessages.getUserMessage()) ? commonErrorMessages.getUserMessage() : userMessage;
		this.stackTrace = stackTrace;
		this.userValidationMessage = !CustomUtils.isEmpty(commonErrorMessages.getUserValidationMessage()) ? commonErrorMessages.getUserValidationMessage() : userValidationMessage;
	}

	public CommonErrorResponsePayload(String message, String details, String userMessage, String stackTrace) {
		this.timestamp = TimestampUtil.getPayloadTimestamp();
		this.message = message;
		this.details = details;
		this.userMessage = userMessage;
		this.stackTrace = stackTrace;
	}

	public CommonErrorResponsePayload(String message, String details, String userMessage, String stackTrace, String cause) {
		this.timestamp = TimestampUtil.getPayloadTimestamp();
		this.message = message;
		this.details = details;
		this.userMessage = userMessage;
		this.stackTrace = stackTrace;
		this.cause = cause;
	}

	public CommonErrorResponsePayload(String message, String details, String userMessage, Map<String, String> userValidationMessage,
									  String stackTrace, String cause) {

		this.timestamp = TimestampUtil.getPayloadTimestamp();
		this.message = message;
		this.details = details;
		this.userMessage = userMessage;
		this.userValidationMessage = userValidationMessage;
		this.stackTrace = stackTrace;
		this.cause = cause;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public String getMessage() {
		return message;
	}

	public String getDetails() {
		return details;
	}

	public String getUserMessage() {
		return userMessage;
	}

	public String getStackTrace() {
		return stackTrace;
	}

	public String getCause() {
		return cause;
	}

	public Map<String, String> getUserValidationMessage() {
		return userValidationMessage;
	}
}
