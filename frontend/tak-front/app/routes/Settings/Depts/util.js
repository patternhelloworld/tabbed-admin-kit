export const sortDeptsByParentCd = (content = []) => {

    const map = {};
    const result = [];

    // 모든 데이터를 맵에 저장
    content.forEach(item => {
        map[item.deptIdx] = { ...item, children: [] };
    });

    // 부모와 자식을 연계
    content.forEach(item => {
        if (item.parentCd && item.parentCd !== item.deptIdx) {
            map[item.parentCd]?.children.push(map[item.deptIdx]);
        } else {
            result.push(map[item.deptIdx]);
        }
    });


    if(result.length < 1){
        return content;
    }

    // 부모-자식 관계를 풀어서 최종 배열로 변환
    const flatten = (arr) => arr.reduce((acc, item) => {
        acc.push(item);
        if (item.children.length > 0) {
            acc = acc.concat(flatten(item.children));
        }
        return acc;
    }, []);

    return flatten(result);
};