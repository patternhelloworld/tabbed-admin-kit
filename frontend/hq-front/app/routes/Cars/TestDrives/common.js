import {safeValue} from "../../../shared/utils/utilities";
import {dateStrToDate} from "../../../shared/utils/date-handler";

export const TestDrivesCommonUtil = {
    createNullOrUndefinedSafeOne  : (one) => {
        return {
            delYn: one.delDt ? "Y" : "N",
            startDate : dateStrToDate(one.startDate),
            endDate : dateStrToDate(one.endDate)
        };
    }
};