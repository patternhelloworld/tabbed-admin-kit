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
        CarsDealerStocks : {},
        CarsVins : {},
        CarsTestDrives: {}
    },
    others: {
        // 현재 이 Recoil Key 만 others 를 사용 중
        SettingsUsersMenus: {
            copyPermissions :false,
            selectedUserIdx : null,
            users : []
        }
    }
};

// Atom to hold the state
export const boardUpdateState = atom({
    key: 'boardUpdateState',
    default: initialState
});

// SelectorFamily for one
export const boardUpdateOneSelector = selectorFamily({
    key: 'boardUpdateOneSelector',
    get: ({ recoilKey }) => ({ get }) => {
        const state = get(boardUpdateState);

        return state.one[recoilKey];
    },
    set: ({ recoilKey }) => ({ set, get }, newValue) => {
        const state = get(boardUpdateState);
        set(boardUpdateState, {
            ...state,
            one: {
                ...state.one,
                [recoilKey]: newValue,
            },
        });
    },
});

// SelectorFamily for modifiedOne
export const boardUpdateModifiedOneSelector = selectorFamily({
    key: 'boardUpdateModifiedOneSelector',
    get: ({ recoilKey }) => ({ get }) => {
        const state = get(boardUpdateState);
        return state.modifiedOne[recoilKey];
    },
    set: ({ recoilKey }) => ({ set, get }, newValue) => {
        const state = get(boardUpdateState);
        set(boardUpdateState, {
            ...state,
            modifiedOne: {
                ...state.modifiedOne,
                [recoilKey]: newValue,
            },
        });
    },
});


export const boardUpdateOthersSelector = selectorFamily({
    key: 'boardUpdateOthersSelector',
    get: ({ recoilKey }) => ({ get }) => {
        const state = get(boardUpdateState);
        return state.others[recoilKey];
    },
    set: ({ recoilKey }) => ({ set, get }, newValue) => {
        const state = get(boardUpdateState);
        set(boardUpdateState, {
            ...state,
            others: {
                ...state.others,
                [recoilKey]: newValue,
            },
        });
    },
});



// SelectorFamily for resetting a specific recoilKey in one
export const boardUpdateResetOneSelector = selectorFamily({
    key: 'boardUpdateResetOneSelector',
    get: () => ({ get }) => {
        // This selector does not return any value
        return;
    },
    set: ({ recoilKey }) => ({ set, get }) => {
        const state = get(boardUpdateState);
        set(boardUpdateState, {
            ...state,
            one: {
                ...state.one,
                [recoilKey]: initialState.one[recoilKey],
            },
        });
    },
});

// SelectorFamily for resetting a specific recoilKey in modifiedOne
export const boardUpdateResetModifiedOneSelector = selectorFamily({
    key: 'boardUpdateResetModifiedOneSelector',
    get: () => ({ get }) => {
        // This selector does not return any value
        return;
    },
    set: ({ recoilKey }) => ({ set, get }) => {
        const state = get(boardUpdateState);
        set(boardUpdateState, {
            ...state,
            modifiedOne: {
                ...state.modifiedOne,
                [recoilKey]: initialState.modifiedOne[recoilKey],
            },
        });
    },
});

export const boardUpdateResetOthersSelector = selectorFamily({
    key: 'boardUpdateResetOthersSelector',
    get: () => ({ get }) => {
        // This selector does not return any value
        return;
    },
    set: ({ recoilKey }) => ({ set, get }) => {
        const state = get(boardUpdateState);
        set(boardUpdateState, {
            ...state,
            others: {
                ...state.others,
                [recoilKey]: initialState.others[recoilKey],
            },
        });
    },
});
