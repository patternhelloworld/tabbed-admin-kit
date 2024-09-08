import React, {useEffect} from 'react';

import { SidebarMenu } from './../../components';

import { useTabs } from '../../shared/providers/TabsContext';
import {useRecoilValue, useSetRecoilState} from "recoil";
import {
    globalInfoAccessTokenUserInfoSelector,
    globalInfoAuthenticatedRoutesSelector
} from "../../shared/recoil/globalInfoState";
import {availableElement, CRUD_COLUMNS, isAuthorized} from "../../shared/utils/authorization";


export const SidebarMiddleNav = () => {

    const me = useRecoilValue(globalInfoAccessTokenUserInfoSelector());
    const globalAuthenticatedRoutes = useRecoilValue(globalInfoAuthenticatedRoutesSelector());
    const setDynamicGlobalAuthenticatedRoutes = useSetRecoilState(globalInfoAuthenticatedRoutesSelector());

    const { addTab } = useTabs();

    const generateMainMenuItem = (menu) => {
        return (<SidebarMenu.Item title={menu.title} key={menu.recoilKey} to={menu.path} onClick={() => addTab(menu.title, menu.path, menu.recoilKey)}/>);
    }

    const generateSidebarMenuItems = (menuList) => {
        const renderMenuItems = (menu) => {
            return (
                <SidebarMenu.Item title={menu.title} key={menu.recoilKey}>
                    {menu.children && menu.children.map((child) => (
                        <SidebarMenu.Item
                            extraClassNames={availableElement("", { accessTokenUserInfo: me, path: child.path, CRUD_COLUMN: CRUD_COLUMNS.READ })}
                            title={child.title}
                            to={child.path}
                            onClick={() => addTab(child.title, child.path, child.recoilKey)}
                            key={child.recoilKey}
                        />
                    ))}
                </SidebarMenu.Item>
            );
        };

        return menuList.map(menu => renderMenuItems(menu));
    };

    return (
        <SidebarMenu>
            {generateMainMenuItem(globalAuthenticatedRoutes.fixed.find(x => x.recoilKey === 'ProjectsDashboard'))}
            {generateSidebarMenuItems(globalAuthenticatedRoutes.dynamic)}
        </SidebarMenu>
    );
}
