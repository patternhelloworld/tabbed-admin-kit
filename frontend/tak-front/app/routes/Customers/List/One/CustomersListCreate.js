import React, {useCallback, useState, useEffect, useRef} from 'react';
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

import agent from "shared/api/agent";
import {renderError} from "shared/utils/CommonErrorHandler";

import LoadingOverlay from 'react-loading-overlay';
import ClockLoader from 'react-spinners/ClockLoader';
import {useRecoilValue, useResetRecoilState, useSetRecoilState} from "recoil";
import {
    boardUpdateOneSelector,
    boardUpdateModifiedOneSelector, boardUpdateResetModifiedOneSelector
} from "shared/recoil/board/boardUpdateState";
import * as Yup from "yup";
import {useFormik} from "formik";

import { useFileUpload } from '../useFileUpload';

import {CRUD_COLUMNS, isAuthorized} from "../../../../shared/utils/authorization";
import {globalInfoAccessTokenUserInfoSelector} from "../../../../shared/recoil/globalInfoState";
import {useFormikUtils} from "../../../../shared/hooks/useFormiklUtils";
import {
    ButtonWrapper,
    DetailHeaderWrapper,
    FormikBirthDateForm
} from "shared/components/OptimizedHtmlElements";
import {dateStrToDate, formatDateTimeWrapper, formatDateWrapper} from "shared/utils/date-handler";
import {CustomersListCommonUtil} from "../common";
import CustomersListCommon from "./CustomersListCommon";
import {ActionIcon, Tooltip, Box, Flex, Menu, Text, Title} from '@mantine/core';

