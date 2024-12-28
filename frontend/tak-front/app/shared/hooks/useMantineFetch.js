import React, { useEffect, useState } from 'react';
import { StatusCodes } from 'http-status-codes';
import { useRecoilValue, useSetRecoilState } from "recoil";
import {
    boardListResponseSelector,
    boardListPrevQueryKeySelector
} from "shared/recoil/board/boardListState";
import { mantineToPMSFilter } from "../utils/url";

export const useMantineFetch = ({
                                    columnFilterFns,
                                    columnFilters,
                                    globalFilter,
                                    sorting,
                                    pagination,
                                    fetchFunc,
                                    cacheKey = undefined,

                                    additionalSearchFilter = {},
                                    additionalDateRangeFilter = {
                                        column: null,
                                        startDate: null,
                                        endDate: null
                                    },
                                    enabled = true,
                                    setEnabled = () => {},
                                    isFirstRender = false,
                                    setIsFirstRender = () => {
                                    }

                                }) => {
    const [data, setData] = useState(undefined);
    const [error, setError] = useState(undefined);
    const [isLoading, setIsLoading] = useState(false);
    const [isError, setIsError] = useState(false);

    const recoiledResponse = useRecoilValue(boardListResponseSelector({ recoilKey: cacheKey }));
    const setRecoiledResponse = useSetRecoilState(boardListResponseSelector({ recoilKey: cacheKey }));


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


                if (recoiledResponse != null && isFirstRender) {
             /*       console.log("ddda")
                    console.log(recoiledResponse)
                    console.log(isFirstRender)*/

                    if (recoiledResponse.statusCode !== StatusCodes.OK) {
                        setIsError(true);
                        setError(recoiledResponse)
                        setData(undefined)
                    }
                    setData(recoiledResponse);
                    return;
                }
                //refreshOne();

                if (typeof fetchFunc !== "undefined") {
                    const response = await fetchFunc({
                        pageNum,
                        pageSize,
                        searchFilter: mergedSearchFilterString,
                        sorterValueFilter,
                        dateRangeFilter: JSON.stringify(additionalDateRangeFilter)
                    });

                    setRecoiledResponse(response);

                    if (response.statusCode !== StatusCodes.OK) {
                        setIsError(true);
                        setError(response)
                        setData(undefined)
                    }else{
                        setData(response);
                        setError(undefined);
                        setIsError(false)
                    }
                } else {
                    console.log("fetchFunc is undefined.");
                }

            } catch (err) {
                setIsError(true);
                setError({...err, userMessage : "불편을 끼쳐드려 송구합니다. 재시도 해도 문제가 지속될 경우 관리자에게 문의 하십시오. 로그 확인이 필요합니다."});
                setData(undefined)
            } finally {
                setIsFirstRender(false);
                setIsLoading(false);
                setEnabled(false); // 요청이 끝나면 enabled를 다시 false로 설정
            }
        };

        if (enabled) {
            fetchData();
        }


    }, [enabled]);


    return { data, error, isError, isLoading };
};
