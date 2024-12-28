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
    getVinUseTypeOptions,
    getGenderOptions,
    getNationalityOptions,
    getPurchasePlanOptions
} from "shared/enums";
import {CodeGeneralUtil, safeValue} from "shared/utils/utilities";
import {Flex} from '@mantine/core';
import DoubleInputField from "shared/components/DoubleInputField";

// true && false : false , true || false : true
const VinsCommon = ({formik = {values: {}, errors: {},}, globalReadOnly = false,
                                 onKeyValueChangeByEvent, onKeyValueChangeByNameValue, PK_NAME}) => {

    return (<Fragment>

            ※ 차량 세부 모델 정보는 Logistics 에서 개발 예정
            <DoubleInputField
                label1="차량 번호"
                input1={
                    <Input
                        type="text"
                        id="vinNumber"
                        name="vinNumber"
                        value={formik.values.vinNumber}
                        onChange={onKeyValueChangeByEvent}

                        readOnly={globalReadOnly}
                        valid={!formik.errors.vinNumber && formik.touched.vinNumber}
                        invalid={!!formik.errors.vinNumber && formik.touched.vinNumber}
                    />
                }
                error1={formik.errors.vinNumber}
                touched1={formik.touched.vinNumber}
                label2=""
                input2={null}
                error2=""
                touched2={false}
            />

        </Fragment>
    );

};

export default React.memo(VinsCommon);
