import { useEffect, useState } from 'react';

export const useMantineMetaLocal = () => {

    const [columnFilters, setColumnFilters] = useState([]);
    const [columnFilterFns, setColumnFilterFns] = useState([]);
    const [globalFilter, setGlobalFilter] = useState(null);
    const [sorting, setSorting] = useState([]);
    const [pagination, setPagination] = useState({
        pageIndex: 0,
        pageSize: 10,
    });

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
            setSorting(funcOrArray(sorting));
        } else if (Array.isArray(funcOrArray)) {
            setSorting(funcOrArray);
        } else {
            console.error("Invalid argument: Expected a function or an array : " + funcOrArray);
        }
    };

    const updatePagination = (funcOrObject) => {
        if (typeof funcOrObject === "function") {
            console.log("Function case (pagination)");
            console.log(pagination);
            setPagination(funcOrObject(pagination));
        } else if (typeof funcOrObject === "object" && funcOrObject !== null) {
            console.log("Object case (pagination)");
            console.log(pagination);
            setPagination(funcOrObject);
        } else {
            console.error("Invalid argument: Expected a function or an object");
        }
    };

    useEffect(() => {
        console.log("[useMantineMetaLocal] state changes");
        console.log({
            columnFilters,
            columnFilterFns,
            globalFilter,
            sorting,
            pagination,
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
        setPagination: updatePagination,
    };
};
