import React, {createContext, useContext, useEffect, useState} from 'react';
import {
    boardUpdateOneSelector, boardUpdateResetOthersSelector,
    boardUpdateResetModifiedOneSelector,
    boardUpdateResetOneSelector
} from "../recoil/board/boardUpdateState";
import {boardListResetSelector} from "../recoil/board/boardListState";
import {useRecoilValue, useResetRecoilState, useSetRecoilState} from "recoil";
import {
    boardCreateResetOthersSelector,
    boardCreateResetModifiedOneSelector,
    boardCreateResetOneSelector
} from "../recoil/board/boardCreateState";

import {useHistory} from "react-router-dom";
import {globalInfoAuthenticatedRoutesSelector, globalInfoSidebarCollapsedSelector} from "../recoil/globalInfoState";




const TabsContext = createContext();

export const TabsProvider = ({ children }) => {

    const history = useHistory();

    const setGlobalSidebarCollapsed = useSetRecoilState(globalInfoSidebarCollapsedSelector());

    const [tabs, setTabs] = useState([]);
    const [activeTab, setActiveTab] = useState(0);

    const [currentRecoilKey, setCurrentRecoilKey] = useState(undefined);

    const resetUpdateOne =  useResetRecoilState(boardUpdateResetOneSelector({ recoilKey : currentRecoilKey }));
    const resetUpdateModifiedOne =  useResetRecoilState(boardUpdateResetModifiedOneSelector({ recoilKey : currentRecoilKey }));
    const resetUpdateOthersOne =  useResetRecoilState(boardUpdateResetOthersSelector({ recoilKey : currentRecoilKey }));

    const resetList =  useResetRecoilState(boardListResetSelector({ recoilKey : currentRecoilKey }));

    const resetCreateOne =  useResetRecoilState(boardCreateResetOneSelector({ recoilKey : currentRecoilKey }));
    const resetCreateModifiedOne =  useResetRecoilState(boardCreateResetModifiedOneSelector({ recoilKey : currentRecoilKey }));
    const resetCreateOthersOne =  useResetRecoilState(boardCreateResetOthersSelector({ recoilKey : currentRecoilKey }));

    const globalAuthenticatedRoutes = useRecoilValue(globalInfoAuthenticatedRoutesSelector());


    const addTab = (title, to, recoilKey) => {

        const existingTabIndex = tabs.findIndex(tab => tab.to === to);

        if (existingTabIndex !== -1) {
            // 이미 열려 있는 탭을 활성화
            setActiveTab(existingTabIndex);
            return;
        }

        if (tabs.length >= 20) {
            alert("20개 이상 금지");
            return false;
        }

        setTabs([...tabs, { title: `${title}`, to: `${to}`, recoilKey }]);
        setActiveTab(tabs.length);
    };
    const removeTab = (index) => {

        if(tabs.length < 2){
            alert("최소한 1개의 Tab 은 있어야 합니다.");
            setGlobalSidebarCollapsed({
                forceUpdate: Math.random(),
                value: false
            })
            return;
        }

        const tabToRemove = tabs[index];
        const newTabs = tabs.filter((tab, i) => i !== index);
        setTabs(newTabs);

        let moveToIndex = 0;
        console.log("index : " + index)
        console.log("newTabs.length : " + newTabs.length)
        if(newTabs.length -1 > index){
            moveToIndex = newTabs.length -1;
        }else {
            if(index < 1){
                moveToIndex = 0
            }else{
                moveToIndex = index - 1;
            }
        }

        setActiveTab(moveToIndex);

        const moveToTab = newTabs.find((tab, i) => i === moveToIndex);
        if(moveToTab){
            history.push(moveToTab.to);
        }

        // 제거된 Tab 은 전역 State 관리 Recoil 에서 제거한다.
        if (tabToRemove.recoilKey) {
            resetRecoils(tabToRemove.recoilKey);
        }
    };

    const resetRecoils = (recoilKey) => {

        setCurrentRecoilKey(recoilKey)

    }

    useEffect(()=>{
        if(tabs.length === 0) {
            const url = new URL(window.location.href);
            const pathname = url.pathname;
            const authenticatedRoute = globalAuthenticatedRoutes.flattenedAll.find(x => x.path === pathname);
            if (authenticatedRoute) {
                //if(authenticatedRoute.recoilKey !== "ProjectsDashboard") {
                    addTab(authenticatedRoute.title, authenticatedRoute.path, authenticatedRoute.recoilKey)
                //}
            }
        }
    })

    useEffect(()=>{
        if(currentRecoilKey !== undefined) {

            // 수정 화면
            resetUpdateOne()
            resetUpdateModifiedOne()
            resetUpdateOthersOne()

            // 신규 화면
            resetCreateOne()
            resetCreateModifiedOne()
            resetCreateOthersOne()

            // 리스트 화면
            resetList()

            console.log("[NOTICE] Reset Recoil : " + currentRecoilKey)
        }
    },[currentRecoilKey])

    return (
        <TabsContext.Provider value={{ tabs, activeTab, setActiveTab, addTab, removeTab }}>
            {children}
        </TabsContext.Provider>
    );
};

export const useTabs = () => {
    const context = useContext(TabsContext);
    if (!context) {
        throw new Error('useTabs must be used within a TabsProvider');
    }
    return context;
};