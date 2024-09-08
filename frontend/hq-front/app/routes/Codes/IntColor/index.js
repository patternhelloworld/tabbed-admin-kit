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

import LoadingOverlay from "react-loading-overlay";
import ClockLoader from "react-spinners/ClockLoader";
import {CRUD_COLUMNS, isAuthorized} from "../../../shared/utils/authorization";


import {ButtonWrapper} from "../../../shared/components/OptimizedHtmlElements";
import {useListSearchModuleMeta} from "../../../shared/hooks/useListSearchModuleMeta";
import CodesExtColorList from "../ExtColor";


/*
*
*   아래 Box 에서 쓸수 있는 스타일링 문법 : https://mantine.dev/styles/style-props/
*
* */

const PK_NAME = "intColorCodeIdx";
const CodesIntColorList = ({recoilKey, ...props}) => {

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

    // 항상 디버깅은 useEffect 사용

    return (<div>"hello" {recoilKey}</div>);
}

export default CodesIntColorList;