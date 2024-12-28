import {useRecoilState, useRecoilValue, useResetRecoilState, useSetRecoilState} from 'recoil';
import {useEffect, useMemo} from 'react';
import {
    boardListColumnFilterFnsSelector,
    boardListColumnFiltersSelector,
    boardListGlobalFilterSelector,
    boardListPaginationSelector,
    boardListSortingSelector
} from '../recoil/board/boardListState';
import {boardUpdateOneSelector, boardUpdateResetOneSelector} from "../recoil/board/boardUpdateState";

export const useMantineMeta = ({columns, recoilKey = "defaultMantineMeta"}) => {

    const columnFilters = useRecoilValue(boardListColumnFiltersSelector({ recoilKey }));
    const setColumnFilters = useSetRecoilState(boardListColumnFiltersSelector({ recoilKey }));

    const columnFilterFns = useRecoilValue(boardListColumnFilterFnsSelector({ recoilKey }));
    const setColumnFilterFns = useSetRecoilState(boardListColumnFilterFnsSelector({ recoilKey }));

    const globalFilter = useRecoilValue(boardListGlobalFilterSelector({ recoilKey }));
    const setGlobalFilter = useSetRecoilState(boardListGlobalFilterSelector({ recoilKey }));

    const sorting = useRecoilValue(boardListSortingSelector({ recoilKey }));
    const setSorting = useSetRecoilState(boardListSortingSelector({ recoilKey }));

    const pagination = useRecoilValue(boardListPaginationSelector({ recoilKey }));
    const setPagination = useSetRecoilState(boardListPaginationSelector({ recoilKey }));


    const updateColumnFilters = (func) => {
        if (func) {
            setColumnFilters(func(columnFilters));
        }
    };

    const updateColumnFilterFns = (obj) => {
         setColumnFilterFns(obj);
    };

    const updateGlobalFilter = (str) => {
        setGlobalFilter(str);
    };

    const updateSorting = (funcOrArray) => {
        if (typeof funcOrArray === "function") {
           // refreshAll();
            setSorting(funcOrArray(sorting));
        } else if (Array.isArray(funcOrArray)) {
          //  refreshAll();
            setSorting(sorting);
        } else {
            console.error("Invalid argument: Expected a function or an array : " + funcOrArray);
        }
    };

    const updatePagination = (funcOrObject) => {
        if (typeof funcOrObject === "function") {
          //  refreshAll();
            console.log("Function case (pagination)");
            console.log(pagination)
            setPagination(funcOrObject(pagination));
        } else if (typeof funcOrObject === "object" && funcOrObject !== null) {
         //   refreshAll();
            console.log("Object case (pagination)");
            console.log(pagination)
            setPagination(funcOrObject);
        } else {
            console.error("Invalid argument: Expected a function or an object");
        }
    };


    useEffect(() => {
        console.log("[useMantineMeta] state 변경 사항");
        console.log({
            columnFilters,
            columnFilterFns,
            globalFilter,
            sorting,
            pagination
        });
    }, [columnFilters, columnFilterFns, globalFilter, sorting, pagination]);

    return {
        columnFilters,
        setColumnFilters: updateColumnFilters,
        columnFilterFns,
        setColumnFilterFns: updateColumnFilterFns,
        globalFilter,
        setGlobalFilter: updateGlobalFilter,
        sorting,
        setSorting: updateSorting,
        pagination,
        setPagination: updatePagination
    };
};
