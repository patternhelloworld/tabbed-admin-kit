import React, {useEffect, useState, Fragment, useCallback} from 'react';
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
    boardListOthersSelector, boardListResponseSelector
} from "../../../shared/recoil/board/boardListState";
import {MRT_Localization_KO_CUSTOM} from "../../../shared/localization/mantine/custom";
import {renderError} from "../../../shared/utils/CommonErrorHandler";
import {
    globalInfoAccessTokenUserInfoSelector,
    globalInfoSidebarCollapsedSelector
} from "../../../shared/recoil/globalInfoState";


import LoadingOverlay from 'react-loading-overlay'
import ClockLoader from 'react-spinners/ClockLoader';


import {
    Input
} from 'reactstrap';
import {Label} from "../../../components/recharts";
import { useMediaQuery } from '@mantine/hooks';
import { IconDeviceMobileMessage, IconMoodSearch } from '@tabler/icons-react';
import {
    findCustomerInfoLabelByValue, findCustomerTypeLabelByValue,
    findGenderLabelByValue, getCustomerInfoOptions, getCustomerTypeOptions,
    getGenderOptions, getLabelByPurchasePlanValue,
    getYNCodeOptions, regDatePlusPurchasePlan
} from "../../../shared/enums";

import {
 fetchTestDrivesMetas,

} from "./prevFetchFuncs";
import BigCalendarComponent from "../../../shared/components/DailyBigCalendarComponent";
import {formatDateWrapper} from "../../../shared/utils/date-handler";
import ListSearchModule from "../../../shared/components/List/ListSearchModule";
import getVinsColumns from "../Vins/columns";
import getTestDrivesColumns from "./columns";
import {useListSearchModuleMeta} from "../../../shared/hooks/useListSearchModuleMeta";
import TestDrivesUpdate from "./One/TestDrivesUpdate";
import {useSecondDepthDepts} from "../../../shared/hooks/special-search/useSecondDepthDepts";


/*
*
*
*
*
*
*    아래 Box 에서 쓸수 있는 스타일링 문법 : https://mantine.dev/styles/style-props/
*
* */

