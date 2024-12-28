import React, {useEffect, useState, Fragment} from 'react';
import {
    Row,
    Col
} from "./../../../../components";
import {   MantineReactTable,
    useMantineReactTable,
    MRT_GlobalFilterTextInput,MRT_ToggleDensePaddingButton,
    MRT_ToggleFiltersButton } from 'mantine-react-table';
import { ActionIcon, Tooltip,  Box, Button, Flex, Menu, Text, Title } from '@mantine/core';

/* 아이콘은 여기서 찾으세요 : https://tabler.io/icons */
import { IconRefresh, IconEdit, IconSquareRoundedX, IconTrash, IconRestore}  from '@tabler/icons-react';

import agent from "../../../../shared/api/agent";

import {useMantineFetch} from "../../../../shared/hooks/useMantineFetch";
import {useMantineMeta} from "../../../../shared/hooks/useMantineMeta";


import {useRecoilCallback, useRecoilValue, useResetRecoilState, useSetRecoilState} from "recoil";
import {
    boardUpdateOthersSelector,
    boardUpdateOneSelector, boardUpdateResetOthersSelector, boardUpdateResetOneSelector,
    boardUpdateState
} from "../../../../shared/recoil/board/boardUpdateState";
import classNames from "classnames";
import {isValidArray, isValidObject} from "../../../../shared/utils/utilities";
import {boardListResetSelector, boardListState} from "../../../../shared/recoil/board/boardListState";
import {MRT_Localization_KO_CUSTOM } from "../../../../shared/localization/mantine/custom";

import {renderError} from "../../../../shared/utils/CommonErrorHandler";
import {globalInfoSidebarCollapsedSelector} from "../../../../shared/recoil/globalInfoState";
import SettingsUsersMenusUpdate from "./One/SettingsUsersMenusUpdate";

import LoadingOverlay from 'react-loading-overlay'
import ClockLoader from 'react-spinners/ClockLoader';
import {useListSearchModuleMeta} from "../../../../shared/hooks/useListSearchModuleMeta";
import ListTopToolbar from "../../../../shared/components/List/ListTopToolbar";

/*
*
*   아래 Box 에서 쓸수 있는 스타일링 문법 : https://mantine.dev/styles/style-props/
*
* */

