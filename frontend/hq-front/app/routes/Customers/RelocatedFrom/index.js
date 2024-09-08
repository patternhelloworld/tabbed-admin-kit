import React, {useEffect, useState, Fragment} from 'react';
import {
    Row,
    Col
} from "./../../../components";
import {
    MantineReactTable,
    useMantineReactTable,MRT_TablePagination,
    MRT_GlobalFilterTextInput, MRT_ToggleDensePaddingButton,
    MRT_ToggleFiltersButton, MRT_ToolbarAlertBanner
} from 'mantine-react-table';
import {ActionIcon, Tooltip, Box, Button, Flex, Menu, Text, Title, Modal} from '@mantine/core';

/* 아이콘은 여기서 찾으세요 : https://tabler.io/icons */
import {IconRefresh, IconEdit, IconSquareRoundedX, IconTrash, IconRestore, IconCirclePlus, IconCloudSearch} from '@tabler/icons-react';

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
import {
    CodeGeneralUtil,
    DeptHierarchyUtil,
    isArray,
    isCreateOne,
    isUpdateOne,
    isValidArray,
    isValidObject
} from "shared/utils/utilities";
import {
    boardListResetSelector,
    boardListState,
    boardListOthersSelector
} from "shared/recoil/board/boardListState";
import {MRT_Localization_KO_CUSTOM} from "shared/localization/mantine/custom";
import {
    globalInfoAccessTokenUserInfoSelector,
    globalInfoSidebarCollapsedSelector
} from "shared/recoil/globalInfoState";

import SettingsCustomersUpdate from "../List/One/CustomersListUpdate";
import LoadingOverlay from 'react-loading-overlay'
import ClockLoader from 'react-spinners/ClockLoader';
import {useListSearchModuleMeta} from "shared/hooks/useListSearchModuleMeta";
import ListTopToolbar from "shared/components/List/ListTopToolbar";
import {sortDeptsByParentCd} from "../../Settings/Depts/util";
import {
    Input
} from 'reactstrap';
import { useMediaQuery } from '@mantine/hooks';
import { IconDeviceMobileMessage, IconMoodSearch } from '@tabler/icons-react';
import {
    fetchCodeCustomersMetas,
    fetchCustomerGroupsMetas,
    fetchDeptsForCurrentDealer,
    fetchUsersForCurrentDealer
} from "../List/prevFetchFuncs";
import UsersListSelectModal from "../../../shared/components/select-modal/UserSelectModalLocal";
import {updateCustomersUserManager} from "../List/othersUpdateFuncs";
import {
    findCustomerInfoLabelByValue,
    findCustomerTypeLabelByValue, findGenderLabelByValue, getCustomerInfoOptions,
    getCustomerTypeOptions, getGenderOptions, getYNCodeOptions
} from "shared/enums";

