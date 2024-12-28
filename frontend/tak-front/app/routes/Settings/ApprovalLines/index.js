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
import {CRUD_COLUMNS, isAuthorized} from "shared/utils/authorization";
import SettingsApprovalLinesCreate from "./One/SettingsApprovalLinesCreate";
import SettingsApprovalLinesUpdate from "./One/SettingsApprovalLinesUpdate";
import {ButtonWrapper} from "../../../shared/components/OptimizedHtmlElements";
import {useListSearchModuleMeta} from "../../../shared/hooks/useListSearchModuleMeta";
import ListTopToolbar from "../../../shared/components/List/ListTopToolbar";
/*
*
*   아래 Box 에서 쓸수 있는 스타일링 문법 : https://mantine.dev/styles/style-props/
*
* */

const PK_NAME = "approvalLineIdx";
const SettingsApprovalLinesList = ({ recoilKey }) => {

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


    const LineDetailsCell = ({ renderedCellValue }) => {
        // Parse the JSON string
        let userIds = [];
        try {
            const lineDetails = JSON.parse(renderedCellValue);
            // Othersct user IDs from the parsed object
            userIds = Object.values(lineDetails)
                .map(item => item.userid)
                .filter(userid => userid); // Filter out empty user IDs
        } catch (error) {
            console.error('Failed to parse lineDetails:', error);
        }

        return (
            <Box fz={"13px"}>
                {userIds.join(', ')} {/* Join IDs with a comma */}
            </Box>
        );
    };

    const columns = React.useMemo(
        () => [
            {
                accessorKey: 'deptNm',
                header: '전시장',
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
                accessorFn: (row) => `${row.deptNm}`,
                Cell: ({ renderedCellValue }) => {
                    return(
                        <Box
                            fz={"13px"}
                        >
                            {renderedCellValue}
                        </Box>
                    )
                },
                searchable: false,
                defaultSearchColumn : false

            },
            {
                accessorKey: 'lineGbStr',
                header: '구분',
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
                searchable: false,
            },
            {
                accessorKey: 'lineDetails',
                header: '결제',
                Header: ({ column, header, table }) => {
                    return (
                        <Box fz={13} fw={600}>
                            {column?.columnDef?.header}
                        </Box>
                    );
                },
                accessorFn: (row) => {
                    const userInfos = row.userInfos || {};
                    const userNames = Object.values(userInfos).map(info => info[0]).filter(name => name); // 사용자 이름만 추출
                    return userNames.join(' ▶ '); // '▶' 기호로 사용자 이름을 결합
                },
                Cell: ({ renderedCellValue }) => {
                    return (
                        <Box fz={"13px"}>
                            {renderedCellValue} {/* 변환된 문자열을 셀에 표시 */}
                        </Box>
                    );
                }

            },
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
    const { data, error, isError, isFetching, isLoading } = useMantineFetch({
        columnFilterFns,
        columnFilters,
        globalFilter,
        pagination,
        sorting,
        fetchFunc : agent.ApprovalLine.fetch,
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
        mantineTableFooterProps : {},
        data: data?.data ?? [],
        initialState: {showColumnFilters: false, density: 'xs', showGlobalFilter: false},
        // manual~ 의 true 에 아래 on 함수 하나씩 필요, filter 는 ListModule 로 이관.
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
            isLoading,
            showAlertBanner: isError,
        },
        renderTopToolbar: ({ table }) => {

            return (
                <ListTopToolbar table={table} recoilKey={recoilKey} refreshAll={refreshAll}
                                columns={columns} searchColumn={null} setSearchColumn={null} searchValue={searchValue} setSearchValue={setSearchValue} dateRange={dateRange} setSafeDateRange={setSafeDateRange} dateRangeColumn={dateRangeColumn} setDateRangeColumn={setDateRangeColumn}
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
        //table.setPagination(pagination)
        // table.setSorting(sorting)
    }, [table])

    // 신규 버튼을 업데이트 화면에서도 있게 하기 위함
    const createOne = ({  localOne  }) => {
        refreshOne();
        setOne({ [PK_NAME] : null, ...localOne });
        setGlobalSidebarCollapsed({
            forceUpdate: Math.random(),
            value: true
        })
    }

    /*
    *   Event Handler
    * */
    const handleRowClick = async ({ row }) =>{

        // refreshOne 을 여기서 공통적으로 한번 해주는 이유는 1의 case 에서도 ...row.original 이런식으로 할당할 경우 기존 객체를 완전히 초기화 하지 못해 이전 데이터가 남겨져서 보여지는 상황 발생
        refreshOne()

        if(row.original[PK_NAME] === null) {
            createOne({localOne : row.original});
        } else {
            if(isValidObject(one)){
                // row.original[PK_NAME] : 현재 수정 버튼을 클릭한 row 의 PK 값
                // one[PK_NAME] : 이전에 수정 버튼을 클릭하여 넣어진 recoil 의 PK 값
                if(row.original[PK_NAME] !== one[PK_NAME]){
                    // 이번에 클릭한 row 의 객체로 넣는다.
                    onePrevCycle({ localOne : row.original})
                }else{
                    // 중앙 저장소(recoil)의 현재 RECOIL_KEY에 해당하는 one 을 초기화
                    setGlobalSidebarCollapsed({
                        forceUpdate: Math.random(),
                        value: false
                    })
                }
            }else{
                onePrevCycle({ localOne : row.original})
                setGlobalSidebarCollapsed({
                    forceUpdate: Math.random(),
                    value: true
                })
            }
        }
    }

    /*
     *       Fetch Cycle
     * */
    const onePrevCycle = async ({  localOne  }) => {
        // 이번에 클릭한 row 의 객체로 넣는다.
        setLoading(true)
        try {
            const re = await Promise.all([fetchShowroomMetas()]);
            setOne({...localOne, meta : {
                    showrooms : re[0] ? re[0] : []
                }});
            setGlobalSidebarCollapsed({
                forceUpdate: Math.random(),
                value: true
            })
        }finally {
            setLoading(false)
        }
    }

    /*
    *
    *   DB Fetch
    * */
    const fetchShowroomMetas = async () => {
        try {
            const re = await agent.ApprovalLine.fetchShowroomMetas();

            if (re.statusCode === 200) {
                if(!Array.isArray(re.data)){
                    alert("전시장 데이터가 확인되지 않습니다. 관리자에게 문의 하십시오.");
                    return null;
                } else {
                    return re.data;
                }
            } else {
                renderError({errorObj: re});
                return null;
            }
        } catch (error) {
            console.error("Error fetching dealer metas:", error);
            return null;
        }
    };

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
                        {isCreateOne(one,PK_NAME) ? <SettingsApprovalLinesCreate refreshAll={refreshAll} refreshOne={refreshOne} recoilKey={recoilKey}/>
                            : isUpdateOne(one,PK_NAME) ?
                                <SettingsApprovalLinesUpdate refreshAll={refreshAll} refreshOne={refreshOne} recoilKey={recoilKey}/> : ""}
                    </Col>
                </Row>

            </LoadingOverlay>
        </Fragment>)
};

export default SettingsApprovalLinesList;