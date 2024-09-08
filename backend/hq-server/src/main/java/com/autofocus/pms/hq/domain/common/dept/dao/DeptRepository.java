package com.autofocus.pms.hq.domain.common.dept.dao;

import com.autofocus.pms.hq.config.database.typeconverter.YNCode;
import com.autofocus.pms.hq.domain.common.dept.entity.Dept;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface DeptRepository extends JpaRepository<Dept, Integer>, QuerydslPredicateExecutor<Dept> {
    Dept findByDeptIdxAndDelYn(Integer deptIdx, YNCode delYn);

    /*
         부모들의 dept_idx 들을 List 로 가져옴
     *
     *   natveQuery = true 라는 의미는 JPQL 이 아닌, 여기서는 mysql 쿼리로 작동된다는 의미 = 특정 DB 종속성이 생긴다는 의미.
     *   아직 테스트 해보지는 않았지만 필요하다면 추후 사용. 프론트에서는 이와 같은 기능을 하는 함수가 있음.
     * */
    @Query(value = "WITH RECURSIVE cte (dept_idx, parent_cd) AS (" +
            "    SELECT dept_idx, parent_cd FROM dept WHERE dept_idx = :deptIdx" +
            "    UNION ALL" +
            "    SELECT d.dept_idx, d.parent_cd FROM dept d" +
            "    INNER JOIN cte ON d.dept_idx = cte.parent_cd" +
            "    WHERE cte.parent_cd <> cte.dept_idx" +
            ") SELECT dept_idx FROM cte", nativeQuery = true)
    List<Integer> findDeptHierarchy(@Param("deptIdx") Integer deptIdx);


    /* 위와 반대 방향 */
    @Query(value = "WITH RECURSIVE cte (dept_idx, parent_cd) AS (" +
            "    SELECT dept_idx, parent_cd FROM dept WHERE parent_cd = :deptIdx" +  // Start from the parent department
            "    UNION ALL" +
            "    SELECT d.dept_idx, d.parent_cd FROM dept d" +
            "    INNER JOIN cte ON d.parent_cd = cte.dept_idx" +  // Recursive join to find all children
            ") SELECT dept_idx FROM cte", nativeQuery = true)
    List<Integer> findChildDepts(@Param("deptIdx") Integer deptIdx);

}