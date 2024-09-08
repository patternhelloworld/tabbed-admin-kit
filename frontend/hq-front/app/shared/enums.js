import {formatDateWrapper} from "./utils/date-handler";

export const CustomerType = Object.freeze({
    NONE: 0,
    PERSONAL: 1,
    CORPORATE: 2
});
export function getCustomerTypeOptions() {
    const labels = {
        NONE: "-",
        PERSONAL: "개인",
        CORPORATE: "법인"
    };
    return Object.entries(CustomerType).map(([key, value]) => ({
        value,
        text: labels[key]
    }));
}
export function findCustomerTypeLabelByValue(value) {
    const options = getCustomerTypeOptions(); // getCustomerInfoOptions 함수 호출
    const option = options.find(option => option.value === Number(value)); // 배열에서 값 찾기

    return option ? option.text : ""; // 찾은 경우 텍스트 반환, 아니면 기본값
}




export const CustomerInfo = Object.freeze({
    NONE: 0,
    CONTRACT: 1,
    POTENTIAL: 2
});
// One 에서 사용
export function getCustomerInfoOptions() {
    const labels = {
        NONE: "-",
        CONTRACT: "계약고객",
        POTENTIAL: "가망고객"
    };

    return Object.entries(CustomerInfo).map(([key, value]) => ({
        value,
        text: labels[key]
    }));
}
// List 에서 사용
export function findCustomerInfoLabelByValue(value) {
    const options = getCustomerInfoOptions(); // getCustomerInfoOptions 함수 호출
    const option = options.find(option => option.value === Number(value)); // 배열에서 값 찾기

    return option ? option.text : ""; // 찾은 경우 텍스트 반환, 아니면 기본값
}





export const CustomerPurchaseType = Object.freeze({
    NONE: 0,
    NEW_PURCHASE: 1,
    REPEAT_PURCHASE_2: 2,
    REPEAT_PURCHASE_3_OR_MORE: 3,
    USED_PURCHASE: 4,
    DEMO_PURCHASE: 5
});

export function getCustomerPurchaseTypeOptions() {
    const labels = {
        NONE: "없음",
        NEW_PURCHASE: "신규 구매",
        REPEAT_PURCHASE_2: "재구매 (2회)",
        REPEAT_PURCHASE_3_OR_MORE: "재구매 3회 이상",
        USED_PURCHASE: "중고구매",
        DEMO_PURCHASE: "Demo 구매"
    };

    return Object.entries(CustomerPurchaseType).map(([key, value]) => ({
        value,
        text: labels[key]
    }));
}




export const PurchasePlan = Object.freeze({
    IMMEDIATE: "Immediate",
    ONE_MONTH: "1M",
    THREE_MONTHS: "3M",
    SIX_MONTHS: "6M",
    TWELVE_MONTHS: "12M",
    UNDECIDED: "Undecided"
});

export function getPurchasePlanOptions() {
    const labels = {
        IMMEDIATE: "즉시",
        ONE_MONTH: "1개월",
        THREE_MONTHS: "3개월",
        SIX_MONTHS: "6개월",
        TWELVE_MONTHS: "12개월",
        UNDECIDED: "미정"
    };

    return Object.entries(PurchasePlan).map(([key, value]) => ({
        value,
        text: labels[key]
    }));
}

/**
 * 특정 PurchasePlan 값에 해당하는 레이블을 찾는 함수
 * @param {string} planValue - PurchasePlan 값 ("Immediate", "1M", "3M", "6M", "12M", "Undecided")
 * @returns {string} - 해당하는 한글 레이블 (예: "즉시")
 */
export function getLabelByPurchasePlanValue(planValue) {
    // getPurchasePlanOptions 함수를 호출하여 옵션 리스트를 가져옴
    const options = getPurchasePlanOptions();

    // planValue에 해당하는 레이블을 찾음
    const foundOption = options.find(option => option.value === planValue);

    // 해당하는 레이블이 있으면 반환, 없으면 undefined
    return foundOption ? foundOption.text : undefined;
}

