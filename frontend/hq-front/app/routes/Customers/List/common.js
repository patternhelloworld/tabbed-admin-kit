import {safeValue} from "../../../shared/utils/utilities";

export const CustomersListCommonUtil = {
    createNullOrUndefinedSafeOne  : (one) => {
        return {
            customerType: safeValue(one.customerType, "0"),
            customerInfo: safeValue(one.customerInfo, "0"),
            purchasePlan: safeValue(one.purchasePlan, "Undecided"),
            nationality: safeValue(one.nationality, "None"),
            purchaseType: safeValue(one.purchaseType, "0"),
            gender: safeValue(one.gender, "None"),


            codeGeneralPositionIdx : safeValue(one.codeGeneralPositionIdx, 0),
            codeGeneralJobIdx : safeValue(one.codeGeneralJobIdx, 0),
            codeGeneralContactMethodIdx : safeValue(one.codeGeneralContactMethodIdx, 0),
            codeGeneralPurchaseDecisionFactorIdx: safeValue(one.codeGeneralPurchaseDecisionFactorIdx, 0),
            codeGeneralInterestFieldIdx : safeValue(one.codeGeneralInterestFieldIdx, 0),


            customerGrade: safeValue(one.customerGrade, "None"),

            customerGroupIdx: safeValue(one.customerGroupIdx, "0"),

            smsSubscription: safeValue(one.smsSubscription, "N"),
            emailSubscription: safeValue(one.emailSubscription, "N"),
            postalMailSubscription: safeValue(one.postalMailSubscription, "N"),
            personalDataConsent: safeValue(one.personalDataConsent, "N"),
            delYn: one.delDt ? "Y" : "N",
        };
    }
};