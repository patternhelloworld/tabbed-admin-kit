// columns.js
import React from 'react';
import { Box } from '@mantine/core';

const getSettingsUsersColumns = ({codeCustomersListOthers, PK_NAME}) =>[
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
        accessorKey: 'name',
        size: 300,
        header: '사용자',
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
                    {renderedCellValue} ({row.original.userId})
                </Box>
            )
        },
        searchable: true,
        defaultSearchColumn : true
    },
    {
        accessorKey: 'dealerNm',
        header: '딜러명',
        Header: ({column, header, table}) => {
            return (<Box
                fz={13}
                fw={600}
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
        searchable: true,
    },
    {
        accessorKey: 'deptNm',
        header: '부서명',
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
                        fontSize: "12px",
                    }}
                >
                    {cell.getValue()}
                </Box>
            )
        },
        searchable: true,
    },
    {
        accessorKey: 'position',
        header: '직책',
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
                        fontSize: "12px",
                    }}
                >
                    {cell.getValue()}
                </Box>
            )
        },
        searchable: true,
        selectBoxList: [
            { text: "-", value: ""},
            {value: "대리", text: "대리"}, { value: "과장", text: "과장"}, { value: "차장", text: "차장"}
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
        dateRangeSearchable: true
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
        dateRangeSearchable: true,
        defaultDateRangeSearchColumn: true
    }
];

export default getSettingsUsersColumns;
