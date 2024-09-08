package com.autofocus.pms.hq.domain.crm.submenu.dto;

import com.autofocus.pms.common.domain.common.dto.DateRangeFilter;
import com.autofocus.pms.common.domain.common.dto.SorterValueFilter;
import lombok.Data;

@Data
public class CombinedSubMenuFilters {
    private DateRangeFilter dateRangeFilter;
    private SubMenuSearchFilter subMenuSearchFilter;
    private SorterValueFilter sorterValueFilter;
}
