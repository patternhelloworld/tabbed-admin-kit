import { atom, selectorFamily, selector } from 'recoil';

// Define the initial state
const initialState = {
    one: {
        Issues: {},   // recoilKey
        SettingsMenusMains: {}, // recoilKey
        SettingsMenusSubs: {}, // recoilKey
        SettingsDealers: {},
        SettingsUsersMenus: [],
        SettingsUsers: {},
        SettingsDepts: {},
        SettingsApprovalLines: {},
        CodesCustomer: {},
        CustomersList: {},
        CustomersGroups: {},
        CustomerGroupAssign: {},
        CustomersRelocateTo: {},
        CustomersRelocatedFrom: {},
        CustomersAfterDelivery: {},
        CustomersExpectationsPurchases: {},
        CodesExtColors : {},
        CodesIntColors : {},
        CarsDealerStocks : {},
        CarsVins : {},
        CarsTestDrives: {}
    },
    modifiedOne: {
        Issues: {},   // recoilKey
        SettingsMenusMains: {}, // recoilKey
        SettingsMenusSubs: {}, // recoilKey
        SettingsDealers: {},
        SettingsUsersMenus: [],
        SettingsUsers: {},
        SettingsDepts: {},
        SettingsApprovalLines: {},
        CodesCustomer: {},
        CustomersList: {},
        CustomersGroups: {},
        CustomerGroupAssign: {},
        CustomersRelocateTo: {},
        CustomersRelocatedFrom: {},
        CustomersAfterDelivery: {},
        CustomersExpectationsPurchases: {},
        CodesExtColors : {},
        CodesIntColors : {},
        CarsDealerStocks : {},
        CarsVins : {},
        CarsTestDrives: {}
    },
    others: {
        // 현재 이 Recoil Key 만 others 를 사용 중
        SettingsUsersMenus: {
        },
        CarsDealerStocks : {

        },
        CarsVins : {

        },
        CarsTestDrives: {}
    }
};

// Atom to hold the state
export const boardCreateState = atom({
    key: 'boardCreateState',
    default: initialState
});

// SelectorFamily for one
export const boardCreateOneSelector = selectorFamily({
    key: 'boardCreateOneSelector',
    get: ({ recoilKey }) => ({ get }) => {
        const state = get(boardCreateState);
        return state.one[recoilKey];
    },
    set: ({ recoilKey }) => ({ set, get }, newValue) => {
        const state = get(boardCreateState);
        set(boardCreateState, {
            ...state,
            one: {
                ...state.one,
                [recoilKey]: newValue,
            },
        });
    },
});

// SelectorFamily for modifiedOne
export const boardCreateModifiedOneSelector = selectorFamily({
    key: 'boardCreateModifiedOneSelector',
    get: ({ recoilKey }) => ({ get }) => {
        const state = get(boardCreateState);
        return state.modifiedOne[recoilKey];
    },
    set: ({ recoilKey }) => ({ set, get }, newValue) => {
        const state = get(boardCreateState);
        set(boardCreateState, {
            ...state,
            modifiedOne: {
                ...state.modifiedOne,
                [recoilKey]: newValue,
            },
        });
    },
});


export const boardCreateOthersSelector = selectorFamily({
    key: 'boardCreateOthersSelector',
    get: ({ recoilKey }) => ({ get }) => {
        const state = get(boardCreateState);
        return state.others[recoilKey];
    },
    set: ({ recoilKey }) => ({ set, get }, newValue) => {
        const state = get(boardCreateState);
        set(boardCreateState, {
            ...state,
            others: {
                ...state.others,
                [recoilKey]: newValue,
            },
        });
    },
});



// SelectorFamily for resetting a specific recoilKey in one
export const boardCreateResetOneSelector = selectorFamily({
    key: 'boardCreateResetOneSelector',
    get: () => ({ get }) => {
        // This selector does not return any value
        return;
    },
    set: ({ recoilKey }) => ({ set, get }) => {
        const state = get(boardCreateState);
        set(boardCreateState, {
            ...state,
            one: {
                ...state.one,
                [recoilKey]: initialState.one[recoilKey],
            },
        });
    },
});

// SelectorFamily for resetting a specific recoilKey in modifiedOne
export const boardCreateResetModifiedOneSelector = selectorFamily({
    key: 'boardCreateResetModifiedOneSelector',
    get: () => ({ get }) => {
        // This selector does not return any value
        return;
    },
    set: ({ recoilKey }) => ({ set, get }) => {
        const state = get(boardCreateState);
        set(boardCreateState, {
            ...state,
            modifiedOne: {
                ...state.modifiedOne,
                [recoilKey]: initialState.modifiedOne[recoilKey],
            },
        });
    },
});

export const boardCreateResetOthersSelector = selectorFamily({
    key: 'boardCreateResetOthersSelector',
    get: () => ({ get }) => {
        // This selector does not return any value
        return;
    },
    set: ({ recoilKey }) => ({ set, get }) => {
        const state = get(boardCreateState);
        set(boardCreateState, {
            ...state,
            others: {
                ...state.others,
                [recoilKey]: initialState.others[recoilKey],
            },
        });
    },
});
