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
      .then(response => response.body)
      .catch(errorResponseBody),
};


const Auth = {
  login: ({ userId, password }) =>
    superagent
      .post(API_ROOT + "/traditional-oauth/token")
      .type("application/x-www-form-urlencoded")
      .send({
        username: userId.trim(),
        password: password.trim(),
        grant_type: "password",
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
  register: ({ name, userId, password }) =>
    superagent
      .post(API_ROOT + "/users/register")
      .send({ name, userId, password })
      .then(registerSuccessResponseBody)
      .catch(errorResponseBody)
};

// Issue 이건 샘플 입니다. (실제 미사용)
const Issue = {
  fetch: ({
    pageNum,
    pageSize,
    searchFilter,
    sorterValueFilter,
    dateRangeFilter,
  }) =>
    requests.get(
      "/issues/paged?" +
        queryString.stringify({
          pageNum,
          pageSize,
          issueSearchFilter : searchFilter,
          sorterValueFilter,
          dateRangeFilter,
        })
    ),
  create: (issue) => requests.post("/issues", issue),
  update: (issue) => requests.put("/issues/" + issue.id, (({ id, ...rest }) => rest)(issue)),

  delete: (issue) => requests.patch("/issues/" + issue.id + "/delete"),
  restore: (issue) => requests.patch("/issues/" + issue.id + "/restore"),
  destroy: (issue) => requests.del("/issues/" + issue.id),
};

const Menu = {
    fetchMains: ({
                pageNum,
                pageSize,
                searchFilter,
                sorterValueFilter,
                dateRangeFilter,
            }) =>
        requests.get(
            "/settings/menus/mains?" +
            queryString.stringify({
                pageNum,
                pageSize,
                mainMenuSearchFilter : searchFilter,
                sorterValueFilter,
                dateRangeFilter,
            })
        ),
    fetchSubs: ({
                     pageNum,
                     pageSize,
                     searchFilter,
                     sorterValueFilter,
                     dateRangeFilter,
                 }) =>
        requests.get(
            "/settings/menus/subs?" +
            queryString.stringify({
                pageNum,
                pageSize,
                subMenuSearchFilter : searchFilter,
                sorterValueFilter,
                dateRangeFilter,
            })
        ),
    fetchSubsNo401Redirect: () =>
        superagent
            .get(`${API_ROOT}/settings/menus/subs`)
            .use(tokenPlugin)
            .then(responseBody),
    create: (item) => requests.post("/settings/menus/mains", item),

    update: (item) => requests.put("/settings/menus/mains/" + item.id, (({ id, ...rest }) => rest)(item)),
    updateSub: (item) => requests.put("/settings/menus/subs/" + item.id, (({ id, ...rest }) => rest)(item)),

    delete: (item) => requests.patch("/settings/menus/mains/" + item.id + "/delete"),
    restore: (item) => requests.patch("/settings/menus/mains/" + item.id + "/restore"),
    destroy: (item) => requests.del("/settings/menus/mains/" + item.id),


    // 권한
    fetchForUser: ({
                  userIdx
            }) =>
        requests.get(
            "/settings/users/menus?" +
            queryString.stringify({
                userIdx
            })
     ),
    updateForUser: ({item, userIdx, copyToUserIdx}) => requests.put("/settings/users/menus/" + userIdx, {copyToUserIdx, permissions : item}),
};

const Dealer = {
  fetch: ({
            pageNum,
            pageSize,
            searchFilter,
            sorterValueFilter,
            dateRangeFilter,
          }) =>
      requests.get(
          "/settings/dealers?" +
          queryString.stringify({
            pageNum,
            pageSize,
            dealerSearchFilter : searchFilter,
            sorterValueFilter,
            dateRangeFilter,
          })
      ),
  fetchMetas: () =>
        requests.get(
            "/settings/dealers/metas"
        ),
    create: (item) => requests.post("/settings/dealers", item),
    update: (dealer) => requests.put("/settings/dealers/" + dealer.dealerCd, (({ id, ...rest }) => rest)(dealer)),
};

const Dept = {
    fetch: ({
                pageNum,
                pageSize,
                searchFilter,
                sorterValueFilter,
                dateRangeFilter,
            }) =>
        requests.get(
            "/settings/depts?" +
            queryString.stringify({
                pageNum,
                pageSize: 1000,
                deptSearchFilter : searchFilter,
                sorterValueFilter,
                dateRangeFilter,
            })
        ),
    fetchForCurrentDealer: ({
                pageNum,
                pageSize,
                searchFilter,
                sorterValueFilter,
                dateRangeFilter,
            }) =>
        requests.get(
            "/settings/depts/dealers/me?" +
            queryString.stringify({
                skipPagination : true,
                pageNum,
                pageSize,
                deptSearchFilter : searchFilter,
                sorterValueFilter,
                dateRangeFilter,
            })
        ),
    fetchMetas: () =>
        requests.get(
            "/settings/depts/metas"
        ),
    create: (item) => requests.post("/settings/depts", item),
    update: (dept) => requests.put("/settings/depts/" + dept.deptIdx, (({ id, ...rest }) => rest)(dept)),
    updateForUser: ({item, userIdx}) => requests.put("/settings/users/menus" + userIdx, item)
};


const User = {
  fetch: ({
            skipPagination,
            pageNum,
            pageSize,
            searchFilter,
            sorterValueFilter,
            dateRangeFilter,
          }) =>
      requests.get(
          "/settings/users?" +
          queryString.stringify({
            skipPagination,
            pageNum,
            pageSize,
            userSearchFilter : searchFilter,
            sorterValueFilter,
            dateRangeFilter,
          })
      ),
  create: (item) => requests.post("/settings/users", item),
  update: (item) => requests.put("/settings/users/" + item.userIdx, (({ userIdx, ...rest }) => rest)(item)),

  delete: (item) => requests.patch("/settings/users/" + item.userIdx + "/delete"),
  restore: (item) => requests.patch("/settings/users/" + item.userIdx + "/restore"),
  destroy: (item) => requests.del("/settings/users/" + item.userIdx),
};


const Customer = {
    fetch: ({
                skipPagination,
                pageNum,
                pageSize,
                searchFilter,
                sorterValueFilter,
                dateRangeFilter,
            }) =>
        requests.get(
            "/customers/list?" +
            queryString.stringify({
                skipPagination,
                pageNum,
                pageSize,
                customerSearchFilter : searchFilter,
                sorterValueFilter,
                dateRangeFilter,
            })
        ),
    create: (item) => requests.post("/customers/list", item),
    update: (item) => requests.put("/customers/list/" + item.customerIdx, (({ customerIdx, ...rest }) => rest)(item)),
    updateUserManager: (item) => requests.patch("/customers/list/user-managers", item),

    delete: (item) => requests.patch("/customers/list/" + item.customerIdx + "/delete"),
    restore: (item) => requests.patch("/customers/list/" + item.customerIdx + "/restore"),
    destroy: (item) => requests.del("/customers/list/" + item.customerIdx),
};



const ApprovalLine = {
    fetch: () => requests.get("/settings/approvallines"),
    create: (item) => requests.post("/settings/approvallines", item),
    update: (approvalLine) => requests.put("/settings/approvallines/" + approvalLine.approvalLineIdx, (({ id, ...rest }) => rest)(approvalLine)),
    delete: (item) => requests.patch("/settings/approvallines/" + item.approvalLineIdx + "/delete"),
    fetchShowroomMetas: () =>
        requests.get(
            "/settings/approvallines/showroommetas"
        ),
};

const CodeCustomer = {
    fetch: ({
                skipPagination,
                pageNum,
                pageSize,
                searchFilter,
                sorterValueFilter,
                dateRangeFilter,
            }) =>
        requests.get(
            "/codes/customer?" +
            queryString.stringify({
                skipPagination,
                pageNum,
                pageSize,
                codeCustomerSearchFilter : searchFilter,
                sorterValueFilter,
                dateRangeFilter,
            })
        ),
    create: (item) => requests.post("/codes/customer", item),
    update: (codeCustomer) => requests.put("/codes/customer/" + codeCustomer.codeCustomerIdx, (({ id, ...rest }) => rest)(codeCustomer)),
    delete: (item) => requests.patch("/codes/customer/" + item.codeCustomerIdx + "/delete"),
    fetchNonCategoryMetas: (codeCustomerIdx) =>
        requests.get(
            "/codes/customer/"+codeCustomerIdx+"/metas"
        ),
    metaCreate: (item) => requests.post("/codes/customer/"+item.codeCustomerIdx+"/metas", item),
    metaUpdate: (codeCustomer) => requests.put("/codes/customer/"+codeCustomer.codeCustomerIdx+"/metas", (({ id, ...rest }) => rest)(codeCustomer)),
};

const CustomerGroup = {
    fetch: ({
                skipPagination,
                pageNum,
                pageSize,
                searchFilter,
                sorterValueFilter,
                dateRangeFilter,
            }) =>
        requests.get(
            "/customers/groups?" +
            queryString.stringify({
                skipPagination,
                pageNum,
                pageSize,
                customerGroupSearchFilter : searchFilter,
                sorterValueFilter,
                dateRangeFilter,
            })
        ),
    create: (item) => requests.post("/customers/groups", item),
    update: (customersGroups) => requests.put("/customers/groups/" + customersGroups.customerGroupIdx, (({ id, ...rest }) => rest)(customersGroups)),
    delete: (item) => requests.patch("/customers/groups/" + item.customerGroupIdx + "/delete"),
};

const RelocateLog = {
    fetch: ({
                skipPagination,
                pageNum,
                pageSize,
                searchFilter,
                sorterValueFilter,
                dateRangeFilter,
            }) =>
        requests.get(
            "/customers/relocated?" +
            queryString.stringify({
                skipPagination,
                pageNum,
                pageSize,
                customerSearchFilter : searchFilter,
                sorterValueFilter,
                dateRangeFilter,
            })
        )
};


const DealerStock = {
    fetch: ({
                skipPagination,
                pageNum,
                pageSize,
                searchFilter,
                sorterValueFilter,
                dateRangeFilter,
            }) =>
        requests.get(
            "/cars/dealer-stocks?" +
            queryString.stringify({
                skipPagination,
                pageNum,
                pageSize,
                dealerStockSearchFilter : searchFilter,
                sorterValueFilter,
                dateRangeFilter,
            })
        ),
    create: (item) => requests.post("/cars/dealer-stocks", item),
    update: (item) => requests.put("/cars/dealer-stocks/" + item.testDriveIdx, (({ testDriveIdx, ...rest }) => rest)(item)),

    delete: (item) => requests.patch("/cars/dealer-stocks/" + item.testDriveIdx + "/delete"),
    restore: (item) => requests.patch("/cars/dealer-stocks/" + item.testDriveIdx + "/restore"),
    destroy: (item) => requests.del("/cars/dealer-stocks/" + item.testDriveIdx),
};

const CarMaker = {
    fetchSearch: () => requests.get("/cars/makers/search"),
};

const CarModel = {
    fetchSearch: ({
                carMakerIdx
            }) =>
        requests.get(
            "/cars/models/search?" +
            queryString.stringify({
                carMakerIdx
            })
        ),
};

const CarModelDetail = {
    fetchDistinctYearsSearch: () => requests.get("/cars/models/details/search/distinct-years"),
    fetchSearch: ({
                      carModelIdx
                  }) =>
        requests.get(
            "/cars/models/details/search?" +
            queryString.stringify({
                carModelIdx
            })
        ),
};

const File= {
    create: (item) => requests.post(item.url, item.body),
    imgFetch: (url) => requests.getBlob(url)
};

const Vin = {
    fetch: ({
                skipPagination,
                pageNum,
                pageSize,
                searchFilter,
                sorterValueFilter,
                dateRangeFilter,
            }) =>
        requests.get(
            "/cars/vins?" +
            queryString.stringify({
                skipPagination,
                pageNum,
                pageSize,
                vinSearchFilter : searchFilter,
                sorterValueFilter,
                dateRangeFilter,
            })
        ),
    create: (item) => requests.post("/cars/vins", item),
    update: (item) => requests.put("/cars/vins/" + item.vinIdx, (({ vinIdx, ...rest }) => rest)(item)),

    delete: (item) => requests.patch("/cars/vins/" + item.vinIdx + "/delete"),
    restore: (item) => requests.patch("/cars/vins/" + item.vinIdx + "/restore"),
    destroy: (item) => requests.del("/cars/vins/" + item.vinIdx),
};

const TestDrive = {
    fetch: ({
                skipPagination,
                pageNum,
                pageSize,
                searchFilter,
                sorterValueFilter,
                dateRangeFilter,
            }) =>
        requests.get(
            "/cars/test-drives?" +
            queryString.stringify({
                skipPagination,
                pageNum,
                pageSize,
                testDriveSearchFilter : searchFilter,
                sorterValueFilter,
                dateRangeFilter,
            })
        ),
    create: (item) => requests.post("/cars/test-drives", item),
    update: (item) => requests.put("/cars/test-drives/" + item.testDriveIdx, (({ testDriveIdx, ...rest }) => rest)(item)),

    delete: (item) => requests.patch("/cars/test-drives/" + item.testDriveIdx + "/delete"),
    restore: (item) => requests.patch("/cars/test-drives/" + item.testDriveIdx + "/restore"),
    destroy: (item) => requests.del("/cars/test-drives/" + item.testDriveIdx),
};

const PrivacyAgree = {
    fetchOne: (customerIdx) => requests.get("/customers/privacyagree/"+customerIdx),
    create: (item) => requests.post("/customers/privacyagree", item)
};

const ExtCode = {
  fetch: ({
              skipPagination,
              pageNum,
              pageSize,
              searchFilter,
              sorterValueFilter,
              dateRangeFilter,
          }) =>
      requests.get(
          "/codes/extcodes?" +
          queryString.stringify({
              skipPagination,
              pageNum,
              pageSize,
              extCodeSearchFilter : searchFilter,
              sorterValueFilter,
              dateRangeFilter,
          })
      ),
  create: (item) => requests.post("/codes/extcodes", item),
  update: (extCode) => requests.put("/codes/extcodes/" + extCode.ext_color_code_idx, (({ id, ...rest }) => rest)(extCode)),
  delete: (item) => requests.patch("/codes/extcodes/" + item.ext_color_code_idx + "/delete"),
};

const GroupAssign = {
    fetch: ({
        skipPagination,
        pageNum,
        pageSize,
        searchFilter,
        sorterValueFilter,
        dateRangeFilter,
    }) =>
        requests.get(
            "/customers/group-assign?" +
            queryString.stringify({
                skipPagination,
                pageNum,
                pageSize,
                groupAssginSearchFilter : searchFilter,
                sorterValueFilter,
                dateRangeFilter,
            })
        ),
};

export default {
    Auth,
    Dealer,
    Dept,
    Menu,
    Issue,
    User,
    Customer,
    ApprovalLine,
    CodeCustomer,
    CustomerGroup,
    RelocateLog,
    DealerStock,
    CarMaker,
    CarModel,
    CarModelDetail,
    File,
    Vin,
    PrivacyAgree,
    TestDrive,
    ExtCode,
    GroupAssign
};
