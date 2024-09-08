package com.autofocus.pms.common.config.response.error.dto;

import lombok.*;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ErrorMessages {

	// Never to be returned to clients, but must be logged.
	// @JsonIgnore
	private String message;
	private String userMessage;
	private Map<String, String> userValidationMessage;

	// Spring Security
	private UserDetails userDetails;

	public String getMessage() {
		return message;
	}

	public String getUserMessage() {
		return userMessage;
	}

	public Map<String, String> getUserValidationMessage() {
		return userValidationMessage;
	}

	public UserDetails getUserDetails() {
		return userDetails;
	}
}