export function regDatePlusPurchasePlan(dateStr, purchasePlan) {

    const date = new Date(dateStr);

    if (isNaN(date.getTime())) {
        throw new Error('유효하지 않은 날짜 형식입니다.');
    }

    switch (purchasePlan) {
        case PurchasePlan.IMMEDIATE:
            // 즉시인 경우 날짜를 변경하지 않음
            return dateStr;
        case PurchasePlan.ONE_MONTH:
            date.setMonth(date.getMonth() + 1);
            break;
        case PurchasePlan.THREE_MONTHS:
            date.setMonth(date.getMonth() + 3);
            break;
        case PurchasePlan.SIX_MONTHS:
            date.setMonth(date.getMonth() + 6);
            break;
        case PurchasePlan.TWELVE_MONTHS:
            date.setFullYear(date.getFullYear() + 1);
            break;
        case PurchasePlan.UNDECIDED:
            // 미정인 경우 처리하지 않음
            return null;
        default:
            throw new Error('유효하지 않은 purchase_plan 값입니다.');
    }

    return formatDateWrapper(date);
}




export const Nationality = Object.freeze({
    DOMESTIC: "Domestic",
    FOREIGNER: "Foreigner",
    NONE: "None"
});

export function getNationalityOptions() {
    const labels = {
        DOMESTIC: "내국인",
        FOREIGNER: "외국인",
        NONE: "미정"
    };

    return Object.entries(Nationality).map(([key, value]) => ({
        value,
        text: labels[key]
    }));
}




export const Gender = Object.freeze({
    MALE: "Male",
    FEMALE: "Female",
    NONE: "None"
});

export function getGenderOptions() {
    const labels = {
        MALE: "남",
        FEMALE: "여",
        NONE: "-"
    };

    return Object.entries(Gender).map(([key, value]) => ({
        value,
        text: labels[key]
    }));
}
export function findGenderLabelByValue(value) {
    const options = getGenderOptions(); // getCustomerInfoOptions 함수 호출
    const option = options.find(option => option.value === value); // 배열에서 값 찾기

    return option ? option.text : ""; // 찾은 경우 텍스트 반환, 아니면 기본값
}




export const YNCode = Object.freeze({
    Y: "Y",
    N: "N"
});

export function getYNCodeOptions() {
    const labels = {
        Y: "Y",
        N: "N"
    };

    return Object.entries(YNCode).map(([key, value]) => ({
        value,
        text: labels[key]
    }));
}




// Define the ManagementDepartment enum
export const ManagementDepartment = Object.freeze({
    MANAGEMENT: "Management",
    SALES: "Sales",
    SUPPORT: "Support",
    NONE: "None"
});

export function getManagementDepartmentOptions() {
    const labels = {
        MANAGEMENT: "관리",
        SALES: "판매",
        SUPPORT: "지원",
        NONE: "-"
    };

    return Object.entries(ManagementDepartment).map(([key, value]) => ({
        value,
        text: labels[key]
    }));
}
export function findManagementDepartmentLabelByValue(value) {
    const options = getManagementDepartmentOptions(); // Call getManagementDepartmentOptions function
    const option = options.find(option => option.value === value); // Find the option in the array

    return option ? option.text : ""; // Return the text if found, otherwise return the default
}




export const DealerStockUseType = Object.freeze({
    UNDEFINED: 0,  // 미정
    PENDING: 1,    // 대기
    DISPLAY: 2,    // 전시
    TEST_DRIVE: 3, // 시승
    TEST_CAR_SALE: 4 // 시승차 판매
});

export function getDealerStockUseTypeOptions() {
    const labels = {
        UNDEFINED: "미정",
        PENDING: "대기",
        DISPLAY: "전시",
        TEST_DRIVE: "시승",
        TEST_CAR_SALE: "시승차 판매"
    };
    return Object.entries(DealerStockUseType).map(([key, value]) => ({
        value,
        text: labels[key]
    }));
}

export function findDealerStockUseTypeLabelByValue(value) {
    const options = getDealerStockUseTypeOptions();

    const option = options.find(option => option.value == Number(value));

    return option ? option.text : "";
}



// Define ApprovalStatus enum with values
export const ApprovalStatus = Object.freeze({
    PENDING: 0,   // 대기
    APPROVED: 1,  // 승인
    REJECTED: 2   // 반려
});

// Get options for ApprovalStatus with corresponding labels
export function getApprovalStatusOptions() {
    const labels = {
        PENDING: "대기",
        APPROVED: "승인",
        REJECTED: "반려"
    };
    return Object.entries(ApprovalStatus).map(([key, value]) => ({
        value,
        text: labels[key]
    }));
}

// Find label for a specific ApprovalStatus value
export function findApprovalStatusLabelByValue(value) {
    const options = getApprovalStatusOptions();
    const option = options.find(option => option.value === Number(value));

    return option ? option.text : ""; // Return the text if found, or empty string if not
}