// true && false : false , true || false : true
const CustomersListCreate = ({createOne = () => {}, refreshAll = () => {}, refreshOne = () => {}, recoilKey, PK_NAME}) => {

    const history = useHistory();

    const me = useRecoilValue(globalInfoAccessTokenUserInfoSelector());
    const globalReadOnly = !isAuthorized({recoilKey, accessTokenUserInfo: me, CRUD_COLUMN: CRUD_COLUMNS.CREATE})


    const one = useRecoilValue(boardUpdateOneSelector({recoilKey}));

    const modifiedOne = useRecoilValue(boardUpdateModifiedOneSelector({recoilKey}));
    const setModifiedOne = useSetRecoilState(boardUpdateModifiedOneSelector({recoilKey}));

    const [loading, setLoading] = useState(false);

    const { file, onFileChange, uploadFile } = useFileUpload();

    const oneValidationSchema = Yup.object().shape({
        customerName: Yup.string()
            .min(2, '고객명은 최소 2 글자입니다.')
            .max(50, '고객명은 최대 50 글자입니다.')
            .required('고객명은 필수 입니다.'),
        email: Yup.string()
            .email('올바른 이메일 형식이 아닙니다.')
            .required('이메일은 필수 입니다.'),
        birthYear: Yup.string()
            .nullable()
            .test('birth-required', '연도를 선택하세요.', function (value) {
                const {birthMonth, birthDay} = this.parent;
                if (birthMonth || birthDay) {
                    return value;
                } else {
                    return true;
                }
            }),
        birthMonth: Yup.string()
            .nullable()
            .test('birth-required', '월을 선택하세요.', function (value) {
                const {birthYear, birthDay} = this.parent;
                if (birthYear || birthDay) {
                    return value;
                } else {
                    return true;
                }
            }),
        birthDay: Yup.string()
            .nullable()
            .test('birth-required', '일을 선택하세요.', function (value) {
                const {birthYear, birthMonth} = this.parent;
                if (birthYear || birthMonth) {
                    return value;
                } else {
                    return true;
                }
            }),
        hp: Yup.string()
            .required('핸드폰번호는 필수입니다.')
    });


    const formik = useFormik({
        initialValues: {},
        validate: values => {
            try {
                oneValidationSchema.validateSync(values, {abortEarly: false});
            } catch (errors) {
                return errors.inner.reduce((acc, curr) => {
                    acc[curr.path] = curr.message;
                    return acc;
                }, {});
            }
        },
        // 값 변경시마다 validation 체크
        validateOnChange: true,
        // 인풋창 블러시에 validation 체크
        validateOnBlur: true
    });

    const {
        onKeyValueChangeByEventMemoized, onKeyValueChangeByNameValueMemoized,
        initializeFormikCommon, onKeyValueChangeByEvent, onKeyValueChangeByNameValue
    } = useFormikUtils({formik, oneValidationSchema});

    /*
    *
    *   Event Handler
    *
    * */

    const onSubmit = async (e) => {

        try {
            if (e) {
                // 예를 들어 이 element 가 a 태그라면 href 의 기능을 항상 막겠다.
                e.preventDefault()
                // 이 버튼을 클릭하였을 때, 상위 element 로의 전파를 막고 이 기능만 실행한다.
                e.stopPropagation()
            }

            if (formik.isValid && formik.dirty) {

                setLoading(true);

                const {meta, ...valuesWithoutMeta} = formik.values;
                const re = await Promise.all([agent.Customer.create({
                    ...valuesWithoutMeta,
                    birthDate: valuesWithoutMeta.birthYear && valuesWithoutMeta.birthMonth && valuesWithoutMeta.birthDay ?
                        formatDateWrapper(valuesWithoutMeta.birthYear + "-" + valuesWithoutMeta.birthMonth + "-" + valuesWithoutMeta.birthDay) : null
                })]);


                if (re[0].statusCode === 200) {
                    const newCustomerIdx = re[0].data.customerIdx;

                    try {
                        let fileInfo;
                        if (file && valuesWithoutMeta.personalDataConsent === "Y") {
                            fileInfo = await uploadFile(file, newCustomerIdx);
                        }

                        const [fname, sname] = fileInfo ? fileInfo.split(' : ').map(s => s.trim()) : [null, null];

                        await Promise.all([agent.PrivacyAgree.create({
                            customerIdx : newCustomerIdx,
                            isAgree : valuesWithoutMeta.personalDataConsent,
                            agreeDate : valuesWithoutMeta.personalDataConsentDate,
                            fname,
                            sname
                        })]);
                    } catch(error) {
                        console.error('파일등록 중 오류 발생:', error);
                    }

                    refreshAll();
                    alert('등록 완료.')
                } else {
                    renderError({errorObj: re[0], formik});
                }
            }
        } finally {
            setLoading(false);
        }
    }


    /* Life Cycle */
    useEffect(() => {

        const nullOrUndefinedSafeOne = CustomersListCommonUtil.createNullOrUndefinedSafeOne(one);

        initializeFormikCommon({
            one,
            modifiedOne,
            PK_NAME,
            customFormikSetOneFunc: (one) => {
                if (one.birthDate) {
                    const [birthYear, birthMonth, birthDay] = one.birthDate.split('-').map(val => parseInt(val, 10));
                    formik.setValues({
                        ...formik.initialValues,
                        ...one,
                        birthYear,
                        birthMonth,
                        birthDay,
                        ...nullOrUndefinedSafeOne
                    });
                } else {
                    formik.setValues({
                        ...formik.initialValues,
                        ...one,
                        ...nullOrUndefinedSafeOne
                    });
                }
            }
        })
    }, [one]);


    useEffect(() => {
        console.log("Formik 값 변화")
        console.log(formik.values)
        setModifiedOne(formik.values);
    }, [formik.values])


    return (
        <LoadingOverlay
            spinner={<ClockLoader color="#ffffff" size={20}/>}
            active={loading}
        >
            <Card className="mb-3">
                <CardHeader>
                    <DetailHeaderWrapper id={formik.values.customerIdx} name={formik.values.customerName}/>
                </CardHeader>
                <CardBody>
                    <Form className="mt-4 mb-3">
                        <CustomersListCommon formik={formik} globalReadOnly={globalReadOnly}
                                             onKeyValueChangeByEvent={onKeyValueChangeByEvent}
                                             onKeyValueChangeByNameValue={onKeyValueChangeByNameValue}
                                             PK_NAME={PK_NAME} onFileChange={onFileChange}></CustomersListCommon>

                        <Flex p="md" justify="center" className={"mt-4"}>
                            <Flex gap="lg">
                                <ButtonWrapper
                                    color={"primary"}
                                    btnText={"등록"}
                                    formik={formik}
                                    handleClick={onSubmit}
                                    me={me}
                                    recoilKey={recoilKey}
                                    crudColumn={CRUD_COLUMNS.CREATE}
                                />
                                <ButtonWrapper
                                    color={"dark"}
                                    btnText={"취소"}
                                    handleClick={() => {
                                        refreshOne();
                                    }}
                                    me={me}
                                    recoilKey={recoilKey}
                                    crudColumn={CRUD_COLUMNS.CREATE}
                                />
                            </Flex>
                        </Flex>

                    </Form>
                </CardBody>
            </Card>
        </LoadingOverlay>
    );

};

export default React.memo(CustomersListCreate);
