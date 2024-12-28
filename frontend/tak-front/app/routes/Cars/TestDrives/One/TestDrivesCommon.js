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

import { DateTimePicker } from '@mantine/dates';

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
    getPurchasePlanOptions, getApprovalStatusOptions, findApprovalStatusLabelByValue
} from "shared/enums";
import {CodeGeneralUtil, safeValue} from "shared/utils/utilities";
import {Flex} from '@mantine/core';
import DoubleInputField from "shared/components/DoubleInputField";
import UsersListSelectModal from "../../../../shared/components/select-modal/UserSelectModalLocal";
import {CRUD_COLUMNS} from "../../../../shared/utils/authorization";
import getTestDrivesColumns from "../columns";
import CustomerSelectModalLocal from "../../../../shared/components/select-modal/CustomerSelectModalLocal";
import UserSelectModalLocal from "../../../../shared/components/select-modal/UserSelectModalLocal";
import SingleInputField from "../../../../shared/components/SingleInputField";

// true && false : false , true || false : true
const TestDrivesCommon = ({formik = {values: {}, errors: {},}, globalReadOnly = false,
                                 onKeyValueChangeByEvent, onKeyValueChangeByNameValue, PK_NAME, me, recoilKey}) => {

    const [userSelectModalOpen, setUserSelectModalOpen] = useState(false);
    const [customerSelectModalOpen, setCustomerSelectModalOpen] = useState(false);

    const handleUserSelected = (v) => {

        onKeyValueChangeByNameValue({ name : "userName", value : v.name})
        onKeyValueChangeByNameValue({ name : "userIdx", value : v.userIdx})

        onKeyValueChangeByNameValue({ name : "deptNm", value : v.deptNm})
        onKeyValueChangeByNameValue({ name : "deptIdx", value : v.deptIdx})

        setUserSelectModalOpen(false);
    }

    const handleCustomerSelected = (v) => {
        // console.log(v)
        onKeyValueChangeByNameValue({ name : "customerName", value : v.customerName})
        onKeyValueChangeByNameValue({ name : "customerIdx", value : v.customerIdx})

        setCustomerSelectModalOpen(false);
    }

    return (<Fragment>

                <DoubleInputField
                    label1="차량 번호"
                    input1={
                        <Input
                            type="text"
                            id="carNo"
                            name="carNo"
                            value={formik.values.carNo}
                            onChange={onKeyValueChangeByEvent}

                            readOnly={globalReadOnly}
                            valid={!formik.errors.carNo && formik.touched.carNo}
                            invalid={!!formik.errors.carNo && formik.touched.carNo}
                        />
                    }
                    error1={formik.errors.carNo}
                    touched1={formik.touched.carNo}

                    label2="고객명"
                    input2={
                        <Fragment>
                            <Input
                                type="text"
                                id="customerName"
                                name="customerName"
                                className={"mr-1"}
                                value={formik.values.customerName}
                                onChange={onKeyValueChangeByEvent}
                                readOnly={globalReadOnly}
                                disabled={true}
                                valid={!formik.errors.customerName && formik.touched.customerName}
                                invalid={!!formik.errors.customerName && formik.touched.customerName}
                            />
                            <ButtonWrapper
                            color={"gray"}
                            size={"sm"}
                            btnText={"고객 선택"}
                            handleClick={() => {
                                setCustomerSelectModalOpen(true)
                            }}
                            me={me}
                            recoilKey={recoilKey}
                            crudColumn={CRUD_COLUMNS.CREATE}
                            />
                        </Fragment>
                    }
                    error2={formik.errors.customerName}
                    touched2={formik.touched.customerName}

                />

            {/* 고객 선택 */}
            <CustomerSelectModalLocal onCustomerSelected={handleCustomerSelected} customerSelectModalOpen={customerSelectModalOpen} setCustomerSelectModalOpen={setCustomerSelectModalOpen}/>

            <SingleInputField
                label1="조직 및 담당자"
                input1={
                <Fragment>
                    <Input
                        type="text"
                        id="deptNm"
                        name="deptNm"
                        className={"mr-1"}
                        value={formik.values.deptNm}
                        onChange={onKeyValueChangeByEvent}
                        readOnly={globalReadOnly}
                        disabled={true}
                        valid={!formik.errors.deptNm && formik.touched.deptNm}
                        invalid={!!formik.errors.deptNm && formik.touched.deptNm}
                    />
                    <Input
                        type="text"
                        id="userName"
                        name="userName"
                        className={"mr-1"}
                        value={formik.values.userName}
                        onChange={onKeyValueChangeByEvent}
                        readOnly={globalReadOnly}
                        disabled={true}
                        valid={!formik.errors.userName && formik.touched.userName}
                        invalid={!!formik.errors.userName && formik.touched.userName}
                    />
                    <ButtonWrapper
                        color={"gray"}
                        size={"sm"}
                        btnText={"조직 및 담당자 선택"}
                        handleClick={() => {
                            setUserSelectModalOpen(true)
                        }}
                        me={me}
                        recoilKey={recoilKey}
                        crudColumn={CRUD_COLUMNS.CREATE}
                    />
                </Fragment>
                }
                error1={formik.errors.userName}
                touched1={formik.touched.userName}

            ></SingleInputField>



            {/* 담당자 선택 */}
            <UserSelectModalLocal onUserSelected={handleUserSelected} userSelectModalOpen={userSelectModalOpen} setUserSelectModalOpen={setUserSelectModalOpen}/>



            <DoubleInputField
                label1="결제 여부"
                input1={
                    <Input
                        type="text"
                        id="isApproved"
                        name="isApproved"
                        value={findApprovalStatusLabelByValue(formik.values.isApproved)}
                        onChange={onKeyValueChangeByEvent}
                        readOnly={globalReadOnly}
                        disabled={true}
                        valid={!formik.errors.isApproved && formik.touched.isApproved}
                        invalid={!!formik.errors.isApproved && formik.touched.isApproved}
                    />
                }
                error1={formik.errors.isApproved}
                touched1={formik.touched.isApproved}

                label2="시승 여부"
                input2={
                    <Fragment>
                        <Input
                            type="text"
                            id="isDrive"
                            name="isDrive"
                            value={formik.values.isDrive}
                            onChange={onKeyValueChangeByEvent}
                            readOnly={globalReadOnly}
                            disabled={true}
                            valid={!formik.errors.isDrivee && formik.touched.isDrive}
                            invalid={!!formik.errors.isDrive && formik.touched.isDrive}
                        />

                    </Fragment>
                }
                error2={formik.errors.userName}
                touched2={formik.touched.userName}
            />


            <DoubleInputField
                label1="시승 시작"
                input1={
                    <DateTimePicker
                        clearable
                        value={formik.values.startDate}
                        onChange={(v)=>{
                            onKeyValueChangeByNameValue({ name : 'startDate', value : v})
                        }}
                        placeholder="시승 시작 시간을 정확히 기입해주세요."
                    />
                }
                error1={formik.errors.startDate}
                touched1={formik.touched.startDate}

                label2="시승 종료"
                input2={
                    <DateTimePicker
                        clearable
                        value={formik.values.endDate}
                        onChange={(v)=>{
                            onKeyValueChangeByNameValue({ name : 'endDate', value : v})
                        }}
                        placeholder="시승 종료 시간을 정확히 기입해주세요."
                    />
                }
                error2={formik.errors.endDate}
                touched2={formik.touched.endDate}

            />

        </Fragment>
    );

};

export default React.memo(TestDrivesCommon);
