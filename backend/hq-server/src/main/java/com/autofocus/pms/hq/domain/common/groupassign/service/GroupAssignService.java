package com.autofocus.pms.hq.domain.common.groupassign.service;

import com.autofocus.pms.hq.domain.common.customer.dao.CustomerRepositorySupport;
import com.autofocus.pms.hq.domain.common.groupassign.dao.GroupAssignRepository;
import com.autofocus.pms.hq.domain.common.groupassign.dao.GroupAssignRepositorySupport;
import com.autofocus.pms.hq.domain.common.groupassign.dto.GroupAssignResDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class GroupAssignService {
    private final GroupAssignRepository groupAssignRepository;
    private final GroupAssignRepositorySupport groupAssignRepositorySupport;
    private final CustomerRepositorySupport customerRepositorySupport;

    public Page<GroupAssignResDTO.One> getGroupedCustomers (Boolean skipPagination, Integer pageNum, Integer pageSize, String groupAssignSearchFilter, String sorterValueFilter, String dateRangeFilter) throws JsonProcessingException {
        return customerRepositorySupport.findByPageAndFilterAndGroupAssign(skipPagination, pageNum, pageSize, groupAssignSearchFilter, sorterValueFilter, dateRangeFilter);
    }
}
