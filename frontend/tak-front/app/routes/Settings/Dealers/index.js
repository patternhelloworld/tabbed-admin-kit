import React, {useEffect, useState, Fragment} from 'react';
import {
    Row,
    Col
} from "components";
import {
    MantineReactTable,
    useMantineReactTable,
    MRT_GlobalFilterTextInput, MRT_ToggleDensePaddingButton,
    MRT_ToggleFiltersButton, MRT_ToolbarAlertBanner
} from 'mantine-react-table';
import {ActionIcon, Tooltip, Box, Flex, Menu, Button} from '@mantine/core';

/* 아이콘은 여기서 찾으세요 : https://tabler.io/icons */
import {IconRefresh, IconSquareRoundedX, IconTrash, IconRestore, IconEdit} from '@tabler/icons-react';

import agent from "shared/api/agent";

import {useMantineFetch} from "shared/hooks/useMantineFetch";
import {useMantineMeta} from "shared/hooks/useMantineMeta";

import {useRecoilCallback, useRecoilValue, useResetRecoilState, useSetRecoilState} from "recoil";
import {
    boardUpdateOneSelector, boardUpdateResetModifiedOneSelector,
    boardUpdateResetOneSelector,
    boardUpdateState
} from "shared/recoil/board/boardUpdateState";
import classNames from "classnames";
import {isCreateOne, isUpdateOne, isValidArray, isValidObject} from "shared/utils/utilities";
import {boardListResetSelector, boardListState}from "shared/recoil/board/boardListState";
import {MRT_Localization_KO_CUSTOM } from "shared/localization/mantine/custom";
import {globalInfoAccessTokenUserInfoSelector, globalInfoSidebarCollapsedSelector} from "shared/recoil/globalInfoState";

import LoadingOverlay from "react-loading-overlay";
import ClockLoader from "react-spinners/ClockLoader";
import SettingsDealersCreate from "./One/SettingsDealersCreate";
import SettingsDealersUpdate from "./One/SettingsDealersUpdate";
import ListTopToolbar from "shared/components/List/ListTopToolbar";
import {useListSearchModuleMeta} from "shared/hooks/useListSearchModuleMeta";
import {getYNCodeOptions} from "shared/enums";

/*
*
*   아래 Box 에서 쓸수 있는 스타일링 문법 : https://mantine.dev/styles/style-props/
*
* */

