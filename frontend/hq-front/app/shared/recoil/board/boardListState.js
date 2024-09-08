import { atom, selectorFamily } from 'recoil';

const initialState = {

    /* 1. MantineSearchModule Filter */
    mantineSearchModule: {
        Issues: {
            columnFilters: [],   // recoilKey
            columnFilterFns: [], // recoilKey
            globalFilter: null,   // recoilKey
            sorting: [],   // recoilKey
            pagination: {
                pageIndex: 0,
                pageSize: 10,
            }
        },
        SettingsMenusMains: {
            columnFilters: [],   // recoilKey
            columnFilterFns: [], // recoilKey
            globalFilter: null,   // recoilKey
            sorting: [],   // recoilKey
            pagination: {
                pageIndex: 0,
                pageSize: 10,
            }
        },
        SettingsMenusSubs: {
            columnFilters: [],   // recoilKey
            columnFilterFns: [], // recoilKey
            globalFilter: null,   // recoilKey
            sorting: [],   // recoilKey
            pagination: {
                pageIndex: 0,
                pageSize: 10,
            }
        },
        SettingsDealers: {
            columnFilters: [],   // recoilKey
            columnFilterFns: [], // recoilKey
            globalFilter: null,   // recoilKey
            sorting: [],   // recoilKey
            pagination: {
                pageIndex: 0,
                pageSize: 10,
            }
        },
        SettingsUsersMenus: {
            columnFilters: [],   // recoilKey
            columnFilterFns: [], // recoilKey
            globalFilter: null,   // recoilKey
            sorting: [],   // recoilKey
            pagination: {
                pageIndex: 0,
                pageSize: 10,
            }
        },
        SettingsUsers: {
            columnFilters: [],   // recoilKey
            columnFilterFns: [], // recoilKey
            globalFilter: null,   // recoilKey
            sorting: [],   // recoilKey
            pagination: {
                pageIndex: 0,
                pageSize: 10,
            }
        },
        SettingsDepts: {
            columnFilters: [],   // recoilKey
            columnFilterFns: [], // recoilKey
            globalFilter: null,   // recoilKey
            sorting: [],   // recoilKey
            pagination: {
                pageIndex: 0,
                pageSize: 10,
            }
        },
        SettingsApprovalLines: {
            columnFilters: [],   // recoilKey
            columnFilterFns: [], // recoilKey
            globalFilter: null,   // recoilKey
            sorting: [],   // recoilKey
            pagination: {
                pageIndex: 0,
                pageSize: 10,
            }
        },
        CodesCustomer: {
            columnFilters: [],   // recoilKey
            columnFilterFns: [], // recoilKey
            globalFilter: null,   // recoilKey
            sorting: [],   // recoilKey
            pagination: {
                pageIndex: 0,
                pageSize: 10,
            }
        },
        CustomersList: {
            columnFilters: [],   // recoilKey
            columnFilterFns: [], // recoilKey
            globalFilter: null,   // recoilKey
            sorting: [],   // recoilKey
            pagination: {
                pageIndex: 0,
                pageSize: 10,
            }
        },
        CustomersGroups: {
            columnFilters: [],   // recoilKey
            columnFilterFns: [], // recoilKey
            globalFilter: null,   // recoilKey
            sorting: [],   // recoilKey
            pagination: {
                pageIndex: 0,
                pageSize: 10,
            }
        },
        CustomersGroupAssign: {
            columnFilters: [],   // recoilKey
            columnFilterFns: [], // recoilKey
            globalFilter: null,   // recoilKey
            sorting: [],   // recoilKey
            pagination: {
                pageIndex: 0,
                pageSize: 10,
            }
        },
        CustomersRelocateTo: {
            columnFilters: [],   // recoilKey
            columnFilterFns: [], // recoilKey
            globalFilter: null,   // recoilKey
            sorting: [],   // recoilKey
            pagination: {
                pageIndex: 0,
                pageSize: 10,
            }
        },
        CustomersRelocatedFrom: {
            columnFilters: [],   // recoilKey
            columnFilterFns: [], // recoilKey
            globalFilter: null,   // recoilKey
            sorting: [],   // recoilKey
            pagination: {
                pageIndex: 0,
                pageSize: 10,
            }
        },
        CustomersAfterDelivery: {
            columnFilters: [],   // recoilKey
            columnFilterFns: [], // recoilKey
            globalFilter: null,   // recoilKey
            sorting: [],   // recoilKey
            pagination: {
                pageIndex: 0,
                pageSize: 10,
            }
        },
        CustomersExpectationsPurchases: {
            columnFilters: [],   // recoilKey
            columnFilterFns: [], // recoilKey
            globalFilter: null,   // recoilKey
            sorting: [],   // recoilKey
            pagination: {
                pageIndex: 0,
                pageSize: 10,
            }
        },
        CodesExtColors : {
            columnFilters: [],   // recoilKey
            columnFilterFns: [], // recoilKey
            globalFilter: null,   // recoilKey
            sorting: [],   // recoilKey
            pagination: {
                pageIndex: 0,
                pageSize: 10,
            }
        },
        CodesIntColors : {
            columnFilters: [],   // recoilKey
            columnFilterFns: [], // recoilKey
            globalFilter: null,   // recoilKey
            sorting: [],   // recoilKey
            pagination: {
                pageIndex: 0,
                pageSize: 10,
            }
        },
        CarsDealerStocks : {
            columnFilters: [],   // recoilKey
            columnFilterFns: [], // recoilKey
            globalFilter: null,   // recoilKey
            sorting: [],   // recoilKey
            pagination: {
                pageIndex: 0,
                pageSize: 10,
            }
        },
        CarsVins : {
            columnFilters: [],   // recoilKey
            columnFilterFns: [], // recoilKey
            globalFilter: null,   // recoilKey
            sorting: [],   // recoilKey
            pagination: {
                pageIndex: 0,
                pageSize: 10,
            }
        },
        CarsTestDrives: {
            columnFilters: [],   // recoilKey
            columnFilterFns: [], // recoilKey
            globalFilter: null,   // recoilKey
            sorting: [],   // recoilKey
            pagination: {
                pageIndex: 0,
                pageSize: 10,
            }
        }
    },

    /* 2. ListSearchModule Filter */
    listSearchModule: {
        Issues: {
            searchColumn: null,
            searchValue: null,
            dateRangeColumn: null,
            dateRange: {
                startDate: null,
                endDate: null
            }
        },
        SettingsMenusMains: {
            searchColumn: null,
            searchValue: null,
            dateRangeColumn: null,
            dateRange: {
                startDate: null,
                endDate: null
            }
        },
        SettingsMenusSubs: {
            searchColumn: null,
            searchValue: null,
            dateRangeColumn: null,
            dateRange: {
                startDate: null,
                endDate: null
            }
        },
        SettingsDealers: {
            searchColumn: null,
            searchValue: null,
            dateRangeColumn: null,
            dateRange: {
                startDate: null,
                endDate: null
            }
        },
        SettingsUsersMenus: {
            searchColumn: null,
            searchValue: null,
            dateRangeColumn: null,
            dateRange: {
                startDate: null,
                endDate: null
            }
        },
        SettingsUsers: {
            searchColumn: null,
            searchValue: null,
            dateRangeColumn: null,
            dateRange: {
                startDate: null,
                endDate: null
            }
        },
        SettingsDepts: {
            searchColumn: null,
            searchValue: null,
            dateRangeColumn: null,
            dateRange: {
                startDate: null,
                endDate: null
            }
        },
        SettingsApprovalLines: {
            searchColumn: null,
            searchValue: null,
            dateRangeColumn: null,
            dateRange: {
                startDate: null,
                endDate: null
            }
        },
        CodesCustomer: {
            searchColumn: null,
            searchValue: null,
            dateRangeColumn: null,
            dateRange: {
                startDate: null,
                endDate: null
            }
        },
        CustomersList: {
            searchColumn: null,
            searchValue: null,
            dateRangeColumn: null,
            dateRange: {
                startDate: null,
                endDate: null
            }
        },
        CustomersGroups: {
            searchColumn: null,
            searchValue: null,
            dateRangeColumn: null,
            dateRange: {
                startDate: null,
                endDate: null
            }
        },
        CustomersGroupAssign: {
            searchColumn: null,
            searchValue: null,
            dateRangeColumn: null,
            dateRange: {
                startDate: null,
                endDate: null
            }
        },
        CustomersRelocateTo: {
            searchColumn: null,
            searchValue: null,
            dateRangeColumn: null,
            dateRange: {
                startDate: null,
                endDate: null
            }
        },
        CustomersRelocatedFrom: {
            searchColumn: null,
            searchValue: null,
            dateRangeColumn: null,
            dateRange: {
                startDate: null,
                endDate: null
            }
        },
        CustomersAfterDelivery: {
            searchColumn: null,
            searchValue: null,
            dateRangeColumn: null,
            dateRange: {
                startDate: null,
                endDate: null
            }
        },
        CustomersExpectationsPurchases: {
            searchColumn: null,
            searchValue: null,
            dateRangeColumn: null,
            dateRange: {
                startDate: null,
                endDate: null
            }
        },
        CodesExtColors : {
            searchColumn: null,
            searchValue: null,
            dateRangeColumn: null,
            dateRange: {
                startDate: null,
                endDate: null
            }
        },
        CodesIntColors : {
            searchColumn: null,
            searchValue: null,
            dateRangeColumn: null,
            dateRange: {
                startDate: null,
                endDate: null
            }
        },
        CarsDealerStocks : {
            searchColumn: null,
            searchValue: null,
            dateRangeColumn: null,
            dateRange: {
                startDate: null,
                endDate: null
            }
        },
        CarsVins : {
            searchColumn: null,
            searchValue: null,
            dateRangeColumn: null,
            dateRange: {
                startDate: null,
                endDate: null
            }
        },
        CarsTestDrives: {
            searchColumn: null,
            searchValue: null,
            dateRangeColumn: null,
            dateRange: {
                startDate: null,
                endDate: null
            }
        }
    },

    /* 3. 응답 (성공, 실패 모두 포함) */
    response: {
        Issues: null,
        SettingsMenusMains: null,
        SettingsMenusSubs: null,
        SettingsDealers: null,
        SettingsUsersMenus: null,
        SettingsUsers: null,
        SettingsDepts: null,
        SettingsApprovalLines: null,
        CodesCustomer : null,
        CustomersList : null,
        CustomersGroups : null,
        CustomersGroupAssign : null,
        CustomersRelocateTo : null,
        CustomersRelocatedFrom : null,
        CustomersAfterDelivery: null,
        CustomersExpectationsPurchases: null,
        CodesExtColors : null,
        CodesIntColors : null,
        CarsDealerStocks : null,
        CarsVins : null,
        CarsTestDrives: null
    },


    /* 4. 기타 */
    others: {
        Issues: null,
        SettingsMenusMains: null,
        SettingsMenusSubs: null,
        SettingsDealers: null,
        SettingsUsersMenus: null,
        SettingsUsers: null,
        SettingsDepts: null,
        SettingsApprovalLines: null,
        CodesCustomer : null,
        CustomersList : {
            deptsWithUsers : [],
            selectedDeptWithUser : "", // select box 의 value 이므로 string 타입이며 이와 같다 : JSON.stringify({ deptIdx : null,deptNm : null, userIdx : null, userName : null,depth :null })
            codeCustomers : [],
            selectedRows : []
        },
        CustomersGroups : null,
        CustomersGroupAssign : {
            deptsWithUsers : [],
            selectedDeptWithUser : "", // select box 의 value 이므로 string 타입이며 이와 같다 : JSON.stringify({ deptIdx : null,deptNm : null, userIdx : null, userName : null,depth :null })
            codeCustomers : [],
            selectedRows : []
        },
        CustomersRelocateTo : {
            deptsWithUsers : [],
            selectedDeptWithUser : "", // select box 의 value 이므로 string 타입이며 이와 같다 : JSON.stringify({ deptIdx : null,deptNm : null, userIdx : null, userName : null,depth :null })
            codeCustomers : [],
            selectedRows : []
        },
        CustomersRelocatedFrom : {
            deptsWithUsers : [],
            selectedDeptWithUser : "", // select box 의 value 이므로 string 타입이며 이와 같다 : JSON.stringify({ deptIdx : null,deptNm : null, userIdx : null, userName : null,depth :null })
            codeCustomers : [],
            selectedRows : []
        },
        CustomersAfterDelivery : {
            deptsWithUsers : [],
            selectedDeptWithUser : "", // select box 의 value 이므로 string 타입이며 이와 같다 : JSON.stringify({ deptIdx : null,deptNm : null, userIdx : null, userName : null,depth :null })
            codeCustomers : [],
            selectedRows : []
        },
        CustomersExpectationsPurchases : {
            deptsWithUsers : [],
            selectedDeptWithUser : "", // select box 의 value 이므로 string 타입이며 이와 같다 : JSON.stringify({ deptIdx : null,deptNm : null, userIdx : null, userName : null,depth :null })
            codeCustomers : [],
            selectedRows : []
        },
        CarsDealerStocks : {
            secondDepthDepts : [],
            selectedSecondDepthDeptIdx : "",

            years : [],
            selectedYearValue : "",

            carMakers : [],
            selectedCarMakerIdx : "",

            carModels : [],
            selectedCarModelIdx : "",

            carModelDetails : [],
            selectedCarModelDetailIdx : ""
        },
        CarsVins : {
            years : [],
            selectedYearValue : "",

            carMakers : [],
            selectedCarMakerIdx : "",

            carModels : [],
            selectedCarModelIdx : "",

            carModelDetails : [],
            selectedCarModelDetailIdx : ""
        },
        CarsTestDrives: {
            secondDepthDepts : [],
            selectedSecondDepthDeptIdx : "",
            calendarDateRange : {
                startDate : null,
                endDate : null
            }
        }

    }
};

