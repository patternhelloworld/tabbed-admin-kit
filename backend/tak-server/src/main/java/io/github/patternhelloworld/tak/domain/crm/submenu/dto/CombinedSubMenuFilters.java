package io.github.patternhelloworld.tak.domain.crm.submenu.dto;

import io.github.patternhelloworld.common.domain.common.dto.DateRangeFilter;
import io.github.patternhelloworld.common.domain.common.dto.SorterValueFilter;
import lombok.Data;

@Data
public class CombinedSubMenuFilters {
    private DateRangeFilter dateRangeFilter;
    private SubMenuSearchFilter subMenuSearchFilter;
    private SorterValueFilter sorterValueFilter;
}
