package com.autofocus.pms.hq.config.logger.module;

import com.autofocus.pms.common.config.logger.common.CommonLoggingRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/*
 *   resources/logback-spring.xml 전달 받아서 확인. (양식은 resources/logback-spring.xml.sample 참조)
 * */
@Slf4j
public class RestTemplateClientErrorLogConfig implements ResponseErrorHandler {

  private static final Logger logger = LoggerFactory.getLogger(RestTemplateClientLogConfig.class);

  @Override
  public boolean hasError(@NonNull final ClientHttpResponse response) throws IOException {
    final HttpStatusCode statusCode = response.getStatusCode();
//    response.getBody() 넘겨 받은 body 값으로 적절한 예외 상태 확인 이후 boolean return
    return !statusCode.is2xxSuccessful();
  }

  @Override
  public void handleError(@NonNull final ClientHttpResponse response) throws IOException {

    CommonLoggingRequest commonLoggingRequest = new CommonLoggingRequest();
    String loggedText = commonLoggingRequest.getText();

    final String error = getErrorAsString(response);

    loggedText += "\n======Error Response from Server====== (Thread ID : " + Thread.currentThread().getId() + ")\n";
    loggedText += "Headers: " + response.getHeaders() + "\n";
    loggedText += "Response Status : " + response.getRawStatusCode() + "\n";
    loggedText += "Response body: " + error + "\n";
    loggedText += "======Error Response from Server======\n";

    logger.error(loggedText);
  }

  private String getErrorAsString(@NonNull final ClientHttpResponse response) throws IOException {
    try (BufferedReader br = new BufferedReader(new InputStreamReader(response.getBody()))) {
      return br.readLine();
    }
  }
}

