package com.autofocus.pms.common.domain.common.dto;

import lombok.Data;

@Data
public class SorterValueFilter {
    private String column;
    private Boolean asc;
}
