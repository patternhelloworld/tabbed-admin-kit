package com.autofocus.pms.hq.domain.common.user.dto;

import com.autofocus.pms.common.domain.common.dto.DateRangeFilter;
import com.autofocus.pms.common.domain.common.dto.SorterValueFilter;
import lombok.Data;

@Data
public class CombinedUserFilters {
    private DateRangeFilter dateRangeFilter;
    private UserSearchFilter userSearchFilter;
    private SorterValueFilter sorterValueFilter;
}