export const boardListState = atom({
    key: 'boardListState',
    default: initialState,
});



/* 1. MantineSearchModule Filter */

export const boardListColumnFiltersSelector = selectorFamily({
    key: 'boardListColumnFiltersSelector',
    get: ({ recoilKey }) => ({ get }) => {
        const state = get(boardListState);
        return state.mantineSearchModule[recoilKey].columnFilters;
    },
    set: ({ recoilKey }) => ({ set, get }, newValue) => {
        const state = get(boardListState);
        set(boardListState, {
            ...state,
            mantineSearchModule: {
                ...state.mantineSearchModule,
                [recoilKey]: {
                    ...state.mantineSearchModule[recoilKey],
                    columnFilters: newValue,
                },
            },
        });
    },
});

export const boardListColumnFilterFnsSelector = selectorFamily({
    key: 'boardListColumnFilterFnsSelector',
    get: ({ recoilKey }) => ({ get }) => {
        const state = get(boardListState);
        return state.mantineSearchModule[recoilKey].columnFilterFns;
    },
    set: ({ recoilKey }) => ({ set, get }, newValue) => {
        const state = get(boardListState);
        set(boardListState, {
            ...state,
            mantineSearchModule: {
                ...state.mantineSearchModule,
                [recoilKey]: {
                    ...state.mantineSearchModule[recoilKey],
                    columnFilterFns: newValue,
                },
            },
        });
    },
});

