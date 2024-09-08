import React, {useCallback, useState, useEffect, useRef, Fragment} from 'react';
import {Link, useHistory} from 'react-router-dom';
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
import {ActionIcon, Tooltip, Box,  Flex, Menu, Text, Title} from '@mantine/core';
import agent from "../../../../shared/api/agent";
import {renderError} from "../../../../shared/utils/CommonErrorHandler";

import LoadingOverlay from 'react-loading-overlay';
import ClockLoader from 'react-spinners/ClockLoader';
import {useRecoilValue, useResetRecoilState, useSetRecoilState} from "recoil";
import {
    boardUpdateOneSelector,
    boardUpdateModifiedOneSelector, boardUpdateResetModifiedOneSelector
} from "shared/recoil/board/boardUpdateState";
import * as Yup from "yup";
import {useFormik} from "formik";


import KakaoAddress from "../../../../shared/components/kakao-address/KakaoAddress";
import {CRUD_COLUMNS, isAuthorized} from "../../../../shared/utils/authorization";
import {globalInfoAccessTokenUserInfoSelector} from "../../../../shared/recoil/globalInfoState";
import {useFormikUtils} from "../../../../shared/hooks/useFormiklUtils";
import {ButtonWrapper, DetailHeaderWrapper} from "../../../../shared/components/OptimizedHtmlElements";
import CustomDatePicker from "../../../../shared/components/CustomDatePicker";
import {dateStrToDate, formatDateTimeWrapper, formatDateWrapper} from "../../../../shared/utils/date-handler";
import DoubleInputField from "../../../../shared/components/DoubleInputField";
import {getManagementDepartmentOptions} from "../../../../shared/enums";
import {DeptHierarchyUtil} from "../../../../shared/utils/utilities";


