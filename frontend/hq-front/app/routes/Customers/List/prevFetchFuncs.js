// fetchFunctions.js

import agent from "../../../shared/api/agent";
import { renderError } from "../../../shared/utils/CommonErrorHandler";
import { sortDeptsByParentCd } from "../../Settings/Depts/util";
import { DeptHierarchyUtil } from "../../../shared/utils/utilities";

export const fetchCodeCustomersMetas = async () => {
    try {
        const re = await agent.CodeCustomer.fetch({ skipPagination: true });
        if (re.statusCode === 200) {
            if (!Array.isArray(re?.data?.content)) {
                alert("딜러 메타 데이터가 확인되지 않습니다. 관리자에게 문의 하십시오.");
                return null; // Return null if the data is invalid
            } else {
                return re?.data?.content; // Return the fetched metas
            }
        } else {
            renderError({ errorObj: re });
            return null; // Return null if there's an error
        }
    } catch (error) {
        console.error("Error fetching dealer metas:", error);
        return null; // Return null in case of error
    }
};

export const fetchCustomerGroupsMetas = async () => {
    try {
        const re = await agent.CustomerGroup.fetch({ skipPagination: true });
        if (re.statusCode === 200) {
            if (!Array.isArray(re?.data?.content)) {
                alert("딜러 메타 데이터가 확인되지 않습니다. 관리자에게 문의 하십시오.");
                return null; // Return null if the data is invalid
            } else {
                return re?.data?.content; // Return the fetched metas
            }
        } else {
            renderError({ errorObj: re });
            return null; // Return null if there's an error
        }
    } catch (error) {
        console.error("Error fetching dealer metas:", error);
        return null; // Return null in case of error
    }
};

export const fetchDeptsForCurrentDealer = async () => {
    try {
        const re = await agent.Dept.fetchForCurrentDealer({});
        if (re.statusCode === 200) {
            if (!Array.isArray(re.data?.content)) {
                alert("조직 메타 데이터가 확인되지 않습니다. 관리자에게 문의 하십시오.");
                return null; // Return null if the data is invalid
            } else {
                return re.data?.content; // Return the fetched metas
            }
        } else {
            renderError({ errorObj: re });
            return null; // Return null if there's an error
        }
    } catch (error) {
        console.error("Error fetching dealer metas:", error);
        return null; // Return null in case of error
    }
};

export const fetchUsersForCurrentDealer = async () => {
    try {
        const re = await agent.User.fetch({ skipPagination: true });
        if (re.statusCode === 200) {
            if (!Array.isArray(re.data?.content)) {
                alert("조직 메타 데이터가 확인되지 않습니다. 관리자에게 문의 하십시오.");
                return null; // Return null if the data is invalid
            } else {
                return re.data?.content; // Return the fetched metas
            }
        } else {
            renderError({ errorObj: re });
            return null; // Return null if there's an error
        }
    } catch (error) {
        console.error("Error fetching dealer metas:", error);
        return null; // Return null in case of error
    }
};

export const getDeptsWithUsers = async () => {
    const re = await Promise.all([fetchDeptsForCurrentDealer(), fetchUsersForCurrentDealer()]);

    if (re[0] && re[1]) {
        // depts
        const depts = sortDeptsByParentCd(re[0].map(x => ({
            deptIdx: x.deptIdx,
            parentCd: x.parentCd,
            deptNm: x.deptNm,
            depth: DeptHierarchyUtil.getDepthByDeptIdx(re[0], x.deptIdx)
        })));
        // users
        const users = re[1];

        return DeptHierarchyUtil.mergeDeptsAndUsers(depts, users);
    } else {
        return [];
    }
};

export const fetchPrivacyAgree = async (customerIdx) => {
    if(customerIdx === null) {
        return null;
    }

    try {
        const re = await agent.PrivacyAgree.fetchOne(customerIdx);
        if (re.statusCode === 200) {
            if (!re.data) {
                return null;
            } else {
                return re.data;
            }
        } else {
            renderError({ errorObj: re });
            return null;
        }
    } catch (error) {
        console.error("Error fetching privacyAgree metas:", error);
        return null;
    }
};