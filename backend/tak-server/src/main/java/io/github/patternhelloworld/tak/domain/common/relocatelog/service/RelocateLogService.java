package io.github.patternhelloworld.tak.domain.common.relocatelog.service;

import io.github.patternhelloworld.tak.domain.common.relocatelog.dao.RelocateLogRepository;
import io.github.patternhelloworld.tak.domain.common.relocatelog.dao.RelocateLogRepositorySupport;
import io.github.patternhelloworld.tak.domain.common.relocatelog.dto.RelocateLogResDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RelocateLogService {
    private final RelocateLogRepositorySupport relocateLogRepositorySupport;
    private final RelocateLogRepository relocateLogRepository;

    public Page<RelocateLogResDTO.One> getRelocatedCustomerPage(Boolean skipPagination, Integer pageNum, Integer pageSize, String customerSearchFilter, String sorterValueFilter, String dateRangeFilter) throws JsonProcessingException {
        return relocateLogRepositorySupport.findByPageAndFilter(skipPagination, pageNum, pageSize, customerSearchFilter, sorterValueFilter, dateRangeFilter);
    }

}
