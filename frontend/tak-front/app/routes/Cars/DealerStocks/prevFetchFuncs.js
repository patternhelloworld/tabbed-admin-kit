// fetchFunctions.js

import agent from "../../../shared/api/agent";
import { renderError } from "../../../shared/utils/CommonErrorHandler";


export const fetchSecondDepthDepts = async () => {
    try {
        const re = await agent.Dept.fetchForCurrentDealer({
            searchFilter : JSON.stringify({
                onlySecondDepth : true
            })
        });
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

export const fetchDistinctYearsSearch = async () => {
    try {
        const re = await agent.CarModelDetail.fetchDistinctYearsSearch();
        if (re.statusCode === 200) {
            return re.data; // Return the fetched metas
        } else {
            renderError({ errorObj: re });
            return null; // Return null if there's an error
        }
    } catch (error) {
        console.error("Error fetching dealer metas:", error);
        return null; // Return null in case of error
    }
};


export const fetchCarMakersSearch = async () => {
    try {
        const re = await agent.CarMaker.fetchSearch();
        if (re.statusCode === 200) {
            return re.data; // Return the fetched metas
        } else {
            renderError({ errorObj: re });
            return null; // Return null if there's an error
        }
    } catch (error) {
        console.error("Error fetching dealer metas:", error);
        return null; // Return null in case of error
    }
};

export const fetchCarModelsSearch = async (carMakerIdx) => {
    try {
        const re = await agent.CarModel.fetchSearch({ carMakerIdx });
        if (re.statusCode === 200) {
            return re.data; // Return the fetched metas
        } else {
            renderError({ errorObj: re });
            return null; // Return null if there's an error
        }
    } catch (error) {
        console.error("Error fetching dealer metas:", error);
        return null; // Return null in case of error
    }
};

export const fetchCarModelDetailsSearch = async (carModelIdx) => {
    try {
        const re = await agent.CarModelDetail.fetchSearch({ carModelIdx });
        if (re.statusCode === 200) {
            return re.data; // Return the fetched metas
        } else {
            renderError({ errorObj: re });
            return null; // Return null if there's an error
        }
    } catch (error) {
        console.error("Error fetching dealer metas:", error);
        return null; // Return null in case of error
    }
};




