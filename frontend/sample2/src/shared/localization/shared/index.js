import LocalizedStrings from 'react-localization';

let strings = new LocalizedStrings({
    en:{
        apiNetworkError : "API Network Error.",
    },
    ko: {
        apiNetworkError :"네트워크 오류 입니다. 잠시 후 재시도 하십시오."
    }
});

export default strings;