import superagentPromise from "superagent-promise";
import _superagent from "superagent";
import {
  ReasonPhrases,
  StatusCodes,
  getReasonPhrase,
  getStatusCode,
} from 'http-status-codes'

import {
  getAccessToken,
  setAccessToken,
  removeAccessTokenToLoginPage,
  removeAccessToken,
} from "./AccessTokenManager";

import queryString from "query-string";

const superagent = superagentPromise(_superagent, Promise);
// const LoginURL = `${process.env.API_URL}/v1/auth/login`
const DOMAIN_ROOT = `${process.env.API_URL}`;
const API_ROOT = DOMAIN_ROOT + `/api/v1`;

const encode = encodeURIComponent;

/*
   1. requests 가 삿용.then (성공), .catch (오류) 시 사용하는 객체
*/
const responseBody = (res) => {
  return { ...res.body, statusCode: res.status };
};
const errorResponseBody = (err) => {
  console.log(err);

  if (err.response === undefined) {
    // 서버 타임아웃 또는 인터넷 끊김
    return { statusCode: undefined };
  } else {
    if (err.response.status === StatusCodes.UNAUTHORIZED) {
      removeAccessTokenToLoginPage();
    } else {
      return { ...err.response.body, statusCode: err.status };
    }
  }
};



const bearerTokenResponseBody = (res) => {
  setAccessToken(res.body.data.access_token);
  return { ...res.body, statusCode: res.status };
};
const bearerTokenErrorResponseBody = (err) => {
  console.log(err);

  removeAccessToken();
  if (err.response === undefined) {
    // 서버 타임아웃 또는 인터넷 끊김
    return { statusCode: undefined };
  } else {
    return { ...err.response.body, statusCode: err.status };
  }
};

const registerSuccessResponseBody = (res) => {
  return { ...res.body, statusCode: res.status };
};

const basicTokenPlugin = (req) => {
  req.set(
    "Authorization",
    "Basic " +
      btoa(
        `${process.env.REACT_APP_OAUTH2_CLIENT_ID}:${process.env.REACT_APP_OAUTH2_CLIENT_SECRET}`
      )
  );
};

const tokenPlugin = (req) => {
  const token = getAccessToken();
  if (token) {
    req.set("authorization", `Bearer ${token}`);
  }
};

const requests = {
  del: (url) =>
    superagent
      .del(`${API_ROOT}${url}`)
      .use(tokenPlugin)
      .then(responseBody)
      .catch(errorResponseBody),
  get: (url) =>
    superagent
      .get(`${API_ROOT}${url}`)
      .use(tokenPlugin)
      .then(responseBody)
      .catch(errorResponseBody),
  put: (url, body) =>
    superagent
      .put(`${API_ROOT}${url}`, body)
      .use(tokenPlugin)
      .then(responseBody)
      .catch(errorResponseBody),
  patch: (url, body) =>
    superagent
      .patch(`${API_ROOT}${url}`, body)
      .use(tokenPlugin)
      .then(responseBody)
      .catch(errorResponseBody),
  post: (url, body) =>
    superagent
      .post(`${API_ROOT}${url}`, body)
      .use(tokenPlugin)
      .then(responseBody)
      .catch(errorResponseBody),
  postOneFile: (url, formData) =>
    superagent
      .post(`${DOMAIN_ROOT}${url}`)
      .send(formData)
      .use(tokenPlugin)
      .then(responseBody)
      .catch(errorResponseBody),
  getBlob: (url) =>
    superagent
      .get(`${DOMAIN_ROOT}${url}`)
      .responseType("blob")
      .use(tokenPlugin)
      .catch(errorResponseBody),
};


const Auth = {
  login: ({ idName, password, otpValue }) =>
    superagent
      .post(API_ROOT + "/traditional-oauth/token")
      .type("application/x-www-form-urlencoded")
      .send({
        username: idName.trim(),
        password: password.trim(),
        grant_type: "password",
        otp_value: otpValue,
      })
      .use(basicTokenPlugin)
      .then(bearerTokenResponseBody)
      .catch(bearerTokenErrorResponseBody),
  logout: () =>
    requests
      .get("/users/me/logout")
      .then(responseBody)
      .catch(errorResponseBody)
      .finally(() => {
        removeAccessTokenToLoginPage();
      }),
  current: () => requests.get("/users/me"),
  register: ({ name, email, password }) =>
    superagent
      .post(API_ROOT + "/users/register")
      .send({ name, email, password })
      .then(registerSuccessResponseBody)
      .catch(errorResponseBody)
};

// Issue 이건 샘플 입니다. (실제 미사용)
const Issue = {
  fetch: ({
    pageNum,
    pageSize,
    issueSearchFilter,
    sorterValueFilter,
    dateRangeFilter,
  }) =>
    requests.get(
      "/issues/paged?" +
        queryString.stringify({
          pageNum,
          pageSize,
          issueSearchFilter,
          sorterValueFilter,
          dateRangeFilter,
        })
    ),
  create: (issue) => requests.post("/issues", issue),
  update: (issue) => requests.put("/issues/" + issue.id, issue),
  delete: (issue) => requests.patch("/issues/" + issue.id + "/delete"),
  restore: (issue) => requests.patch("/issues/" + issue.id + "/restore"),
  destroy: (issue) => requests.del("/issues/" + issue.id),
};


export default {
  Auth,
  Issue
};
