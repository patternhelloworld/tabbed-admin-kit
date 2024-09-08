import { format } from 'date-fns';

const getHumanReadableTimeDiff = (startDate, endDate) => {
    // 문자열을 Date 객체로 파싱
    var start = new Date(startDate);
    var end = new Date(endDate);

    // 두 시간의 차이 계산 (밀리초 단위)
    var differenceInMilliseconds = end - start;

    // 밀리초를 시, 분, 초로 변환
    var hours = Math.floor(differenceInMilliseconds / (1000 * 60 * 60));
    var minutes = Math.floor((differenceInMilliseconds % (1000 * 60 * 60)) / (1000 * 60));
    var seconds = Math.floor((differenceInMilliseconds % (1000 * 60)) / 1000);

    // 결과 문자열 생성
    return `${hours}시간 ${minutes}분 ${seconds}초`;
}

const getStartEndDateStrs = (days) => {
    // 현재 날짜를 가져옴
    const currentDate = new Date();

    // endDate를 현재 날짜의 문자열 형태로 설정
    const endDate = currentDate.toISOString().split('T')[0];

    // startDate를 현재 날짜에서 7일을 빼서 계산
    currentDate.setDate(currentDate.getDate() - 7);
    const startDate = currentDate.toISOString().split('T')[0];

    return [startDate, endDate];
}

const getStartEndDates = (days) => {
    // 현재 날짜의 복사본을 만듭니다.
    const currentDate = new Date();
    const pastDate = new Date(currentDate);

    // 과거 날짜를 설정합니다. 여기서는 `days` 매개변수를 사용하지 않고 있으니, 이를 사용하도록 수정합니다.
    pastDate.setDate(currentDate.getDate() - days);

    // 수정된 과거 날짜 (pastDate) 와 현재 날짜 (currentDate) 를 배열로 반환합니다.
    return [pastDate, currentDate];
};



export const formatDate = (date) => format(date, 'yyyy-MM-dd');
export const formatDateTime = (date) => format(date, 'yyyy-MM-dd HH:mm:ss');
export const formatDateWrapper = (value) => {
    try {
        if (!value) {
            return null;
        } else if (value instanceof Date) {
            return formatDate(value);
        } else if (typeof value === 'string') {
            return formatDate(new Date(value));
        } else {
            throw new Error('Invalid date value');
        }
    }catch (e){
        console.log(e);
        return null;
    }
};

export const formatDateTimeWrapper = (value) => {
    try {
        if (!value) {
            return null;
        } else if (value instanceof Date) {
            return formatDateTime(value);
        } else if (typeof value === 'string') {
            return formatDateTime(new Date(value));
        } else {
            throw new Error('Invalid date value');
        }
    }catch (e){
        console.log(e)
        return null;
    }

};

export function dateToYmdStr(date) {

    if (date === undefined || date === null || typeof date !== 'object') {
        return null;
    }

    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0'); // getMonth()는 0부터 시작하므로 1을 더해줍니다.
    const day = String(date.getDate()).padStart(2, '0');

    return `${year}-${month}-${day}`;
}

export function dateStrToDate(dateStr) {
    if (!dateStr) {
        return null;
    }

    // Check if dateStr is already a Date object
    if (dateStr instanceof Date) {
        return dateStr;
    }


    const date = new Date(dateStr);

    if (isNaN(date.getTime())) {
        return null;
    }

    return date;
}
