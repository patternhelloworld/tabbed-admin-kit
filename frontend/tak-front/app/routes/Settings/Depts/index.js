import React, {useEffect, useState, Fragment, useCallback} from 'react';
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

import SettingsDeptsUpdate from "./One/SettingsDeptsUpdate";

/* 아이콘은 여기서 찾으세요 : https://tabler.io/icons */
import {IconRefresh, IconEdit, IconSquareRoundedX, IconTrash, IconRestore} from '@tabler/icons-react';

import agent from "shared/api/agent";

import {useMantineFetch} from "shared/hooks/useMantineFetch";
import {useMantineMeta} from "shared/hooks/useMantineMeta";

import {useRecoilCallback, useRecoilValue, useResetRecoilState, useSetRecoilState} from "recoil";
import {
    boardUpdateOneSelector, boardUpdateResetModifiedOneSelector,
    boardUpdateResetOneSelector,
    boardUpdateState
} from "../../../shared/recoil/board/boardUpdateState";
import classNames from "classnames";
import {isCreateOne, isUpdateOne, isValidArray, isValidObject, DeptHierarchyUtil} from "shared/utils/utilities";
import {boardListResetSelector, boardListState} from "shared/recoil/board/boardListState";
import {MRT_Localization_KO_CUSTOM} from "shared/localization/mantine/custom";
import {globalInfoAccessTokenUserInfoSelector, globalInfoSidebarCollapsedSelector} from "shared/recoil/globalInfoState";

import SquareNumberIcon from "assets/images/square-number/SquareNumberIcon";
import LoadingOverlay from "react-loading-overlay";
import ClockLoader from "react-spinners/ClockLoader";
import {CRUD_COLUMNS, isAuthorized} from "../../../shared/utils/authorization";
import SettingsDeptsCreate from "./One/SettingsDeptsCreate";
import {sortDeptsByParentCd} from "./util";
import {ButtonWrapper} from "../../../shared/components/OptimizedHtmlElements";
import {useListSearchModuleMeta} from "../../../shared/hooks/useListSearchModuleMeta";
import getCustomersListColumns from "../../Customers/List/columns";
import getSettingsDeptsColumns from "./columns";

/*
*
*   아래 Box 에서 쓸수 있는 스타일링 문법 : https://mantine.dev/styles/style-props/
*
* */

const PK_NAME = "deptIdx";
const SettingsDeptsList = ({recoilKey, ...props}) => {

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

    const columns =  React.useMemo(() => getSettingsDeptsColumns({ PK_NAME }), [])

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
    } = useMantineMeta({columns, recoilKey});



    //call our custom react-query hook
    const {data, error, isError, isFetching, isLoading, refetch} = useMantineFetch({
        columnFilterFns,
        columnFilters,
        globalFilter,
        pagination,
        sorting,
        fetchFunc: agent.Dept.fetchForCurrentDealer,
        cacheKey: recoilKey,
        enabled: enableMantineFetch,
        setEnabled: setEnableMantineFetch,
        isFirstRender: mantineFetchFirst,
        setIsFirstRender: setMantineFetchFirst
    });

    const getContent =  useCallback(() => {
        if (data && data.data && data.data.content) {
            return data.data.content.map(item => {
                return {
                    ...item,
                    depts: data.data.content.map(x => ({
                        deptIdx: x.deptIdx,
                        parentCd: x.parentCd,
                    }))
                };
            });
        }
        return [];
    }, [data]);

    const table = useMantineReactTable({
        columns,
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
        mantineTableHeadProps: {
            sx: {
            }
        },
        mantineTableBodyProps: {},
        mantineTableHeadCellProps: {
            sx: {
                fontFamily: 'Verdana, sans-serif',
                fontFamilyMonospace: 'Monaco, Courier, monospace'
            },
        },
        mantineTableFooterProps: {},
        data: sortDeptsByParentCd(getContent()) ?? [],
        enableColumnActions: false,
        enableColumnFilters: false,
        enableSorting: false,
        enableColumnFilterModes: false,
        enableColumnFilter: false,
        enablePagination: false,
        mantineToolbarAlertBannerProps: isError
            ? {
                color: 'red',
                children: error.userMessage,
            }
            : undefined,
        state: {
            showAlertBanner: isError,
            showProgressBars: isFetching
        },
        renderTopToolbar: ({table}) => {

            return (
                <Flex p="md" justify="space-between">
                    <Flex gap="lg">
                        <ButtonWrapper
                            color={"dark"}
                            btnText={"신규"}
                            handleClick={createOne}
                            me={me}
                            recoilKey={recoilKey}
                            crudColumn={CRUD_COLUMNS.CREATE}
                        />
                    </Flex>
                </Flex>
            );
        },
        mantineTableBodyRowProps: ({row}) => ({
            onClick: () => {
                handleRowClick({row})
            },
            sx: {cursor: 'pointer'},
        }),
        //   enableRowActions: true,
        localization: {...MRT_Localization_KO_CUSTOM, actions: "수정"}
    });


    const createOne = () => {
        refreshOne()
        setOne({[PK_NAME]: null, depts: getContent()})
    }


    /*
    *   Event Handler
    * */
    const handleRowClick = async ({row}) => {

        refreshOne()

        console.log('수정버튼클릭')
        console.log(row)
        console.log(table)
        if (isValidObject(one)) {
            // row.original[PK_NAME] : 현재 수정 버튼을 클릭한 row 의 PK 값
            // one[PK_NAME] : 이전에 수정 버튼을 클릭하여 넣어진 recoil 의 PK 값
            if (row.original[PK_NAME] !== one[PK_NAME]) {
                console.log("1 : 이전에 클릭한 것과 다른 row 를 클릭하였다.")
                setOne({...row.original, depts: getContent()})
            } else {
                console.log("2 : 이전에 클릭한 것과 같은 row 를 클릭하였다.")
                // 중앙 저장소(recoil)의 현재 RECOIL_KEY에 해당하는 one 을 초기화
                setGlobalSidebarCollapsed({
                    forceUpdate: Math.random(),
                    value: false
                })
            }
        } else {
            console.log("3  : one 이 isValidObject 아니라는 의미는, 한번도 수정 버튼을 클릭한 적이 없다.")

            setOne({...row.original, depts: getContent()})
            setGlobalSidebarCollapsed({
                forceUpdate: Math.random(),
                value: true
            })
        }
    }


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
                    <Col lg={!isValidObject(one) ? 12 : 6}>
                        <MantineReactTable table={table}/>
                    </Col>
                    <Col>
                        {isCreateOne(one, PK_NAME) ? <SettingsDeptsCreate refreshAll={refreshAll} refreshOne={refreshOne} recoilKey={recoilKey}/>
                            : isUpdateOne(one, PK_NAME) ?
                                <SettingsDeptsUpdate createOne={createOne} refreshAll={refreshAll} refreshOne={refreshOne} recoilKey={recoilKey}/> : ""}
                    </Col>
                </Row>

            </LoadingOverlay>
        </Fragment>)
};


export default SettingsDeptsList;