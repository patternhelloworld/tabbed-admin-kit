// columns.js
import React from 'react';
import {Box} from '@mantine/core';
import {DeptHierarchyUtil} from "../../../shared/utils/utilities";
import SquareNumberIcon from "../../../assets/images/square-number/SquareNumberIcon";


const getSettingsDeptsColumns = ({PK_NAME}) => [
/*    {
        accessorKey: 'deptIdx',
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
    },*/
    {
        accessorKey: 'dealerNm',
        header: '딜러명',
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

    },
    {
        accessorKey: 'deptNm',
        header: '조직명',
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
        accessorFn: (row) => ({
            depth: DeptHierarchyUtil.getDepthByDeptIdx(row.depts, row.deptIdx),
            renderedValue: ` ${row.deptNm}`
        }),
        Cell: ({cell}) => {
            const {depth, renderedValue} = cell.getValue();
            return (
                <Box
                    fz={"13px"}
                    sx={{
                        display: 'flex',
                        alignItems: 'center',
                        marginLeft: `${depth * 20}px`,
                        whiteSpace: 'pre'
                    }}
                >
                    <SquareNumberIcon number={depth}/>
                    {renderedValue}
                </Box>
            )
        },

    },
    {
        accessorKey: 'deptSort',
        header: '같은 층에서의 정렬',
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
        accessorFn: (row) => ({
            depth: DeptHierarchyUtil.getDepthByDeptIdx(row.depts, row.deptIdx),
            renderedValue: ` ${row.deptSort}`
        }),
        Cell: ({cell}) => {
            const {depth, renderedValue} = cell.getValue();
            return (
                <Box
                    fz={"13px"}
                    sx={{
                        display: 'flex',
                        alignItems: 'center',
                        marginLeft: `${depth * 20}px`,
                        whiteSpace: 'pre'
                    }}
                >
                    <SquareNumberIcon number={depth}/>
                    {renderedValue}
                </Box>
            )
        },
    },
];

export default getSettingsDeptsColumns;
