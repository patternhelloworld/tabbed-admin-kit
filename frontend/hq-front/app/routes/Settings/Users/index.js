import React, {useEffect, useState, Fragment} from 'react';
import {
    Row,
    Col
} from "./../../../components";
import {
    MantineReactTable,
    useMantineReactTable,
    MRT_GlobalFilterTextInput, MRT_ToggleDensePaddingButton,
    MRT_ToggleFiltersButton, MRT_ToolbarAlertBanner
} from 'mantine-react-table';
import {ActionIcon, Tooltip, Box, Button, Flex, Menu, Text, Title} from '@mantine/core';

/* 아이콘은 여기서 찾으세요 : https://tabler.io/icons */
import {IconRefresh, IconEdit, IconSquareRoundedX, IconTrash, IconRestore} from '@tabler/icons-react';

import agent from "../../../shared/api/agent";

import {useMantineFetch} from "../../../shared/hooks/useMantineFetch";
import {useMantineMeta} from "../../../shared/hooks/useMantineMeta";


import {useRecoilCallback, useRecoilValue, useResetRecoilState, useSetRecoilState} from "recoil";
import {
    boardUpdateOneSelector, boardUpdateResetModifiedOneSelector,
    boardUpdateResetOneSelector,
    boardUpdateState
} from "../../../shared/recoil/board/boardUpdateState";
import classNames from "classnames";
import {
    CodeGeneralUtil, DeptHierarchyUtil,
    isArray,
    isCreateOne,
    isUpdateOne,
    isValidArray,
    isValidObject
} from "../../../shared/utils/utilities";
import {boardListResetSelector, boardListState} from "../../../shared/recoil/board/boardListState";
import {MRT_Localization_KO_CUSTOM} from "../../../shared/localization/mantine/custom";

import {renderError} from "../../../shared/utils/CommonErrorHandler";
import {
    globalInfoAccessTokenUserInfoSelector,
    globalInfoSidebarCollapsedSelector
} from "../../../shared/recoil/globalInfoState";
import SettingsMenusMainsUpdate from "../Menus/Mains/One/SettingsMenusMainsUpdate";
import SettingsUsersUpdate from "./One/SettingsUsersUpdate";

import LoadingOverlay from 'react-loading-overlay'
import ClockLoader from 'react-spinners/ClockLoader';
import {Link} from "react-router-dom";
import {CRUD_COLUMNS, isAuthorized} from "../../../shared/utils/authorization";
import SettingsUsersCreate from "./One/SettingsUsersCreate";
import {ButtonWrapper} from "../../../shared/components/OptimizedHtmlElements";
import ListSearchModule from "../../../shared/components/List/ListSearchModule";
import {useListSearchModuleMeta} from "../../../shared/hooks/useListSearchModuleMeta";
import ListTopToolbar from "../../../shared/components/List/ListTopToolbar";
import getCustomersListColumns from "../../Customers/List/columns";
import getSettingsUsersColumns from "./columns";
import {fetchDeptsForCurrentDealer, fetchUsersForCurrentDealer} from "../../Customers/List/prevFetchFuncs";
import {sortDeptsByParentCd} from "../Depts/util";



/*
*
*   아래 Box 에서 쓸수 있는 스타일링 문법 : https://mantine.dev/styles/style-props/
*
* */

const PK_NAME = "userIdx";

