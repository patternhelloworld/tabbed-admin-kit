package io.github.patternhelloworld.tak.domain.common.user.dao;


import io.github.patternhelloworld.common.config.response.error.exception.data.ResourceNotFoundException;
import io.github.patternhelloworld.common.domain.common.dto.DateRangeFilter;
import io.github.patternhelloworld.common.domain.common.dto.SorterValueFilter;
import io.github.patternhelloworld.common.util.CustomUtils;
import io.github.patternhelloworld.common.util.PaginationUtil;
import io.github.patternhelloworld.tak.config.database.CommonQuerydslRepositorySupport;

import io.github.patternhelloworld.tak.config.database.typeconverter.YNCode;
import io.github.patternhelloworld.tak.domain.common.dept.entity.QDept;
import io.github.patternhelloworld.tak.domain.common.user.dto.*;
import io.github.patternhelloworld.tak.domain.common.user.entity.QUser;
import io.github.patternhelloworld.tak.domain.common.user.dto.CombinedUserFilters;
import io.github.patternhelloworld.tak.domain.common.user.dto.UserCommonDTO;
import io.github.patternhelloworld.tak.domain.common.user.dto.UserSearchFilter;
import io.github.patternhelloworld.tak.domain.common.user.entity.User;
import io.github.patternhelloworld.tak.mapper.UserMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/*
*
*   QueryDsl 을 써야하는 경우 = 다른 엔터티들 Join, Group by, having... + 동적 where
*
*   Repository 에는 Repository 가 다른 엔터티 종류는 못옴
* */
@Repository
public class UserRepositorySupport extends CommonQuerydslRepositorySupport {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JPAQueryFactory jpaQueryFactory;


    private EntityManager entityManager;

    public UserRepositorySupport(UserRepository userRepository, UserMapper userMapper,
                                 @Qualifier("commonJpaQueryFactory") JPAQueryFactory jpaQueryFactory) {

        super(User.class);
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    @PersistenceContext(unitName = "commonEntityManager")
    public void setEntityManager(EntityManager entityManager) {
        super.setEntityManager(entityManager);
        this.entityManager = entityManager;
    }


    public User findById(Long userIdx) throws ResourceNotFoundException {
        return userRepository.findById(userIdx).orElseThrow(() -> new ResourceNotFoundException("findById - User not found for this userIdx :: " + userIdx));
    }

    public User findByUserId(String userId) throws ResourceNotFoundException {
        return userRepository.findByUserId(userId).orElseThrow(() -> new ResourceNotFoundException("findById - User not found for this userId :: " + userId));
    }


    /*
    *
    *   다른 Database 와의 Join 을 위해 Mybatis 를 사용. (JPA 의 연관 관계 설정은 같은 DB에서만 가능하다)
    *   기본적으로는 CRM이 아닌 Common DB 쪽의 설정으로 되어 있다. 하지만, CRM 과의 Join 에는 문제가 없다.
    *
    * */
    @Transactional(value = "commonTransactionManager" ,readOnly = true)
    public Page<UserCommonDTO.OneWithDeptDealer> getUsersPage(Integer pageNum,
                                                              Integer pageSize,
                                                              String userSearchFilter,
                                                              String sorterValueFilter,
                                                              String dateRangeFilter) throws JsonProcessingException {

        CombinedUserFilters combinedUserFilters = new CombinedUserFilters();

        ObjectMapper objectMapper = new ObjectMapper();

        if(!CustomUtils.isEmpty(userSearchFilter)) {
            UserSearchFilter deserializedUserSearchFilter = (UserSearchFilter) objectMapper.readValue(userSearchFilter, UserSearchFilter.class);
            combinedUserFilters.setUserSearchFilter(deserializedUserSearchFilter);
        }
        if(!CustomUtils.isEmpty(dateRangeFilter)) {
            DateRangeFilter deserializedDateRangeFilter = (DateRangeFilter) objectMapper.readValue(dateRangeFilter, DateRangeFilter.class);
            if (deserializedDateRangeFilter.getEndDate() != null &&
                    !deserializedDateRangeFilter.getEndDate().matches(".*\\d{2}:\\d{2}:\\d{2}$")) {
                String newEndDate = deserializedDateRangeFilter.getEndDate() + "T23:59:59";
                deserializedDateRangeFilter.setEndDate(newEndDate);
            }
            combinedUserFilters.setDateRangeFilter(deserializedDateRangeFilter);
        }
        if(!CustomUtils.isEmpty(sorterValueFilter)) {
            SorterValueFilter deserializedSorterValueFilter = (SorterValueFilter) objectMapper.readValue(sorterValueFilter, SorterValueFilter.class);
            combinedUserFilters.setSorterValueFilter(deserializedSorterValueFilter);
        }

        long offset = (pageNum - 1) * pageSize;
        long limit = pageSize;

        List<UserCommonDTO.OneWithDeptDealer> users = userMapper.findWithDeptDealerByPageFilter(combinedUserFilters,
                limit, offset);

        long total = userMapper.countWithDeptDealerByPageFilter(combinedUserFilters);

        PaginationUtil paginationUtil = new PaginationUtil();
        return paginationUtil.applyPagination(users, pageNum, pageSize, total);
    }

    public List<UserCommonDTO.OneWithDept> findUserInfosByUserIds(List<Long> userIds) {
        QUser qUser = QUser.user;
        QDept qDept = QDept.dept;


        JPQLQuery<UserCommonDTO.OneWithDept> query = jpaQueryFactory.select(new QUserCommonDTO_OneWithDept(qUser.userIdx, qUser.name,
                        qUser.position, qDept.deptNm))
                .from(qUser)
                .leftJoin(qDept).on(qDept.deptIdx.eq(qUser.dept.deptIdx))
                .where(
                        qUser.userIdx.in(userIds)
                );

        return query.fetch();
    }

    public void deleteOne(Long userIdx, String modifier) {
        User user = findById(userIdx);
        user.setDelDt(LocalDateTime.now());
        user.setDelYn(YNCode.Y);
        user.setDelUserId(modifier);
    }


    public void restoreOne(Long userIdx, String modifier) {
        User user = findById(userIdx);

        user.setDelDt(null);
        user.setDelYn(YNCode.N);
        user.setDelUserId(null);
        user.setModUserId(modifier);
    }

    public void destroyOne(Long userIdx) {
        User user = findById(userIdx);
        userRepository.delete(user);
    }

}