export const boardListGlobalFilterSelector = selectorFamily({
    key: 'boardListGlobalFilterSelector',
    get: ({ recoilKey }) => ({ get }) => {
        const state = get(boardListState);
        return state.mantineSearchModule[recoilKey].globalFilter;
    },
    set: ({ recoilKey }) => ({ set, get }, newValue) => {
        const state = get(boardListState);
        set(boardListState, {
            ...state,
            mantineSearchModule: {
                ...state.mantineSearchModule,
                [recoilKey]: {
                    ...state.mantineSearchModule[recoilKey],
                    globalFilter: newValue,
                },
            },
        });
    },
});

export const boardListSortingSelector = selectorFamily({
    key: 'boardListSortingSelector',
    get: ({ recoilKey }) => ({ get }) => {
        const state = get(boardListState);
        return state.mantineSearchModule[recoilKey].sorting;
    },
    set: ({ recoilKey }) => ({ set, get }, newValue) => {
        const state = get(boardListState);
        set(boardListState, {
            ...state,
            mantineSearchModule: {
                ...state.mantineSearchModule,
                [recoilKey]: {
                    ...state.mantineSearchModule[recoilKey],
                    sorting: newValue,
                },
            },
        });
    },
});

export const boardListPaginationSelector = selectorFamily({
    key: 'boardListPaginationSelector',
    get: ({ recoilKey }) => ({ get }) => {
        const state = get(boardListState);
        return state.mantineSearchModule[recoilKey].pagination;
    },
    set: ({ recoilKey }) => ({ set, get }, newValue) => {
        const state = get(boardListState);
        set(boardListState, {
            ...state,
            mantineSearchModule: {
                ...state.mantineSearchModule,
                [recoilKey]: {
                    ...state.mantineSearchModule[recoilKey],
                    pagination: newValue,
                },
            },
        });
    },
});