/*
*
*
*
*
*
*    아래 Box 에서 쓸수 있는 스타일링 문법 : https://mantine.dev/styles/style-props/
*
* */
const customer_PK_NAME = "customerIdx";
const PK_NAME = "relocatedLogIdx";
const SettingsCustomersRelocatedFromList = ({recoilKey, ...props}) => {
    const isMobile = useMediaQuery('(max-width: 900px)');
    /*
    *   Recoil
    *    : 읽기 - useRecoilValue, 쓰기 - useSetRecoilState, 초기화 - useResetRecoilState
    *
    *      one 에다가 곧장 넣은 사례 :  Settings/Users/Menus/One, 넣는 것은 index.js 에서 넣는다.
    * */

    // 읽기 전용
    const one = useRecoilValue(boardUpdateOneSelector({recoilKey}));
    const deptsWithUsersListOthers = useRecoilValue(boardListOthersSelector({recoilKey, topic: "deptsWithUsers"}));
    const deptWithUserListOthers = useRecoilValue(boardListOthersSelector({
        recoilKey,
        topic: "selectedDeptWithUser"
    }));
    const codeCustomersListOthers = useRecoilValue(boardListOthersSelector({recoilKey, topic: "codeCustomers"}));
    const selectedRowsOthers = useRecoilValue(boardListOthersSelector({
        recoilKey,
        topic: "selectedRows"
    }));

    // 쓰기 전용
    const setGlobalSidebarCollapsed = useSetRecoilState(globalInfoSidebarCollapsedSelector());

    const setOne = useSetRecoilState(boardUpdateOneSelector({recoilKey}));
    const setDeptsWithUsersListOthers = useSetRecoilState(boardListOthersSelector({recoilKey, topic: "deptsWithUsers"}));
    const setSelectedDeptWithUserListOthers = useSetRecoilState(boardListOthersSelector({
        recoilKey,
        topic: "selectedDeptWithUser"
    }));
    const setCodeCustomersListOthers = useSetRecoilState(boardListOthersSelector({recoilKey, topic: "codeCustomers"}));
    const setSelectedRowsOthers = useSetRecoilState(boardListOthersSelector({
        recoilKey,
        topic: "selectedRows"
    }));
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

    const [usersListSelectModalOpen, setUsersListSelectModalOpen] = useState(false);

    const columns = React.useMemo(
        () => [
            {
                accessorKey: 'relocateRegDt',
                header: '이관일',
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
                Cell: ({renderedCellValue, row, cell}) => {
                    return (
                        <Box
                            fz={"13px"}
                            fw={600}
                        >
                            {renderedCellValue}
                        </Box>
                    )
                },
            },
            {
                accessorKey: 'customerType',
                header: '고객구분',
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
                Cell: ({renderedCellValue, row, cell}) => {
                    return (
                        <Box
                            fz={"13px"}
                            fw={600}
                        >
                            {findCustomerTypeLabelByValue(renderedCellValue)}
                        </Box>
                    )
                },
                searchable: true,
                selectBoxList: [
                    ...getCustomerTypeOptions()
                ]
            },
            {
                accessorKey: 'customerInfo',
                header: '고객정보',
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
                Cell: ({renderedCellValue, row, cell}) => {
                    return (
                        <Box
                            fz={"13px"}
                            fw={600}
                        >
                            {findCustomerInfoLabelByValue(renderedCellValue)}
                        </Box>
                    )
                },
                searchable: true,
                selectBoxList: [
                    ...getCustomerInfoOptions()
                ]
            },
            {
                accessorKey: 'customerName',
                header: '고객',
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
                Cell: ({renderedCellValue, row, cell}) => {
                    return (
                        <Box
                            fz={"13px"}
                            fw={600}
                        >
                            {renderedCellValue}
                        </Box>
                    )
                },
                searchable: true
            },
            {
                accessorKey: 'fromUserManagerNm',
                header: '(이관전) 사원',
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
                Cell: ({renderedCellValue, row, cell}) => {
                    return (
                        <Box
                            fz={"13px"}
                            fw={600}
                        >
                            {renderedCellValue}
                        </Box>
                    )
                },
                enableSorting: false
            },
            {
                accessorKey: 'userManagerName',
                header: '(이관후) 사원',
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
                Cell: ({renderedCellValue, row, cell}) => {
                    return (
                        <Box
                            fz={"13px"}
                            fw={600}
                        >
                            {renderedCellValue}
                        </Box>
                    )
                },
                enableSorting: false
            },

            {
                accessorKey: 'hp',
                header: '헨드폰',
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
                Cell: ({renderedCellValue, row, cell}) => {
                    return (
                        <Box
                            fz={"13px"}
                        >
                            {renderedCellValue}
                        </Box>
                    )
                },
                enableSorting: false,
                searchable: true
            },
            {
                accessorKey: 'tel',
                header: '전화',
                Header: ({column, header, table}) => {
                    return (<Box
                        sx={{
                            fontSize: "13px",
                        }}
                    >
                        {column?.columnDef?.header}
                    </Box>);
                },
                Cell: ({renderedCellValue, row, cell}) => {
                    return (
                        <Box
                            fz={"13px"}
                            fw={600}
                        >
                            {renderedCellValue}
                        </Box>
                    )
                },
                enableSorting: false,
                searchable: true
            },
            {
                accessorKey: 'gender',
                header: '성별',
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
                Cell: ({renderedCellValue, row, cell}) => {
                    return (
                        <Box
                            fz={"13px"}
                            fw={600}
                        >
                            {findGenderLabelByValue(renderedCellValue)}
                        </Box>
                    )
                },
                searchable: true,
                selectBoxList: [
                    ...getGenderOptions()
                ]
            },
            {
                accessorKey: 'birthDate',
                header: '생일',
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
                Cell: ({renderedCellValue, row, cell}) => {
                    return (
                        <Box
                            fz={"13px"}
                            fw={600}
                        >
                            {'m'}
                        </Box>
                    )
                },
                searchable : false,
                dateRangeSearchable: true
            },
            {
                accessorKey: 'regDt',
                header: '등록일',
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
            },
        ],
        [],
    );

    const listPrevCycle = (always) => {
        // 조직 + 담당자 검색 기능
        setDeptsWithUsers(always);
        // Code 테이블 의존 Select box : (예) 접촉 경로
        setCodeCustomersMetas(always);
    }

    /*
    *   셀렉트 박스 : 접촉 경로
    * */
    const setCodeCustomersMetas = async (always = false) => {
        // 이번에 클릭한 row 의 객체로 넣는다.
        if(codeCustomersListOthers.length === 0 || always) {
            const re = await Promise.all([fetchCodeCustomersMetas()]);
            setCodeCustomersListOthers(re[0])
        }
    }


    /*
    *   특수 검색 : "담당 조직 또는 사원으로 검색"
    * */
    const setDeptsWithUsers = async (always = false) => {
        if(deptsWithUsersListOthers.length === 0 || always) {
            const deptsWithUsers = await getDeptsWithUsers();
            setDeptsWithUsersListOthers(deptsWithUsers)
        }
    }
    const getDeptsWithUsers = async () => {
        const re = await Promise.all([fetchDeptsForCurrentDealer(), fetchUsersForCurrentDealer()]);

        if (re[0] && re[1]) {
            // depts
            const depts = sortDeptsByParentCd(re[0].map(x => ({
                deptIdx: x.deptIdx,
                parentCd: x.parentCd,
                deptNm: x.deptNm,
                depth: DeptHierarchyUtil.getDepthByDeptIdx(re[0], x.deptIdx)
            })));
            // users
            const users = re[1];

            return DeptHierarchyUtil.mergeDeptsAndUsers(depts, users);
        } else {
            return [];
        }
    };

    const refreshOne = () => {
        resetOne()
        resetModifiedOne()
    }

    const refreshAll = async () => {
        resetList(recoilKey)
        // resetList 물론 rowSelection 정보가 날아가지만 현재 아래에 useMantineTable 에서 state 에 rowSelection 을 mantineMeta 를 통해 관리하고 있지 않으므로, 동기화 필요.
        refreshOne()
        listPrevCycle(true)
        setEnableMantineFetch(true)
    }

    useEffect(()=>{
        if(enableMantineFetch) {
            table.resetRowSelection();
        }
    },[enableMantineFetch])

    // 여기는 1) 최초 진입하거나 2) 다른 탭에 갔다가 오는 경우 (이 또한 recoil 이 유지 될 뿐 최초 진입이나 다름 없음)
    useEffect(() => {
        setMantineFetchFirst(true)
        setEnableMantineFetch(true)

        listPrevCycle(false)
    }, [])


    /*
    *
    *  Mantine 의 검색 조건 (Meta) 을 담당
    *    : columnFilters, columnFilterFns, globalFilter : 더 이상 미사용
    *    : sorting, pagination : 사용 중
    * */
    const {
        columnFilters, setColumnFilters,
        columnFilterFns, setColumnFilterFns,
        globalFilter, setGlobalFilter,
        sorting, setSorting,
        pagination, setPagination
    } = useMantineMeta({columns, recoilKey});


    /*
    *
    *   Mantine 의 검색 조건 (Meta) 을 담당 (그런데, Mantine 에서 제공하는 것이 아닌, <ListSearchModule/> 을 사용)
    *    : UI 를 보시면 ( dateRangeColumn , dateRange, searchColumn, searchValue )
    *
    * */
    const {
        createSearchFilter, createDateRangeFilter,
        searchColumn, setSearchColumn, searchValue, setSearchValue,
        dateRangeColumn, setDateRangeColumn,
        dateRange, setSafeDateRange
    } = useListSearchModuleMeta({recoilKey});

    /*
    *
    *   실제 DB 가져오기
    *
    * */

    const {data, error, isError, isFetching, isLoading} = useMantineFetch({
        columnFilterFns,
        columnFilters,
        globalFilter,
        pagination,
        sorting,
        fetchFunc: agent.RelocateLog.fetch,
        cacheKey: recoilKey,
        additionalSearchFilter: {...createSearchFilter(), deptWithUser : deptWithUserListOthers },
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
        mantinePaginationProps: {
            radius: 'xl',
            size: 'md',
        },
        mantineTableProps: {
            striped: true,
            verticalSpacing: "xs",
            sx: {

                fontFamily: 'Verdana, sans-serif',
                fontFamilyMonospace: 'Monaco, Courier, monospace'
            },
        },
        mantineTableBodyProps: {},
        mantineTableHeadProps: {
            sx: {
           //    zIndex: 1.99
            }
        },
        mantineTableHeadCellProps: {
            sx: {
                fontFamily: 'Verdana, sans-serif',
                fontFamilyMonospace: 'Monaco, Courier, monospace'
            },
        },
        mantineTopToolbarProps :{
            sx: {
            }
        },
        mantineTableFooterProps: {},
        data: data?.data?.content ?? [],
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
                children: error?.userMessage,
            }
            : undefined,
        rowCount: data?.data?.totalElements ?? 0,
        state: {
            pagination,
            sorting,
           // rowSelection : selectedRowsOthers,
            isLoading,
            showAlertBanner: isError,
        },
        renderTopToolbar: ({table}) => {

            return (
                <Fragment>
                    <Row>
                        <Col className={"ml-4"}>
                            <ListTopToolbar
                                table={table}
                                recoilKey={recoilKey}
                                refreshAll={refreshAll}
                                columns={columns}
                                searchColumn={searchColumn}
                                setSearchColumn={setSearchColumn}
                                searchValue={searchValue}
                                setSearchValue={setSearchValue}
                                dateRange={dateRange}
                                setSafeDateRange={setSafeDateRange}
                                dateRangeColumn={dateRangeColumn}
                                setDateRangeColumn={setDateRangeColumn}
                                createBtn={false}
                                createOne={null}
                                setEnableMantineFetch={setEnableMantineFetch}
                                searchModuleShow={false}
                            >
                                <Input
                                    style={{ width : "300px"}}
                                    type="select"
                                    id="deptsWithUsers"
                                    name="deptsWithUsers"
                                    className={"mr-2 mb-2"}
                                    value={deptWithUserListOthers.compositeValue}
                                    defaultValue={""}
                                    onChange={(e) => setSelectedDeptWithUserListOthers(e.target.value)}
                                    required
                                >
                                    {deptsWithUsersListOthers.map((option) => {
                                        const value = JSON.stringify({
                                            deptIdx: option.deptIdx,
                                            deptNm: option.deptNm,
                                            userIdx: option.userIdx,
                                            userId : option.userId,
                                            userName: option.userName,
                                            depth: option.depth
                                        });

                                        // depth 만큼 공백 추가
                                        const spaces = '\u00A0'.repeat(option.depth * 2); // 각 depth에 대해 4칸 공백 추가

                                        // 표시할 텍스트 결정
                                        const displayText = !option.userIdx ? option.deptNm : option.userName;

                                        return (
                                            <option key={option.deptIdx + "-" + option.userIdx} value={value}>
                                                {spaces}[{option.depth}] {displayText}
                                            </option>
                                        );
                                    })}
                                    <option key="-" value="">조직 및 담당자로 검색</option>
                                </Input>

                            </ListTopToolbar>

                        </Col>
                    </Row>
                    <Row>
                        <Col lg={7} className={"d-flex align-items-center ml-4 mb-4"}>
                            <Button
                                className={classNames('ml-2')}
                                leftIcon={<IconDeviceMobileMessage size={14}/>}
                                variant="gradient"
                                gradient={{ from: 'orange', to: 'red' }}
                                /*disabled={!(table.getIsSomeRowsSelected() || table.getIsAllRowsSelected())}*/
                                onClick={handleSMSSendAction}
                            >
                                SMS전송
                            </Button>
                            <Button
                                className={classNames('ml-2')}
                                leftIcon={<IconMoodSearch size={14}/>}
                                variant="gradient"
                                gradient={{ from: 'orange', to: 'red' }}
                                /*disabled={!(table.getIsSomeRowsSelected() || table.getIsAllRowsSelected())}*/
                                onClick={handleUserChangeAction}
                            >
                                담당사원변경
                            </Button>
                        </Col>
                    </Row>
                </Fragment>
            );
        },
        mantineTableContainerProps : ({row}) => ({
            sx: {cursor: 'pointer', maxHeight  :"500px"},
        }),
        mantineTableBodyRowProps: ({row}) => ({
            onClick: (e) => {
                handleRowClick({row})
            },
            sx: {cursor: 'pointer'},
        }),
        //   enableRowActions: true,
        localization: {...MRT_Localization_KO_CUSTOM, actions: "수정"},
        paginationDisplayMode: 'pages',
        renderBottomToolbar: ({table}) => {
            return (<Fragment>
                <Row>
                    <Col lg={12} className={"d-flex align-items-center ml-4 mt-1"}>
                        <MRT_TablePagination table={table}  />
                    </Col>
                </Row>
            </Fragment>)
        },
        enableRowSelection: true,
        mantineSelectCheckboxProps: {
            color: 'orange',
        },
        enableMultiSort : false

    });


    // 여기는 1) 최초 진입하거나 2) 다른 탭에 갔다가 오는 경우 (이 또한 recoil 이 유지 될 뿐 최초 진입이나 다름 없음)
    // 아래 table 객체는 위 1),2) 경우를 제외하고는 table 객체 내 다른 상태가 변한다고 해서 감지되지 않음을 확인.
    useEffect(() => {

        // 이 것을 안해주면 예를 들어 2페이지에 있다가 다른 탭에 갔다오면 1페이지로 초기화 되는 버그가 있다.
        // 이는 Mantine table 이 참조하는 Tanstack 테이블 문제로 보인다.
        // https://www.mantine-react-table.com/docs/api/table-options#onPaginationChange-prop
        // pagination recoil 은 문제가 없다. 그렇다고, const table 자체를 모두 recoil 저장 하려면 안에 수 많은 함수들과 참조 때문에 저장 실패한다.
        // 초기화 되는 문제가 있는 다른 것들이 발견된다면 여기서 set 한다.
        table.setPagination(pagination)
        // console.log("aaa")
        // console.log(selectedRowsOthers)
        table.setRowSelection(selectedRowsOthers);

    }, [table])

    useEffect(() => {
        // console.log("bbb")
        // console.log(table.getState())
        // console.log(table.getSelectedRowModel().rows)
        setSelectedRowsOthers(table.getState().rowSelection)
    }, [table.getState().rowSelection])

    const handleSMSSendAction = () => {
        if(table.getSelectedRowModel().rows.length < 1) {
            alert('선택된 고객이 없습니다.');
            return;
        }

        alert('개발 준비 중');
        table.getSelectedRowModel().flatRows.map((row) => {
            console.log(row);
        });
    };

    const handleUserChangeAction = () => {
        if(table.getSelectedRowModel().rows.length < 1) {
            alert('선택된 고객이 없습니다.');
            return;
        }
        setUsersListSelectModalOpen(true)
    };


    /*
    *   Event Handler
    * */
    const handleRowClick = async ({row}) => {
        refreshOne();
        const customerRow = await fetchCustomerData(row.original.customerIdx);

        onePrevCycle({localOne: customerRow})
        setGlobalSidebarCollapsed({
            forceUpdate: Math.random(),
            value: true
        })
    }

    const fetchCustomerData = async (customerIdx) => {
        try {
            const searchFilter = JSON.stringify({ customerIdx: customerIdx });
            const re = await Promise.all([agent.Customer.fetch({
                searchFilter: searchFilter
            })]);

            if (!re[0].statusCode === 200) {
                throw new Error(`서버 응답 오류: ${re[0].statusCode}`);
            }

            const data = re[0].data;
            return data.content[0];

        } catch (error) {
            console.error('데이터 fetch 중 에러 발생:', error);
            return [];
        }
    };


    /*
     *       Fetch Cycle
     * */
    const onePrevCycle = async ({localOne}) => {
        // 이번에 클릭한 row 의 객체로 넣는다.
        setLoading(true)
        try {
            const re = await Promise.all([fetchCodeCustomersMetas(), fetchCustomerGroupsMetas()]);
            setOne({
                ...localOne, meta: {
                    codeCustomers: re[0] ? re[0] : [],
                    customerGroups : re[1] ? re[1] : []
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

    const handleUserSelected = (originalUserRow) => {
        // 선택된 행의 고객 이름을 문자열로 합치기
        const customersStr = table.getSelectedRowModel().rows.map(x => x.original.customerName).join(',');

        // 확인 메시지 구성
        const confirmationMessage =
            `${originalUserRow.dealerNm} (${originalUserRow.deptNm}) 소속의 ${originalUserRow.name} 직원에게 ${customersStr} 고객을 담당하게 하시겠습니까?`;

        // 사용자 확인
        if (confirm(confirmationMessage)) {
            updateCustomersUserManager({ customerIdxList : table.getSelectedRowModel().
                rows.map(x => x.original.customerIdx), userIdx : originalUserRow.userIdx, refreshAll });

            setUsersListSelectModalOpen(false);
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
            <UsersListSelectModal
                onUserSelected={handleUserSelected}
                userSelectModalOpen={usersListSelectModalOpen} setUserSelectModalOpen={setUsersListSelectModalOpen}/>
            <LoadingOverlay
                spinner={<ClockLoader color="#ffffff" size={20}/>}
                active={loading}
            >
                <Row>
                    <Col xl={12} >
                        <MantineReactTable table={table}/>
                    </Col>

                    <Modal
                        size={"75%"}
                        opened={isUpdateOne(one, customer_PK_NAME)}
                        onClose={()=>{
                            refreshOne()
                        }}
                        style={{
                            overflow : "auto"
                        }}
                        zIndex={100000}
                        fullScreen={isMobile}
                        overlayProps={{
                            backgroundOpacity: 0.55,
                            blur: 3,
                        }}
                    >
                        {isUpdateOne(one, customer_PK_NAME) ? <SettingsCustomersUpdate refreshAll={refreshAll} injectedGlobalReadOnly={true}
                                                                                       refreshOne={refreshOne} recoilKey={recoilKey} PK_NAME={customer_PK_NAME}/> : ""}
                    </Modal>
                </Row>
            </LoadingOverlay>
        </Fragment>)
};

export default SettingsCustomersRelocatedFromList;