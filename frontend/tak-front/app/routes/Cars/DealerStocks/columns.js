// columns.js
import React from 'react';
import { Box } from '@mantine/core';
import {
    findDealerStockUseTypeLabelByValue,
    findGenderLabelByValue, getDealerStockUseTypeOptions,

    getGenderOptions,
    getYNCodeOptions
} from '../../../shared/enums';
import { CodeGeneralUtil } from '../../../shared/utils/utilities';

const getDealerStocksColumns = ({PK_NAME}) =>[
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
        accessorKey: 'useType',
        header: '사용상태',
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
                    {findDealerStockUseTypeLabelByValue(renderedCellValue)}
                </Box>
            )
        },
        searchable: true,
        selectBoxList: [
            ...getDealerStockUseTypeOptions()
        ]
    },
    {
        accessorKey: 'carModelDetailYear',
        header: '연식',
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
        }
    },
    {
        accessorKey: 'carMakerIdx',
        header: '메이커',
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
                    {row.original.carMakerNm}
                </Box>
            )
        }
    },
    {
        accessorKey: 'carModelIdx',
        header: '모델',
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
                    {row.original.carModelCode} {row.original.carModelName} {row.original.carModelSvcCode} {row.original.carModelSvcName}
                </Box>
            )
        }
    },
    {
        accessorKey: 'carModelDetailIdx',
        header: '세부 모델',
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
                    {row.original.carModelDetailName} {row.original.carModelDetailCode} {row.original.carModelDetailMotorType} {row.original.carModelDetailCarName}
                </Box>
            )
        },
        enableSorting: false
    },
    {
        accessorKey: 'vinNumber',
        header: '차대번호',
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
        searchable: true,
        defaultSearchColumn : true
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
        dateRangeSearchable: true
    },
];

export default getDealerStocksColumns;
