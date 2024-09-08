import React, { useState, useEffect, useCallback, useRef } from "react";
import {dateToYmdStr} from "../utils/date-handler";
import {
  boardListDateRangeColumnSelector, boardListDateRangeSelector,
  boardListSearchColumnSelector,
  boardListSearchValueSelector
} from "../recoil/board/boardListState";
import {useRecoilState, useRecoilValue, useResetRecoilState, useSetRecoilState} from 'recoil';

export const useListSearchModuleMeta = ({ recoilKey = undefined}) => {

  const [searchColumn, setSearchColumn] = useRecoilState(boardListSearchColumnSelector({ recoilKey }));
  const [searchValue, setSearchValue] = useRecoilState(boardListSearchValueSelector({ recoilKey }));
  const [dateRangeColumn, setDateRangeColumn] = useRecoilState(boardListDateRangeColumnSelector({ recoilKey }));
  const [dateRange, setDateRange] = useRecoilState(boardListDateRangeSelector({ recoilKey }));


  const setSafeDateRange = ({name, value}) => {
    setDateRange({
      ...dateRange,
      [name]: value,
    });
  }

  const createSearchFilter = () => {
    if (searchColumn && (searchValue !== undefined && searchValue !== null && searchValue !== "")) {
      return { [searchColumn]: searchValue };
    } else {
      return undefined;
    }
  };
  const createDateRangeFilter = () => {
    return {
      column: dateRangeColumn,
      startDate: dateToYmdStr(dateRange.startDate),
      endDate: dateToYmdStr(dateRange.endDate),
    };
  };


  const clearSearch = () => {
    setDateRangeColumn(undefined);
    setDateRange(undefined)

    setSearchColumn(undefined);
    setSearchValue("");
  };

  return { createSearchFilter, createDateRangeFilter,
          searchColumn, setSearchColumn, searchValue, setSearchValue,
            dateRangeColumn, setDateRangeColumn,
            dateRange, setSafeDateRange, clearSearch};
}
