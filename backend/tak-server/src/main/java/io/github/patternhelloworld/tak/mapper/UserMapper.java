package io.github.patternhelloworld.tak.mapper;


import io.github.patternhelloworld.tak.domain.common.user.dto.CombinedUserFilters;
import io.github.patternhelloworld.tak.domain.common.user.dto.UserCommonDTO;
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