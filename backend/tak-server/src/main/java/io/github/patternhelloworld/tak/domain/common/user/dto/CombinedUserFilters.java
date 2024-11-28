package io.github.patternhelloworld.tak.domain.common.user.dto;

import io.github.patternhelloworld.common.domain.common.dto.DateRangeFilter;
import io.github.patternhelloworld.common.domain.common.dto.SorterValueFilter;
import lombok.Data;

@Data
public class CombinedUserFilters {
    private DateRangeFilter dateRangeFilter;
    private UserSearchFilter userSearchFilter;
    private SorterValueFilter sorterValueFilter;
}