const PK_NAME = "testDriveIdx";
const CarsTestDrivesList = ({recoilKey, ...props}) => {

    /*
    *
    *   설정
    *
    * */
    const isMobile = useMediaQuery('(max-width: 900px)');

    /*
    *
    *   Recoil
    *
    * */
    const setGlobalSidebarCollapsed = useSetRecoilState(globalInfoSidebarCollapsedSelector());

    // 읽기 전용
    const one = useRecoilValue(boardUpdateOneSelector({recoilKey}));
    const recoiledResponse = useRecoilValue(boardListResponseSelector({ recoilKey }));
    // 읽기 전용 (달력 검색)
    const calendarDateRange = useRecoilValue(boardListOthersSelector({recoilKey, topic: "calendarDateRange"}));


    // 쓰기 전용
    const setOne = useSetRecoilState(boardUpdateOneSelector({recoilKey}));
    const setRecoiledResponse = useSetRecoilState(boardListResponseSelector({ recoilKey }));
    const setCalendarDateRange = useSetRecoilState(boardListOthersSelector({recoilKey, topic: "calendarDateRange"}));

    /// 선택한 One 만 Reset
    const resetOne = useResetRecoilState(boardUpdateResetOneSelector({recoilKey}));
    const resetModifiedOne = useResetRecoilState(boardUpdateResetModifiedOneSelector({recoilKey}));

    // 세션
    const me = useRecoilValue(globalInfoAccessTokenUserInfoSelector());

    /// list 초기화 Reset
    const resetList = useRecoilCallback(({set}) => (recoilKey) => {
        set(boardListResetSelector({recoilKey}), null);
    });


    /*
    *
    *   State
    *
    * */
    const [loading, setLoading] = useState(false);
    const [enableMantineFetch, setEnableMantineFetch] = useState(null);
    const [mantineFetchFirst, setMantineFetchFirst] = useState(true);

    const columns = React.useMemo(() => getTestDrivesColumns({ PK_NAME}), []);


    /*
    *
    *   Refresh
    *
    * */
    const refreshOne = () => {
        resetOne()
        resetModifiedOne()
    }

    const refreshAll = () => {
        // 여기 fetch 함수의 전제 조건은 항상 calendarDateRange  에 값이 있어야 하는데, 이렇게 하면 같이 날아감.
        // resetList(recoilKey)
        refreshOne()
        setEnableMantineFetch(true)
    }


    /*
    *
    *   Hook
    *
    * */


    /*
    *
    *   ListSearchModule 의 검색 조건 (Meta) 을 담당 (그런데, Mantine 에서 제공하는 것이 아닌, <ListSearchModule/> 을 사용)
    *    : UI 를 보시면 ( dateRangeColumn , dateRange, searchColumn, searchValue )
    *
    * */
    const {
        createSearchFilter, createDateRangeFilter,
        searchColumn, setSearchColumn, searchValue, setSearchValue,
        dateRangeColumn, setDateRangeColumn,
        dateRange, setSafeDateRange
    } = useListSearchModuleMeta({recoilKey});


    const { secondDepthDepts, selectedSecondDepthDeptIdx, setSelectedSecondDepthDeptIdx
        ,handleSecondDepthDepts } = useSecondDepthDepts({recoilKey});


    /*
    *
    *   Fetch 함수
    *
    * */
    const fetchTestDrivesMetasWrapper = useCallback(
        async (controlLoading) => {
            if (controlLoading) {
                setLoading(true);
            }

            if (!calendarDateRange.startDate && !calendarDateRange.endDate) {
                return;
            }

            let re;
            try {
                re = await fetchTestDrivesMetas({
                    skipPagination: true,
                    searchFilter: JSON.stringify({...createSearchFilter(), deptIdx : selectedSecondDepthDeptIdx}),
                    dateRangeFilter: JSON.stringify({
                        column: "startEndDate",
                        startDate: formatDateWrapper(calendarDateRange.startDate),
                        endDate: formatDateWrapper(calendarDateRange.endDate),
                    }),
                });
            } finally {
                setRecoiledResponse(re);
                setEnableMantineFetch(false);
                if (controlLoading) {
                    setLoading(false);
                }
            }
        },
        [selectedSecondDepthDeptIdx, calendarDateRange, createSearchFilter] // 종속성 배열
    );



    /*
    *
    *   Event Handler (클릭, 선택 등..)
    *
    * */
    const handleEventClick = useCallback((clickedOne) => {
        refreshOne();
        if (isValidObject(one)) {
            if (clickedOne.original[PK_NAME] !== one[PK_NAME]) {
                setOne({ ...clickedOne });
            } else {
                setGlobalSidebarCollapsed({
                    forceUpdate: Math.random(),
                    value: false,
                });
            }
        } else {
            setOne({ ...clickedOne });
            setGlobalSidebarCollapsed({
                forceUpdate: Math.random(),
                value: true,
            });
        }
    }, [one, refreshOne, setOne, setGlobalSidebarCollapsed]);


    const handleCalendarMonthChange = useCallback(({ startDate, endDate }) => {
        if(startDate && endDate){
            setCalendarDateRange({ startDate, endDate })
        }
    }, []);


    /*
    *   LifeCycle
    * */
    useEffect(()=>{
        handleSecondDepthDepts(true);
    }, [])

    useEffect(()=>{
        if(enableMantineFetch){
            fetchTestDrivesMetasWrapper(true);
        }
    },[enableMantineFetch])

    useEffect(()=>{
        fetchTestDrivesMetasWrapper(true);
    },[calendarDateRange])


    return (    <Fragment>
                    <LoadingOverlay
                        spinner={<ClockLoader color="#ffffff" size={20}/>}
                        active={loading}
                    >
                        <Row>
                            <Col xl={12}>
                                <ListSearchModule fields={columns}
                                                  searchColumn={searchColumn}
                                                  searchValue={searchValue}
                                                  onSearchColumnChange={setSearchColumn}
                                                  dateRangeColumn={dateRangeColumn}
                                                  onDateSearchColumnChange={setDateRangeColumn}
                                                  onSearchValueChange={setSearchValue}
                                                  dateRange={dateRange} onDateRangeChange={(name, value) => {
                                    setSafeDateRange({
                                        name, value
                                    });
                                }}
                                                  onSearchButtonClick={() => {
                                                      setEnableMantineFetch(true)
                                                  }}
                                                  onSearchBoxKeyDown={(e) => {
                                                      if (e.keyCode === 13) {
                                                          setEnableMantineFetch(true)
                                                      }
                                                  }} >

                                    <Input
                                        type="select"
                                        id="secondDepthDepts"
                                        name="secondDepthDepts"
                                        className={"mr-2 mb-2"}
                                        value={selectedSecondDepthDeptIdx}
                                        defaultValue={""}
                                        onChange={(e) => {
                                            setSelectedSecondDepthDeptIdx(e.target.value)}}
                                        required
                                    >
                                        {secondDepthDepts.map((option) => {

                                            // 표시할 텍스트 결정
                                            const displayText = option.deptNm;

                                            return (
                                                <option key={option.deptIdx} value={option.deptIdx}>
                                                    {displayText}
                                                </option>
                                            );
                                        })}
                                        <option key="-" value=""><b>전시장 전체</b></option>
                                    </Input>

                                </ListSearchModule>
                            </Col>
                        </Row>
                        <Row>
                            <Col xl={12} >
                                <BigCalendarComponent events={recoiledResponse?.data?.content.map(x => {

                                    return {...x,
                                        id : x.testDriveIdx,
                                        title: `${x.carNo}(${x.deptNm})`,
                                        start: x.startDate,
                                        end: x.endDate,
                                        allDay: false
                                    }
                                })} refreshAll={refreshAll} onMonthChange={handleCalendarMonthChange}
                                                      onEventClick={handleEventClick} PK_NAME={PK_NAME} loading={loading} setLoading={setLoading} ></BigCalendarComponent>
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
                                {isUpdateOne(one, PK_NAME) ? <TestDrivesUpdate refreshAll={refreshAll}
                                                                                      refreshOne={refreshOne} recoilKey={recoilKey} PK_NAME={PK_NAME}/> : ""}
                            </Modal>

                        </Row>

        </LoadingOverlay>
    </Fragment>)
}
export default CarsTestDrivesList;