const SettingsUsersList = ({recoilKey, ...props}) => {

    const setGlobalSidebarCollapsed = useSetRecoilState(globalInfoSidebarCollapsedSelector());

    // 읽기 전용
    const one = useRecoilValue(boardUpdateOneSelector({recoilKey}));
    // 쓰기 전용
    const setOne = useSetRecoilState(boardUpdateOneSelector({recoilKey}));
    /// 선택한 One 만 Reset
    const resetOne = useResetRecoilState(boardUpdateResetOneSelector({recoilKey}));
    const resetModifiedOne = useResetRecoilState(boardUpdateResetModifiedOneSelector({recoilKey}));
    // 세션
    const me = useRecoilValue(globalInfoAccessTokenUserInfoSelector());

    /// list 초기화 Reset
    const resetList = useRecoilCallback(({set}) => (recoilKey) => {
        set(boardListResetSelector({recoilKey}), null);
    });

    const [loading, setLoading] = useState(false);
    const [enableMantineFetch, setEnableMantineFetch] = useState(null);
    const [mantineFetchFirst, setMantineFetchFirst] = useState(true);

    const columns = React.useMemo(() => getSettingsUsersColumns({PK_NAME}), []);


    useEffect(() => {
        setMantineFetchFirst(true)
        setEnableMantineFetch(true)
    }, [])


    const refreshOne = () => {
        resetOne()
        resetModifiedOne()
    }

    const refreshAll = () => {
        resetList(recoilKey)
        resetOne()
        resetModifiedOne()
        setEnableMantineFetch(true)
    }


    const {
        columnFilters, setColumnFilters,
        columnFilterFns, setColumnFilterFns,
        globalFilter, setGlobalFilter,
        sorting, setSorting,
        pagination, setPagination
    } = useMantineMeta({columns, recoilKey});

    const {
        createSearchFilter, createDateRangeFilter,
        searchColumn, setSearchColumn, searchValue, setSearchValue,
        dateRangeColumn, setDateRangeColumn,
        dateRange, setSafeDateRange
    } = useListSearchModuleMeta({recoilKey});

    //call our custom react-query hook
    const {data, error, isError, isLoading} = useMantineFetch({
        columnFilterFns,
        columnFilters,
        globalFilter,
        pagination,
        sorting,
        fetchFunc: agent.User.fetch,
        cacheKey: recoilKey,
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
        mantineTableContainerProps: {sx: {maxHeight: '500px'}},
        mantinePaginationProps: {
            radius: 'xl',
            size: 'md',
        },
        mantineTableProps: {
            striped: true,
            verticalSpacing: "xs",
            sx: {
                tableLayout: 'fixed',
                fontFamily: 'Verdana, sans-serif',
                fontFamilyMonospace: 'Monaco, Courier, monospace'
            },
        },
        mantineTableBodyProps: {},
        mantineTableHeadProps: {
            sx: {
            }
        },
        mantineTableHeadCellProps: {
            sx: {
                fontFamily: 'Verdana, sans-serif',
                fontFamilyMonospace: 'Monaco, Courier, monospace'
            },
        },
        mantineTableFooterProps: {},
        data: data?.data?.content ?? [],
        initialState: {showColumnFilters: false, density: 'xs', showGlobalFilter: false},
        mantineToolbarAlertBannerProps: isError
            ? {
                color: 'red',
                children: error?.userMessage,
            }
            : undefined,
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
        rowCount: data?.data?.totalElements ?? 0,
        state: {
            isLoading,
            pagination,
            sorting,
            showAlertBanner: isError,
            showProgressBars: isLoading,
        },
        renderTopToolbar: ({table}) => {

            return (
                <ListTopToolbar table={table} recoilKey={recoilKey} refreshAll={refreshAll}
                                columns={columns} searchColumn={searchColumn} setSearchColumn={setSearchColumn} searchValue={searchValue} setSearchValue={setSearchValue} dateRange={dateRange} setSafeDateRange={setSafeDateRange} dateRangeColumn={dateRangeColumn} setDateRangeColumn={setDateRangeColumn}
                                createBtn={true} createOne={createOne} setEnableMantineFetch={setEnableMantineFetch}  />
            );
        },
        /*       renderDetailPanel: ({ row }) => {
                   return (<div className={"w-50"}><MenusUserEdit one={data?.data?.content.find(x => x[PK_NAME] === row?.original[PK_NAME])} refetch={refetch}/></div>)
               },*/
        mantineTableBodyRowProps: ({row}) => ({
            onClick: () => {
                handleRowClick({row})
            },
            sx: {cursor: 'pointer'},
        }),
        //   enableRowActions: true,
        localization: {...MRT_Localization_KO_CUSTOM, actions: "수정"}
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
        onePrevCycle({localOne: {[PK_NAME]: null}})
    }


    /*
    *   Event Handler
    * */
    const handleRowClick = async ({row}) => {

        refreshOne()
        // console.log('수정버튼클릭')
        // console.log(row)
        // console.log(table)
        if (isValidObject(one)) {
            // row.original[PK_NAME] : 현재 수정 버튼을 클릭한 row 의 PK 값
            // one[PK_NAME] : 이전에 수정 버튼을 클릭하여 넣어진 recoil 의 PK 값
            if (row.original[PK_NAME] !== one[PK_NAME]) {
                // console.log("1 : 이전에 클릭한 것과 다른 row 를 클릭하였다.")

                onePrevCycle({localOne: row.original})
            } else {
                //  console.log("2 : 이전에 클릭한 것과 같은 row 를 클릭하였다.")
                // 중앙 저장소(recoil)의 현재 RECOIL_KEY에 해당하는 one 을 초기화 (위에서 초기화 하였음)
                // refreshOne()
                setGlobalSidebarCollapsed({
                    forceUpdate: Math.random(),
                    value: false
                })
            }
        } else {
            // console.log("3  : one 이 isValidObject 아니라는 의미는, 한번도 수정 버튼을 클릭한 적이 없다.")

            onePrevCycle({localOne: row.original})
        }
    }

    /*
     *       Fetch Cycle
     * */
    const onePrevCycle = async ({localOne}) => {
        // 이번에 클릭한 row 의 객체로 넣는다.
        setLoading(true)
        try {
            const re = await Promise.all([fetchDealersMetas(), fetchDeptsForCurrentDealer()]);
            setOne({
                ...localOne, meta: {
                    dealers: re[0] ? re[0] : [],
                    depts: re[1] ? sortDeptsByParentCd(re[1].map(x => ({
                        deptIdx: x.deptIdx,
                        parentCd: x.parentCd,
                        deptNm: x.deptNm,
                        depth: DeptHierarchyUtil.getDepthByDeptIdx(re[1], x.deptIdx)
                    }))) : []
                }
            });
            setGlobalSidebarCollapsed({
                forceUpdate: Math.random(),
                value: true
            })
        } finally {
            setLoading(false)
        }
    }

    /*
    *
    *   DB Fetch
    * */
    const fetchDealersMetas = async () => {
        try {
            const re = await agent.Dealer.fetchMetas();
            if (re.statusCode === 200) {
                if (!Array.isArray(re.data)) {
                    alert("딜러 메타 데이터가 확인되지 않습니다. 관리자에게 문의 하십시오.");
                    return null; // Return null if the data is invalid
                } else {
                    return re.data; // Return the fetched metas
                }
            } else {
                renderError({errorObj: re});
                return null; // Return null if there's an error
            }
        } catch (error) {
            console.error("Error fetching dealer metas:", error);
            return null; // Return null in case of error
        }
    };

    const fetchDeptsMetas = async () => {
        try {
            const re = await agent.Dept.fetchMetas();
            if (re.statusCode === 200) {
                if (!Array.isArray(re.data)) {
                    alert("조직 메타 데이터가 확인되지 않습니다. 관리자에게 문의 하십시오.");
                    return null; // Return null if the data is invalid
                } else {
                    return re.data; // Return the fetched metas
                }
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
                    <Col xl={!isValidObject(one) ? 12 : 6}>
                        <MantineReactTable table={table}/>
                    </Col>
                    <Col className={"mt-2"}>
                        {isCreateOne(one, PK_NAME) ?
                            <SettingsUsersCreate refreshAll={refreshAll} refreshOne={refreshOne} recoilKey={recoilKey} PK_NAME={PK_NAME}/>
                            : isUpdateOne(one, PK_NAME) ?
                                <SettingsUsersUpdate createOne={createOne} refreshAll={refreshAll}
                                                     refreshOne={refreshOne} recoilKey={recoilKey} PK_NAME={PK_NAME}/> : ""}
                    </Col>
                </Row>

            </LoadingOverlay>
        </Fragment>)
};


export default SettingsUsersList;