/* 2. ListSearchModule Filter */

export const boardListSearchColumnSelector = selectorFamily({
    key: 'boardListSearchColumnSelector',
    get: ({ recoilKey }) => ({ get }) => {
        const state = get(boardListState);
        return state.listSearchModule[recoilKey]?.searchColumn;
    },
    set: ({ recoilKey }) => ({ set, get }, newValue) => {
        const state = get(boardListState);
        set(boardListState, {
            ...state,
            listSearchModule: {
                ...state.listSearchModule,
                [recoilKey]: {
                    ...state.listSearchModule[recoilKey],
                    searchColumn: newValue,
                },
            },
        });
    },
});

export const boardListSearchValueSelector = selectorFamily({
    key: 'boardListSearchValueSelector',
    get: ({ recoilKey }) => ({ get }) => {
        const state = get(boardListState);
        return state.listSearchModule[recoilKey]?.searchValue;
    },
    set: ({ recoilKey }) => ({ set, get }, newValue) => {
        const state = get(boardListState);
        set(boardListState, {
            ...state,
            listSearchModule: {
                ...state.listSearchModule,
                [recoilKey]: {
                    ...state.listSearchModule[recoilKey],
                    searchValue: newValue,
                },
            },
        });
    },
});

export const boardListDateRangeColumnSelector = selectorFamily({
    key: 'boardListDateRangeColumnSelector',
    get: ({ recoilKey }) => ({ get }) => {
        const state = get(boardListState);
        return state.listSearchModule[recoilKey]?.dateRangeColumn;
    },
    set: ({ recoilKey }) => ({ set, get }, newValue) => {
        const state = get(boardListState);
        set(boardListState, {
            ...state,
            listSearchModule: {
                ...state.listSearchModule,
                [recoilKey]: {
                    ...state.listSearchModule[recoilKey],
                    dateRangeColumn: newValue,
                },
            },
        });
    },
});

