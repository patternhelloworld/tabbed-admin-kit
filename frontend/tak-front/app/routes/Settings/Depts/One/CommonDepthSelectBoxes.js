import React, { useState, useEffect } from 'react';
import { Input, FormGroup, Label, Col } from 'reactstrap';
import { DeptHierarchyUtil } from "../../../../shared/utils/utilities";  // 필요에 따라 import 방식 조정
; // 유틸리티 함수 가져오기

/*
*   upDeptIdx : ",1,2" 와 같은 양식의 스트링. 상위를 알 수 있다.
* */
const CommonDepthSelectBoxes = ({ deptsObj = {}, setDepth, upDeptIdxArr = undefined, formik, onDepthSelectBoxesChange }) => {

    const filterByParent = (depth, parentDeptIdx, upDeptIdxArr) => {
        return deptsObj[depth].filter(item => item.parentCd === parentDeptIdx);
    };

    return (
        <div>
            {Object.keys(deptsObj)
                .map(Number) // 키가 문자열이므로 숫자로 변환
                .sort((a, b) => a - b) // 오름차순 정렬
                .filter(depth => depth <= setDepth) // currentDepth보다 작은 depth만 표시
                .map((depth, index) => {
                    const parentDeptIdx = upDeptIdxArr[depth - 2]; // 마지막 선택된 deptIdx 사용
                    const options = depth === 1
                        ? deptsObj[depth] // depth가 1일 경우 필터링 없이 전체 표시
                        : filterByParent(depth, parentDeptIdx, upDeptIdxArr); // 부모가 있는 경우 필터링

                    return (
                        <FormGroup row key={depth}>
                            <Label for={`depth-${depth}`} lg={3}>
                                {depth} 단계
                            </Label>
                            <Col lg={9}>
                                <Input
                                    type="select"
                                    name={`depth-${depth}`}
                                    id={`depth-${depth}`}
                                    valid={!formik.errors[`depth-${depth}`] && formik.touched[`depth-${depth}`]}
                                    invalid={!!formik.errors[`depth-${depth}`] && formik.touched[`depth-${depth}`]}
                                    value={upDeptIdxArr[index] || ''}
                                    onChange={(e) => onDepthSelectBoxesChange(e.target.value, depth)}
                                >
                                    {options.map((item) => (
                                        <option key={item.deptIdx} value={item.deptIdx}>
                                            {item.deptNm} (me:{item.deptIdx}, parent:{item.parentCd})
                                        </option>
                                    ))}
                                    <option key="-" value={""}>미선택</option>
                                </Input>
                            </Col>
                        </FormGroup>
                    );
                })}
        </div>
    );
};

export default React.memo(CommonDepthSelectBoxes);
