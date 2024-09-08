import React, {useEffect, useState, Fragment} from 'react';
import {
    Row,
    Col
} from "../../../components";
import {
    MantineReactTable,
    useMantineReactTable,MRT_TablePagination,
    MRT_GlobalFilterTextInput, MRT_ToggleDensePaddingButton,
    MRT_ToggleFiltersButton, MRT_ToolbarAlertBanner
} from 'mantine-react-table';
import {ActionIcon, Tooltip, Box, Button, Flex, Menu, Text, Title, Modal} from '@mantine/core';

/* 아이콘은 여기서 찾으세요 : https://tabler.io/icons */
import {IconRefresh, IconEdit, IconSquareRoundedX, IconTrash, IconRestore, IconCirclePlus, IconCloudSearch} from '@tabler/icons-react';

import agent from "../../api/agent";

import {useMantineFetch} from "../../hooks/useMantineFetch";
import {useMantineMeta} from "../../hooks/useMantineMeta";


import {useRecoilCallback, useRecoilValue, useResetRecoilState, useSetRecoilState} from "recoil";
import {
    boardUpdateOneSelector, boardUpdateResetModifiedOneSelector,
    boardUpdateResetOneSelector,
    boardUpdateState
} from "../../recoil/board/boardUpdateState";
import classNames from "classnames";
import {
    CodeGeneralUtil,
    DeptHierarchyUtil,
    isArray,
    isCreateOne,
    isUpdateOne,
    isValidArray,
    isValidObject
} from "../../utils/utilities";

import {MRT_Localization_KO_CUSTOM} from "../../localization/mantine/custom";
import {renderError} from "../../utils/CommonErrorHandler";
import {
    globalInfoAccessTokenUserInfoSelector,
    globalInfoSidebarCollapsedSelector
} from "../../recoil/globalInfoState";



import LoadingOverlay from 'react-loading-overlay'
import ClockLoader from 'react-spinners/ClockLoader';
import {Link} from "react-router-dom";

import ListSearchModule from "../List/ListSearchModule";
import {useListSearchModuleMeta} from "../../hooks/useListSearchModuleMeta";
import {func} from "prop-types";
import ListTopToolbar from "../List/ListTopToolbar";

import {
    Input
} from 'reactstrap';

import { useMediaQuery } from '@mantine/hooks';
import { IconDeviceMobileMessage, IconMoodSearch } from '@tabler/icons-react';

import getVinsColumns from "../../../routes/Cars/Vins/columns";
import {
    fetchDistinctYearsSearch,
    fetchCarMakersSearch,
    fetchCarModelsSearch,
    fetchCarModelDetailsSearch,
} from "../../../routes/Cars/Vins/prevFetchFuncs";
import {useMantineFetchLocal} from "../../hooks/useMantineFetchLocal";
import {useMantineMetaLocal} from "../../hooks/useMantineMetaLocal";
import {useListSearchModuleMetaLocal} from "../../hooks/useListSearchModuleMetaLocal";

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

const VinSelectListLocal = ({ onKeyValueChangeByNameValue }) => {
    const isMobile = useMediaQuery('(max-width: 900px)');
    /*
    *   Recoil
    *    : 읽기 - useRecoilValue, 쓰기 - useSetRecoilState, 초기화 - useResetRecoilState
    *
    *      one 에다가 곧장 넣은 사례 :  Settings/Users/Menus/One, 넣는 것은 index.js 에서 넣는다.
    * */

    // 읽기 전용
    const globalSidebarCollapsed = useRecoilValue(globalInfoSidebarCollapsedSelector());

    const [one, setOne] = useState({});

    const [years, setYears] = useState([]);
    const [selectedYearValue, setSelectedYearValue] = useState("");

    const [carMakers, setCarMakers] = useState([]);
    const [selectedCarMakerIdx, setSelectedCarMakerIdx] = useState("");

    const [carModels, setCarModels] = useState([]);
    const [selectedCarModelIdx, setSelectedCarModelIdx] = useState("");

    const [carModelDetails, setCarModelDetails] = useState([]);
    const [selectedCarModelDetailIdx, setSelectedCarModelDetailIdx] = useState("");


    const [loading, setLoading] = useState(false);
    const [enableMantineFetch, setEnableMantineFetch] = useState(null);
    const [mantineFetchFirst, setMantineFetchFirst] = useState(true);



    const columns = React.useMemo(() => getVinsColumns({ PK_NAME}), []);


    const refreshAll = () => {
        setEnableMantineFetch(true)
    }


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

    useEffect(()=>{
        if(enableMantineFetch) {
            table.resetRowSelection();
        }
    },[enableMantineFetch])

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
    } = useMantineMetaLocal({columns});


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
    } = useListSearchModuleMetaLocal();

    /*
    *
    *   실제 DB 가져오기
    *
    * */

    const {data, error, isError, isFetching, isLoading} = useMantineFetchLocal({
        columnFilterFns,
        columnFilters,
        globalFilter,
        pagination,
        sorting,
        fetchFunc: agent.Vin.fetch,
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
        enableRowSelection: true,
        enableMultiRowSelection : false,
        mantineSelectCheckboxProps: {
            color: 'orange',
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

    // Mantine table 의 선택이 변경되었을 경우
    useEffect(() => {
        // console.log("bbb")
        // console.log(table.getState())
        // console.log(table.getSelectedRowModel().rows)
        // console.log(table.getSelectedRowModel().rows)

        if(table.getSelectedRowModel().rows.length > 0) {
            onKeyValueChangeByNameValue({name: "vinIdx", value: table.getSelectedRowModel().rows[0].original.vinIdx})
        }

    }, [table.getState().rowSelection])




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
            setGlobalSidebarCollapsed({
                forceUpdate: Math.random(),
                value: true
            })
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

                </Row>

            </LoadingOverlay>
        </Fragment>)
};

export default VinSelectListLocal;