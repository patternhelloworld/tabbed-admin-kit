import React, {useEffect, useMemo, useState, Fragment} from 'react';
import {
    Row,
    Col
} from "components";
import {   MantineReactTable,
    useMantineReactTable,
    MRT_GlobalFilterTextInput,MRT_ToggleDensePaddingButton,
    MRT_ToggleFiltersButton } from 'mantine-react-table';
import { ActionIcon, Tooltip,  Box, Button, Flex, Menu, Text, Title } from '@mantine/core';

/* 아이콘은 여기서 찾으세요 : https://tabler.io/icons */
import { IconRefresh, IconEdit, IconSquareRoundedX, IconTrash, IconRestore}  from '@tabler/icons-react';

import agent from "shared/api/agent";

import {useMantineFetch} from "shared/hooks/useMantineFetch";
import {useMantineMeta} from "shared/hooks/useMantineMeta";

import SampleEdit from "./One/SettingsMenusSubsUpdate";
import {useRecoilCallback, useRecoilValue, useResetRecoilState, useSetRecoilState} from "recoil";
import {
    boardUpdateOneSelector,
    boardUpdateResetModifiedOneSelector,
    boardUpdateResetOneSelector,
    boardUpdateState
} from "shared/recoil/board/boardUpdateState";
import classNames from "classnames";
import {isValidObject} from "shared/utils/utilities";
import {boardListResetSelector} from "shared/recoil/board/boardListState";
import {MRT_Localization_KO_CUSTOM } from "../../../../shared/localization/mantine/custom";
import {globalInfoSidebarCollapsedSelector} from "../../../../shared/recoil/globalInfoState";
import SettingsMenusSubsUpdate from "./One/SettingsMenusSubsUpdate";
import {useListSearchModuleMeta} from "../../../../shared/hooks/useListSearchModuleMeta";
import ListTopToolbar from "../../../../shared/components/List/ListTopToolbar";

/*
*
*   아래 Box 에서 쓸수 있는 스타일링 문법 : https://mantine.dev/styles/style-props/
*
* */
const PK_NAME = "subMenuIdx";

