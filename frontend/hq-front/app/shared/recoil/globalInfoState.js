import {atom, selector, selectorFamily} from 'recoil';

export const globalInfoState = atom({
    key: 'globalInfoState',
    default: {
        accessTokenUserInfo: {
            info: {}
        },
        sidebarCollapsed: {
            forceUpdate: null,
            value: false
        },
        anonymousRoutes : [
            { path: '/pages/login', exact: true, name: 'Login', recoilKey : 'Login' }
        ],
        authenticatedRoutes: {
            fixed: [
                {title: '게시판 베이스', path: '/issues', exact: true, recoilKey: 'Issues'},
                {title: '대시보드 메인', path: '/dashboards/projects', exact: true, recoilKey: 'ProjectsDashboard'},
            ],
            dynamic: [
            ],
            flattenedAll : []
        }
    }
});

export const globalInfoAccessTokenUserInfoSelector = selectorFamily({
    key: 'globalInfoAccessTokenUserInfoSelector',
    get: () => ({get}) => {
        const state = get(globalInfoState);
        return state.accessTokenUserInfo;
    },
    set: () => ({set, get}, newValue) => {
        const state = get(globalInfoState);
        set(globalInfoState, {
            ...state,
            accessTokenUserInfo: newValue
        });
    },
});


export const globalInfoSidebarCollapsedSelector = selectorFamily({
    key: 'globalInfoSidebarCollapsedSelector',
    get: () => ({get}) => {
        const state = get(globalInfoState);
        return state.sidebarCollapsed;
    },
    set: () => ({set, get}, newValue) => {
        const state = get(globalInfoState);
        set(globalInfoState, {
            ...state,
            sidebarCollapsed: newValue
        });
    },
});

const convertSubMenuListFromServerToFront = (menuData) => {
    const menuMap = new Map();

    menuData.forEach(item => {
        if (!menuMap.has(item.mainMenuIdx)) {
            menuMap.set(item.mainMenuIdx, {
                title: item.mainMenuNm,
                path: `/${item.mainMenuPath}`,
                recoilKey: item.mainMenuKey,
                children: []
            });
        }

        const parent = menuMap.get(item.mainMenuIdx);
        parent.children.push({
            title: item.subMenuNm,
            path: `/${item.mainMenuPath}/${item.subMenuPath}`,
            exact: true,
            recoilKey: `${item.mainMenuKey}${item.subMenuKey}`
        });
    });

    // Convert the map to an array of menus
    return Array.from(menuMap.values());
}

const flattenDynamicGlobalAuthenticatedRoutes = (menuData) => {

    let flatArray = [];

    const flatten = (menu) => {
        // Add the current menu item to the flat array
        flatArray.push({
            title: menu.title,
            path: menu.path,
            exact: true, // Ensure exact is set to true
            recoilKey: menu.recoilKey
        });

        // If the menu has children, recursively flatten them
        if (menu.children) {
            menu.children.forEach(child => flatten(child));
        }
    };

    // Start flattening from the top-level menu items
    menuData.forEach(menu => flatten(menu));

    return flatArray;
};


// [주의] 불러오는 것은 authenticatedRoutes 를 불러오나, set 은 authenticatedRoutes.dynamic 에만 함
export const globalInfoAuthenticatedRoutesSelector = selectorFamily({
    key: 'globalInfoAuthenticatedRoutesSelector',
    get: () => ({get}) => {
        const state = get(globalInfoState);
        return state.authenticatedRoutes;
    },
    set: () => ({set, get}, newValue) => {
        const state = get(globalInfoState);
        const dynamicList = convertSubMenuListFromServerToFront(newValue);
        set(globalInfoState, {
            ...state,
            authenticatedRoutes: {
                ...state.authenticatedRoutes,
                dynamic: dynamicList,
                flattenedAll : [...state.authenticatedRoutes.fixed, ...flattenDynamicGlobalAuthenticatedRoutes(dynamicList)]
            }
        });
    },
});

export const globalInfoAnonymousRoutesSelector = selectorFamily({
    key: 'globalInfoAnonymousRoutesSelector',
    get: () => ({get}) => {
        const state = get(globalInfoState);
        return state.anonymousRoutes;
    },
    set: () => ({set, get}, newValue) => {
        const state = get(globalInfoState);
        set(globalInfoState, {
            ...state,
            anonymousRoutes: newValue
        });
    },
});