import queryString from 'query-string';
import { omit } from 'lodash';

export const queryStringToObject = (str, options = {}) =>
  queryString.parse(str, {
    arrayFormat: 'bracket',
    ...options,
  });

export const objectToQueryString = (obj, options = {}) =>
  queryString.stringify(obj, {
    arrayFormat: 'bracket',
    ...options,
  });

export const omitFromQueryString = (str, keys) =>
  objectToQueryString(omit(queryStringToObject(str), keys));

export const addToQueryString = (str, fields) =>
  objectToQueryString({
    ...queryStringToObject(str),
    ...fields,
  });

/*
*   mantine 필터 양식을 우리의 ListSearchModule 과 서버에서 사용하는 양식으로 변경해주는 함수
* */
export const mantineToPMSFilter = ({
                                       columnFilterFns,
                                       columnFilters,
                                       globalFilter,
                                       sorting,
                                       pagination,
                                   }) => {

    return {
        pageNum : pagination.pageIndex + 1, pageSize : pagination.pageSize,
        sorterValueFilter : sorting.length > 0 ? JSON.stringify({ column : sorting[0].id, asc : !sorting[0].desc}) : null,
        searchFilter : columnFilters.length > 0 ?
            JSON.stringify({
        globalField : globalFilter,
        ...columnFilters.reduce((acc, filter) => {
            acc[filter.id] = filter.value;
            return acc;
        }, {})}) : JSON.stringify({ globalField : globalFilter})
    }
}