import React, { useState } from "react";
import { dateToYmdStr } from "../utils/date-handler";

export const useListSearchModuleMetaLocal = () => {
    // Using local state instead of Recoil
    const [searchColumn, setSearchColumn] = useState(undefined);
    const [searchValue, setSearchValue] = useState("");
    const [dateRangeColumn, setDateRangeColumn] = useState(undefined);
    const [dateRange, setDateRange] = useState({
        startDate: null,
        endDate: null,
    });

    const setSafeDateRange = ({ name, value }) => {
        setDateRange({
            ...dateRange,
            [name]: value,
        });
    };

    const createSearchFilter = () => {
        if (searchColumn && (searchValue !== undefined && searchValue !== null && searchValue !== "")) {
            return { [searchColumn]: searchValue };
        } else {
            return undefined;
        }
    };

    const createDateRangeFilter = () => {
        return {
            column: dateRangeColumn,
            startDate: dateToYmdStr(dateRange.startDate),
            endDate: dateToYmdStr(dateRange.endDate),
        };
    };

    const clearSearch = () => {
        setDateRangeColumn(undefined);
        setDateRange({ startDate: null, endDate: null });

        setSearchColumn(undefined);
        setSearchValue("");
    };

    return {
        createSearchFilter,
        createDateRangeFilter,
        searchColumn,
        setSearchColumn,
        searchValue,
        setSearchValue,
        dateRangeColumn,
        setDateRangeColumn,
        dateRange,
        setSafeDateRange,
        clearSearch,
    };
};
