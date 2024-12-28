import React, { useEffect, useState } from 'react';
import { StatusCodes } from 'http-status-codes';
import { mantineToPMSFilter } from "../utils/url";

export const useMantineFetchLocal = ({
                                         columnFilterFns,
                                         columnFilters,
                                         globalFilter,
                                         sorting,
                                         pagination,
                                         fetchFunc,
                                         refreshOne = () => {},
                                         additionalSearchFilter = {},
                                         additionalDateRangeFilter = {
                                             column: null,
                                             startDate: null,
                                             endDate: null
                                         },
                                         enabled = true,
                                         setEnabled = () => {},
                                         isFirstRender = false,
                                         setIsFirstRender = () => {}
                                     }) => {
    const [data, setData] = useState(undefined);
    const [error, setError] = useState(undefined);
    const [isLoading, setIsLoading] = useState(false);
    const [isError, setIsError] = useState(false);

    // Local version of what was previously Recoil response
    const [localResponse, setLocalResponse] = useState(undefined);

    useEffect(() => {
        const fetchData = async () => {
            try {
                setIsLoading(true);
                setIsError(false);

                const { pageNum, pageSize, searchFilter, sorterValueFilter } = mantineToPMSFilter({
                    columnFilterFns,
                    columnFilters,
                    globalFilter,
                    sorting,
                    pagination,
                });

                const parsedSearchFilter = searchFilter ? JSON.parse(searchFilter) : {};
                const mergedSearchFilter = { ...parsedSearchFilter, ...additionalSearchFilter };
                const mergedSearchFilterString = JSON.stringify(mergedSearchFilter);

                if (localResponse != null && isFirstRender) {
                    if (localResponse.statusCode !== StatusCodes.OK) {
                        setIsError(true);
                        setError(localResponse);
                        setData(undefined);
                    } else {
                        setData(localResponse);
                        setError(undefined);
                        setIsError(false);
                    }
                    return;
                }

                if (typeof fetchFunc !== "undefined") {
                    const response = await fetchFunc({
                        pageNum,
                        pageSize,
                        searchFilter: mergedSearchFilterString,
                        sorterValueFilter,
                        dateRangeFilter: JSON.stringify(additionalDateRangeFilter)
                    });

                    setLocalResponse(response); // Store the response in local state

                    if (response.statusCode !== StatusCodes.OK) {
                        setIsError(true);
                        setError(response);
                        setData(undefined);
                    } else {
                        setData(response);
                        setError(undefined);
                        setIsError(false);
                    }
                } else {
                    console.log("fetchFunc is undefined.");
                }

            } catch (err) {
                setIsError(true);
                setError({
                    ...err,
                    userMessage: "불편을 끼쳐드려 송구합니다. 재시도 해도 문제가 지속될 경우 관리자에게 문의 하십시오. 로그 확인이 필요합니다."
                });
                setData(undefined);
            } finally {
                setIsFirstRender(false);
                setIsLoading(false);
                setEnabled(false); // Set enabled to false after the request completes
            }
        };

        if (enabled) {
            fetchData();
        }

    }, [enabled, columnFilterFns, columnFilters, globalFilter, sorting, pagination, fetchFunc, additionalSearchFilter, additionalDateRangeFilter, isFirstRender, localResponse, setEnabled, setIsFirstRender]);

    return { data, error, isError, isLoading };
};
