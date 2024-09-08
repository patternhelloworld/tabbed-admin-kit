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
        issueSearchFilter : columnFilters.length > 0 ? JSON.stringify({
        globalField : globalFilter,
        ...columnFilters.reduce((acc, filter) => {
            acc[filter.id] = filter.value;
            return acc;
        }, {})}) : JSON.stringify({ globalField : globalFilter})
    }
}