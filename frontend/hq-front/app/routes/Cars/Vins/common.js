import {safeValue} from "../../../shared/utils/utilities";

export const VinsCommonUtil = {
    createNullOrUndefinedSafeOne  : (one) => {
        return {
            delYn: one.delDt ? "Y" : "N",
        };
    }
};