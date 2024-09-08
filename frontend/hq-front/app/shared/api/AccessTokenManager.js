const ACCESS_TOKEN_NAME = 'authToken';
const LOGIN_PAGE_PATH = '/pages/login';
function hrefToLoginPage() {
  window.location.href = LOGIN_PAGE_PATH;
}

function pushToLoginPage(history) {
   history.push(LOGIN_PAGE_PATH);
}

function removeAccessTokenToLoginPage(history = null) {
  removeAccessToken();
  if(!history) {
    hrefToLoginPage();
  }else{
    pushToLoginPage();
  }
}

function removeAccessToken() {
  localStorage.removeItem(ACCESS_TOKEN_NAME);
}

function setAccessToken(newToken) {
  try {
    localStorage.setItem(ACCESS_TOKEN_NAME, newToken);
  } catch (e) {
    removeAccessTokenToLoginPage();
  }
}

function getAccessToken() {
  return localStorage.getItem(ACCESS_TOKEN_NAME);
}

export {
  setAccessToken,
  getAccessToken,
  removeAccessTokenToLoginPage,
  removeAccessToken
};
