import React, {useEffect, useState, Fragment} from 'react';

import {useRecoilCallback, useRecoilValue, useResetRecoilState, useSetRecoilState} from "recoil";
import {boardListOthersSelector} from "../../recoil/board/boardListState";
import {fetchSecondDepthDepts} from "../../../routes/Cars/DealerStocks/prevFetchFuncs";
import {sortDeptsByParentCd} from "../../../routes/Settings/Depts/util";



export const useSecondDepthDepts = ({recoilKey}) => {

    // 읽기 전용 - 검색창
    const secondDepthDepts = useRecoilValue(boardListOthersSelector({recoilKey, topic: "secondDepthDepts"}));
    const selectedSecondDepthDeptIdx = useRecoilValue(boardListOthersSelector({recoilKey, topic: "selectedSecondDepthDeptIdx"}));

    // 쓰기 전용 - 검색창
    const setSecondDepthDepts = useSetRecoilState(boardListOthersSelector({recoilKey, topic: "secondDepthDepts"}));
    const setSelectedSecondDepthDeptIdx = useSetRecoilState(boardListOthersSelector({
        recoilKey,
        topic: "selectedSecondDepthDeptIdx"
    }));


    /*
    *   특수 검색 : "담당 조직 2단계 전시장으로 검색"
    * */
    const handleSecondDepthDepts = async (always = false) => {
        if(secondDepthDepts.length === 0 || always) {
            const secondDepthDepts = await fetchSecondDepthDeptsCycle();
            setSecondDepthDepts(secondDepthDepts)
        }
    }
    const fetchSecondDepthDeptsCycle = async () => {
        const re = await Promise.all([fetchSecondDepthDepts({})]);
        if (re[0]) {
            // depts
            return sortDeptsByParentCd(re[0].map(x => ({
                deptIdx: x.deptIdx,
                parentCd: x.parentCd,
                deptNm: x.deptNm
            })));

        } else {
            return [];
        }
    };


    return {
        secondDepthDepts,setSecondDepthDepts,
        selectedSecondDepthDeptIdx, setSelectedSecondDepthDeptIdx,
        handleSecondDepthDepts, fetchSecondDepthDeptsCycle
    }

};
