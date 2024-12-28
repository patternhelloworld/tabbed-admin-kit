import React, {useEffect, useState, Fragment, useCallback} from 'react';
import {
    Row,
    Col
} from "../../../../components";
import {
    MantineReactTable,
    useMantineReactTable,MRT_TablePagination,
    MRT_GlobalFilterTextInput, MRT_ToggleDensePaddingButton,
    MRT_ToggleFiltersButton, MRT_ToolbarAlertBanner
} from 'mantine-react-table';
import {ActionIcon, Tooltip, Box, Button, Flex, Menu, Text, Title, Modal} from '@mantine/core';

/* 아이콘은 여기서 찾으세요 : https://tabler.io/icons */
import {IconRefresh, IconEdit, IconSquareRoundedX, IconTrash, IconRestore, IconCirclePlus, IconCloudSearch} from '@tabler/icons-react';

import agent from "../../../../shared/api/agent";

import {useMantineFetch} from "../../../../shared/hooks/useMantineFetch";
import {useMantineMeta} from "../../../../shared/hooks/useMantineMeta";


import {useRecoilCallback, useRecoilValue, useResetRecoilState, useSetRecoilState} from "recoil";
import {
    boardUpdateOneSelector, boardUpdateResetModifiedOneSelector,
    boardUpdateResetOneSelector,
    boardUpdateState
} from "../../../../shared/recoil/board/boardUpdateState";
import classNames from "classnames";
import {
    CodeGeneralUtil,
    DeptHierarchyUtil,
    isArray,
    isCreateOne,
    isUpdateOne,
    isValidArray,
    isValidObject
} from "../../../../shared/utils/utilities";
import {
    boardListResetSelector,
    boardListState,
    boardListOthersSelector, boardListResponseSelector
} from "../../../../shared/recoil/board/boardListState";
import {MRT_Localization_KO_CUSTOM} from "../../../../shared/localization/mantine/custom";
import {renderError} from "../../../../shared/utils/CommonErrorHandler";
import {
    globalInfoAccessTokenUserInfoSelector,
    globalInfoSidebarCollapsedSelector
} from "../../../../shared/recoil/globalInfoState";


import LoadingOverlay from 'react-loading-overlay'
import ClockLoader from 'react-spinners/ClockLoader';
import {Link} from "react-router-dom";
import {CRUD_COLUMNS, isAuthorized} from "../../../../shared/utils/authorization";

import {ButtonWrapper} from "../../../../shared/components/OptimizedHtmlElements";
import ListSearchModule from "../../../../shared/components/List/ListSearchModule";
import {useListSearchModuleMeta} from "../../../../shared/hooks/useListSearchModuleMeta";
import {func} from "prop-types";
import ListTopToolbar from "../../../../shared/components/List/ListTopToolbar";
import {sortDeptsByParentCd} from "../../../Settings/Depts/util";
import {
    Input
} from 'reactstrap';
import {Label} from "../../../../components/recharts";
import { useMediaQuery } from '@mantine/hooks';
import { IconDeviceMobileMessage, IconMoodSearch } from '@tabler/icons-react';
import {
    findCustomerInfoLabelByValue, findCustomerTypeLabelByValue,
    findGenderLabelByValue, getCustomerInfoOptions, getCustomerTypeOptions,
    getGenderOptions, getLabelByPurchasePlanValue,
    getYNCodeOptions, regDatePlusPurchasePlan
} from "../../../../shared/enums";

import {
    fetchCodeCustomersMetas,
    fetchCustomerGroupsMetas, fetchCustomersMetas,
    fetchDeptsForCurrentDealer,
    fetchUsersForCurrentDealer
} from "./prevFetchFuncs";
import BigCalendarComponent from "../../../../shared/components/MonthlyBigCalendarComponent";
import {formatDateWrapper} from "../../../../shared/utils/date-handler";
import UsersListSelectModal from "../../../../shared/components/select-modal/UserSelectModalLocal";
import SettingsCustomersCreate from "../../List/One/CustomersListCreate";
import SettingsCustomersUpdate from "../../List/One/CustomersListUpdate";


/*
*
*
*
*
*
*    아래 Box 에서 쓸수 있는 스타일링 문법 : https://mantine.dev/styles/style-props/
*
* */

const PK_NAME = "customerIdx";
const CustomersExpectationsPurchasesList = ({recoilKey, ...props}) => {

    const isMobile = useMediaQuery('(max-width: 900px)');

    const setGlobalSidebarCollapsed = useSetRecoilState(globalInfoSidebarCollapsedSelector());

    // 읽기 전용
    const one = useRecoilValue(boardUpdateOneSelector({recoilKey}));
    const recoiledResponse = useRecoilValue(boardListResponseSelector({ recoilKey }));

    // 쓰기 전용
    const setOne = useSetRecoilState(boardUpdateOneSelector({recoilKey}));
    const setRecoiledResponse = useSetRecoilState(boardListResponseSelector({ recoilKey }));

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


    const customersExpectationsPurchasesListCycle = useCallback(async ({ startDate, endDate }) => {
        let re;
        try {
            re = await fetchCustomersMetas({
                skipPagination: true,
                dateRangeFilter: JSON.stringify({
                    column : "expectedPurchaseDt",
                    startDate: formatDateWrapper(startDate),
                    endDate: formatDateWrapper(endDate),
                }),
            });
        } finally {
            setRecoiledResponse(re);
        }
    }, [setRecoiledResponse, setLoading]);


    const refreshOne = () => {
        resetOne()
        resetModifiedOne()
    }

    const refreshAll = () => {
        resetList(recoilKey)
        refreshOne()
    }

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


    return (    <Fragment>
                    <LoadingOverlay
                        spinner={<ClockLoader color="#ffffff" size={20}/>}
                        active={loading}
                    >
                        <Row>
                            <Col xl={12} >
                                <BigCalendarComponent events={recoiledResponse?.data?.content.map(x => {

                                    const v = regDatePlusPurchasePlan(x.regDt, x.purchasePlan)

                                    return {...x,
                                        id : x.customerIdx,
                                        title: x.customerName + "(" + getLabelByPurchasePlanValue(x.purchasePlan) + ")",
                                        start: v,
                                        end: v,
                                        allDay: true
                                    }
                                })} refreshAll={refreshAll} onMonthChange={customersExpectationsPurchasesListCycle}
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
                                {isUpdateOne(one, PK_NAME) ? <SettingsCustomersUpdate refreshAll={refreshAll} injectedGlobalReadOnly={true}
                                                                                      refreshOne={refreshOne} recoilKey={recoilKey} PK_NAME={PK_NAME}/> : ""}
                            </Modal>

                        </Row>

        </LoadingOverlay>
    </Fragment>)
}
export default CustomersExpectationsPurchasesList;