import React, { useMemo, useState } from 'react';
import {
    ReasonPhrases,
    StatusCodes,
    getReasonPhrase,
    getStatusCode,
} from 'http-status-codes';
import {useSetRecoilState} from "recoil";
import {globalInfoAuthenticatedRoutesSelector} from "../recoil/globalInfoState";
import agent from "../api/agent";

//custom react-query hook
export const useGlobalSubMenusReload = () => {

    const setDynamicGlobalAuthenticatedRoutes = useSetRecoilState(globalInfoAuthenticatedRoutesSelector());

    const reloadDynamicRoutes = async () => {

        const re = await agent.Menu.fetchSubsNo401Redirect();
        if (re.statusCode === StatusCodes.OK) {
            setDynamicGlobalAuthenticatedRoutes(re.data);
        }else{
            console.log("reloadDynamicRoutes 실패 : " + re)
        }

    }

    return { reloadDynamicRoutes }
};
