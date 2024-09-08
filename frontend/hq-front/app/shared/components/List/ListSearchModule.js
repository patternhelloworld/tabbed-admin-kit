import React, {useEffect, useState, useCallback } from 'react'

import CustomDatePicker from "../CustomDatePicker";
import {
  Form,
  FormFeedback,
  FormGroup,
  Input,
  Label,
  Col,
  Row, Table, FormText, Card, CardBody, CustomInput
} from 'reactstrap';
import {ActionIcon, Tooltip, Box, Button, Flex, Menu, Text, Title} from '@mantine/core';
import classNames from "classnames";
import '../../../styles/components/list-serach-module.scss'
import {isValidObject} from "../../utils/utilities";
import { IconSearch, IconMoodSearch } from '@tabler/icons-react';
import {isObject} from "lodash";

const ListSearchModule = ({
                            fields, searchReadOnly,
                            searchColumn, onSearchColumnChange, searchValue, onSearchValueChange,
                            dateRangeColumn, onDateSearchColumnChange, dateRange, onDateRangeChange,
                            onSearchButtonClick, onSearchBoxKeyDown, children
                          }) => {


  const [currentSearchField, setCurrentSearchField] = useState(null);



  const handleCurrentSearchField = () => {
    if(!searchColumn){
      const defaultSearchField = fields.find(x => x.searchable && x.defaultSearchColumn);
      if(defaultSearchField){
        setCurrentSearchField(defaultSearchField)
      }else{
        const firstSearchField = fields.find(x => x.searchable);
        if(firstSearchField){
          setCurrentSearchField(firstSearchField)
        }else{
          // 없으면 currentSearchField 는 null
        }
      }
    }else{
      const foundSearchField = fields.find(x => x.accessorKey === searchColumn || x.searchKey === searchColumn);
      if(foundSearchField){
        setCurrentSearchField(foundSearchField);
      }else {
        // 없으면 currentSearchField 는 null
      }
    }
  }

  useEffect(() => {

    handleCurrentSearchField()

    const defaultSearchDateRangeField = fields.find(x => x.defaultDateRangeSearchColumn);

    if (defaultSearchDateRangeField) {
      if(!dateRangeColumn) {
        onDateSearchColumnChange(defaultSearchDateRangeField.accessorKey)
      }
    }

  }, [searchColumn])


  useEffect(() => {

    if (currentSearchField) {
      if(currentSearchField.searchKey){
        onSearchColumnChange(currentSearchField.searchKey)
      }else{
        onSearchColumnChange(currentSearchField.accessorKey)
      }

      if(currentSearchField.selectBoxList && Array.isArray(currentSearchField.selectBoxList) && currentSearchField.selectBoxList.length > 0) {

        const defaultSelectItem = currentSearchField.selectBoxList.find(selectBox => (selectBox.value === undefined || selectBox.value === null || (typeof selectBox.value === 'string' && selectBox.value.trim() === "")));
        if(searchValue){
          onSearchValueChange(searchValue)
        }else if(defaultSelectItem){
          onSearchValueChange(defaultSelectItem.value)
        }else{
          if(isObject(currentSearchField.selectBoxList[0])){
            onSearchValueChange(currentSearchField.selectBoxList[0].value)
          }else{
            onSearchValueChange(currentSearchField.selectBoxList[0])
          }
        }
      }

    }

  }, [currentSearchField])

  const renderDateSearchColumn = () => {

    const options = fields.reduce((acc, curr) => {
      if (curr.dateRangeSearchable) {
        acc.push(<option key={curr.accessorKey} value={curr.accessorKey}>{curr.header}</option>);
      }
      return acc;
    }, [])

    //options.push(<option value={clearColumnName}>검색 창 초기화</option>)

    return (<Input type={"select"}  className={classNames('sel mr-2 mb-2 date-search-column', { 'invisible': !fields.find(x => x.dateRangeSearchable) })}
                   onChange={(e) => onDateSearchColumnChange(e.target.value)} value={dateRangeColumn || ""}>{options}</Input>)

  }


  const renderSearchColumnBox = () => {

    const options = fields.reduce((acc, field) => {
      if (field.searchable) {
        if (field.searchKey) {
          acc.push(<option key={field.searchKey} value={field.searchKey}>{field.header}</option>);
        } else {
          acc.push(<option key={field.accessorKey} value={field.accessorKey}>{field.header}</option>);
        }
      }
      return acc;
    }, [])

    //options.push(<option value={clearColumnName}>검색 창 초기화</option>)
    if (currentSearchField) {
      return (<Input type={"select"} className="sel mr-2 mb-2 search-column-value"
                     onChange={(e) => {

                       onSearchColumnChange(e.target.value)

                       const searchColumnMatchedField = fields.find(x => x.accessorKey === e.target.value);
                       if(searchColumnMatchedField && searchColumnMatchedField.emptySearchValueBox){
                         onSearchValueChange(true);
                         return;
                       }

                       // useListSearchModuleMeta.js 에서 빈 searchValue 는 무시된다.
                       onSearchValueChange("");

                     }} value={searchColumn || ""} disabled={searchReadOnly}>{options}</Input>)
    }else{
      return '';
    }

  }


  const renderSearchValueBox = () => {

    if (currentSearchField) {
      if(currentSearchField.emptySearchValueBox){
        return '';
      }else{
        return (<div className="mb-2 mr-1"
                     style={searchReadOnly ? {backgroundColor: "#f4f4f4"} : {}}>
          {currentSearchField.selectBoxList && Array.isArray(currentSearchField.selectBoxList) ? <Input type={"select"} className="sel mr-2 search-column-value" name="" style={searchReadOnly ? {backgroundColor: "#f4f4f4"} : {}}
                                                                                                        value={searchValue}
                                                                                                        onChange={(e) => onSearchValueChange(e.target.value)} onKeyDown={onSearchBoxKeyDown}
                                                                                                        defaultValue={searchValue}
                                                                                                        disabled={searchReadOnly}>
                {currentSearchField.selectBoxList.map((field, i) => {
                  return (<option key={field.value + i} value={field.value}>{field.text}</option>)
                })}</Input> :
              (<Input type="text" className="in_scal search-column-value" style={searchReadOnly ? {backgroundColor: "#f4f4f4"} : {}}
                      value={searchValue || ""} onChange={(e) => onSearchValueChange(e.target.value)} onKeyDown={onSearchBoxKeyDown}
                      readOnly={searchReadOnly}/>)}
        </div>)
      }
    }else{
      return '';
    }

  }

  useEffect(()=>{

    //console.log("dateRangeColumn " + dateRangeColumn);

    if(!searchColumn && fields.find(x => x.searchable)){
      const field = fields.find(x => x.searchable &&  x.defaultSearchColumn);
      if(field){
        onSearchColumnChange(field.accessorKey)
      }else{
        onSearchColumnChange(null)
      }
    }
    if(!dateRangeColumn && fields.find(x => x.dateRangeSearchable)){
      const field = fields.find(x => x.defaultDateRangeSearchColumn);
      if(field){
        onDateSearchColumnChange(field.accessorKey)
      }else{
        onDateSearchColumnChange(null)
      }
    }
  },[searchColumn, dateRangeColumn])


  return (
      <div className="tb_util" >
        {children}
        {dateRangeColumn !== null && (
            <>
              {/* DateSearchColumn */}
              {renderDateSearchColumn()}

              {/* DateRange */}
              <div className={classNames('calendar_item mb-2', { 'invisible': !fields.find(x => x.dateRangeSearchable) })}  >
                <CustomDatePicker selectedDate={dateRange.startDate} setSelectedDate={(v) => {
                  return onDateRangeChange("startDate", v);
                }}/>
              </div>
              <span className={classNames('mb-2', { 'invisible': !fields.find(x => x.dateRangeSearchable) })} >~</span>
              <div className={classNames('calendar_item mr-2  mb-2', { 'invisible': !fields.find(x => x.dateRangeSearchable) })} >
                <CustomDatePicker selectedDate={dateRange.endDate} setSelectedDate={(v) => {
                  return onDateRangeChange("endDate", v);
                }}/>
              </div>
            </>
        )}
        {searchColumn !== null && (
            <>
              {/* SearchColumn */}
              {renderSearchColumnBox()}

              {/* SearchValue */}
              {renderSearchValueBox()}
              <Button
                  className={"ml-2 mb-2"}
                  leftIcon={<IconSearch size={14}/>}
                  variant="gradient" gradient={{ from: 'blue', to: 'cyan', deg: 90 }}a
                  onClick={onSearchButtonClick}
              >검색</Button>
              {/*     <Button className={"ml-2 mb-2"} color="primary" size="md" style={{ minWidth : "45px"}}
                onClick={onSearchButtonClick}>검색</Button>*/}
            </>
        )}

      </div>
  )
}

export default React.memo(ListSearchModule)
