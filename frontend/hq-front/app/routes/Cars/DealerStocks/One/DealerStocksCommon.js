import React, {useCallback, useState, useEffect, useRef, Fragment} from 'react';

import {
    Form,
    FormFeedback,
    FormGroup,
    Input,
    Button,
    Label,
    Col,
    Row, Table, FormText, Card, CardBody, CustomInput, CardHeader
} from 'reactstrap';

import KakaoAddress from "shared/components/kakao-address/KakaoAddress";

import {
    ButtonWrapper,
    DetailHeaderWrapper,
    FormikBirthDateForm
} from "shared/components/OptimizedHtmlElements";
import CustomDatePicker from "shared/components/CustomDatePicker";
import {dateStrToDate, formatDateTimeWrapper, formatDateWrapper} from "shared/utils/date-handler";
import {
    getDealerStockUseTypeOptions,
    getGenderOptions,
    getNationalityOptions,
    getPurchasePlanOptions
} from "shared/enums";
import {CodeGeneralUtil, safeValue} from "shared/utils/utilities";
import {Flex} from '@mantine/core';
import DoubleInputField from "shared/components/DoubleInputField";

// true && false : false , true || false : true
const DealerStocksCommon = ({formik = {values: {}, errors: {},}, globalReadOnly = false,
                                 onKeyValueChangeByEvent, onKeyValueChangeByNameValue, PK_NAME}) => {

    return (<Fragment>

            <DoubleInputField
                label1="사용 상태"
                input1={
                    <Input
                        type="select"
                        id="useType"
                        name="useType"
                        value={formik.values.useType}
                        onChange={onKeyValueChangeByEvent}
                        disabled={globalReadOnly}
                    >
                        {getDealerStockUseTypeOptions().map((option) => (
                            <option key={option.value} value={option.value}>
                                {option.text}
                            </option>
                        ))}
                    </Input>
                }
                error1={formik.errors.useType}
                touched1={formik.touched.useType}


                label2="입고일"
                input2={
                    <div className="calendar_item ml-3">
                        <CustomDatePicker
                            selectedDate={dateStrToDate(formik.values.importDate)}
                            setSelectedDate={(v) => {
                                onKeyValueChangeByNameValue({
                                    name: "importDate",
                                    value: v,
                                });
                            }}
                            disabled={globalReadOnly}
                        />
                    </div>
                }
                error2={formik.errors.importDate}
                touched2={formik.touched.importDate}
            />

            <DoubleInputField
                label1="전시장 선택"
                input1={
                    <Input
                        type="select"
                        id="deptIdx"
                        name="deptIdx"
                        value={formik.values.deptIdx}
                        onChange={onKeyValueChangeByEvent}
                        disabled={globalReadOnly}
                    >
                        {formik.values?.meta?.secondDepthDepts.map(x => ({
                            value: x.deptIdx,
                            text: x.deptNm,
                        })).map((option) => (
                            <option key={option.value} value={option.value}>
                                {option.text}
                            </option>
                        ))}
                        <option key="-" value="">미선택</option>
                    </Input>
                }
                error1={formik.errors.deptIdx}
                touched1={formik.touched.deptIdx}
                label2=""
                input2={null}
                error2=""
                touched2={false}
            />




        </Fragment>
    );

};

export default React.memo(DealerStocksCommon);