const SettingsMenusSubsList = ({ recoilKey }) => {

    const globalSidebarCollapsed = useRecoilValue(globalInfoSidebarCollapsedSelector());
    const setGlobalSidebarCollapsed = useSetRecoilState(globalInfoSidebarCollapsedSelector());

    // 읽기 전용
    const one = useRecoilValue(boardUpdateOneSelector({ recoilKey }));
    // 쓰기 전용
    const setOne = useSetRecoilState(boardUpdateOneSelector({ recoilKey }));
    // 초기화 하는 함수
    /// 1. 선택한 One 만
    const resetOne =  useResetRecoilState(boardUpdateResetOneSelector({recoilKey}));
    const resetModifiedOne = useResetRecoilState(boardUpdateResetModifiedOneSelector({ recoilKey}));
    /// 2. list 초기화
    const resetList = useRecoilCallback(({ set }) => (recoilKey) => {
        set(boardListResetSelector({ recoilKey }), null);
    });

    const [enableMantineFetch, setEnableMantineFetch] = useState(null);
    const [mantineFetchFirst, setMantineFetchFirst] = useState(true);

    const columns = useMemo(
        () => [
            {
                accessorKey: 'mainMenuNm',
                header: '대메뉴명',
                Header: ({column, header, table})=> {
                    return (  <Box
                        sx={{
                            fontSize : "13px",
                            fontWeight: 600
                        }}
                    >
                        {column?.columnDef?.header}
                    </Box>);
                },
                // renderedCellValue 로 해도 되고, cell.getValue() 해도 되고, row.original.title
                // accessorFn: (row) => `${row[PK_NAME]}:${row.title}`,
                Cell: ({ renderedCellValue, row, cell}) => {
                    return(
                        <Box
                            fz={"13px"}
                            fw={600}
                        >
                            {renderedCellValue}{/*-{row.original.title}*/}
                        </Box>
                    )
                },
                //    enableClickToCopy: true
            },
            {
                accessorKey: 'subMenuNm',
                header: '메뉴명',
                Header: ({column, header, table})=> {
                    return (  <Box
                        sx={{
                            fontSize : "13px",
                            fontWeight: 600
                        }}
                    >
                        {column?.columnDef?.header}
                    </Box>);
                },
                // renderedCellValue 로 해도 되고, cell.getValue() 해도 되고, row.original.title
               // accessorFn: (row) => `${row[PK_NAME]}:${row.title}`,
                Cell: ({ renderedCellValue, row, cell}) => {
                    return(
                        <Box
                            fz={"13px"}
                            fw={600}
                        >
                            {renderedCellValue}{/*-{row.original.title}*/}
                        </Box>
                    )
                },
                searchable: true,
                defaultSearchColumn : true
            },
            {
                accessorKey: 'subMenuSort',
                header: '정렬순서',
                Header: ({column, header, table})=> {
                    return (  <Box
                        fz={13}
                        fw={600}
                    >
                        {column?.columnDef?.header}
                    </Box>);
                },
                Cell: ({ renderedCellValue, row, cell}) => {
                    return(
                        <Box
                            sx={{
                                fontSize : "12px"
                            }}
                        >
                            {renderedCellValue}
                        </Box>
                    )
                },

            },
            {
                accessorKey: 'modDt',
                header: '최종 수정일',
                Header: ({column, header, table})=> {
                    return (  <Box
                        sx={{
                            fontSize : "13px",
                            fontWeight: 600
                        }}
                    >
                        {column?.columnDef?.header}
                    </Box>);
                },
                Cell: ({ cell, column }) => {
                    return(
                        <Box
                            sx={{
                                fontSize : "12px"
                            }}
                        >
                            {cell.getValue()}
                        </Box>
                    )
                },
                dateRangeSearchable: true,
                defaultDateRangeSearchColumn: true
            }
        ],
        [],
    );
    const refreshOne = () => {
        resetOne()
        resetModifiedOne()
    }

    const refreshAll = () => {
        resetList(recoilKey)
        refreshOne()
        setEnableMantineFetch(true)
    }

    useEffect(() => {
        setMantineFetchFirst(true)
        setEnableMantineFetch(true)
    }, [])

    const {
        columnFilters, setColumnFilters,
        columnFilterFns, setColumnFilterFns,
        globalFilter, setGlobalFilter,
        sorting, setSorting,
        pagination, setPagination
    } = useMantineMeta({ columns, recoilKey});


    const {
        createSearchFilter, createDateRangeFilter,
        searchColumn, setSearchColumn, searchValue, setSearchValue,
        dateRangeColumn, setDateRangeColumn,
        dateRange, setSafeDateRange
    } = useListSearchModuleMeta({recoilKey});

    //call our custom react-query hook
    const { data, error, isError, isFetching, isLoading, refetch } = useMantineFetch({
        columnFilterFns,
        columnFilters,
        globalFilter,
        pagination,
        sorting,
        fetchFunc : agent.Menu.fetchSubs,
        cacheKey : recoilKey,
        additionalSearchFilter: createSearchFilter(),
        additionalDateRangeFilter: createDateRangeFilter(),
        enabled: enableMantineFetch,
        setEnabled: setEnableMantineFetch,
        isFirstRender: mantineFetchFirst,
        setIsFirstRender: setMantineFetchFirst
    });


    const table = useMantineReactTable({
        columns,
        enableColumnResizing: true,
        columnResizeMode: 'onEnd',
        /*        mantinePaperProps: {
                    style: { '--mrt-base-background-color': 'rgb(33, 24, 44)' },
                },*/
        enableStickyHeader: true,
        mantineTableContainerProps: { sx: { maxHeight: '500px' } },
        mantinePaginationProps: {
            radius: 'xl',
            size: 'md',
        },
        mantineTableProps : {
            striped: true,
            verticalSpacing: "xs",
            sx: {
                tableLayout: 'fixed',
                fontFamily: 'Verdana, sans-serif',
                fontFamilyMonospace: 'Monaco, Courier, monospace'
            },
        },
        mantineTableHeadProps: {
            sx: {
                zIndex: 0
            }
        },
        mantineTableBodyProps : {
        },
        mantineTableHeadCellProps: {
            sx: {
                fontFamily: 'Verdana, sans-serif',
                fontFamilyMonospace: 'Monaco, Courier, monospace'
            },
        },
        mantineTableFooterProps : {

        },
        data: data?.data ?? [],
        enableColumnFilterModes: false,
        initialState: { showColumnFilters: false, density: 'xs',  showGlobalFilter: false },
        enablePagination: false,
        manualSorting: true,
        onSortingChange: (funcOrArray) => {
            setEnableMantineFetch(true)
            setSorting(funcOrArray)
        },
        mantineToolbarAlertBannerProps: isError
            ? {
                color: 'red',
                children: error.userMessage,
            }
            : undefined,
        // rowCount: data?.data?.totalElements ?? 0,
        state: {
            sorting,
            isLoading,
            showAlertBanner: isError
        },
        renderTopToolbar: ({ table }) => {

            return (
                <ListTopToolbar table={table} recoilKey={recoilKey} refreshAll={refreshAll}
                                columns={columns} searchColumn={searchColumn} setSearchColumn={setSearchColumn} searchValue={searchValue} setSearchValue={setSearchValue} dateRange={dateRange} setSafeDateRange={setSafeDateRange} dateRangeColumn={dateRangeColumn} setDateRangeColumn={setDateRangeColumn}
                                createBtn={false} setEnableMantineFetch={setEnableMantineFetch}  />
            );
        },
        mantineTableBodyRowProps: ({ row }) => ({
            onClick: ()=> {
                handleRowClick({row})
            },
            sx: { cursor: 'pointer' },
        }),
      //  enableRowActions: true,
        localization: {
            ...MRT_Localization_KO_CUSTOM,
            actions: '권한 조정'
        }
    });


    /*
    *   Event Handler
    * */
    const handleRowClick = ({ row }) =>{

        refreshOne()

        console.log('수정버튼클릭')
        console.log(row)
        console.log(table)
        if(isValidObject(one)){
            // row.original[PK_NAME] : 현재 수정 버튼을 클릭한 row 의 PK 값
            // one[PK_NAME] : 이전에 수정 버튼을 클릭하여 넣어진 recoil 의 PK 값
            if(row.original[PK_NAME] !== one[PK_NAME]){
                console.log("1 : 이전에 클릭한 것과 다른 row 를 클릭하였다.")
                // 이번에 클릭한 row 의 객체로 넣는다.
                setOne({...row.original});
                setGlobalSidebarCollapsed({
                    forceUpdate: Math.random(),
                    value: true
                })
            }else{
                console.log("2 : 이전에 클릭한 것과 같은 row 를 클릭하였다.")
                // 중앙 저장소(recoil)의 현재 RECOIL_KEY에 해당하는 one 을 초기화
                setGlobalSidebarCollapsed({
                    forceUpdate: Math.random(),
                    value: false
                })
            }
        }else{
            console.log("3  : one 이 isValidObject 아니라는 의미는, 한번도 수정 버튼을 클릭한 적이 없다.")
            setOne({...row.original});
            setGlobalSidebarCollapsed({
                forceUpdate: Math.random(),
                value: true
            })
        }
    }

    useEffect(()=>{
        // console.log("디버깅")
        // console.log(one)
    }, [one])

    /*
    *  클래스 css 사용법 : https://www.w3schools.com/bootstrap4/bootstrap_grid_xlarge.asp
    *
    *  Row = <div className="row">
       Col = <div className="col-lg-4">
    * */
    return (
        <div>
            <Row>
                <Col lg={!isValidObject(one) ? 12 : 8} >
                    <MantineReactTable table={table}/>
                </Col>
                <Col lg={4} className={classNames('', { 'd-none': !isValidObject(one) })}>
                    <SettingsMenusSubsUpdate refreshAll={refreshAll} refreshOne={refreshOne} recoilKey={recoilKey}/>
                </Col>
            </Row>

        </div>)
};


export default SettingsMenusSubsList;