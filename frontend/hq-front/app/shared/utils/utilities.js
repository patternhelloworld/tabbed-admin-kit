const allAvatars = (ctx => {
    let keys = ctx.keys();
    return keys.map(ctx);
})(require.context('../../assets/images/avatars', true, /.*/));

export function randomArray(arr) {
    const index = Math.round(Math.random() * (arr.length - 1));
    return arr[index];
}

export function randomAvatar() {
    return randomArray(allAvatars);
}


export const safeValue = (value, defaultValue) => {
    return value !== undefined && value !== null ? value : defaultValue;
};


/*
*
*   하기 isObject 부터 isValidArray 까지의 함수를 사용해서 recoil 의 initialState 후에 값 들이 들어왔는지 확인이 가능하다.
*
* */

export function isObject(value) {
    return value !== null && typeof value === 'object' && !Array.isArray(value);
}

export function isValidObject(obj){
    return isObject(obj) && Object.keys(obj).length > 0
}

export function isCreateOne(obj, pkName){
    return isValidObject(obj) && obj[pkName] === null
}

export function isUpdateOne(obj, pkName){
    return isValidObject(obj) && obj[pkName]
}

function isArray(arr) {
    return Array.isArray(arr);
}

export function isValidArray(arr) {
    return isArray(arr) && arr.length > 0;
}

export const CodeGeneralUtil = {

    getSelectOptions : ({codeCustomers = [], categoryCd}) => {
        const codeCustomer = codeCustomers.find(x => x.categoryCd === categoryCd);
        if(codeCustomer){
            return codeCustomers.filter(x => x.parent === codeCustomer.codeCustomerIdx).map(y => ({
                value : y.codeCustomerIdx,
                text : y.nm
            }));
        }else{
            return []
        }
    },
    getSelectedText : ({ codeCustomers = [], codeCustomerIdx}) => {
        const codeCustomer = codeCustomers.find(x => x.codeCustomerIdx === codeCustomerIdx);
        if(codeCustomer){
            return codeCustomer.nm;
        }else{
            return "";
        }
    }

}


export const DeptHierarchyUtil = {

    stringToArray : (str = "") => {
        return str.split(',').filter(num => num).map(Number);
    },
    arrayToString : (arr = []) => {
        return `,${arr.filter(Boolean).join(',')}`;
    },

    getParentCdByDeptIdx: (depts = [], deptIdx) => {
        return depts.find(x => x.deptIdx == deptIdx)?.parentCd
    },

    getDeptIdxArrayByParentCd: (depts, parentCd) => {
        const hierarchy = [];

        if (depts && parentCd) {
            const findParent = (currentParentCd) => {
                const parent = depts.find(dept => dept.deptIdx == currentParentCd);

                if (parent) {
                    hierarchy.unshift(parent.deptIdx); // 부모를 배열의 앞에 추가

                    // 자기 참조인 경우, 여기서 끝냄
                    if (parent.deptIdx === parent.parentCd) {
                        return;
                    }else if(!parent.deptIdx || !parent.parentCd){
                        return;
                    }

                    findParent(parent.parentCd); // 부모의 부모를 찾아서 재귀 호출
                }
            };

            findParent(parentCd);

        }

        return hierarchy;
    },

    getDeptNmArrayByParentCd: (depts, parentCd) => {
        const hierarchy = [];


        if (depts && parentCd) {
            const findParent = (currentParentCd) => {
                const parent = depts.find(dept => dept.deptIdx == currentParentCd);

                if (parent) {
                    hierarchy.unshift(parent.deptNm); // 부모를 배열의 앞에 추가

                    // 자기 참조인 경우, 여기서 끝냄
                    if (parent.deptIdx === parent.parentCd) {
                        return;
                    }else if(!parent.deptIdx || !parent.parentCd){
                        return;
                    }

                    findParent(parent.parentCd); // 부모의 부모를 찾아서 재귀 호출
                }
            };

            findParent(parentCd);

        }

        return hierarchy;
    },

    getDepthByDeptIdx: (depts, currentDeptIdx) => {
        let depth = 0;

        if (depts && currentDeptIdx) {
            const visited = new Set(); // To keep track of visited departments to avoid infinite loops

            const findDepth = (currentDeptIdx) => {
                if (visited.has(currentDeptIdx)) {
                    return; // If we've already visited this department, stop to prevent infinite loop
                }
                visited.add(currentDeptIdx);

                const currentDept = depts.find(dept => dept.deptIdx == currentDeptIdx);

                if (currentDept) {
                    depth++; // Increase the depth count

                    // Stop if it's a self-referencing department or there's no valid parent
                    if (currentDept.deptIdx === currentDept.parentCd || !currentDept.parentCd) {
                        return;
                    }

                    findDepth(currentDept.parentCd); // Recursively find the parent's depth
                }
            };

            findDepth(currentDeptIdx);
        }

        return depth;
    },

   mergeDeptsAndUsers : (depts, users) => {
        const result = [];

        depts.forEach(dept => {
            // 부서를 먼저 추가
            result.push(dept);

            // 해당 부서에 속한 사용자들을 추가
            users.filter(user => user.deptIdx == dept.deptIdx).forEach(user => {
                result.push({
                    deptIdx: dept.deptIdx,  // 부서 ID를 그대로 사용
                    userIdx: user.userIdx,  // 사용자 ID
                    userName: user.name, // 사용자 이름
                    userId: user.userId,
                    depth: dept.depth + 1   // 부서 depth 보다 하나 더 깊은 depth
                });
            });
        });

        return result;
    },

    /**
     * Validates if `setDepth` is not `0` and `parentCd` is the same as `deptIdx`.
     * @param {number} setDepth - The depth of the department.
     * @param {number} deptIdx - The ID of the department.
     * @param {number} parentCd - The ID of the parent department.
     * @returns {boolean} - Returns `true` if invalid, `false` otherwise.
     */
    validateSelfReference : (setDepth, deptIdx, parentCd) => {
        if (setDepth !== 0 && deptIdx === parentCd) {
            return true; // Invalid case
        }
        return false; // Valid case
    },


    /**
     * Validates if a department has itself as a parent and has a circular reference with its children.
     * @param {Array} depts - Array of department objects.
     * @param {number} deptIdx - The ID of the department being checked.
     * @param {number} parentCd - The ID of the parent department.
     * @returns {boolean} - Returns `true` if invalid, `false` otherwise.
     */
    validateCircularReference : (depts, deptIdx, parentCd, setDepth) => {
        if(setDepth < 1){
            return false
        }
        // Find departments that have the current deptIdx as their parent
        const children = depts.filter(dept => dept.parentCd === deptIdx);

        // Check if any of these children have deptIdx as their parentCd
        const hasCircularReference = children.some(child => child.deptIdx === parentCd);

        return hasCircularReference;
    },


}
