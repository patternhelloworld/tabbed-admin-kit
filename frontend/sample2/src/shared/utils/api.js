import axios from 'axios';

import history from 'browserHistory';
import toast from 'shared/utils/toast';
import { objectToQueryString } from 'shared/utils/url';
import { getStoredAuthToken, removeStoredAuthToken } from 'shared/utils/authToken';
import {
  ReasonPhrases,
  StatusCodes,
  getReasonPhrase,
  getStatusCode,
} from 'http-status-codes';
import strings from '../localization/shared/index';

const defaults = {
  baseURL: process.env.API_URL,
  headers: () => ({
    'Content-Type': 'application/json',
    Authorization: getStoredAuthToken() ? `Bearer ${getStoredAuthToken()}` : undefined,
  }),
  error: {
    timestamp : null,
    details : null,
    message: strings.apiNetworkError,
    userMessage : strings.apiNetworkError
  },
};

const api = (method, url, variables) =>
  new Promise((resolve, reject) => {
    axios({
      url: `${defaults.baseURL}${url}`,
      method,
      headers: defaults.headers(),
      params: method === 'get' ? variables : undefined,
      data: method !== 'get' ? variables : undefined,
      paramsSerializer: objectToQueryString,
    }).then(
      response => {
        resolve(response.data.data);
      },
      error => {
        if (error.response) {
          if ([StatusCodes.UNAUTHORIZED, StatusCodes.FORBIDDEN].includes(error.response.status)) {
            removeStoredAuthToken();
            history.push('/login');
          } else {
            console.log("[API ERROR] " + url +"(Excepted " + StatusCodes.UNAUTHORIZED + "," + StatusCodes.FORBIDDEN + ")")
            console.log(error.response);
            reject(error.response.data.userMessage);
          }
        } else {
          reject(defaults.error);
        }
      },
    );
  });

const loginApi = variables => {
  return new Promise((resolve, reject) => {
    axios({
      url: `${defaults.baseURL}/api/v1/traditional-oauth/token`,
      method: 'post',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
        Authorization:
          `Basic ${ 
          btoa(
            `${process.env.REACT_APP_OAUTH2_CLIENT_ID}:${process.env.REACT_APP_OAUTH2_CLIENT_SECRET}`,
          )}`,
      },
      params: {
        'grant_type': 'password',
        'username': variables.username,
        'password': variables.password,
      },
      paramsSerializer: objectToQueryString,
    }).then(
      response => {
        /*
           Axios 는 성공 시 response.data 에 서버의 데이터를 가져온다. 그리고, 모든 PMS 프로젝트는 다음과 같이 pms-common 의 GlobalSuccessPayload 를 따르기 때문에, data.data 로 depth 가 결정된다.
        * {
            "timestamp" : "2023-11-01T01:50:40.037+0000",
            "details" : "/api/v1/binders",

            "data" : "<--각 API 들에 따라 다양한 Type 이 올 수 있음-->",
          }
        *  */
        resolve(response.data.data);
      },
      error => {
        if (error.response) {
          reject(error.response.data.userMessage);
        } else {
          reject(defaults.error);
        }
      },
    );
  });
};

const optimisticUpdate = async (url, { updatedFields, currentFields, setLocalData }) => {
  try {
    setLocalData(updatedFields);
    await api('put', url, updatedFields);
  } catch (error) {
    setLocalData(currentFields);
    toast.error(error);
  }
};

export default {
  get: (...args) => api('get', ...args),
  post: (...args) => api('post', ...args),
  put: (...args) => api('put', ...args),
  patch: (...args) => api('patch', ...args),
  delete: (...args) => api('delete', ...args),
  optimisticUpdate,
  loginApi,
};
