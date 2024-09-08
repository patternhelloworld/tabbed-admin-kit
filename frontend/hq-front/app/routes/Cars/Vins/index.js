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
    CodeGeneralUtil,
    DeptHierarchyUtil,
    isArray,
    isCreateOne,
    isUpdateOne,
    isValidArray,
    isValidObject
} from "../../../shared/utils/utilities";
import {
    boardListResetSelector,
    boardListState,
    boardListOthersSelector
} from "../../../shared/recoil/board/boardListState";
import {MRT_Localization_KO_CUSTOM} from "../../../shared/localization/mantine/custom";
import {renderError} from "../../../shared/utils/CommonErrorHandler";
import {
    globalInfoAccessTokenUserInfoSelector,
    globalInfoSidebarCollapsedSelector
} from "../../../shared/recoil/globalInfoState";

import CarsVinsUpdate from "./One/VinsUpdate";

import LoadingOverlay from 'react-loading-overlay'
import ClockLoader from 'react-spinners/ClockLoader';
import {Link} from "react-router-dom";
import {CRUD_COLUMNS, isAuthorized} from "../../../shared/utils/authorization";
import CarsVinsCreate from "./One/VinsCreate";
import {ButtonWrapper} from "../../../shared/components/OptimizedHtmlElements";
import ListSearchModule from "../../../shared/components/List/ListSearchModule";
import {useListSearchModuleMeta} from "../../../shared/hooks/useListSearchModuleMeta";
import {func} from "prop-types";
import ListTopToolbar from "../../../shared/components/List/ListTopToolbar";
import {sortDeptsByParentCd} from "../../Settings/Depts/util";
import {
    Input
} from 'reactstrap';
import {Label} from "../../../components/recharts";
import { useMediaQuery } from '@mantine/hooks';
import { IconDeviceMobileMessage, IconMoodSearch } from '@tabler/icons-react';

import getVinsColumns from "./columns";
import {
    fetchDistinctYearsSearch,
    fetchCarMakersSearch,
    fetchCarModelsSearch,
    fetchCarModelDetailsSearch,
} from "./prevFetchFuncs";

/*
*
*
*
*
*
*    아래 Box 에서 쓸수 있는 스타일링 문법 : https://mantine.dev/styles/style-props/
*
* */

const PK_NAME = "vinIdx";

