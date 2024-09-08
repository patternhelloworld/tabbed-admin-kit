import React, {useEffect, useMemo, useState} from 'react';
import { MantineReactTable, useMantineReactTable } from 'mantine-react-table';
import { ActionIcon, Tooltip } from '@mantine/core';
import { IconRefresh } from '@tabler/icons-react';

import agent from "../../shared/api/agent";
import {mantineToPMSFilter} from "../../shared/utils/url";
import {useMantineFetch} from "../../shared/hooks/useMantineFetch";
import {useMantineMeta} from "../../shared/hooks/useMantineMeta";
import toast from "../../shared/utils/toast";


const PagedList = () => {
    const columns = useMemo(
        () => [
            {
                accessorKey: 'id',
                header: 'ID',
            },
            {
                accessorKey: 'projectName',
                header: '프로젝트명',
            },
            {
                accessorKey: 'title',
                header: '제목',
            },
            {
                accessorKey: 'description',
                header: '설명',
            },
            {
                accessorKey: 'updatedAt',
                header: '최종 수정일',
            },
        ],
        [],
    );

    const {
        columnFilters, setColumnFilters,
        columnFilterFns, setColumnFilterFns,
        globalFilter, setGlobalFilter,
        sorting, setSorting,
        pagination, setPagination
    } = useMantineMeta({ columns });

    //call our custom react-query hook
    const { data, error, isError, isFetching, isLoading, refetch } = useMantineFetch({
        columnFilterFns,
        columnFilters,
        globalFilter,
        pagination,
        sorting,
        fetchFunc : agent.Issue.fetch,
    });


    const table = useMantineReactTable({
        columns,

        enableStickyHeader: true,
        mantineTableContainerProps: { sx: { maxHeight: '500px' } },
        mantinePaginationProps: {
            radius: 'xl',
            size: 'lg',
        },
        mantineTableProps : {
            striped: true,
            verticalSpacing: "xs"
        },
        mantineTableBodyCellProps :{
            fontSize: '10px'
        },
        mantineTableHeadProps : {
        //easier way to create media queries, no useMediaQuery hook needed.
        },
        data: data?.data?.content ?? [],
        enableColumnFilterModes: true,
        columnFilterModeOptions: ['contains'],
        initialState: { showColumnFilters: true, density: 'xs' },
        manualFiltering: true,
        manualPagination: true,
        manualSorting: true,
        mantineToolbarAlertBannerProps: isError
            ? {
                color: 'red',
                children: 'Error loading data',
            }
            : undefined,
        onColumnFilterFnsChange: setColumnFilterFns,
        onColumnFiltersChange: setColumnFilters,
        onGlobalFilterChange: setGlobalFilter,
        onPaginationChange: setPagination,
        onSortingChange: setSorting,
        renderTopToolbarCustomActions: () => (
            <Tooltip label="Refresh Data">
                <ActionIcon onClick={() => refetch()}>
                    <IconRefresh />
                </ActionIcon>
            </Tooltip>
        ),
        rowCount: data?.data?.totalElements ?? 0,
        state: {
            columnFilterFns,
            columnFilters,
            globalFilter,
            isLoading,
            pagination,
            showAlertBanner: isError,
            showProgressBars: isFetching,
            sorting,
        },
    });


    useEffect(()=>{
        if(isError){
           toast.error(error.message);
        }
    }, [isError])

    return (<div style={{ width : "900px", height : "700px", overflow : "auto"}}>
        <MantineReactTable table={table}/>
    </div>);
};


export default PagedList;