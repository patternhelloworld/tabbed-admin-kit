/*
*   서버 측 문서 (/docs/api-app.html) 에 있는, 서버 오류 처리 방식 참조.
* */
import {isObject} from "./utilities";

let prevMessageForIndicator = null;

const renderError = ({ errorObj = {
  userValidationMessage : undefined,
  userMessage : undefined
}, formik = undefined, messageIndicator = function (msg){
    alert(msg);
}, toastDispatch}) => {

  console.log("[Debuggin...] Error")
  console.log(errorObj)

  let formikSet = false;

  let userMessageForIndicator =  "";

  if (errorObj.userValidationMessage && isObject(errorObj.userValidationMessage)) {

    let userValidationMessageConcat = "";
    Object.keys(errorObj.userValidationMessage).forEach(key => {
      if(formik && typeof formik.setErrors === "function") {
        console.log({[underScoreToUpperCamelCase(key)]: errorObj.userValidationMessage[key]})
        formik.setErrors({[underScoreToUpperCamelCase(key)]: errorObj.userValidationMessage[key]});
        formikSet = true;
      }else{
        userValidationMessageConcat += errorObj.userValidationMessage[key] + ",";
      }
    })

    userMessageForIndicator = userValidationMessageConcat.replace(/,$/, "");

  } else {
    if (errorObj.userMessage) {
      userMessageForIndicator = errorObj.userMessage;
    } else {
      if(errorObj.statusCode === undefined){
        // 네트워크 오류
        userMessageForIndicator =  "서버 호출에 실패하였습니다. 인터넷 연결 확인 또는 문제가 지속되면 관리자에게 문의하십시오.";
      }else{
        userMessageForIndicator = "예상치 못한 오류가 발생하였습니다. 문제가 지속되면 관리자에게 문의하십시오.";
      }
    }
  }

/*  if(prevMessageForIndicator === userMessageForIndicator){
    // UX 이유로 동일한 오류 메시지가 연속으로 뜨는 것을 방지.
    prevMessageForIndicator = null;
    return;
  }*/

  prevMessageForIndicator = userMessageForIndicator;

  // formik 으로 이미 메시지가 표시되었다면 toast 나 alert 창으로 띄울 필요가 없다.
  if(!formikSet) {
    if (toastDispatch) {
      // Redux 미사용으로 제외, 추후 Recoil 을 적용한다면 사용.
      /*    toastDispatch({type :  POPUP_TOAST_MESSAGE, payload : {content : messageForIndicator,
            level : ToastLevels.ERROR, timestamp : new Date().getMilliseconds()}})*/
    } else {
      messageIndicator(userMessageForIndicator)
    }
  }

}

const underScoreToUpperCamelCase = (string) => {
  return string.replace(/_([a-zA-Z0-9])/g, function (g) { return g[1].toUpperCase(); });
}
export {
  renderError, underScoreToUpperCamelCase
}
