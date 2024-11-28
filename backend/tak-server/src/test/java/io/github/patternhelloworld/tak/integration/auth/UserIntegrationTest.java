package io.github.patternhelloworld.tak.integration.auth;

import io.github.patternhelloworld.common.config.CustomHttpHeaders;

import io.github.patternhelloworld.tak.config.securityimpl.message.CustomSecurityUserExceptionMessage;
import jakarta.xml.bind.DatatypeConverter;
import lombok.SneakyThrows;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.operation.OperationRequest;
import org.springframework.restdocs.operation.OperationRequestFactory;
import org.springframework.restdocs.operation.OperationResponse;
import org.springframework.restdocs.operation.OperationResponseFactory;
import org.springframework.restdocs.operation.preprocess.OperationPreprocessor;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.restdocs.request.RequestDocumentation.formParameters;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(RestDocumentationExtension.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "target/generated-snippets",uriScheme = "https", uriHost = "vholic.com", uriPort = 8300)
public class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;


    @Value("${app.test.oauth2.user.clientId}")
    private String appUserClientId;
    @Value("${app.test.oauth2.user.clientSecret}")
    private String appUserClientSecret;

    @Value("${app.test.auth.user.username}")
    private String testUserName;
    @Value("${app.test.auth.user.password}")
    private String testUserPassword;


    private RestDocumentationResultHandler document;

    private String basicHeader;

    @Autowired
    private WebApplicationContext webApplicationContext;


    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentationContextProvide) throws UnsupportedEncodingException {

        basicHeader = "Basic " + DatatypeConverter.printBase64Binary((appUserClientId + ":" + appUserClientSecret).getBytes("UTF-8"));

    }

    @Test
    public void test_같은_앱토큰_끼리는_같은_액세스_토큰을_사용_EXPOSED() throws Exception {

        /*
        *    Access Token (앱토큰 : APPTOKENAAA)
        * */

        MvcResult result = mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v1/traditional-oauth/token")
                        .header(HttpHeaders.AUTHORIZATION, basicHeader)
                        .header(CustomHttpHeaders.APP_TOKEN, "APPTOKENAAA")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("grant_type", "password")
                        .param("username", testUserName)
                        .param("password", testUserPassword))
               .andExpect(status().isOk())
                .andDo(document( "{class-name}/{method-name}/oauth-access-token",
                        preprocessRequest(new AccessTokenMaskingPreprocessor()),
                        preprocessResponse(new AccessTokenMaskingPreprocessor(), prettyPrint()),
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("전달 받으 신 client_id 와  client_secret 를 ':' 로 연결하여 base64 함수를 사용하고 맨 앞에 Basic 이라고 기입하십시오. ex) Basic base64(client_id:client_secret)"),
                                headerWithName(CustomHttpHeaders.APP_TOKEN).optional().description("값이 없다고 로그인이 되지는 않지만, App-Token 값이 없는 경우, 같은 계정으로 로그인 할 경우, 개별 기기를 무시하고 같은 access_token 을 공유합니다. 기기별 세션 정책에 따라 필수 값으로 넣어주세요.")
                        ),
                        formParameters(
                                parameterWithName("grant_type").description("Oauth2 grant_type 중 password 방식을 사용합니다. password 라고 기입해주세요."),
                                parameterWithName("username").description("사용자의 유저 ID 입니다."),
                                parameterWithName("password").description("사용자의 비밀번호 입니다.")
                        )))
                .andReturn(); // 응답을 MvcResult 객체에 저장

        // 응답에서 refresh_token 추출
        String responseString = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        JSONObject jsonResponse = new JSONObject(responseString);
        String refreshToken = jsonResponse.getJSONObject("data").getString("refresh_token");
        String accessTokenForAppToken1 = jsonResponse.getJSONObject("data").getString("access_token");


        /*
         *    Access Token (앱토큰 : 없음)
         * */

        result = mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v1/traditional-oauth/token")
                        .header(HttpHeaders.AUTHORIZATION, basicHeader)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("grant_type", "password")
                        .param("username", testUserName)
                        .param("password", testUserPassword))
                .andExpect(status().isOk())
                .andDo(document( "{class-name}/{method-name}/oauth-access-token",
                        preprocessRequest(new AccessTokenMaskingPreprocessor()),
                        preprocessResponse(new AccessTokenMaskingPreprocessor(), prettyPrint()),
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("전달 받으 신 client_id 와  client_secret 를 ':' 로 연결하여 base64 함수를 사용하고 맨 앞에 Basic 이라고 기입하십시오. ex) Basic base64(client_id:client_secret)"),
                                headerWithName(CustomHttpHeaders.APP_TOKEN).optional().description("값이 없다고 로그인이 되지는 않지만, App-Token 값이 없는 경우, 같은 계정으로 로그인 할 경우, 개별 기기를 무시하고 같은 access_token 을 공유합니다. 기기별 세션 정책에 따라 필수 값으로 넣어주세요.")
                        ),
                        formParameters(
                                parameterWithName("grant_type").description("Oauth2 grant_type 중 password 방식을 사용합니다. password 라고 기입해주세요."),
                                parameterWithName("username").description("사용자의 유저 ID 입니다."),
                                parameterWithName("password").description("사용자의 비밀번호 입니다.")
                        )))
                .andReturn(); // 응답을 MvcResult 객체에 저장

        // 응답에서 refresh_token 추출
        responseString = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        jsonResponse = new JSONObject(responseString);
        refreshToken = jsonResponse.getJSONObject("data").getString("refresh_token");
        String accessToken = jsonResponse.getJSONObject("data").getString("access_token");

        /*
         *    Access Token (앱토큰 : APPTOKENAAA2)
         * */
        result = mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v1/traditional-oauth/token")
                        .header(HttpHeaders.AUTHORIZATION, basicHeader)
                        .header(CustomHttpHeaders.APP_TOKEN, "APPTOKENAAA2")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("grant_type", "password")
                        .param("username", testUserName)
                        .param("password", testUserPassword))
                .andExpect(status().isOk()).andReturn(); // 응답을 MvcResult 객체에 저장

        // 응답에서 refresh_token 추출
        responseString = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        jsonResponse = new JSONObject(responseString);
        refreshToken = jsonResponse.getJSONObject("data").getString("refresh_token");
        accessToken = jsonResponse.getJSONObject("data").getString("access_token");



        /*
         *    Refresh Token (앱토큰 : APPTOKENAAA2)
         * */
        result = mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v1/traditional-oauth/token")
                        .header(HttpHeaders.AUTHORIZATION, basicHeader)
                        .header(CustomHttpHeaders.APP_TOKEN, "APPTOKENAAA2")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("grant_type", "refresh_token")
                        .param("refresh_token", refreshToken))
                .andExpect(status().isOk())
                .andDo(document( "{class-name}/{method-name}/oauth-refresh-token",
                        preprocessRequest(new RefreshTokenMaskingPreprocessor()),
                        preprocessResponse(new RefreshTokenMaskingPreprocessor(), prettyPrint()),
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("전달 받으 신 client_id 와  client_secret 를 ':' 로 연결하여 base64 함수를 사용하고 맨 앞에 Basic 이라고 기입하십시오. ex) Basic base64(client_id:client_secret)"),
                                headerWithName(CustomHttpHeaders.APP_TOKEN).optional().description("값이 없다고 로그인이 되지는 않지만, App-Token 값이 없는 경우, 같은 계정으로 로그인 할 경우, 개별 기기를 무시하고 같은 access_token 을 공유합니다. 기기별 세션 정책에 따라 필수 값으로 넣어주세요.")
                        ),
                        formParameters(
                                parameterWithName("grant_type").description("Oauth2 grant_type 중 refresh_token 방식을 사용합니다. refresh_token 이라고 기입해주세요."),
                                parameterWithName("refresh_token").description("XXX")
                        ))).andReturn();

        responseString = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        jsonResponse = new JSONObject(responseString);
        String accessToken2 = jsonResponse.getJSONObject("data").getString("access_token");

        if(accessToken2.equals(accessToken)){
            assertNotEquals("refresh_token 으로 발급 된 신규 access_token 은 기존 access_token 과 값이 같으면 안됩니다.", accessToken2, accessToken);
        }else{
            assertTrue(true, "성공");
        }

        /*
         *      로그 아웃
         *
         *      : 결과적으로 APPTOKENAAA2 만 로그아웃되어야 한다.
         * */
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/users/me/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken2))
                .andExpect(status().isOk())
                .andDo(document( "{class-name}/{method-name}/oauth-user-logout",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("Bearer XXX")
                        ),relaxedResponseFields(
                                fieldWithPath("data.logout").description("true 이면 백앤드에서 logout 성공, false 이면 실패. 이나 이 메시지 무시하고, UX 를 고려하여, 클라이언트에서 토큰 지우고, 로그인 화면으로 이동.")
                        )));


        /*
         *    Access Token (앱토큰 : APPTOKENAAA)
         * */

        result = mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v1/traditional-oauth/token")
                        .header(HttpHeaders.AUTHORIZATION, basicHeader)
                        .header(CustomHttpHeaders.APP_TOKEN, "APPTOKENAAA")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("grant_type", "password")
                        .param("username", testUserName)
                        .param("password", testUserPassword))
                .andExpect(status().isOk())
                .andDo(document( "{class-name}/{method-name}/oauth-access-token",
                        preprocessRequest(new AccessTokenMaskingPreprocessor()),
                        preprocessResponse(new AccessTokenMaskingPreprocessor(), prettyPrint()),
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("전달 받으 신 client_id 와  client_secret 를 ':' 로 연결하여 base64 함수를 사용하고 맨 앞에 Basic 이라고 기입하십시오. ex) Basic base64(client_id:client_secret)"),
                                headerWithName(CustomHttpHeaders.APP_TOKEN).optional().description("값이 없다고 로그인이 되지는 않지만, App-Token 값이 없는 경우, 같은 계정으로 로그인 할 경우, 개별 기기를 무시하고 같은 access_token 을 공유합니다. 기기별 세션 정책에 따라 필수 값으로 넣어주세요.")
                        ),
                        formParameters(
                                parameterWithName("grant_type").description("Oauth2 grant_type 중 password 방식을 사용합니다. password 라고 기입해주세요."),
                                parameterWithName("username").description("사용자의 유저 ID 입니다."),
                                parameterWithName("password").description("사용자의 비밀번호 입니다.")
                        )))
                .andReturn(); // 응답을 MvcResult 객체에 저장

        // 응답에서 refresh_token 추출
        responseString = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        jsonResponse = new JSONObject(responseString);
        refreshToken = jsonResponse.getJSONObject("data").getString("refresh_token");
        String finalAccessTokenForAppToken1 = jsonResponse.getJSONObject("data").getString("access_token");


        // 200 이 나오는지 정도만 확인 (현재 access_token 이 유효한지)
        mockMvc.perform(get("/api/v1/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + finalAccessTokenForAppToken1))
                .andDo(print())
                .andExpect(status().isOk());


        if(!accessTokenForAppToken1.equals(finalAccessTokenForAppToken1)){
            assertEquals("최초 앱토큰에 해당하는 Access Token 이 다르게 나왔습니다.", accessTokenForAppToken1, finalAccessTokenForAppToken1);
        }else{
            assertTrue(true, "성공");
        }


    }

    @Test
    public void test_같은_앱토큰_끼리는_같은_액세스_토큰을_사용_ORIGINAL() throws Exception {

        /*
         *    Access Token (앱토큰 : APPTOKENAAA)
         * */

        MvcResult result = mockMvc.perform(RestDocumentationRequestBuilders.post("/oauth2/token")
                        .header(HttpHeaders.AUTHORIZATION, basicHeader)
                        .header(CustomHttpHeaders.APP_TOKEN, "APPTOKENAAA")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("grant_type", "password")
                        .param("username", testUserName)
                        .param("password", testUserPassword))
                .andExpect(status().isOk())
                .andDo(document( "{class-name}/{method-name}/oauth-access-token",
                        preprocessRequest(new AccessTokenMaskingPreprocessor()),
                        preprocessResponse(new AccessTokenMaskingPreprocessor(), prettyPrint()),
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("전달 받으 신 client_id 와  client_secret 를 ':' 로 연결하여 base64 함수를 사용하고 맨 앞에 Basic 이라고 기입하십시오. ex) Basic base64(client_id:client_secret)"),
                                headerWithName(CustomHttpHeaders.APP_TOKEN).optional().description("값이 없다고 로그인이 되지는 않지만, App-Token 값이 없는 경우, 같은 계정으로 로그인 할 경우, 개별 기기를 무시하고 같은 access_token 을 공유합니다. 기기별 세션 정책에 따라 필수 값으로 넣어주세요.")
                        ),
                        formParameters(
                                parameterWithName("grant_type").description("Oauth2 grant_type 중 password 방식을 사용합니다. password 라고 기입해주세요."),
                                parameterWithName("username").description("사용자의 유저 ID 입니다."),
                                parameterWithName("password").description("사용자의 비밀번호 입니다.")
                        )))
                .andReturn(); // 응답을 MvcResult 객체에 저장

        // 응답에서 refresh_token 추출
        String responseString = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        JSONObject jsonResponse = new JSONObject(responseString);
        String refreshToken = jsonResponse.getString("refresh_token");
        String accessTokenForAppToken1 = jsonResponse.getString("access_token");


        /*
         *    Access Token (앱토큰 : 없음)
         * */

        result = mockMvc.perform(RestDocumentationRequestBuilders.post("/oauth2/token")
                        .header(HttpHeaders.AUTHORIZATION, basicHeader)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("grant_type", "password")
                        .param("username", testUserName)
                        .param("password", testUserPassword))
                .andExpect(status().isOk())
                .andDo(document( "{class-name}/{method-name}/oauth-access-token",
                        preprocessRequest(new AccessTokenMaskingPreprocessor()),
                        preprocessResponse(new AccessTokenMaskingPreprocessor(), prettyPrint()),
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("전달 받으 신 client_id 와  client_secret 를 ':' 로 연결하여 base64 함수를 사용하고 맨 앞에 Basic 이라고 기입하십시오. ex) Basic base64(client_id:client_secret)"),
                                headerWithName(CustomHttpHeaders.APP_TOKEN).optional().description("값이 없다고 로그인이 되지는 않지만, App-Token 값이 없는 경우, 같은 계정으로 로그인 할 경우, 개별 기기를 무시하고 같은 access_token 을 공유합니다. 기기별 세션 정책에 따라 필수 값으로 넣어주세요.")
                        ),
                        formParameters(
                                parameterWithName("grant_type").description("Oauth2 grant_type 중 password 방식을 사용합니다. password 라고 기입해주세요."),
                                parameterWithName("username").description("사용자의 유저 ID 입니다."),
                                parameterWithName("password").description("사용자의 비밀번호 입니다.")
                        )))
                .andReturn(); // 응답을 MvcResult 객체에 저장

        // 응답에서 refresh_token 추출
        responseString = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        jsonResponse = new JSONObject(responseString);
        refreshToken = jsonResponse.getString("refresh_token");
        String accessToken = jsonResponse.getString("access_token");

        /*
         *    Access Token (앱토큰 : APPTOKENAAA2)
         * */
        result = mockMvc.perform(RestDocumentationRequestBuilders.post("/oauth2/token")
                        .header(HttpHeaders.AUTHORIZATION, basicHeader)
                        .header(CustomHttpHeaders.APP_TOKEN, "APPTOKENAAA2")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("grant_type", "password")
                        .param("username", testUserName)
                        .param("password", testUserPassword))
                .andExpect(status().isOk()).andReturn(); // 응답을 MvcResult 객체에 저장

        // 응답에서 refresh_token 추출
        responseString = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        jsonResponse = new JSONObject(responseString);
        refreshToken = jsonResponse.getString("refresh_token");
        accessToken = jsonResponse.getString("access_token");



        /*
         *    Refresh Token (앱토큰 : APPTOKENAAA2)
         * */
        result = mockMvc.perform(RestDocumentationRequestBuilders.post("/oauth2/token")
                        .header(HttpHeaders.AUTHORIZATION, basicHeader)
                        .header(CustomHttpHeaders.APP_TOKEN, "APPTOKENAAA2")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("grant_type", "refresh_token")
                        .param("refresh_token", refreshToken))
                .andExpect(status().isOk())
                .andDo(document( "{class-name}/{method-name}/oauth-refresh-token",
                        preprocessRequest(new RefreshTokenMaskingPreprocessor()),
                        preprocessResponse(new RefreshTokenMaskingPreprocessor(), prettyPrint()),
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("전달 받으 신 client_id 와  client_secret 를 ':' 로 연결하여 base64 함수를 사용하고 맨 앞에 Basic 이라고 기입하십시오. ex) Basic base64(client_id:client_secret)"),
                                headerWithName(CustomHttpHeaders.APP_TOKEN).optional().description("값이 없다고 로그인이 되지는 않지만, App-Token 값이 없는 경우, 같은 계정으로 로그인 할 경우, 개별 기기를 무시하고 같은 access_token 을 공유합니다. 기기별 세션 정책에 따라 필수 값으로 넣어주세요.")
                        ),
                        formParameters(
                                parameterWithName("grant_type").description("Oauth2 grant_type 중 refresh_token 방식을 사용합니다. refresh_token 이라고 기입해주세요."),
                                parameterWithName("refresh_token").description("XXX")
                        ))).andReturn();

        responseString = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        jsonResponse = new JSONObject(responseString);
        String accessToken2 = jsonResponse.getString("access_token");


        if(accessToken2.equals(accessToken)){
            assertNotEquals("refresh_token 으로 발급 된 신규 access_token 은 기존 access_token 과 값이 같으면 안됩니다.", accessToken2, accessToken);
        }else{
            assertTrue(true, "성공");
        }

        /*
         *      로그 아웃
         *
         *      : 결과적으로 APPTOKENAAA2 만 로그아웃되어야 한다.
         * */
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/users/me/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken2))
                .andExpect(status().isOk())
                .andDo(document( "{class-name}/{method-name}/oauth-user-logout",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("Bearer XXX")
                        ),relaxedResponseFields(
                                fieldWithPath("data.logout").description("true 이면 백앤드에서 logout 성공, false 이면 실패. 이나 이 메시지 무시하고, UX 를 고려하여, 클라이언트에서 토큰 지우고, 로그인 화면으로 이동.")
                        )));


        /*
         *    Access Token (앱토큰 : APPTOKENAAA)
         * */

        result = mockMvc.perform(RestDocumentationRequestBuilders.post("/oauth2/token")
                        .header(HttpHeaders.AUTHORIZATION, basicHeader)
                        .header(CustomHttpHeaders.APP_TOKEN, "APPTOKENAAA")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("grant_type", "password")
                        .param("username", testUserName)
                        .param("password", testUserPassword))
                .andExpect(status().isOk())
                .andDo(document( "{class-name}/{method-name}/oauth-access-token",
                        preprocessRequest(new AccessTokenMaskingPreprocessor()),
                        preprocessResponse(new AccessTokenMaskingPreprocessor(), prettyPrint()),
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("전달 받으 신 client_id 와  client_secret 를 ':' 로 연결하여 base64 함수를 사용하고 맨 앞에 Basic 이라고 기입하십시오. ex) Basic base64(client_id:client_secret)"),
                                headerWithName(CustomHttpHeaders.APP_TOKEN).optional().description("값이 없다고 로그인이 되지는 않지만, App-Token 값이 없는 경우, 같은 계정으로 로그인 할 경우, 개별 기기를 무시하고 같은 access_token 을 공유합니다. 기기별 세션 정책에 따라 필수 값으로 넣어주세요.")
                        ),
                        formParameters(
                                parameterWithName("grant_type").description("Oauth2 grant_type 중 password 방식을 사용합니다. password 라고 기입해주세요."),
                                parameterWithName("username").description("사용자의 유저 ID 입니다."),
                                parameterWithName("password").description("사용자의 비밀번호 입니다.")
                        )))
                .andReturn(); // 응답을 MvcResult 객체에 저장

        // 응답에서 refresh_token 추출
        responseString = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        jsonResponse = new JSONObject(responseString);
        refreshToken = jsonResponse.getString("refresh_token");
        String finalAccessTokenForAppToken1 = jsonResponse.getString("access_token");


        // 200 이 나오는지 정도만 확인 (현재 access_token 이 유효한지)
        mockMvc.perform(get("/api/v1/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + finalAccessTokenForAppToken1))
                .andDo(print())
                .andExpect(status().isOk());


        if(!accessTokenForAppToken1.equals(finalAccessTokenForAppToken1)){
            assertEquals("최초 앱토큰에 해당하는 Access Token 이 다르게 나왔습니다.", accessTokenForAppToken1, finalAccessTokenForAppToken1);
        }else{
            assertTrue(true, "성공");
        }
    }


    @Test
    public void test_아이디_또는_패스워드_틀리게_로그인_그리고_Client_ID_Secret_다르게_로그인_ORIGINAL() throws Exception {


        MvcResult result = mockMvc.perform(RestDocumentationRequestBuilders.post("/oauth2/token")
                        .header(HttpHeaders.AUTHORIZATION, basicHeader)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("grant_type", "password")
                        .param("username", testUserName + "wrongcredential")
                        .param("password", testUserPassword))
                .andExpect(status().isUnauthorized()) // 401
                .andDo(document( "{class-name}/{method-name}/oauth-access-token",
                        preprocessRequest(new AccessTokenMaskingPreprocessor()),
                        preprocessResponse(new AccessTokenMaskingPreprocessor(), prettyPrint()),
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("Connect the received client_id and client_secret with ':', use the base64 function, and write Basic at the beginning. ex) Basic base64(client_id:client_secret)"),
                                headerWithName(CustomHttpHeaders.APP_TOKEN).optional().description("Not having a value does not mean you cannot log in, but cases without an App-Token value share the same access_token. Please include it as a required value according to the device-specific session policy.")
                        ),
                        formParameters(
                                parameterWithName("grant_type").description("Uses the password method among Oauth2 grant_types. Please write password."),
                                parameterWithName("username").description("This is the user's email address."),
                                parameterWithName("password").description("This is the user's password.")
                        )))
                .andReturn();


        String responseString = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        JSONObject jsonResponse = new JSONObject(responseString);
        String userMessage = jsonResponse.getString("userMessage");

       // assertEquals(userMessage, CustomSecurityUserExceptionMessage.AUTHENTICATION_LOGIN_FAILURE.getMessage());
        assertTrue(userMessage.contains("을 찾을 수 없습니다."));

        result = mockMvc.perform(RestDocumentationRequestBuilders.post("/oauth2/token")
                        .header(HttpHeaders.AUTHORIZATION, "Basic " + DatatypeConverter.printBase64Binary((appUserClientId + "wrongcred:" + appUserClientSecret).getBytes("UTF-8")))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("grant_type", "password")
                        .param("username", testUserName)
                        .param("password", testUserPassword))
                .andExpect(status().isUnauthorized()) // 401
                .andDo(document( "{class-name}/{method-name}/oauth-access-token",
                        preprocessRequest(new AccessTokenMaskingPreprocessor()),
                        preprocessResponse(new AccessTokenMaskingPreprocessor(), prettyPrint()),
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("Connect the received client_id and client_secret with ':', use the base64 function, and write Basic at the beginning. ex) Basic base64(client_id:client_secret)"),
                                headerWithName(CustomHttpHeaders.APP_TOKEN).optional().description("Not having a value does not mean you cannot log in, but cases without an App-Token value share the same access_token. Please include it as a required value according to the device-specific session policy.")
                        ),
                        formParameters(
                                parameterWithName("grant_type").description("Uses the password method among Oauth2 grant_types. Please write password."),
                                parameterWithName("username").description("This is the user's email address."),
                                parameterWithName("password").description("This is the user's password.")
                        )))
                .andReturn();


        responseString = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        jsonResponse = new JSONObject(responseString);
        userMessage = jsonResponse.getString("userMessage");

        assertEquals(userMessage, CustomSecurityUserExceptionMessage.AUTHENTICATION_WRONG_CLIENT_ID_SECRET.getMessage());



        result = mockMvc.perform(RestDocumentationRequestBuilders.post("/oauth2/token")
                        .header(HttpHeaders.AUTHORIZATION, basicHeader)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("grant_type", "password" + "wronggrant")
                        .param("username", testUserName)
                        .param("password", testUserPassword))
                .andExpect(status().isUnauthorized()) // 401
                .andDo(document( "{class-name}/{method-name}/oauth-access-token",
                        preprocessRequest(new AccessTokenMaskingPreprocessor()),
                        preprocessResponse(new AccessTokenMaskingPreprocessor(), prettyPrint()),
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("Connect the received client_id and client_secret with ':', use the base64 function, and write Basic at the beginning. ex) Basic base64(client_id:client_secret)"),
                                headerWithName(CustomHttpHeaders.APP_TOKEN).optional().description("Not having a value does not mean you cannot log in, but cases without an App-Token value share the same access_token. Please include it as a required value according to the device-specific session policy.")
                        ),
                        formParameters(
                                parameterWithName("grant_type").description("Uses the password method among Oauth2 grant_types. Please write password."),
                                parameterWithName("username").description("This is the user's email address."),
                                parameterWithName("password").description("This is the user's password.")
                        )))
                .andReturn();


        responseString = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        jsonResponse = new JSONObject(responseString);
        userMessage = jsonResponse.getString("userMessage");

        assertEquals(userMessage, CustomSecurityUserExceptionMessage.AUTHENTICATION_WRONG_GRANT_TYPE.getMessage());


    }


    @Test
    public void test_아이디_또는_패스워드_틀리게_로그인_그리고_Client_ID_Secret_다르게_로그인_EXPOSE() throws Exception {

        MvcResult result = mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v1/traditional-oauth/token")
                        .header(HttpHeaders.AUTHORIZATION, basicHeader)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("grant_type", "password")
                        .param("username", testUserName + "wrongcredential")
                        .param("password", testUserPassword))
                .andExpect(status().isUnauthorized()) // 401
                .andDo(document( "{class-name}/{method-name}/oauth-access-token",
                        preprocessRequest(new AccessTokenMaskingPreprocessor()),
                        preprocessResponse(new AccessTokenMaskingPreprocessor(), prettyPrint()),
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("Connect the received client_id and client_secret with ':', use the base64 function, and write Basic at the beginning. ex) Basic base64(client_id:client_secret)"),
                                headerWithName(CustomHttpHeaders.APP_TOKEN).optional().description("Not having a value does not mean you cannot log in, but cases without an App-Token value share the same access_token. Please include it as a required value according to the device-specific session policy.")
                        ),
                        formParameters(
                                parameterWithName("grant_type").description("Uses the password method among Oauth2 grant_types. Please write password."),
                                parameterWithName("username").description("This is the user's email address."),
                                parameterWithName("password").description("This is the user's password.")
                        )))
                .andReturn();


        String responseString = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        JSONObject jsonResponse = new JSONObject(responseString);
        String userMessage = jsonResponse.getString("userMessage");