const PK_NAME = "userIdx";
const SettingsUsersMenusList = ({ recoilKey, ...props}) => {

    const setGlobalSidebarCollapsed = useSetRecoilState(globalInfoSidebarCollapsedSelector());

    // 읽기 전용
    const one = useRecoilValue(boardUpdateOneSelector({ recoilKey  }));
    // 쓰기 전용
    const setOne = useSetRecoilState(boardUpdateOneSelector({ recoilKey  }));
    /// 선택한 One 만 Reset
    const resetOne =  useResetRecoilState(boardUpdateResetOneSelector({ recoilKey  }));

    const setOthers = useSetRecoilState(boardUpdateOthersSelector({ recoilKey  }));
    const resetOthers =  useResetRecoilState(boardUpdateResetOthersSelector({ recoilKey  }));

    /// list 초기화 Reset
    const resetList = useRecoilCallback(({ set }) => (recoilKey) => {
        set(boardListResetSelector({ recoilKey }), null);
    });


    const [loading, setLoading] = useState(false)

    const [enableMantineFetch, setEnableMantineFetch] = useState(null);
    const [mantineFetchFirst, setMantineFetchFirst] = useState(true);

    const columns = React.useMemo(
        () => [
            {
                accessorKey: PK_NAME,
                size: 100,
                header: 'ID',
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
                            fz={"13px"}
                        >
                            {cell.getValue()}
                        </Box>
                    )
                },
            },
            {
                accessorKey: 'name',
                size : 300,
                header: '사용자',
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
               // accessorFn: (row) => `${row[PK_NAME]}:${row.name}`,
                Cell: ({ renderedCellValue, row, cell}) => {
                    return(
                        <Box
                            fz={"13px"}
                            fw={600}
                        >
                            {renderedCellValue} ({row.original.userId})
                        </Box>
                    )
                },
                searchable: true,
                defaultSearchColumn : true
            },
            {
                accessorKey: 'dealerNm',
                header: '딜러명',
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
                accessorKey: 'deptNm',
                header: '부서명',
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
                            {cell.getValue()}
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

            },
        ],
        [],
    );

    const refreshOne = () => {
        resetOne()
        resetOthers()
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
    } = useMantineMeta({ columns, recoilKey  });

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
        fetchFunc : agent.User.fetch,
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
        columnFilterModeOptions: ['contains'],
        initialState: {showColumnFilters: false, density: 'xs', showGlobalFilter: false},
        // manual~ 의 true 에 아래 on 함수 하나씩 필요
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
                                createBtn={false} setEnableMantineFetch={setEnableMantineFetch}  />
            );
        },
        /*       renderDetailPanel: ({ row }) => {
                   return (<div className={"w-50"}><MenusUserEdit one={data?.data?.content.find(x => x[PK_NAME] === row?.original[PK_NAME])} refetch={refetch}/></div>)
               },*/
        mantineTableBodyRowProps: ({ row }) => ({
            onClick: ()=> {
                handleRowClick({row})
            },
            sx: { cursor: 'pointer' },
        }),
        //   enableRowActions: true,
        localization: {...MRT_Localization_KO_CUSTOM, actions : "수정"}
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
    /*
        *   Event Handler
   * */
    const handleRowClick = async ({ row }) =>{

        refreshOne()

        console.log('수정버튼클릭')
        console.log(row)
        console.log(table)
        if(isValidArray(one)){
            // row.original[PK_NAME] : 현재 수정 버튼을 클릭한 row 의 PK 값
            // one[PK_NAME] : 이전에 수정 버튼을 클릭하여 넣어진 recoil 의 PK 값
            if(row.original[PK_NAME] !== one[0]?.userIdx){
                console.log("1 : 이전에 클릭한 것과 다른 row 를 클릭하였다.")
                // 이번에 클릭한 row 의 객체로 넣는다.
                onePrevCycle({row})
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
            onePrevCycle({row})
        }
    }


    /*
    *       Fetch Cycle
    * */
    const onePrevCycle = async ({row}) => {
        // 이번에 클릭한 row 의 객체로 넣는다.
        setLoading(true)
        try {
            const re = await Promise.all([fetchOne(row.original[PK_NAME]), fetchUserOthers({dealerCd: row.original.dealerCd})])
            setOne(re[0]);
            setOthers(prevState => {
                return {
                    ...prevState,
                    selectedUserIdx: null,
                    copyPermissions: false,
                    users: re[1]
                }
            });
            setGlobalSidebarCollapsed({
                forceUpdate: Math.random(),
                value: true
            })
        }finally {
            setLoading(false)
        }
    }

    /*
        *   Fetch
    * */
    const fetchOne = async (userIdx) => {
        const re = await agent.Menu.fetchForUser({userIdx : userIdx});
        if (re.statusCode === 200) {
            if(!isValidArray(re.data)){
                alert("해당 사용자의 권한이 확인되지 않습니다. 관리자에게 문의 하십시오.");
                return null;
            }else{
                return re.data;
            }
        }else{
            renderError({errorObj: re[0]});
            return null;
        }
    }

    const fetchUserOthers = async ({ dealerCd }) => {
        try {
            const re = await agent.User.fetch({ skipPagination : true, searchFilter : JSON.stringify({
                    dealerCd
                })});
            if (re.statusCode === 200) {
                return re.data?.content; // Return the fetched metas
            } else {
                renderError({errorObj: re});
                return null; // Return null if there's an error
            }
        } catch (error) {
            console.error("Error fetching dealer metas:", error);
            return null; // Return null in case of error
        }
    };



    /*
    *   LifeCycle
    * */
    useEffect(()=>{

    }, [])


    /*
    *  클래스 css 사용법 : https://www.w3schools.com/bootstrap4/bootstrap_grid_xlarge.asp
    *
    *  Row = <div className="row">
       Col = <div className="col-lg-4">
    * */
    return (
        <Fragment>
            <LoadingOverlay
                spinner={<ClockLoader color="#ffffff" size={20}/>}
                active={loading}
            >
            <Row>
                <Col xl={!isValidArray(one) ? 12 : 7} >
                    <MantineReactTable table={table}/>
                </Col>
                <Col className={"mt-2"}>
                    {isValidArray(one) ? <SettingsUsersMenusUpdate refreshAll={refreshAll} refreshOne={refreshOne} recoilKey={recoilKey}/> :""}
                </Col>
            </Row>
            </LoadingOverlay>
        </Fragment>)
};


export default SettingsUsersMenusList;