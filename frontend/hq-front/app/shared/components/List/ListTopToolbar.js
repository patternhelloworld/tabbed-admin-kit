import React, {useCallback} from 'react';

import {ButtonWrapper} from "../OptimizedHtmlElements";
import {CRUD_COLUMNS} from "../../utils/authorization";
import ListSearchModule from "./ListSearchModule";
import {
    MantineReactTable,
    useMantineReactTable,MRT_TablePagination,
    MRT_GlobalFilterTextInput, MRT_ToggleDensePaddingButton, MRT_ShowHideColumnsButton,
    MRT_ToggleFiltersButton, MRT_ToolbarAlertBanner
} from 'mantine-react-table';
import {ActionIcon, Tooltip, Box,  Flex, Menu, Text, Title} from '@mantine/core';
import {IconRefresh, IconEdit, IconSquareRoundedX, IconTrash, IconRestore} from '@tabler/icons-react';
import {globalInfoAccessTokenUserInfoSelector} from "../../recoil/globalInfoState";
import {useRecoilCallback, useRecoilValue, useResetRecoilState, useSetRecoilState} from "recoil";
import { IconPencilPlus } from '@tabler/icons-react';

const ListTopToolbar = ({ table, refreshAll, createOne = ()=>{}, createBtn = false, recoilKey = undefined,
                            columns, searchColumn, searchValue, setSearchColumn, setDateRangeColumn, setSearchValue,
                            dateRangeColumn, dateRange,
                            setSafeDateRange, setEnableMantineFetch, searchModuleShow = true, children}) => {

    const me = useRecoilValue(globalInfoAccessTokenUserInfoSelector());

    return (
        <div>
            <Flex p="md" justify="flex-start">
                <Flex gap="lg">
                    <MRT_ToggleDensePaddingButton table={table}/>
                    <MRT_ShowHideColumnsButton table={table} />
                    <Tooltip label="다시 데이터를 불러옵니다." className={"mt-1"}>
                        <ActionIcon onClick={() => {
                            refreshAll()
                        }}>
                            <IconRefresh/>
                        </ActionIcon>
                    </Tooltip>
                    {createBtn? <ButtonWrapper
                        className={"float-right"}
                        btnText={"신규"}
                        variant="gradient"
                        gradient={{ from: 'teal', to: 'lime', deg: 90 }}
                        iconModule={<IconPencilPlus size={14}/>}
                        handleClick={() => {
                            createOne();
                        }}
                        me={me}
                        recoilKey={recoilKey}
                        crudColumn={CRUD_COLUMNS.CREATE}
                    /> : ""}
                    <MRT_ToolbarAlertBanner table={table}/>
                </Flex>
            </Flex>
            <Flex p="md" justify="flex-start" className={""} wrap={true}>
                {searchModuleShow ?
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
                                          }} children={children}></ListSearchModule>

                : ""}

            </Flex>
        </div>
    );
};

export default React.memo(ListTopToolbar);