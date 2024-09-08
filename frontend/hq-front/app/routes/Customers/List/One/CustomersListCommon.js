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

import {FormikBirthDateForm} from "shared/components/OptimizedHtmlElements";
import CustomDatePicker from "shared/components/CustomDatePicker";
import {dateStrToDate, formatDateTimeWrapper, formatDateWrapper} from "shared/utils/date-handler";
import {
    getCustomerInfoOptions,
    getCustomerTypeOptions, getGenderOptions,
    getNationalityOptions,
    getPurchasePlanOptions
} from "shared/enums";
import {CodeGeneralUtil, safeValue} from "shared/utils/utilities";
import {Flex} from '@mantine/core';
import DoubleInputField from "shared/components/DoubleInputField";
import * as CustomerListFragments from "shared/components/CustomerListFragment";
import {AuthImg} from "shared/utils/AuthImg";

// true && false : false , true || false : true
const CustomersListCommon = ({formik = {values: {}, errors: {},}, globalReadOnly = false,
                                 onKeyValueChangeByEvent, onKeyValueChangeByNameValue, PK_NAME, onFileChange}) => {
    const setAddress = ({zipcode, address, si, gugun, bname}) => {
        formik.setFieldValue("addr1", address);
        formik.setFieldValue("zipcode", zipcode);
        formik.setFieldValue("addrSi", si);
        formik.setFieldValue("addrGugun", gugun);
        formik.setFieldValue("addrBname", bname);
    };

    useEffect(() => {
        if (formik.values.customerType === "2") {
            formik.setValues(prevValues => ({
                ...prevValues,
                customerName: "",
                firstNameEn: "",
                lastNameEn: "",
                nationality: "",
                gender: "",
            }));
        } else {
            formik.setValues(prevValues => ({
                ...prevValues,
                chiefNm: "",
                bizNo: "",
                corporationNo: "",
                uptae: "",
                upjong: "",
                ownerName: "",
            }));
        }
    }, [formik.values.customerType]);

    return (
        <Fragment>
            {formik.values.customerType === "2" ? (
                <CustomerListFragments.InfoFrag title={"법인정보"} />
                ) : (
                <CustomerListFragments.InfoFrag title={"고객정보"} />
            )}

            <CustomerListFragments.InputsDiv>
                <DoubleInputField
                    label1="고객 구분"
                    input1={
                        <Input
                            type="select"
                            id="customerType"
                            name="customerType"
                            value={formik.values.customerType}
                            onChange={onKeyValueChangeByEvent}
                            disabled={globalReadOnly}
                        >
                            {getCustomerTypeOptions().map((option) => (
                                <option key={option.value} value={option.value}>
                                    {option.text}
                                </option>
                            ))}
                        </Input>
                    }
                    error1={formik.errors.customerType}
                    touched1={formik.touched.customerType}
                    label2="고객 정보"
                    input2={
                        <Input
                            type="select"
                            id="customerInfo"
                            name="customerInfo"
                            value={formik.values.customerInfo}
                            onChange={onKeyValueChangeByEvent}
                            disabled={globalReadOnly}
                        >
                            {getCustomerInfoOptions().map((option) => (
                                <option key={option.value} value={option.value}>
                                    {option.text}
                                </option>
                            ))}
                        </Input>
                    }
                    error2={formik.errors.customerInfo}
                    touched2={formik.touched.customerInfo}
                />

                {formik.values.customerType === "2" ? (
                    <>
                        <DoubleInputField
                            label1="대표자이름"
                            input1={
                                <Input
                                    type="text"
                                    id="chiefNm"
                                    name="chiefNm"
                                    value={formik.values.chiefNm}
                                    onChange={onKeyValueChangeByEvent}
                                    readOnly={globalReadOnly}
                                    valid={!formik.errors.chiefNm && formik.touched.chiefNm}
                                    invalid={!!formik.errors.chiefNm && formik.touched.chiefNm}
                                />
                            }
                            error1={formik.errors.chiefNm}
                            touched1={formik.touched.chiefNm}
                            label2="사업자번호"
                            input2={
                                <Input
                                    type="text"
                                    id="bizNo"
                                    name="bizNo"
                                    value={formik.values.bizNo}
                                    onChange={onKeyValueChangeByEvent}
                                    readOnly={globalReadOnly}
                                    valid={!formik.errors.bizNo && formik.touched.bizNo}
                                    invalid={!!formik.errors.bizNo && formik.touched.bizNo}
                                />
                            }
                            error2={formik.errors.bizNo}
                            touched2={formik.touched.bizNo}
                        />

                        <DoubleInputField
                            label1="법인번호"
                            input1={
                                <Input
                                    type="text"
                                    id="corporationNo"
                                    name="corporationNo"
                                    value={formik.values.corporationNo}
                                    onChange={onKeyValueChangeByEvent}

                                    readOnly={globalReadOnly}
                                    valid={!formik.errors.corporationNo && formik.touched.corporationNo}
                                    invalid={!!formik.errors.corporationNo && formik.touched.corporationNo}
                                />
                            }
                            error1={formik.errors.corporationNo}
                            touched1={formik.touched.corporationNo}
                            label2="업태"
                            input2={
                                <Input
                                    type="text"
                                    id="uptae"
                                    name="uptae"
                                    value={formik.values.uptae}
                                    onChange={onKeyValueChangeByEvent}
                                    readOnly={globalReadOnly}
                                    valid={!formik.errors.uptae && formik.touched.uptae}
                                    invalid={!!formik.errors.uptae && formik.touched.uptae}
                                />
                            }
                            error2={formik.errors.uptae}
                            touched2={formik.touched.uptae}
                        />

                        <DoubleInputField
                            label1="업종"
                            input1={
                                <Input
                                    type="text"
                                    id="uptae"
                                    name="uptae"
                                    value={formik.values.upjong}
                                    onChange={onKeyValueChangeByEvent}
                                    readOnly={globalReadOnly}
                                    valid={!formik.errors.upjong && formik.touched.upjong}
                                    invalid={!!formik.errors.upjong && formik.touched.upjong}
                                />
                            }
                            error1={formik.errors.upjong}
                            touched1={formik.touched.upjong}
                            label2="실소유자명"
                            input2={
                                <Input
                                    type="text"
                                    id="ownerName"
                                    name="ownerName"
                                    value={formik.values.ownerName}
                                    onChange={onKeyValueChangeByEvent}

                                    readOnly={globalReadOnly}
                                    valid={!formik.errors.ownerName && formik.touched.ownerName}
                                    invalid={!!formik.errors.ownerName && formik.touched.ownerName}
                                />
                            }
                            error2={formik.errors.ownerName}
                            touched2={formik.touched.ownerName}
                        />
                    </>
                ) : (
                    <>
                        <DoubleInputField
                            label1="고객명"
                            input1={
                                <Input
                                    type="text"
                                    id="customerName"
                                    name="customerName"
                                    value={formik.values.customerName}
                                    onChange={onKeyValueChangeByEvent}
                                    readOnly={globalReadOnly}
                                    valid={!formik.errors.customerName && formik.touched.customerName}
                                    invalid={!!formik.errors.customerName && formik.touched.customerName}
                                />
                            }
                            error1={formik.errors.customerName}
                            touched1={formik.touched.customerName}
                            label2=""
                            input2=""
                        />

                        <DoubleInputField
                            label1="영문(이름)"
                            input1={
                                <Input
                                    type="text"
                                    id="firstNameEn"
                                    name="firstNameEn"
                                    value={formik.values.firstNameEn}
                                    onChange={onKeyValueChangeByEvent}

                                    readOnly={globalReadOnly}
                                    valid={!formik.errors.firstNameEn && formik.touched.firstNameEn}
                                    invalid={!!formik.errors.firstNameEn && formik.touched.firstNameEn}
                                />
                            }
                            error1={formik.errors.firstNameEn}
                            touched1={formik.touched.firstNameEn}
                            label2="영문(성)"
                            input2={
                                <Input
                                    type="text"
                                    id="lastNameEn"
                                    name="lastNameEn"
                                    value={formik.values.lastNameEn}
                                    onChange={onKeyValueChangeByEvent}

                                    readOnly={globalReadOnly}
                                    valid={!formik.errors.lastNameEn && formik.touched.lastNameEn}
                                    invalid={!!formik.errors.lastNameEn && formik.touched.lastNameEn}
                                />
                            }
                            error2={formik.errors.lastNameEn}
                            touched2={formik.touched.lastNameEn}
                        />

                        <DoubleInputField
                            label1="내/외국인 여부"
                            input1={
                                <Input
                                    type="select"
                                    id="nationality"
                                    name="nationality"
                                    value={formik.values.nationality}
                                    onChange={onKeyValueChangeByEvent}
                                    disabled={globalReadOnly}
                                >
                                    {getNationalityOptions().map((option) => (
                                        <option key={option.value} value={option.value}>
                                            {option.text}
                                        </option>
                                    ))}
                                </Input>
                            }
                            error1={formik.errors.nationality}
                            touched1={formik.touched.nationality}
                            label2="성별"
                            input2={
                                <Input
                                    type="select"
                                    id="gender"
                                    name="gender"
                                    value={formik.values.gender}
                                    onChange={onKeyValueChangeByEvent}
                                    disabled={globalReadOnly}
                                >
                                    {getGenderOptions().map((option) => (
                                        <option key={option.value} value={option.value}>
                                            {option.text}
                                        </option>
                                    ))}
                                </Input>
                            }
                            error2={formik.errors.gender}
                            touched2={formik.touched.gender}
                            disabled={globalReadOnly}
                        />
                    </>
                )}

                <DoubleInputField
                    label1="전화번호"
                    input1={
                        <Input
                            type="text"
                            id="tel"
                            name="tel"
                            value={formik.values.tel}
                            onChange={formik.handleChange}

                            readOnly={globalReadOnly}
                            valid={!formik.errors.tel && formik.touched.tel}
                            invalid={!!formik.errors.tel && formik.touched.tel}
                        />
                    }
                    error1={formik.errors.tel}
                    touched1={formik.touched.tel}
                    label2="핸드폰"
                    input2={
                        <Input
                            type="text"
                            id="hp"
                            name="hp"
                            value={formik.values.hp}
                            onChange={formik.handleChange}

                            readOnly={globalReadOnly}
                            valid={!formik.errors.hp && formik.touched.hp}
                            invalid={!!formik.errors.hp && formik.touched.hp}
                        />
                    }
                    error2={formik.errors.hp}
                    touched2={formik.touched.hp}
                />

                <DoubleInputField
                    label1="이메일"
                    input1={
                        <Input
                            type="text"
                            id="email"
                            name="email"
                            value={formik.values.email}
                            onChange={formik.handleChange}

                            readOnly={globalReadOnly}
                            valid={!formik.errors.email && formik.touched.email}
                            invalid={!!formik.errors.email && formik.touched.email}
                        />
                    }
                    error1={formik.errors.email}
                    touched1={formik.touched.email}
                    label2="기타 연락처"
                    input2={
                        <Input
                            type="text"
                            id="otherContact"
                            name="otherContact"
                            value={formik.values.otherContact}
                            onChange={formik.handleChange}

                            readOnly={globalReadOnly}
                            valid={!formik.errors.otherContact && formik.touched.otherContact}
                            invalid={!!formik.errors.otherContact && formik.touched.otherContact}
                        />
                    }
                    error2={formik.errors.otherContact}
                    touched2={formik.touched.otherContact}
                />

                <FormikBirthDateForm formik={formik}/>

                <DoubleInputField
                    label1="팩스"
                    input1={
                        <Input
                            type="text"
                            id="fax"
                            name="fax"
                            value={formik.values.fax}
                            onChange={formik.handleChange}

                            readOnly={globalReadOnly}
                            valid={!formik.errors.fax && formik.touched.fax}
                            invalid={!!formik.errors.fax && formik.touched.fax}
                        />
                    }
                    error1={formik.errors.fax}
                    touched1={formik.touched.fax}
                    label2="고객 그룹"
                    input2={
                        <Input
                            type="select"
                            id="customerGroupIdx"
                            name="customerGroupIdx"
                            value={formik.values.customerGroupIdx}
                            onChange={onKeyValueChangeByEvent}
                            disabled={globalReadOnly}
                        >
                            {formik.values?.meta?.customerGroups.map(x => ({
                                value: x.customerGroupIdx,
                                text: x.groupNm,
                            })).map((option) => (
                                <option key={option.value} value={option.value}>
                                    {option.text}
                                </option>
                            ))}
                            <option key="-" value="0">미선택</option>
                        </Input>
                    }
                    error2={formik.errors.customerGroupIdx}
                    touched2={formik.touched.customerGroupIdx}
                />

                <Flex direction="row" align="center" gap={"md"}>
                    <label style={{
                        minWidth: '100px',
                        textAlign: 'left',
                        marginRight: '10px',
                    }} className="mt-2">
                        도로명 주소
                    </label>
                    <Input
                        style={{width : '200px'}}
                        className={"mt-3 mb-3"}
                        type="text"
                        name="zipcode"
                        id="zipcode"
                        value={formik.values.zipcode}
                        readOnly={true}
                    />
                    <KakaoAddress
                        setAddress={setAddress}
                    ></KakaoAddress>
                    <Input
                        type="text"
                        name="addr1"
                        id="addr1"
                        value={formik.values.addr1}
                        readOnly={true}
                    />
                    <Input
                        type="text"
                        name="addr2"
                        id="addr2"
                        valid={!formik.errors.addr2 && formik.touched.addr2}
                        invalid={!!formik.errors.addr2 && formik.touched.addr2}
                        value={formik.values.addr2 || ""}
                        onChange={onKeyValueChangeByEvent}

                    />
                </Flex>

                <Flex direction="row" align="center" gap={"md"}>
                    <label style={{
                        minWidth: '100px',
                        textAlign: 'left',
                        marginRight: '10px',
                    }} className="mt-2">
                        특이사항
                    </label>
                    <Input
                        type="textarea"
                        id="specialNotes"
                        name="specialNotes"
                        value={formik.values.specialNotes}
                        onChange={formik.handleChange}

                        readOnly={globalReadOnly}
                        valid={!formik.errors.specialNotes && formik.touched.specialNotes}
                        invalid={!!formik.errors.specialNotes && formik.touched.specialNotes}
                    />
                    <FormFeedback className={'ml-3'}>{formik.errors.specialNotes}</FormFeedback>
                </Flex>
            </CustomerListFragments.InputsDiv>

            <CustomerListFragments.CompanyFrag />

            <CustomerListFragments.InputsDiv>
                <DoubleInputField
                    label1="회사명"
                    input1={
                        <Input
                            type="text"
                            id="companyName"
                            name="companyName"
                            value={formik.values.companyName}
                            onChange={formik.handleChange}

                            readOnly={globalReadOnly}
                            valid={!formik.errors.companyName && formik.touched.companyName}
                            invalid={!!formik.errors.companyName && formik.touched.companyName}
                        />
                    }
                    error1={formik.errors.companyName}
                    touched1={formik.touched.companyName}
                    label2="직위"
                    input2={
                        <Input
                            type="select"
                            id="codeGeneralPositionIdx"
                            name="codeGeneralPositionIdx"
                            value={formik.values.codeGeneralPositionIdx}
                            onChange={onKeyValueChangeByEvent}
                            disabled={globalReadOnly}
                        >
                            {CodeGeneralUtil.getSelectOptions({
                                codeCustomers: formik.values?.meta?.codeCustomers,
                                categoryCd: "POSITION"
                            }).map((option) => (
                                <option key={option.value} value={option.value}>
                                    {option.text}
                                </option>
                            ))}
                            <option key="-" value="0">미선택</option>
                        </Input>
                    }
                    error2={formik.errors.codeGeneralPositionIdx}
                    touched2={formik.touched.codeGeneralPositionIdx}
                />
                <DoubleInputField
                    label1="직업"
                    input1={
                        <Input
                            type="select"
                            id="codeGeneralJobIdx"
                            name="codeGeneralJobIdx"
                            value={formik.values.codeGeneralJobIdx}
                            onChange={onKeyValueChangeByEvent}
                            disabled={globalReadOnly}
                        >
                            {CodeGeneralUtil.getSelectOptions({
                                codeCustomers: formik.values?.meta?.codeCustomers,
                                categoryCd: "JOB"
                            }).map((option) => (
                                <option key={option.value} value={option.value}>
                                    {option.text}
                                </option>
                            ))}
                            <option key="-" value="0">미선택</option>
                        </Input>
                    }
                    error1={formik.errors.codeGeneralJobIdx}
                    touched1={formik.touched.codeGeneralJobIdx}
                    label2=""
                    input2=""
                />
            </CustomerListFragments.InputsDiv>

            <CustomerListFragments.AgreesFrag />

            <CustomerListFragments.InputsDiv>
                <DoubleInputField
                    label1="SMS 수신 여부"
                    input1={
                        <>
                            <FormGroup check>
                                <Input
                                    type="radio"
                                    className="form-check-input"
                                    id="smsSubscriptionY"
                                    name="smsSubscription"
                                    value="Y"
                                    checked={formik.values.smsSubscription === "Y"}
                                    onChange={onKeyValueChangeByEvent}

                                    disabled={globalReadOnly}
                                />
                                <Label for="smsSubscriptionY" check>
                                    Y
                                </Label>
                            </FormGroup>
                            <FormGroup check className={"ml-2"}>
                                <Input
                                    type="radio"
                                    className="form-check-input"
                                    id="smsSubscriptionN"
                                    name="smsSubscription"
                                    value="N"
                                    checked={formik.values.smsSubscription === "N"}
                                    onChange={onKeyValueChangeByEvent}

                                    disabled={globalReadOnly}
                                />
                                <Label for="smsSubscriptionN" check>
                                    N
                                </Label>
                            </FormGroup>
                        </>
                    }
                    error1={formik.errors.smsSubscription}
                    touched1={formik.touched.smsSubscription}
                    label2="이메일 수신 여부"
                    input2={
                        <>
                            <FormGroup check>
                                <Input
                                    type="radio"
                                    className="form-check-input"
                                    id="emailSubscriptionY"
                                    name="emailSubscription"
                                    value="Y"
                                    checked={formik.values.emailSubscription === "Y"}
                                    onChange={onKeyValueChangeByEvent}

                                    disabled={globalReadOnly}
                                />
                                <Label for="emailSubscriptionY" check>
                                    Y
                                </Label>
                            </FormGroup>
                            <FormGroup check className={"ml-2"}>
                                <Input
                                    type="radio"
                                    className="form-check-input"
                                    id="emailSubscriptionN"
                                    name="emailSubscription"
                                    value="N"
                                    checked={formik.values.emailSubscription === "N"}
                                    onChange={onKeyValueChangeByEvent}

                                    disabled={globalReadOnly}
                                />
                                <Label for="emailSubscriptionN" check>
                                    N
                                </Label>
                            </FormGroup>
                        </>
                    }
                    error2={formik.errors.emailSubscription}
                    touched2={formik.touched.emailSubscription}
                />

                <DoubleInputField
                    label1="우편 수신 여부"
                    input1={
                        <>
                            <FormGroup check>
                                <Input
                                    type="radio"
                                    className="form-check-input"
                                    id="postalMailSubscriptionY"
                                    name="postalMailSubscription"
                                    value="Y"
                                    checked={formik.values.postalMailSubscription === "Y"}
                                    onChange={onKeyValueChangeByEvent}

                                    disabled={globalReadOnly}
                                />
                                <Label for="postalMailSubscriptionY" check>
                                    Y
                                </Label>
                            </FormGroup>
                            <FormGroup check className={"ml-2"}>
                                <Input
                                    type="radio"
                                    className="form-check-input"
                                    id="postalMailSubscriptionN"
                                    name="postalMailSubscription"
                                    value="N"
                                    checked={formik.values.postalMailSubscription === "N"}
                                    onChange={onKeyValueChangeByEvent}

                                    disabled={globalReadOnly}
                                />
                                <Label for="postalMailSubscriptionN" check>
                                    N
                                </Label>
                            </FormGroup>
                        </>
                    }
                    error1={formik.errors.postalMailSubscription}
                    touched1={formik.touched.postalMailSubscription}
                    label2="접촉 경로"
                    input2={
                        <Input
                            type="select"
                            id="codeGeneralContactMethodIdx"
                            name="codeGeneralContactMethodIdx"
                            value={formik.values.codeGeneralContactMethodIdx}
                            onChange={onKeyValueChangeByEvent}
                            disabled={globalReadOnly}
                        >
                            {CodeGeneralUtil.getSelectOptions({
                                codeCustomers: formik.values?.meta?.codeCustomers,
                                categoryCd: "CONTACT_CHANNEL"
                            }).map((option) => (
                                <option key={option.value} value={option.value}>
                                    {option.text}
                                </option>
                            ))}
                            <option key="-" value="0">미선택</option>
                        </Input>
                    }
                    error2={formik.errors.codeGeneralContactMethodIdx}
                    touched2={formik.touched.codeGeneralContactMethodIdx}
                />
            </CustomerListFragments.InputsDiv>

            <CustomerListFragments.InterestsFrag />

            <CustomerListFragments.InputsDiv>
                <DoubleInputField
                    label1="구매 계획"
                    input1={
                        <Input
                            type="select"
                            id="purchasePlan"
                            name="purchasePlan"
                            value={formik.values.purchasePlan}
                            onChange={onKeyValueChangeByEvent}
                            disabled={globalReadOnly}
                        >
                            {getPurchasePlanOptions().map((option) => (
                                <option key={option.value} value={option.value}>
                                    {option.text}
                                </option>
                            ))}
                        </Input>
                    }
                    error1={formik.errors.purchasePlan}
                    touched1={formik.touched.purchasePlan}
                    label2="구매 요인"
                    input2={
                        <Input
                            type="select"
                            id="codeGeneralPurchaseDecisionFactorIdx"
                            name="codeGeneralPurchaseDecisionFactorIdx"
                            value={formik.values.codeGeneralPurchaseDecisionFactorIdx}
                            onChange={onKeyValueChangeByEvent}
                            disabled={globalReadOnly}
                        >
                            {CodeGeneralUtil.getSelectOptions({
                                codeCustomers: formik.values?.meta?.codeCustomers,
                                categoryCd: "PURCHASE_FACTOR"
                            }).map((option) => (
                                <option key={option.value} value={option.value}>
                                    {option.text}
                                </option>
                            ))}
                            <option key="-" value="0">미선택</option>
                        </Input>
                    }
                    error2={formik.errors.codeGeneralPurchaseDecisionFactorIdx}
                    touched2={formik.touched.codeGeneralPurchaseDecisionFactorIdx}
                />
                <DoubleInputField
                    label1="관심 분야"
                    input1={
                        <Input
                            type="select"
                            id="codeGeneralInterestFieldIdx"
                            name="codeGeneralInterestFieldIdx"
                            value={formik.values.codeGeneralInterestFieldIdx}
                            onChange={onKeyValueChangeByEvent}
                            disabled={globalReadOnly}
                        >
                            {CodeGeneralUtil.getSelectOptions({
                                codeCustomers: formik.values?.meta?.codeCustomers,
                                categoryCd: "INTEREST"
                            }).map((option) => (
                                <option key={option.value} value={option.value}>
                                    {option.text}
                                </option>
                            ))}
                            <option key="-" value="0">미선택</option>
                        </Input>
                    }
                    error1={formik.errors.codeGeneralInterestFieldIdx}
                    touched1={formik.touched.codeGeneralInterestFieldIdx}
                    label2=""
                    input2=""
                />
            </CustomerListFragments.InputsDiv>

            <CustomerListFragments.PrivacyFrag />

            <CustomerListFragments.InputsDiv>
                <DoubleInputField
                    label1="개인 정보 동의서"
                    input1={
                        <>
                            <FormGroup check>
                                <Input
                                    type="radio"
                                    className="form-check-input"
                                    id="personalDataConsentY"
                                    name="personalDataConsent"
                                    value="Y"
                                    checked={formik.values.personalDataConsent === "Y"}
                                    onChange={onKeyValueChangeByEvent}
                                    disabled={globalReadOnly}
                                />
                                <Label for="personalDataConsentY" check>
                                    예
                                </Label>
                            </FormGroup>
                            <FormGroup check className={"ml-2"}>
                                <Input
                                    type="radio"
                                    className="form-check-input"
                                    id="personalDataConsentN"
                                    name="personalDataConsent"
                                    value="N"
                                    checked={formik.values.personalDataConsent === "N"}
                                    onChange={onKeyValueChangeByEvent}
                                    disabled={globalReadOnly}
                                />
                                <Label for="personalDataConsentN" check>
                                    아니오
                                </Label>
                            </FormGroup>
                        </>
                    }
                    error1={formik.errors.personalDataConsent}
                    touched1={formik.touched.personalDataConsent}

                    label2="개인 정보 동의일"
                    input2={
                        <div className="calendar_item ml-3">
                            <CustomDatePicker
                                selectedDate={dateStrToDate(formik.values.personalDataConsentDate)}
                                setSelectedDate={(v) => {
                                    onKeyValueChangeByNameValue({
                                        name: "personalDataConsentDate",
                                        value: v,
                                    });
                                }}
                                disabled={formik.values.personalDataConsent !== "Y" || globalReadOnly}
                            />
                        </div>
                    }
                    error2={formik.errors.personalDataConsentDate}
                    touched2={formik.touched.personalDataConsentDate}
                />

                <DoubleInputField
                    label1="파일등록"
                    input1={
                        <>
                            <FormGroup check>
                                <Input
                                    type="file"
                                    id="sname"
                                    name="sname"
                                    onChange={onFileChange}
                                    label={formik.values.sname ? formik.values.sname : "파일을 끌어오세요."}
                                    onBlur={formik.handleBlur}
                                    disabled={formik.values.personalDataConsent !== "Y" || globalReadOnly}
                                />
                                <FormText color="muted">
                                    ※파일은 1개, 10MBtype 까지 가능합니다.
                                </FormText>
                                <FormText color="danger"></FormText>
                                <FormFeedback className="ml-3">{formik.errors.sname}</FormFeedback>
                            </FormGroup>
                        </>
                    }
                    error1={formik.errors.personalDataConsentDate}
                    touched1={formik.touched.personalDataConsentDate}
                    label2={formik.values.fname ? "현재 등록 파일" : ""}
                    input2={
                        formik.values.fname && (
                            <FormGroup check>
                                <AuthImg
                                    src={formik.values.fname}
                                    alt="Uploaded"
                                    style={{maxWidth: '100px', maxHeight: '100px', display: 'block'}}
                                />
                            </FormGroup>
                        )
                    }
                />
            </CustomerListFragments.InputsDiv>
        </Fragment>
    );

};

export default React.memo(CustomersListCommon);
