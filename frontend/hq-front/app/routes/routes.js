import React, {Component, Suspense, useEffect} from 'react';
import {
    Route,
    Switch,
    Redirect
} from 'react-router-dom';


import NavbarOnly from '../layout/components/NavbarOnly';
import SidebarWithNavbar from '../layout/components/SidebarWithNavbar';


// ----------- Layout Imports ---------------
import { DefaultSidebar } from '../layout/components/DefaultSidebar';

import { SidebarANavbar } from '../layout/components/SidebarANavbar';
import { SidebarASidebar } from '../layout/components/SidebarASidebar';
import {getAccessToken, removeAccessTokenToLoginPage} from "../shared/api/AccessTokenManager";
import {authenticatedRoutesComponents, anonymousRoutesComponents } from "./routes-components";

import {useRecoilValue, useSetRecoilState} from "recoil";
import {
    globalInfoAnonymousRoutesSelector,
    globalInfoAuthenticatedRoutesSelector,
} from "../shared/recoil/globalInfoState";
import {useGlobalSubMenusReload} from "../shared/hooks/useGlobalSubMenusReload";


const ProtectedRoute = ({ path, recoilKey,  component: Component }) => {

    return (
    <Route
        path={path}
        render={props => (getAccessToken() ? <Component {...props} path={path} recoilKey={recoilKey} /> : <Redirect to="/pages/login" />)}
    />
)};

const renderAuthenticatedComponent = ({ route, idx}) => {

    return <ProtectedRoute
        key={route.recoilKey}
        path={route.path}
        recoilKey={route.recoilKey}
        component={route.component}/>;
};


/*
*
*   1) 설명
*       globalAuthenticatedRoutes : 권한 구조 검사를 위해 하기위해 서버의 subMenu 테이블의 값을 가져온다.
*       globalInfoAnonymousRoutes : 권한 구조 검사가 필요 없는 라우트인 예를 들어 "로그인 페이지"의 경우 서버에 가서 경로를 가져올 필요가 없다.
*
*   2) routes-components
*     : 실제 경로에 해당하는 컴포넌트들이 맵핑되어 있다. index.js 와 routes-components.js 를 1개의 파일로 할 경우 React 구조적 특성으로, Circular Reference 이슈가 발생할 수 있다.
*       authenticatedRoutesComponents
*       anonymousRoutesComponents
*
* */

export const RoutedContent = () => {

    const globalAuthenticatedRoutes = useRecoilValue(globalInfoAuthenticatedRoutesSelector());
    const globalInfoAnonymousRoutes = useRecoilValue(globalInfoAnonymousRoutesSelector())


    return (<Switch>
                {anonymousRoutesComponents.map(x =>
                    ({...globalInfoAnonymousRoutes.find(y => y.recoilKey === x.recoilKey), ...x})).map((route, idx) => {

                    if(route.component) {
                        return <Route
                            key={route.path}
                            path={route.path}
                            component={route.component}/>;
                    }
                })}

                {authenticatedRoutesComponents.map(x =>
                    ({...globalAuthenticatedRoutes.flattenedAll.find(y => y.recoilKey === x.recoilKey), ...x})).map((route, idx) => {

                            if(route.component) {
                                return renderAuthenticatedComponent({ route, idx, history });
                            }
                    })}
                <Redirect from="/" to="/dashboards/projects" exact={true} />
         </Switch>);
};

//------ Custom Layout Parts --------
export const RoutedNavbars  = () => (
    <Switch>
        { /* Other Navbars: */}
        <Route
            component={ SidebarANavbar }
            path="/layouts/sidebar-a"
        />
        <Route
            component={ NavbarOnly.Navbar }
            path="/layouts/navbar"
        />
        <Route
            component={ SidebarWithNavbar.Navbar }
            path="/layouts/sidebar-with-navbar"
        />
        { /* Default Navbar: */}
        <Route
            component={ SidebarWithNavbar.Navbar }
        />
    </Switch>
);

export const RoutedSidebars = () => (
    <Switch>
        { /* Other Sidebars: */}
        <Route
            component={ SidebarASidebar }
            path="/layouts/sidebar-a"
        />
        <Route
            component={ SidebarWithNavbar.Sidebar }
            path="/layouts/sidebar-with-navbar"
        />
        { /* Default Sidebar: */}
        <Route
            component={ DefaultSidebar }
        />
    </Switch>
);