const CarsVins = ({recoilKey, ...props}) => {
    const isMobile = useMediaQuery('(max-width: 900px)');
    /*
    *   Recoil
    *    : 읽기 - useRecoilValue, 쓰기 - useSetRecoilState, 초기화 - useResetRecoilState
    *
    *      one 에다가 곧장 넣은 사례 :  Settings/Users/Menus/One, 넣는 것은 index.js 에서 넣는다.
    * */

    // 읽기 전용
    const globalSidebarCollapsed = useRecoilValue(globalInfoSidebarCollapsedSelector());

    const one = useRecoilValue(boardUpdateOneSelector({recoilKey}));

    // 읽기 전용 - 검색창
    const years = useRecoilValue(boardListOthersSelector({recoilKey, topic: "years"}));
    const selectedYearValue = useRecoilValue(boardListOthersSelector({recoilKey, topic: "selectedYearValue"}));

    const carMakers = useRecoilValue(boardListOthersSelector({recoilKey, topic: "carMakers"}));
    const selectedCarMakerIdx = useRecoilValue(boardListOthersSelector({recoilKey, topic: "selectedCarMakerIdx"}));

    const carModels = useRecoilValue(boardListOthersSelector({recoilKey, topic: "carModels"}));
    const selectedCarModelIdx = useRecoilValue(boardListOthersSelector({recoilKey, topic: "selectedCarModelIdx"}));

    const carModelDetails = useRecoilValue(boardListOthersSelector({recoilKey, topic: "carModelDetails"}));
    const selectedCarModelDetailIdx = useRecoilValue(boardListOthersSelector({recoilKey, topic: "selectedCarModelDetailIdx"}));


    // 쓰기 전용
    const setGlobalSidebarCollapsed = useSetRecoilState(globalInfoSidebarCollapsedSelector());
    const setOne = useSetRecoilState(boardUpdateOneSelector({recoilKey}));


    // 쓰기 전용 - 검색창
    const setYears = useSetRecoilState(boardListOthersSelector({recoilKey, topic: "years"}));
    const setSelectedYearValue = useSetRecoilState(boardListOthersSelector({
        recoilKey,
        topic: "selectedYearValue"
    }));

    const setCarMakers = useSetRecoilState(boardListOthersSelector({recoilKey, topic: "carMakers"}));
    const setSelectedCarMakerIdx = useSetRecoilState(boardListOthersSelector({
        recoilKey,
        topic: "selectedCarMakerIdx"
    }));

    const setCarModels = useSetRecoilState(boardListOthersSelector({recoilKey, topic: "carModels"}));
    const setSelectedCarModelIdx = useSetRecoilState(boardListOthersSelector({
        recoilKey,
        topic: "selectedCarModelIdx"
    }));

    const setCarModelDetails = useSetRecoilState(boardListOthersSelector({recoilKey, topic: "carModelDetails"}));
    const setSelectedCarModelDetailIdx = useSetRecoilState(boardListOthersSelector({
        recoilKey,
        topic: "selectedCarModelDetailIdx"
    }));




    /// 선택한 One 만 Reset
    const resetOne = useResetRecoilState(boardUpdateResetOneSelector({recoilKey}));
    const resetModifiedOne = useResetRecoilState(boardUpdateResetModifiedOneSelector({recoilKey}));

    // 세션
    const me = useRecoilValue(globalInfoAccessTokenUserInfoSelector());

    /// 초기화 : 검색 창을 포함한 모든 위에 것들 (boardList Recoil)
    const resetList = useRecoilCallback(({set}) => (recoilKey) => {
        set(boardListResetSelector({recoilKey}), null);
    });

    const [loading, setLoading] = useState(false);
    const [enableMantineFetch, setEnableMantineFetch] = useState(null);
    const [mantineFetchFirst, setMantineFetchFirst] = useState(true);



    const columns = React.useMemo(() => getVinsColumns({ PK_NAME}), []);




    /*
    *   특수 검색 : "연도 검색"
    * */
    const handleYears = async (always = false) => {
        if(years.length === 0 || always) {
            const re = await Promise.all([fetchDistinctYearsSearch()]);
            if(!re[0]){
                // undefined 가 박히면 오류 발생
                setYears([])
            }else{
                setYears(re[0])
            }
        }
    }
    /*
    *   특수 검색 : "자동차 제조사"
    * */
    const handleCarMakers = async (always = false) => {
        if(carMakers.length === 0 || always) {
            const re = await Promise.all([fetchCarMakersSearch()]);
            if(!re[0]){
                // undefined 가 박히면 오류 발생
                setCarMakers([])
            }else{
                setCarMakers(re[0])
            }
        }
    }
    /*
    *   특수 검색 : "자동차 제조사에 따른 자동차 모델 검색"
    * */
    const handleCarModels = async (always = false) => {
        if(!selectedCarMakerIdx){
            setCarModels([]);
        }else {
            if (carModels.length === 0 || always) {
                const re = await Promise.all([fetchCarModelsSearch(selectedCarMakerIdx)]);
                if(!re[0]){
                    // undefined 가 박히면 오류 발생
                    setCarModels([])
                }else{
                    setCarModels(re[0])
                    // 여기 select box 변경 시 width 가 늘어나서, UX 를 고려하여 sidebar collapse
                    if(!globalSidebarCollapsed.value) {
                        setGlobalSidebarCollapsed({
                            forceUpdate: Math.random(),
                            value: true
                        })
                    }
                }
            }
        }
    }
    /*
    *   특수 검색 : "자동차 모델에 따른 세부 모델 검색"
    * */
    const handleCarModelDetails = async (always = false) => {
        if(!selectedCarModelIdx){
            setCarModelDetails([])
        }else {
            if (carModelDetails.length === 0 || always) {
                const re = await Promise.all([fetchCarModelDetailsSearch(selectedCarModelIdx)]);
                if(!re[0]){
                    // undefined 가 박히면 오류 발생
                    setCarModelDetails([])
                }else{
                    setCarModelDetails(re[0])
                }
            }
        }
    }


    const refreshOne = () => {
        resetOne()
        resetModifiedOne()
    }

    const refreshAll = async () => {
        resetList(recoilKey)

        refreshOne()
        listPrevCycle(true)
        setEnableMantineFetch(true)
    }

    const listPrevCycle = (always) => {

        // 연도 검색 기능
        handleYears(always);
        // 자동차 제조사
        handleCarMakers(always)

    }

    // 여기는 1) 최초 진입하거나 2) 다른 탭에 갔다가 오는 경우 (이 또한 recoil 이 유지 될 뿐 최초 진입이나 다름 없음)
    useEffect(() => {
        setMantineFetchFirst(true)
        setEnableMantineFetch(true)

        listPrevCycle(false)
    }, [])

    // 여기는 "메이커" 가 변할 때마다 "모델" 과 "세부 모델" 이 변하는 것을 구현
    useEffect(() => {
        handleCarModels(true)
    }, [selectedCarMakerIdx])


    // 여기는 "모델" 이 변할 때마다 "세부 모델" 이 변하는 것을 구현
    useEffect(() => {
        handleCarModelDetails(true)
    }, [selectedCarModelIdx])

    /*
    *
    *  Mantine 의 검색 조건 (Meta) 을 담당
    *    : columnFilters, columnFilterFns, globalFilter : UI 변경으로 더 이상 미사용
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
        fetchFunc: agent.Vin.fetch,
        cacheKey: recoilKey,
        additionalSearchFilter: {...createSearchFilter(),
            carModelDetailYear : selectedYearValue, carMakerIdx : selectedCarMakerIdx, carModelIdx : selectedCarModelIdx, carModelDetailIdx : selectedCarModelDetailIdx },
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
            highlightOnHover: false,
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
                                //createOne={createOne}
                                setEnableMantineFetch={setEnableMantineFetch}
                            >
                                <Input
                                    type="select"
                                    id="years"
                                    name="years"
                                    className={"mr-2 mb-2"}
                                    value={selectedYearValue}
                                    defaultValue={""}
                                    onChange={(e) => setSelectedYearValue(e.target.value)}
                                    required
                                >
                                    {years.map((year) => {
                                        // Assuming years is an array of year numbers or objects with a 'year' property
                                        const displayText = year; // Directly use the year value if it's an array of numbers

                                        return (
                                            <option key={year} value={year}>
                                                {displayText}
                                            </option>
                                        );
                                    })}
                                    <option key="-" value=""><b>전체 연도</b></option> {/* For displaying all years */}
                                </Input>


                                <Input
                                    type="select"
                                    id="carMakers"
                                    name="carMakers"
                                    className={"mr-2 mb-2"}
                                    value={selectedCarMakerIdx}
                                    defaultValue={""}
                                    onChange={(e) => setSelectedCarMakerIdx(e.target.value)}
                                    required
                                >
                                    {carMakers.map((option) => {
                                        // 표시할 텍스트 결정
                                        const displayText = option.carMakerNm ? option.carMakerNm : ""; // Assume carMakers has a property named makerName

                                        return (
                                            <option key={option.carMakerIdx} value={option.carMakerIdx}>
                                                {displayText}
                                            </option>
                                        );
                                    })}
                                    <option key="-" value=""><b>전체 메이커</b></option> {/* For displaying all makers */}
                                </Input>

                                <Input
                                    type="select"
                                    id="carModels"
                                    name="carModels"
                                    className={"mr-2 mb-2"}
                                    value={selectedCarModelIdx}
                                    defaultValue={""}
                                    onChange={(e) => setSelectedCarModelIdx(e.target.value)}
                                    required
                                >
                                    {carModels.map((option) => {
                                        // 표시할 텍스트 결정
                                        const displayText =
                                            (option.modelCode || "") + " " +
                                            (option.modelName || "") + " " +
                                            (option.svcCode || "") + " " +
                                            (option.svcName || "");

                                        return (
                                            <option key={option.carModelIdx} value={option.carModelIdx}>
                                                {displayText}
                                            </option>
                                        );
                                    })}
                                    <option key="-" value=""><b>전체 모델 (메이커 먼저 선택)</b></option> {/* For displaying all models */}
                                </Input>


                                <Input
                                    type="select"
                                    id="carModelDetails"
                                    name="carModelDetails"
                                    className={"mr-2 mb-2"}
                                    value={selectedCarModelDetailIdx}
                                    defaultValue={""}
                                    onChange={(e) => setSelectedCarModelDetailIdx(e.target.value)}
                                    required
                                >
                                    {carModelDetails.map((option) => {
                                        // 표시할 텍스트 결정
                                        const displayText =
                                            (option.name ? option.name : "") + " " +
                                            (option.code ? option.code : "") + " " +
                                            (option.motorType ? option.motorType : "") + " " +
                                            (option.carName ? option.carName : "");

                                        return (
                                            <option key={option.carModelDetailIdx} value={option.carModelDetailIdx}>
                                                {displayText}
                                            </option>
                                        );
                                    })}
                                    <option key="-" value=""><b>전체 세부 모델 (모델 먼저 선택)</b></option> {/* For displaying all details */}
                                </Input>


                            </ListTopToolbar>

                        </Col>
                    </Row>

                </Fragment>
            );
        },
        /*       renderDetailPanel: ({ row }) => {
                   return (<div className={"w-50"}><MenusUserEdit one={data?.data?.content.find(x => x[PK_NAME] === row?.original[PK_NAME])} refetch={refetch}/></div>)
               },*/
        mantineTableContainerProps : ({row}) => ({
/*            onClick: (e) => {
                alert('aaa')
            },*/
            sx: {maxHeight  :"500px"},
        }),
        mantineTableBodyRowProps: ({row}) => ({
/*            onClick: (e) => {
                handleRowClick({row})
            },*/
            sx: {},
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


        enableMultiSort : false
       // positionPagination : "top"

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
            setOne({
                ...localOne, meta: {
                }
            });
            // 모달로 뜨므로 왼쪽 접을 필요 없어 보임
            /*        setGlobalSidebarCollapsed({
                        forceUpdate: Math.random(),
                        value: true
                    })*/
        } finally {
            setLoading(false)
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
                    <Col xl={12} >
                        <MantineReactTable table={table}/>
                    </Col>

                        <Modal
                            size={"75%"}
                            opened={isCreateOne(one, PK_NAME) || isUpdateOne(one, PK_NAME)}
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
                            {isCreateOne(one, PK_NAME) ? <CarsVinsCreate refreshAll={refreshAll} refreshOne={refreshOne}
                                                     recoilKey={recoilKey} PK_NAME={PK_NAME}/> : ""}
                            {isUpdateOne(one, PK_NAME) ? <CarsVinsUpdate createOne={createOne} refreshAll={refreshAll}
                                                     refreshOne={refreshOne} recoilKey={recoilKey} PK_NAME={PK_NAME}/> : ""}
                        </Modal>

                </Row>

            </LoadingOverlay>
        </Fragment>)
};

export default CarsVins;