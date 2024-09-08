// fetchFunctions.js

import agent from "../../../shared/api/agent";
import { renderError } from "../../../shared/utils/CommonErrorHandler";

export const fetchTestDrivesMetas = async (filterAll) => {
    try {
        const re = await agent.TestDrive.fetch(filterAll);
        if (re.statusCode === 200) {
            return re; // Return the fetched metas
        } else {
            renderError({ errorObj: re });
            return null; // Return null if there's an error
        }
    } catch (error) {
        console.error("Error fetching dealer metas:", error);
        return null; // Return null in case of error
    }
};
