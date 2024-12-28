// fetchFunctions.js

import agent from "../../../shared/api/agent";
import { renderError } from "../../../shared/utils/CommonErrorHandler";



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