export const boardListDateRangeSelector = selectorFamily({
    key: 'boardListDateRangeSelector',
    get: ({ recoilKey }) => ({ get }) => {
        const state = get(boardListState);
        return state.listSearchModule[recoilKey]?.dateRange;
    },
    set: ({ recoilKey }) => ({ set, get }, newValue) => {
        const state = get(boardListState);
        set(boardListState, {
            ...state,
            listSearchModule: {
                ...state.listSearchModule,
                [recoilKey]: {
                    ...state.listSearchModule[recoilKey],
                    dateRange: newValue,
                },
            },
        });
    },
});

/* 3. 응답 (성공, 실패 모두 포함) */
export const boardListResponseSelector = selectorFamily({
    key: 'boardListResponseSelector',
    get: ({ recoilKey }) => ({ get }) => {
        const state = get(boardListState);
        return state.response[recoilKey];
    },
    set: ({ recoilKey }) => ({ set, get }, newValue) => {
        const state = get(boardListState);
        set(boardListState, {
            ...state,
            response: {
                ...state.response,
                [recoilKey]: newValue,
            },
        });
    },
});


/* 4. 기타 */
export const boardListOthersSelector = selectorFamily({
    key: 'topicSelector',
    get: ({ recoilKey, topic }) => ({ get }) => {
        const state = get(boardListState);
        return state.others[recoilKey]?.[topic];
    },
    set: ({ recoilKey, topic }) => ({ set, get }, newValue) => {
        const state = get(boardListState);
        set(boardListState, {
            ...state,
            others: {
                ...state.others,
                [recoilKey]: {
                    ...state.others[recoilKey],
                    [topic]: newValue,
                },
            },
        });
    },
});

/* [IMPORTANT] Reset All */
export const boardListResetSelector = selectorFamily({
    key: 'boardListResetSelector',
    set: ({ recoilKey }) => ({ set, get }) => {
        const state = get(boardListState);

        set(boardListState, {
            ...state,
            mantineSearchModule: {
                ...state.mantineSearchModule,
                [recoilKey]: {
                    ...state.mantineSearchModule[recoilKey],
                    columnFilters: [],   // recoilKey
                    columnFilterFns: [], // recoilKey
                    globalFilter: null,   // recoilKey
                    sorting: [],   // recoilKey
                    pagination: {
                        pageIndex: 0,
                        pageSize: 10,
                    }
                },
            },
            listSearchModule: {
                ...state.listSearchModule,
                [recoilKey]: {
                    ...state.listSearchModule[recoilKey],
                    searchColumn: null,
                    searchValue: null,
                    dateRangeColumn: null,
                    dateRange: {
                        startDate: null,
                        endDate: null
                    }
                },
            },
            response: {
                ...state.response,
                [recoilKey]: initialState.response[recoilKey],
            },
            others: {
                ...state.others,
                [recoilKey]: initialState.others[recoilKey],
            }
        });
    },
});

