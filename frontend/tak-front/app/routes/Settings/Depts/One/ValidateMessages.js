import React from 'react';
import {DeptHierarchyUtil} from "../../../../shared/utils/utilities";


const ValidationMessages = ({ selfReferenceError, currentDepth, depthMismatchWarning }) => {

    //  단계가 지금과 다른 조직으로 편입될 예정입니다. 비어 있는 단계가 있다면 무시되고 현재 조직이 상위 조직으로 올라갑니다. (setDepth : {setDepth}, {Number(DeptHierarchyUtil.getDepthByDeptIdx(depts, deptIdx)) - 1})

    return (
        <div>
            {/* Self Reference Error */}
            {(selfReferenceError()) ? (
                <h6 className="mt-2 mb-2 text-danger">
                    자기 자신을 자기 자신의 하위 조직으로 넣을 수 없습니다.
                </h6>
            ) :
                (depthMismatchWarning() && currentDepth() > -1)? (
                    <h6 className="mt-2 mb-2 text-warning">
                        단계가 지금과 다른 조직으로 편입될 예정입니다. 최종 삽입 단계에서 비어 있는 단계가 있다면 무시되고 현재 조직이 상위 단계로 올라갑니다.
                    </h6>
                ) : ""
            }
        </div>
    );
};

export default React.memo(ValidationMessages);
