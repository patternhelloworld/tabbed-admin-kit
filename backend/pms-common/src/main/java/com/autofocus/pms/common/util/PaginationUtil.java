package com.autofocus.pms.common.util;

import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.sql.JPASQLQuery;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;


public class PaginationUtil {


    /*
    *
    *   1. JPQL
    *
    * */

    public <T> Page<T> applyPagination(JPQLQuery<T> query, int pageNum, int pageSize, boolean skipPagination) {
        return applyPagination(query, pageNum, pageSize, skipPagination, false, null);
    }

    public <T> Page<T> applyPagination(JPQLQuery<T> query, int pageNum, int pageSize, boolean skipPagination, boolean skipCalculateTotalElements) {
        return applyPagination(query, pageNum, pageSize, skipPagination, skipCalculateTotalElements, null);
    }

    public <T> Page<T> applyPagination(JPQLQuery<T> query, int pageNum, int pageSize, boolean skipPagination, Long totalElements) {
        return applyPagination(query, pageNum, pageSize, skipPagination, false, totalElements);
    }

    private <T> Page<T> applyPagination(JPQLQuery<T> query, int pageNum, int pageSize, boolean skipPagination, boolean skipCalculateTotalElements, Long totalElements) {
        PageRequest pageRequest = getPageRequest(pageNum, pageSize, skipPagination);

        if (totalElements == null) {
            totalElements = skipCalculateTotalElements ? Integer.parseInt(CommonConstant.NO_NEED_FOR_TOTAL_ELEMENT_COUNTS_PAGE_SIZE_DEFAULT) : query.fetchCount();
        }

        if (pageRequest.isPaged()) {
            query.offset(pageRequest.getOffset());
            query.limit(pageRequest.getPageSize());
        }

        return new PageImpl<>(query.fetch(), pageRequest, totalElements);
    }

    private PageRequest getPageRequest(int pageNum, int pageSize, boolean skipPagination) {
        if (skipPagination) {
            pageNum = Integer.parseInt(CommonConstant.COMMON_PAGE_NUM);
            pageSize = Integer.parseInt(CommonConstant.COMMON_PAGE_SIZE_DEFAULT_MAX);
        }

        return PageRequest.of(pageNum - 1, pageSize);
    }



    /*
    *
    *
    *   2. JPASQL (JPQL 아님. JPQL 은 모든 DB에 적용되는 공통 쿼리이고, JPASQL 은 특정 DB의 쿼리 사용 - MyBatis 와 같음.)
    *
    * */


    public <T> Page<T> applyPagination(@NotNull JPASQLQuery<T> query, int pageNum, int pageSize, boolean skipPagination) {
        PageRequest pageRequest = getPageRequest(pageNum, pageSize, skipPagination);

        List<T> contents = query.fetch();

        long totalElements = contents.size();

        if (pageRequest.isPaged()) {
            query.offset(pageRequest.getOffset());
            query.limit(pageRequest.getPageSize());
        }

        return new PageImpl<>(contents, pageRequest, totalElements);
    }


    public <T> Page<T> applyPagination(List<T> list, int pageNum, int pageSize, long total) {
        return new PageImpl<>(list, PageRequest.of(pageNum - 1, pageSize), total);
    }
}