//        assertEquals(userMessage, CustomSecurityUserExceptionMessage.AUTHENTICATION_LOGIN_FAILURE.getMessage());
        assertTrue(userMessage.contains("을 찾을 수 없습니다."));



        result = mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v1/traditional-oauth/token")
                        .header(HttpHeaders.AUTHORIZATION, "Basic " + DatatypeConverter.printBase64Binary((appUserClientId + "wrongcred:" + appUserClientSecret).getBytes("UTF-8")))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("grant_type", "password")
                        .param("username", testUserName)
                        .param("password", testUserPassword))
                .andExpect(status().isUnauthorized()) // 401
                .andDo(document( "{class-name}/{method-name}/oauth-access-token",
                        preprocessRequest(new AccessTokenMaskingPreprocessor()),
                        preprocessResponse(new AccessTokenMaskingPreprocessor(), prettyPrint()),
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("Connect the received client_id and client_secret with ':', use the base64 function, and write Basic at the beginning. ex) Basic base64(client_id:client_secret)"),
                                headerWithName(CustomHttpHeaders.APP_TOKEN).optional().description("Not having a value does not mean you cannot log in, but cases without an App-Token value share the same access_token. Please include it as a required value according to the device-specific session policy.")
                        ),
                        formParameters(
                                parameterWithName("grant_type").description("Uses the password method among Oauth2 grant_types. Please write password."),
                                parameterWithName("username").description("This is the user's email address."),
                                parameterWithName("password").description("This is the user's password.")
                        )))
                .andReturn();


        responseString = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        jsonResponse = new JSONObject(responseString);
        userMessage = jsonResponse.getString("userMessage");

        assertEquals(userMessage, CustomSecurityUserExceptionMessage.AUTHENTICATION_WRONG_CLIENT_ID_SECRET.getMessage());



        result = mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v1/traditional-oauth/token")
                        .header(HttpHeaders.AUTHORIZATION, basicHeader)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("grant_type", "password" + "wronggrant")
                        .param("username", testUserName)
                        .param("password", testUserPassword))
                .andExpect(status().isUnauthorized()) // 401
                .andDo(document( "{class-name}/{method-name}/oauth-access-token",
                        preprocessRequest(new AccessTokenMaskingPreprocessor()),
                        preprocessResponse(new AccessTokenMaskingPreprocessor(), prettyPrint()),
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("Connect the received client_id and client_secret with ':', use the base64 function, and write Basic at the beginning. ex) Basic base64(client_id:client_secret)"),
                                headerWithName(CustomHttpHeaders.APP_TOKEN).optional().description("Not having a value does not mean you cannot log in, but cases without an App-Token value share the same access_token. Please include it as a required value according to the device-specific session policy.")
                        ),
                        formParameters(
                                parameterWithName("grant_type").description("Uses the password method among Oauth2 grant_types. Please write password."),
                                parameterWithName("username").description("This is the user's email address."),
                                parameterWithName("password").description("This is the user's password.")
                        )))
                .andReturn();


        responseString = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        jsonResponse = new JSONObject(responseString);
        userMessage = jsonResponse.getString("userMessage");

        assertEquals(userMessage, CustomSecurityUserExceptionMessage.AUTHENTICATION_WRONG_GRANT_TYPE.getMessage());
    }


    private static class AccessTokenMaskingPreprocessor implements OperationPreprocessor {

        @Override
        public OperationRequest preprocess(OperationRequest request) {


            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.putAll(request.getHeaders());
            httpHeaders.set("Authorization", "Basic XXX");


/*           String originalContent = new String(request.getContent(), StandardCharsets.UTF_8);
            originalContent += "&"
            JsonObject jsonObject = JsonParser.parseString(originalContent).getAsJsonObject();
            jsonObject.addProperty("grant_type", "XXX");*/
            byte[] updatedContent = "grant_type=password&username=XXX&password=XXX".getBytes(StandardCharsets.UTF_8);



            return new OperationRequestFactory().create(request.getUri(),
                    request.getMethod(), updatedContent, httpHeaders,
                    request.getParts());
        }

        @SneakyThrows
        @Override
        public OperationResponse preprocess(OperationResponse response) {
            // 1. 응답 본문 마스킹
            byte[] modifiedContent = response.getContent();

            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> contentMap = objectMapper.readValue(response.getContent(), Map.class);
            if (contentMap.containsKey("data")) {
                Map<String, Object> dataMap = (Map<String, Object>) contentMap.get("data");
                if (dataMap.containsKey("access_token")) {
                    dataMap.put("access_token", "XXX");
                }

                if (dataMap.containsKey("refresh_token")) {
                    dataMap.put("refresh_token", "XXX");
                }

                contentMap.put("data", dataMap);

                modifiedContent = objectMapper.writeValueAsBytes(contentMap);
            }

            // 2. 응답 헤더 마스킹
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.putAll(response.getHeaders());

            return new OperationResponseFactory().create(response.getStatus(), httpHeaders, modifiedContent);
        }

    }

    private static class RefreshTokenMaskingPreprocessor implements OperationPreprocessor {

        @Override
        public OperationRequest preprocess(OperationRequest request) {

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.putAll(request.getHeaders());
            httpHeaders.set("Authorization", "Basic XXX");

 /*           String originalContent = new String(request.getContent(), StandardCharsets.UTF_8);
            JsonObject jsonObject = JsonParser.parseString(originalContent).getAsJsonObject();
            jsonObject.addProperty("grant_type", "XXX");
            byte[] updatedContent = jsonObject.toString().getBytes(StandardCharsets.UTF_8);
*/
            byte[] updatedContent = "grant_type=refresh_token&refresh_token=XXX".getBytes(StandardCharsets.UTF_8);

            return new OperationRequestFactory().create(request.getUri(),
                    request.getMethod(), updatedContent, httpHeaders,
                    request.getParts());
        }

        @SneakyThrows
        @Override
        public OperationResponse preprocess(OperationResponse response) {
            // 1. 응답 본문 마스킹
            byte[] modifiedContent = response.getContent();

            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> contentMap = objectMapper.readValue(response.getContent(), Map.class);
            if (contentMap.containsKey("data")) {
                Map<String, Object> dataMap = (Map<String, Object>) contentMap.get("data");
                if (dataMap.containsKey("access_token")) {
                    dataMap.put("access_token", "XXX");
                }

                if (dataMap.containsKey("refresh_token")) {
                    dataMap.put("refresh_token", "XXX");
                }

                contentMap.put("data", dataMap);

                modifiedContent = objectMapper.writeValueAsBytes(contentMap);
            }


            // 2. 응답 헤더 마스킹
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.putAll(response.getHeaders());

            return new OperationResponseFactory().create(response.getStatus(), httpHeaders, modifiedContent);
        }

    }




}