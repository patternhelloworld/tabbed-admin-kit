import React, {useEffect, useMemo, useState, Fragment} from 'react';
import {
    Container,
    Row,
    Card,
    CardBody,
    Badge,
    Table,
    CardTitle,
    InputGroup,
    InputGroupAddon,
    Input,
    ListGroup,
    ListGroupItem,
    Media,
    Col
} from "./../../../components";
import {   MantineReactTable,
    useMantineReactTable,
    MRT_GlobalFilterTextInput,MRT_ToggleDensePaddingButton,
    MRT_ToggleFiltersButton } from 'mantine-react-table';
import { ActionIcon, Tooltip,  Box, Button, Flex, Menu, Text, Title } from '@mantine/core';

/* 아이콘은 여기서 찾으세요 : https://tabler.io/icons */
import { IconRefresh, IconEdit, IconSquareRoundedX, IconTrash, IconRestore}  from '@tabler/icons-react';

import agent from "../../../shared/api/agent";

import {useMantineFetch} from "../../../shared/hooks/useMantineFetch";
import {useMantineMeta} from "../../../shared/hooks/useMantineMeta";
import toast from "../../../shared/utils/toast";
import Parser from 'html-react-parser';
import Edit from './edit';
import SampleEdit from "./edit";
import {useRecoilCallback, useRecoilValue, useResetRecoilState, useSetRecoilState} from "recoil";
import {boardUpdateOneSelector, boardUpdateState} from "../../../shared/recoil/board/boardUpdateState";
import classNames from "classnames";
import {isValidObject} from "../../../shared/utils/utilities";
import {boardListResetSelector} from "../../../shared/recoil/board/boardListState";
import {MRT_Localization_KO_CUSTOM } from "../../../shared/localization/mantine/custom";

/*
*
*   아래 Box 에서 쓸수 있는 스타일링 문법 : https://mantine.dev/styles/style-props/
*
* */
const RECOIL_KEY = "issues";
const PK_NAME = "userIdx";

