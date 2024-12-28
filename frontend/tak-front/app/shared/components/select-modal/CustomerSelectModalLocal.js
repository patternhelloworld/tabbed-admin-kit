import React, {useEffect, useState, Fragment} from 'react';
import {
    MantineReactTable,
    useMantineReactTable,
    MRT_GlobalFilterTextInput, MRT_ToggleDensePaddingButton,
    MRT_ToggleFiltersButton, MRT_ToolbarAlertBanner
} from 'mantine-react-table';
import { useDisclosure } from '@mantine/hooks';
import {ActionIcon, Tooltip, Box, Button, Flex, Menu, Text, Title, Modal} from '@mantine/core';
import { useMediaQuery } from '@mantine/hooks';
/* 아이콘은 여기서 찾으세요 : https://tabler.io/icons */
import {
    IconRefresh,
    IconEdit,
    IconSquareRoundedX,
    IconTrash,
    IconRestore,
    IconDeviceMobileMessage, IconMoodSearch
} from '@tabler/icons-react';

import agent from "shared/api/agent";

import LoadingOverlay from 'react-loading-overlay'
import ClockLoader from 'react-spinners/ClockLoader';

import ListTopToolbar from "shared/components/List/ListTopToolbar";


import {MRT_Localization_KO_CUSTOM} from "shared/localization/mantine/custom";
import {useMantineMetaLocal} from "shared/hooks/useMantineMetaLocal";
import {useListSearchModuleMetaLocal} from "shared/hooks/useListSearchModuleMetaLocal";
import {useMantineFetchLocal} from "shared/hooks/useMantineFetchLocal";
import getSettingsCustomersColumns from "../../../routes/Customers/List/columns";
import {Col, Row} from "../../../components";
import classNames from "classnames";


/*
*
*   해당 모듈은 검색해 보시면 여러 곳에서 사용 중임에 유의해 주세요.
*
*   아래 Box 에서 쓸수 있는 스타일링 문법 : https://mantine.dev/styles/style-props/
* */

const PK_NAME = "customerIdx";

const CustomerSelectModalLocal = ({customerSelectModalOpen = false, setCustomerSelectModalOpen = ()=> {}, onCustomerSelected = undefined, ...props}) => {

    const isMobile = useMediaQuery('(max-width: 900px)');

    const [loading, setLoading] = useState(false);
    const [enableMantineFetch, setEnableMantineFetch] = useState(null);
    const [mantineFetchFirst, setMantineFetchFirst] = useState(true);

    const columns = React.useMemo(() => getSettingsCustomersColumns({PK_NAME}), []);

    const refreshAll = () => {
        setEnableMantineFetch(true)
    }
    useEffect(() => {
        if(customerSelectModalOpen) {
            setEnableMantineFetch(true)
        }
    }, [customerSelectModalOpen])

    useEffect(()=>{
        if(enableMantineFetch) {
            table.resetRowSelection();
        }
    },[enableMantineFetch])

    const {
        columnFilters, setColumnFilters,
        columnFilterFns, setColumnFilterFns,
        globalFilter, setGlobalFilter,
        sorting, setSorting,
        pagination, setPagination
    } = useMantineMetaLocal();

    const {
        createSearchFilter, createDateRangeFilter,
        searchColumn, setSearchColumn, searchValue, setSearchValue,
        dateRangeColumn, setDateRangeColumn,
        dateRange, setSafeDateRange
    } = useListSearchModuleMetaLocal();

    //call our custom react-query hook
    const {data, error, isError, isLoading} = useMantineFetchLocal({
        columnFilterFns,
        columnFilters,
        globalFilter,
        pagination,
        sorting,
        fetchFunc: agent.Customer.fetch,
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
                children: error?.customerMessage,
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
                <Fragment>
                    <Row>
                        <Col>
                            <ListTopToolbar table={table} refreshAll={refreshAll}
                                            columns={columns} searchColumn={searchColumn} setSearchColumn={setSearchColumn} searchValue={searchValue} setSearchValue={setSearchValue} dateRange={dateRange} setSafeDateRange={setSafeDateRange} dateRangeColumn={dateRangeColumn} setDateRangeColumn={setDateRangeColumn}
                                            createBtn={false} setEnableMantineFetch={setEnableMantineFetch}  />
                        </Col>
                    </Row>
                    <Row>
                        <Col lg={7} className={"d-flex align-items-center ml-2 mb-4"}>
                            <Button
                                className={classNames('ml-2')}
                                leftIcon={<IconMoodSearch size={14}/>}
                                variant="gradient"
                                gradient={{ from: 'orange', to: 'red' }}
                                onClick={handleBtnClickAction}
                            >
                                선택
                            </Button>
                        </Col>
                    </Row>
                </Fragment>
            );
        },

        //   enableRowActions: true,
        localization: {...MRT_Localization_KO_CUSTOM, actions: "수정"},
        enableRowSelection: true,
        enableMultiRowSelection : false,
        mantineSelectCheckboxProps: {
            color: 'orange',
        },
        enableMultiSort : false
    });

    const handleBtnClickAction = () => {
        if(table.getSelectedRowModel().rows.length < 1) {
            alert('선택된 사원이 없습니다.');
            return;
        }
        onCustomerSelected(table.getSelectedRowModel().rows[0].original)
    }

    /*
    *  클래스 css 사용법 : https://www.w3schools.com/bootstrap4/bootstrap_grid_xlarge.asp
    *
    *  Row = <div className="row">
       Col = <div className="col-lg-4">
    * */
    return (
        <Fragment>
            <Modal
                size={"60%"}
                opened={customerSelectModalOpen}
                onClose={()=>{
                    setCustomerSelectModalOpen(false)
                }}
                zIndex={100000}
                fullScreen={isMobile}
                overlayProps={{
                    backgroundOpacity: 0.55,
                    blur: 3,
                }}
                closeButtonProps={{
                    style: {width: '70px', height: '30px' },
                }}
            >
                <LoadingOverlay
                    spinner={<ClockLoader color="#ffffff" size={20}/>}
                    active={loading}
                >
                    <MantineReactTable table={table}/>
                </LoadingOverlay>
            </Modal>
        </Fragment>)
};


export default React.memo(CustomerSelectModalLocal);