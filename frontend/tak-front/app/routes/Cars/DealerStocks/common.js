import {safeValue} from "../../../shared/utils/utilities";

export const DealerStocksCommonUtil = {
    createNullOrUndefinedSafeOne  : (one) => {
        return {
            useType: safeValue(one.useType, 0),
            delYn: one.delDt ? "Y" : "N",
        };
    }
};