const PagedList = () => {

    // 읽기 전용
    const one = useRecoilValue(boardUpdateOneSelector({ recoilKey : RECOIL_KEY }));
    // 쓰기 전용
    const setOne = useSetRecoilState(boardUpdateOneSelector({ recoilKey : RECOIL_KEY }));
    // 초기화 하는 함수
    /// 1. 선택한 One 만
    const resetOne =  useResetRecoilState(boardUpdateOneSelector({recoilKey : RECOIL_KEY}));
    /// 2. list 초기화
    const resetList = useRecoilCallback(({ set }) => (recoilKey) => {
        set(boardListResetSelector({ recoilKey }), null);
    });

    const columns = useMemo(
        () => [
            {
                accessorKey: 'id',
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
                enableColumnFilterModes: false,
                enableColumnFilter: false
            },
            {
                accessorKey: 'title',
                header: '제목',
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
               // accessorFn: (row) => `${row[PK_NAME]}:${row.title}`,
                Cell: ({ renderedCellValue, row, cell}) => {
                    return(
                        <Box
                            fz={"13px"}
                            fw={600}
                        >
                            {renderedCellValue}{/*-{row.original.title}*/}
                        </Box>
                    )
                },
            //    enableClickToCopy: true
            },
            {
                accessorKey: 'projectName',
                header: '프로젝트명',
                Header: ({column, header, table})=> {
                    return (  <Box
                        fz={13}
                        fw={600}
                    >
                        {column?.columnDef?.header}
                    </Box>);
                },
                Cell: ({ renderedCellValue, row, cell}) => {
                    return(
                        <Box
                            sx={{
                                fontSize : "12px"
                            }}
                        >
                            {renderedCellValue}
                        </Box>
                    )
                },
            },
            {
                accessorKey: 'description',
                size: 350,
                header: '설명',
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
                Cell: ({ renderedCellValue, row, cell}) => {
                    return(
                        <Box
                            sx={{
                                fontSize : "12px",
                            }}
                        >
                            {cell.getValue() ? Parser(cell.getValue()) : ""}
                        </Box>
                    )
                },
            },
            {
                accessorKey: 'updatedAt',
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
                enableColumnFilterModes: false,
                enableColumnFilter: false
            },
        ],
        [],
    );

    const {
        columnFilters, setColumnFilters,
        columnFilterFns, setColumnFilterFns,
        globalFilter, setGlobalFilter,
        sorting, setSorting,
        pagination, setPagination
    } = useMantineMeta({ columns, recoilKey : RECOIL_KEY });

    //call our custom react-query hook
    const { data, error, isError, isFetching, isLoading, refetch } = useMantineFetch({
        columnFilterFns,
        columnFilters,
        globalFilter,
        pagination,
        sorting,
        fetchFunc : agent.Issue.fetch,
        cacheKey : RECOIL_KEY
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
        enableColumnFilterModes: false,
        columnFilterModeOptions: ['contains'],
        initialState: { showColumnFilters: true, density: 'xs',  showGlobalFilter: true  },
        manualFiltering: true,
        manualPagination: true,
        manualSorting: true,
        mantineToolbarAlertBannerProps: isError
            ? {
                color: 'red',
                children: error.userMessage,
            }
            : undefined,
        onColumnFilterFnsChange: setColumnFilterFns,
        onColumnFiltersChange: setColumnFilters,
        onGlobalFilterChange: setGlobalFilter,
        onPaginationChange: setPagination,
        onSortingChange: setSorting,
        renderTopToolbarCustomActions: () => (
            <Tooltip label="다시 데이터를 불러옵니다.">
                <ActionIcon onClick={() => refetch()}>
                    <IconRefresh />
                </ActionIcon>
            </Tooltip>
        ),
        rowCount: data?.data?.totalElements ?? 0,
        state: {
            columnFilterFns,
            columnFilters,
            globalFilter,
            isLoading,
            pagination,
            showAlertBanner: isError,
            showProgressBars: isFetching,
            sorting,
        },
        renderTopToolbar: ({ table }) => {

            return (
                <Flex p="md" justify="space-between">
                    <Flex gap="lg">
                        <MRT_GlobalFilterTextInput table={table} />
                        <MRT_ToggleFiltersButton table={table} />
                        <MRT_ToggleDensePaddingButton table={table}/>
                        <Tooltip label="다시 데이터를 불러옵니다." className={"mt-1"}>
                            <ActionIcon onClick={() => {
                                resetList(RECOIL_KEY)
                                refetch()
                            }}>
                                <IconRefresh />
                            </ActionIcon>
                        </Tooltip>
                    </Flex>
                </Flex>
            );
        },
        /*       renderDetailPanel: ({ row }) => {
                   return (<div className={"w-50"}><SampleEdit one={data?.data?.content.find(x => x[PK_NAME] === row?.original[PK_NAME])} refetch={refetch}/></div>)
               },*/
        renderRowActionMenuItems: ({row, table}) => {

            return (
                <>
                    <Menu.Item onClick={()=> {
                        console.log('수정버튼클릭')
                        console.log(row)
                        console.log(table)
                        if(isValidObject(one)){
                            // row.original[PK_NAME] : 현재 수정 버튼을 클릭한 row 의 PK 값
                            // one[PK_NAME] : 이전에 수정 버튼을 클릭하여 넣어진 recoil 의 PK 값
                            if(row.original[PK_NAME] !== one[PK_NAME]){
                                console.log("1 : 이전에 클릭한 것과 다른 row 를 클릭하였다.")
                                // 이번에 클릭한 row 의 객체로 넣는다.
                                setOne(row.original);
                            }else{
                                console.log("2 : 이전에 클릭한 것과 같은 row 를 클릭하였다.")
                                // 중앙 저장소(recoil)의 현재 RECOIL_KEY에 해당하는 one 을 초기화
                                resetOne()
                            }
                        }else{
                            console.log("3  : one 이 isValidObject 아니라는 의미는, 한번도 수정 버튼을 클릭한 적이 없다.")
                            setOne(row.original);
                        }
                    }} icon={<IconEdit />}>수정</Menu.Item>
                    <Menu.Item icon={<IconTrash />}>비활성화</Menu.Item>
                    <Menu.Item icon={<IconRestore />}>복원</Menu.Item>
                    <Menu.Item icon={<IconSquareRoundedX />}>삭제</Menu.Item>
                </>
            )},
        enableRowActions: true,
        localization: MRT_Localization_KO_CUSTOM
    });


    /*
    *  클래스 css 사용법 : https://www.w3schools.com/bootstrap4/bootstrap_grid_xlarge.asp
    *
    *  Row = <div className="row">
       Col = <div className="col-lg-4">
    * */
    return (
        <div>
            <Row>
                <Col lg={!isValidObject(one) ? 12 : 8} >
                    <MantineReactTable table={table}/>
                </Col>
                <Col lg={4} className={classNames('', { 'd-none': !isValidObject(one) })}>
                    {/*<SEampledit one={data?.data?.content.find(x => x[PK_NAME] === row?.original[PK_NAME])} refetch={refetch}/>*/}
                    <SampleEdit refetch={refetch}/>
                </Col>
            </Row>

        </div>)
};


export default PagedList;