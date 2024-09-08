import React, { useMemo, useState } from 'react';
import {
    QueryClient,
    QueryClientProvider,
    useQuery,
} from '@tanstack/react-query';
import agent from "shared/api/agent";
import {mantineToPMSFilter} from "shared/utils/url";
import {
    ReasonPhrases,
    StatusCodes,
    getReasonPhrase,
    getStatusCode,
} from 'http-status-codes';

//custom react-query hook
export const useMantineFetch = ({
                               columnFilterFns,
                               columnFilters,
                               globalFilter,
                               sorting,
                               pagination,
                               fetchFunc,
                               cacheKey = undefined
                           }) => {

    const params = mantineToPMSFilter({
        columnFilterFns,
        columnFilters,
        globalFilter,
        sorting,
        pagination,
    });

    return useQuery({
        queryKey: [cacheKey === undefined ? "common" : cacheKey, JSON.stringify(params)], //refetch whenever the URL changes (columnFilters, globalFilter, sorting, pagination)
        queryFn: async () => {
            const response = await fetchFunc(params)
            if (response.statusCode !== StatusCodes.OK) {
               // console.log(response.userMessage)
                throw new Error(response.userMessage)
            }
            return response;
        },
        keepPreviousData: true, //useful for paginated queries by keeping data from previous pages on screen while fetching the next page
        staleTime: 30000, //don't refetch previously viewed pages until cache is more than 30 seconds old
    });
};