const PK_NAME = "dealerCd";
const SettingsDealersList = ({ recoilKey }) => {

    const setGlobalSidebarCollapsed = useSetRecoilState(globalInfoSidebarCollapsedSelector());

    // 읽기 전용
    const one = useRecoilValue(boardUpdateOneSelector({ recoilKey  }));
    // 쓰기 전용
    const setOne = useSetRecoilState(boardUpdateOneSelector({ recoilKey  }));
    /// 선택한 One 만 Reset
    const resetOne =  useResetRecoilState(boardUpdateResetOneSelector({recoilKey  }));
    const resetModifiedOne =  useResetRecoilState(boardUpdateResetModifiedOneSelector({recoilKey  }));
    // 세션
    const me = useRecoilValue(globalInfoAccessTokenUserInfoSelector());

    /// list 초기화 Reset
    const resetList = useRecoilCallback(({ set }) => (recoilKey) => {
        set(boardListResetSelector({ recoilKey }), null);
    });

    const [loading, setLoading] = useState(false);
    const [enableMantineFetch, setEnableMantineFetch] = useState(null);
    const [mantineFetchFirst, setMantineFetchFirst] = useState(true);

    const columns = React.useMemo(
        () => [
            {
                accessorKey: 'dealerNm',
                header: '딜러명',
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
                accessorFn: (row) => `${row.dealerNm}`,
                Cell: ({ renderedCellValue }) => {
                    return(
                        <Box
                            fz={"13px"}
                        >
                            {renderedCellValue}
                        </Box>
                    )
                },
                searchable: true,
                defaultSearchColumn : true
            },
            {
                accessorKey: 'isMain',
                header: '본사유무',
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
                                fontSize : "12px",
                            }}
                        >
                            {cell.getValue() ? cell.getValue() : ""}
                        </Box>
                    )
                },
                searchable: true,
                selectBoxList: [
                    { text: "-", value: ""},
                    ...getYNCodeOptions()
                ]
            },
            {
                accessorKey: 'shortNm',
                header: '딜러약어',
                Header: ({column, header, table})=> {
                    return (  <Box
                        fz={13}
                        fw={600}
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
                searchable: true
            },
            {
                accessorKey: 'modDt',
                header: '최종 수정일',
                Header: ({column, header, table}) => {
                    return (<Box
                        sx={{
                            fontSize: "13px",
                            fontWeight: 600
                        }}
                    >
                        {column?.columnDef?.header}
                    </Box>);
                },
                Cell: ({cell, column}) => {
                    return (
                        <Box
                            sx={{
                                fontSize: "12px"
                            }}
                        >
                            {cell.getValue()}
                        </Box>
                    )
                },
                searchable : false,
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
    } = useMantineMeta({ columns, recoilKey });


    const {
        createSearchFilter, createDateRangeFilter,
        searchColumn, setSearchColumn, searchValue, setSearchValue,
        dateRangeColumn, setDateRangeColumn,
        dateRange, setSafeDateRange
    } = useListSearchModuleMeta({recoilKey});

    //call our custom react-query hook
    const { data, error, isError, isLoading } = useMantineFetch({
        columnFilterFns,
        columnFilters,
        globalFilter,
        pagination,
        sorting,
        fetchFunc : agent.Dealer.fetch,
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
        data: data?.data?.content ?? [],
        enableColumnFilterModes: false,
        initialState: { showColumnFilters: false, density: 'xs',  showGlobalFilter: false  },
        manualPagination: true,
        manualSorting: true,
        onPaginationChange: (funcOrObject) => {
            setEnableMantineFetch(true)
            setPagination(funcOrObject)
        },
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
        rowCount: data?.data?.totalElements ?? 0,
        state: {
            sorting,
            pagination,
            isLoading,
            showAlertBanner: isError
        },
        renderTopToolbar: ({ table }) => {

            return (
                <ListTopToolbar table={table} recoilKey={recoilKey} refreshAll={refreshAll}
                                columns={columns} searchColumn={searchColumn} setSearchColumn={setSearchColumn} searchValue={searchValue} setSearchValue={setSearchValue} dateRange={dateRange} setSafeDateRange={setSafeDateRange} dateRangeColumn={dateRangeColumn} setDateRangeColumn={setDateRangeColumn}
                                createBtn={true} createOne={createOne} setEnableMantineFetch={setEnableMantineFetch}  />
            );
        },
        mantineTableBodyRowProps: ({ row }) => ({
            onClick: ()=> {
                handleRowClick({row})
            },
            sx: { cursor: 'pointer' },
        }),
        localization: {...MRT_Localization_KO_CUSTOM}
    });

    useEffect(() => {
        // 이 것을 안해주면 예를 들어 2페이지에 있다가 다른 탭에 갔다오면 1페이지로 초기화 되는 버그가 있다.
        // 이는 Mantine table 이 참조하는 Tanstack 테이블 문제로 보인다.
        // https://www.mantine-react-table.com/docs/api/table-options#onPaginationChange-prop
        // pagination recoil 은 문제가 없다. 그렇다고, const table 자체를 모두 recoil 저장 하려면 안에 수 많은 함수들과 참조 때문에 저장 실패한다.
        // 초기화 되는 문제가 있는 다른 것들이 발견된다면 여기서 set 한다.
        table.setPagination(pagination)
        // table.setSorting(sorting)
    }, [table])

    const createOne = () => {
        refreshOne()
        setOne({[PK_NAME] : null})
    }


    /*
    *   Event Handler
    * */
    const handleRowClick = async ({ row }) =>{

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
            }else{
                console.log("2 : 이전에 클릭한 것과 같은 row 를 클릭하였다.")
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


    /*
    *   LifeCycle
    * */
    useEffect(()=>{

    }, [])

    /*
    *  클래스 css 사용법 : https://www.w3schools.com/bootstrap4/bootstrap_grid_xlarge.asp
    * */
    return (
        <Fragment>
            <LoadingOverlay
                spinner={<ClockLoader color="#ffffff" size={20}/>}
                active={loading}
            >
                <Row>
                    <Col lg={!isValidObject(one) ? 12 : 6} >
                        <MantineReactTable table={table}/>
                    </Col>
                    <Col>
                        {isCreateOne(one,PK_NAME) ? <SettingsDealersCreate refreshAll={refreshAll} refreshOne={refreshOne} recoilKey={recoilKey}/>
                            : isUpdateOne(one,PK_NAME) ?
                                <SettingsDealersUpdate createOne={createOne} refreshAll={refreshAll} refreshOne={refreshOne} recoilKey={recoilKey}/> : ""}
                    </Col>
                </Row>

            </LoadingOverlay>
        </Fragment>)
};

export default SettingsDealersList;