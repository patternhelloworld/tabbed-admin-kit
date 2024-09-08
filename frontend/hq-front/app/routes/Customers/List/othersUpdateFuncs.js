// fetchFunctions.js

import agent from "../../../shared/api/agent";
import { renderError } from "../../../shared/utils/CommonErrorHandler";
import { sortDeptsByParentCd } from "../../Settings/Depts/util";
import {DeptHierarchyUtil, isObject} from "../../../shared/utils/utilities";

export const updateCustomersUserManager = async ({ customerIdxList, userIdx, refreshAll = () =>{}}) => {
    try {
        const re = await agent.Customer.updateUserManager({ customerIdxList, userIdx});
        if (re.statusCode === 200) {
                refreshAll()
        } else {
            renderError({ errorObj: re });
        }
    } catch (error) {
        console.error("Error updateCustomersUserManager : ", error);
    }
};

