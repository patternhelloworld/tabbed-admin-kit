// columns.js
import React from 'react';
import { Box } from '@mantine/core';

import { CodeGeneralUtil } from '../../../shared/utils/utilities';

const getTestDrivesColumns = ({PK_NAME}) =>[
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
        defaultSearchColumn: true
    },
    {
        accessorKey: 'carNo',
        header: '차량번호',
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
    }
];

export default getTestDrivesColumns;
