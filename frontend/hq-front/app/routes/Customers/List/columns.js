// columns.js
import React from 'react';
import { Box } from '@mantine/core';
import {
    findCustomerInfoLabelByValue,
    findCustomerTypeLabelByValue, findGenderLabelByValue,
    getCustomerInfoOptions,
    getCustomerTypeOptions,
    getGenderOptions,
    getYNCodeOptions
} from '../../../shared/enums';
import { CodeGeneralUtil } from '../../../shared/utils/utilities';

const getCustomersListColumns = ({codeCustomersListOthers, PK_NAME}) =>[
    {
        accessorKey: PK_NAME,
        size: 100,
        header: 'ID',
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
                    fz={"13px"}
                >
                    {cell.getValue()}
                </Box>
            )
        }
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
        // renderedCellValue 로 해도 되고, cell.getValue() 해도 되고, row.original.title
        // accessorFn: (row) => `${row[PK_NAME]}:${row.name}`,
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
        // renderedCellValue 로 해도 되고, cell.getValue() 해도 되고, row.original.title
        // accessorFn: (row) => `${row[PK_NAME]}:${row.name}`,
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
        accessorKey: 'userManagerName',
        header: '담당자',
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
        // renderedCellValue 로 해도 되고, cell.getValue() 해도 되고, row.original.title
        // accessorFn: (row) => `${row[PK_NAME]}:${row.name}`,
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
        // renderedCellValue 로 해도 되고, cell.getValue() 해도 되고, row.original.title
        // accessorFn: (row) => `${row[PK_NAME]}:${row.name}`,
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
        // renderedCellValue 로 해도 되고, cell.getValue() 해도 되고, row.original.title
        // accessorFn: (row) => `${row[PK_NAME]}:${row.name}`,
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
        // renderedCellValue 로 해도 되고, cell.getValue() 해도 되고, row.original.title
        // accessorFn: (row) => `${row[PK_NAME]}:${row.name}`,
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
        // renderedCellValue 로 해도 되고, cell.getValue() 해도 되고, row.original.title
        // accessorFn: (row) => `${row[PK_NAME]}:${row.name}`,
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
        // renderedCellValue 로 해도 되고, cell.getValue() 해도 되고, row.original.title
        // accessorFn: (row) => `${row[PK_NAME]}:${row.name}`,
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
        searchable : false,
        dateRangeSearchable: true
    },
    {
        accessorKey: 'personalDataConsent',
        header: '개인정보동의',
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
        defaultSearchColumn : true,
        searchable: true,
        selectBoxList: [
            { text: "-", value: ""},
            ...getYNCodeOptions()
        ],
        searchKey: "personalDataConsent"
    },
    {
        accessorKey: 'codeGeneralContactMethodIdx',
        header: '접촉 경로',
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
        // renderedCellValue 로 해도 되고, cell.getValue() 해도 되고, row.original.title
        // accessorFn: (row) => `${row[PK_NAME]}:${row.name}`,
        Cell: ({renderedCellValue, row, cell}) => {
            return (
                <Box
                    fz={"13px"}
                    fw={600}
                >
                    {CodeGeneralUtil.getSelectedText({codeCustomers : codeCustomersListOthers,
                        codeCustomerIdx : Number(renderedCellValue)})}
                </Box>
            )
        },
        searchable: true,
        selectBoxList: [
            { text: "-", value: ""},
            ...CodeGeneralUtil.getSelectOptions({codeCustomers : codeCustomersListOthers, categoryCd : "CONTACT_CHANNEL"})
        ]
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
    {
        accessorKey: 'modDt',
        header: '최종 수정일',
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
        defaultDateRangeSearchColumn: false
    },
];

export default getCustomersListColumns;
