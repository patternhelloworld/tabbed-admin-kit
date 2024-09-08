package com.autofocus.pms.hq.mapper;


import com.autofocus.pms.hq.domain.common.user.dto.CombinedUserFilters;
import com.autofocus.pms.hq.domain.common.user.dto.UserCommonDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    UserCommonDTO.OneWithDeptDealerMenus findOneWithDeptDealerMenus(String username);

    List<UserCommonDTO.OneWithDeptDealer> findWithDeptDealerByPageFilter(
            @Param("filter") CombinedUserFilters filter,
            @Param("limit") long limit,
            @Param("offset") long offset
    );
    long countWithDeptDealerByPageFilter(@Param("filter") CombinedUserFilters filter);
}