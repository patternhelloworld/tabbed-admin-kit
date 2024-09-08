import React, { useMemo, useState } from 'react';


export const useMantineMeta = ({
                               columns
                           }) => {

    //Manage MRT state that we want to pass to our API
    const [columnFilters, setColumnFilters] = useState([]);
    const [columnFilterFns, setColumnFilterFns] = //filter modes
        useState(
            Object.fromEntries(
                columns.map(({ accessorKey }) => [accessorKey, 'contains']),
            ),
        ); //default to "contains" for all columns
    const [globalFilter, setGlobalFilter] = useState('');
    const [sorting, setSorting] = useState([]);
    const [pagination, setPagination] = useState({
        pageIndex: 0,
        pageSize: 10,
    });

    return {
        columnFilters, setColumnFilters,
        columnFilterFns, setColumnFilterFns,
        globalFilter, setGlobalFilter,
        sorting, setSorting,
        pagination, setPagination
    };
};