// true && false : false , true || false : true
const SettingsUsersCommon = ({
                         formik = {
                             values: {}, errors: {},
                         }, globalReadOnly = false, onKeyValueChangeByEvent, onKeyValueChangeByNameValue, PK_NAME
        }) => {

    const setAddress = ({zipcode, address, si, gugun, bname}) => {
        formik.setFieldValue("addr1", address);
        formik.setFieldValue("zipcode", zipcode);
        formik.setFieldValue("addrSi", si);
        formik.setFieldValue("addrGugun", gugun);
        formik.setFieldValue("addrBname", bname);
    };


    return (
        <Fragment>

            {/* 조직 선택 및 사용자 이름 입력 필드 */}
            <FormText className="mute mb-1 mt-1">
                {DeptHierarchyUtil.getDeptNmArrayByParentCd(formik.values?.meta?.depts, DeptHierarchyUtil.getParentCdByDeptIdx(formik.values?.meta?.depts, formik.values.deptIdx)).join(" -> ")}
            </FormText>
            <DoubleInputField
                label1="조직 선택"
                input1={
                    <Input
                        type="select"
                        id="deptIdx"
                        name="deptIdx"
                        value={formik.values.deptIdx}
                        onChange={onKeyValueChangeByEvent}
                        onBlur={formik.handleBlur}
                        valid={!formik.errors.deptIdx && formik.touched.deptIdx}
                        invalid={!!formik.errors.deptIdx && formik.touched.deptIdx}
                        required
                        disabled={globalReadOnly}
                    >

                        {formik.values.meta ? formik.values.meta.depts.map((option) => {

                            // depth 만큼 공백 추가
                            const spaces = '\u00A0'.repeat(option.depth * 2); // 각 depth에 대해 4칸 공백 추가

                            return (
                                <option key={option.deptIdx} value={option.deptIdx}>
                                    {spaces}[{option.depth}] {option.deptNm}
                                </option>
                            );
                        }): ""}

                        <option key={"-"} value={0}>미선택</option>
                    </Input>
                }
                error1={formik.errors.deptIdx}
                touched1={formik.touched.deptIdx}
                label2="사용자 이름"
                input2={
                    <Input
                        type="text"
                        id="name"
                        name="name"
                        placeholder="Name"
                        value={formik.values.name}
                        onChange={onKeyValueChangeByEvent}
                        onBlur={formik.handleBlur}
                        valid={!formik.errors.name && formik.touched.name}
                        invalid={!!formik.errors.name && formik.touched.name}
                        readOnly={globalReadOnly}
                    />
                }
                error2={formik.errors.name}
                touched2={formik.touched.name}
            />

            {/* 입사일 및 생년월일 입력 필드 */}
            <DoubleInputField
                label1="입사일"
                input1={
                    <div className="calendar_item">
                        <CustomDatePicker
                            selectedDate={dateStrToDate(formik.values.joiningDate)}
                            setSelectedDate={(v) => {
                                onKeyValueChangeByNameValue({
                                    name: "joiningDate",
                                    value: v,
                                });
                            }}
                            disabled={globalReadOnly}
                        />
                    </div>
                }
                error1={formik.errors.joiningDate}
                touched1={formik.touched.joiningDate}
                label2="생년월일"
                input2={
                    <div className="calendar_item">
                        <CustomDatePicker
                            selectedDate={dateStrToDate(formik.values.birthDate)}
                            setSelectedDate={(v) => {
                                onKeyValueChangeByNameValue({
                                    name: "birthDate",
                                    value: v,
                                });
                            }}
                            disabled={globalReadOnly}
                        />
                    </div>
                }
                error2={formik.errors.birthDate}
                touched2={formik.touched.birthDate}
            />

            {/* 퇴사일 입력 필드 (단일 필드 사용) */}
            <DoubleInputField
                label1="퇴사일"
                input1={
                    <div className="calendar_item">
                        <CustomDatePicker
                            selectedDate={dateStrToDate(formik.values.outDt)}
                            setSelectedDate={(v) => {
                                onKeyValueChangeByNameValue({
                                    name: "outDt",
                                    value: v,
                                });
                            }}
                            disabled={globalReadOnly}
                        />
                    </div>
                }
                error1={formik.errors.outDt}
                touched1={formik.touched.outDt}
                label2="" // 두 번째 필드는 빈 문자열로 설정
                input2={null} // 두 번째 필드를 null로 설정
                error2="" // 두 번째 필드의 에러 메시지 빈 값
                touched2={false} // 두 번째 필드의 터치 상태 false
            />

            {/* 핸드폰 및 사용자 ID 입력 필드 */}
            <DoubleInputField
                label1="핸드폰"
                input1={
                    <Input
                        type="text"
                        id="phoneNumber"
                        name="phoneNumber"
                        value={formik.values.phoneNumber}
                        onChange={onKeyValueChangeByEvent}
                        onBlur={formik.handleBlur}
                        valid={!formik.errors.phoneNumber && formik.touched.phoneNumber}
                        invalid={!!formik.errors.phoneNumber && formik.touched.phoneNumber}
                        readOnly={globalReadOnly}
                    />
                }
                error1={formik.errors.phoneNumber}
                touched1={formik.touched.phoneNumber}
                label2="사용자 ID"
                input2={
                    <>
                        <Input
                            type="text"
                            id="userId"
                            name="userId"
                            value={formik.values.userId}
                            onChange={onKeyValueChangeByEvent}
                            onBlur={formik.handleBlur}
                            valid={!formik.errors.userId && formik.touched.userId}
                            invalid={!!formik.errors.userId && formik.touched.userId}
                            readOnly={globalReadOnly || formik.values[PK_NAME]}
                        />
                    </>
                }
                error2={formik.errors.userId}
                touched2={formik.touched.userId}
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
                    onBlur={formik.handleBlur}
                />
            </Flex>


            {/* 사용여부 및 퇴사여부 입력 필드 */}
            <DoubleInputField
                label1="사용여부"
                input1={
                    <>
                        <FormGroup check>
                            <Input
                                type="radio"
                                className="form-check-input"
                                id="delYnYes"
                                name="delYn"
                                value="Y"
                                checked={formik.values.delYn === "Y"}
                                onChange={onKeyValueChangeByEvent}
                                onBlur={formik.handleBlur}
                                disabled={globalReadOnly}
                            />
                            <Label for="delYnYes" check>
                                Y
                            </Label>
                        </FormGroup>
                        <FormGroup check className={"ml-2"}>
                            <Input
                                type="radio"
                                className="form-check-input"
                                id="delYnNo"
                                name="delYn"
                                value="N"
                                checked={formik.values.delYn === "N"}
                                onChange={onKeyValueChangeByEvent}
                                onBlur={formik.handleBlur}
                                disabled={globalReadOnly}
                            />
                            <Label for="delYnNo" check>
                                N
                            </Label>
                        </FormGroup>
                    </>
                }
                error1={formik.errors.delYn}
                touched1={formik.touched.delYn}
                label2="퇴사여부"
                input2={
                    <>
                        <FormGroup check>
                            <Input
                                type="radio"
                                className="form-check-input"
                                id="outYnYes"
                                name="outYn"
                                value="Y"
                                checked={formik.values.outYn === "Y"}
                                onChange={onKeyValueChangeByEvent}
                                onBlur={formik.handleBlur}
                                disabled={globalReadOnly}
                            />
                            <Label for="outYnYes" check>
                                Y
                            </Label>
                        </FormGroup>
                        <FormGroup check className={"ml-2"}>
                            <Input
                                type="radio"
                                className="form-check-input"
                                id="outYnNo"
                                name="outYn"
                                value="N"
                                checked={formik.values.outYn === "N"}
                                onChange={onKeyValueChangeByEvent}
                                onBlur={formik.handleBlur}
                                disabled={globalReadOnly}
                            />
                            <Label for="outYnNo" check>
                                N
                            </Label>
                        </FormGroup>
                    </>
                }
                error2={formik.errors.outYn}
                touched2={formik.touched.outYn}
            />

            {/* 직책 선택 및 관리자 여부 입력 필드 */}
            <DoubleInputField
                label1="직책 선택"
                input1={
                    <Input
                        type="select"
                        id="position"
                        name="position"
                        value={formik.values.position || ""}
                        onChange={onKeyValueChangeByEvent}
                        onBlur={formik.handleBlur}
                        valid={!formik.errors.position && formik.touched.position}
                        invalid={!!formik.errors.position && formik.touched.position}
                        disabled={globalReadOnly}
                    >
                        {[{value: "대리", text: "대리"}, { value: "과장", text: "과장"}, { value: "차장", text: "차장"}].map((option) => (
                            <option key={option.value} value={option.value}>
                                {option.text}
                            </option>
                        ))}
                        <option value="">-</option>
                    </Input>
                }
                error1={formik.errors.position}
                touched1={formik.touched.position}
                label2="관리자 여부"
                input2={
                    <Input
                        type="select"
                        id="managementDepartment"
                        name="managementDepartment"
                        value={formik.values.managementDepartment || ""}
                        onChange={onKeyValueChangeByEvent}
                        onBlur={formik.handleBlur}
                        valid={!formik.errors.managementDepartment && formik.touched.managementDepartment}
                        invalid={!!formik.errors.managementDepartment && formik.touched.managementDepartment}
                        disabled={globalReadOnly}
                    >
                        {getManagementDepartmentOptions().map((option) => (
                            <option key={option.value} value={option.value}>
                                {option.text}
                            </option>
                        ))}
                    </Input>
                }
                error2={formik.errors.managementDepartment}
                touched2={formik.touched.managementDepartment}
            />

            {/* 보기 권한 입력 필드 (단일 필드 사용) */}
            <DoubleInputField
                label1="보기 권한"
                input1={
                    <Input
                        type="select"
                        id="viewPermission"
                        name="viewPermission"
                        value={formik.values.viewPermission || ""}
                        onChange={onKeyValueChangeByEvent}
                        onBlur={formik.handleBlur}
                        valid={!formik.errors.viewPermission && formik.touched.viewPermission}
                        invalid={!!formik.errors.viewPermission && formik.touched.viewPermission}
                        disabled={globalReadOnly}
                    >
                        {[{ value: "All", text: "전체" }, { value: "Dept", text: "부서" }].map((option) => (
                            <option key={option.value} value={option.value}>
                                {option.text}
                            </option>
                        ))}
                        <option value="None">-</option>
                    </Input>
                }
                error1={formik.errors.viewPermission}
                touched1={formik.touched.viewPermission}
                label2="" // 두 번째 필드는 빈 문자열로 설정
                input2={null} // 두 번째 필드를 null로 설정
                error2="" // 두 번째 필드의 에러 메시지 빈 값
                touched2={false} // 두 번째 필드의 터치 상태 false
            />

        </Fragment>);
};

export default React.memo(SettingsUsersCommon);
