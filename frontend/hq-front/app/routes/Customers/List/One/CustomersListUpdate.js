import React, {useCallback, useState, useEffect, useRef} from 'react';
import {Link, useHistory} from 'react-router-dom';
import {
    Form,
    FormFeedback,
    FormGroup,
    Input,
    Label,
    Col,
    Row, Table, FormText, Card, CardBody, CustomInput, CardHeader
} from 'reactstrap';
import classNames from "classnames";
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

import { useFileUpload } from '../useFileUpload';

import {CRUD_COLUMNS, isAuthorized} from "../../../../shared/utils/authorization";
import {globalInfoAccessTokenUserInfoSelector} from "../../../../shared/recoil/globalInfoState";
import {useFormikUtils} from "../../../../shared/hooks/useFormiklUtils";
import {
    ButtonWrapper,
    DetailHeaderWrapper,
    FormikBirthDateForm
} from "../../../../shared/components/OptimizedHtmlElements";
import CustomDatePicker from "../../../../shared/components/CustomDatePicker";
import {dateStrToDate, formatDateTimeWrapper, formatDateWrapper} from "../../../../shared/utils/date-handler";
import {
    getCustomerInfoOptions,
    getCustomerTypeOptions, getGenderOptions,
    getNationalityOptions,
    getPurchasePlanOptions
} from "../../../../shared/enums";
import {CodeGeneralUtil, isValidObject} from "../../../../shared/utils/utilities";
import {CustomersListCommonUtil} from "../common";
import CustomersListCommon from "./CustomersListCommon";
import {ActionIcon, Tooltip, Box,  Flex, Menu, Text, Title} from '@mantine/core';

// true && false : false , true || false : true
const CustomersListUpdate = ({createOne = () => {}, refreshAll = () => {}, refreshOne = () => {}, recoilKey, PK_NAME, injectedGlobalReadOnly = null}) => {

    const history = useHistory();

    const me = useRecoilValue(globalInfoAccessTokenUserInfoSelector());
    const globalReadOnly = injectedGlobalReadOnly != null ? injectedGlobalReadOnly : !isAuthorized({recoilKey, accessTokenUserInfo: me, CRUD_COLUMN: CRUD_COLUMNS.UPDATE})


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
        validateOnChange: !globalReadOnly,
        // 인풋창 블러시에 validation 체크
        validateOnBlur: !globalReadOnly
    });

    const {
        onKeyValueChangeByEventMemoized, onKeyValueChangeByNameValueMemoized,
        initializeFormikCommon, onKeyValueChangeByEvent, onKeyValueChangeByNameValue
    } = useFormikUtils({formik, oneValidationSchema, initialTouched : !globalReadOnly});

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

                // index.js 에서 one cycle 함수를 실행하며 만든 meta 객체를 서버에 던질 필요가 없다
                const {meta, ...valuesWithoutMeta} = formik.values;
                const re = await Promise.all([agent.Customer.update({
                    ...valuesWithoutMeta,
                    birthDate: formatDateWrapper(valuesWithoutMeta.birthYear + "-" + valuesWithoutMeta.birthMonth + "-" + valuesWithoutMeta.birthDay),
                    delDt: formatDateTimeWrapper(valuesWithoutMeta.delDt),
                    modDt: formatDateTimeWrapper(valuesWithoutMeta.modDt),
                    regDt: formatDateTimeWrapper(valuesWithoutMeta.regDt)
                })]);

                if (re[0].statusCode === 200) {
                    const newCustomerIdx = re[0].data.customerIdx;
                    const privacyChanged = (
                        one.personalDataConsent !== modifiedOne.personalDataConsent ||
                        one.personalDataConsentDate !== modifiedOne.personalDataConsentDate ||
                        file
                    );

                    if(privacyChanged) {
                        try {
                            let fileInfo;
                            if (file && valuesWithoutMeta.personalDataConsent === "Y") {
                                fileInfo = await uploadFile(file, newCustomerIdx);
                            }

                            const [fname, sname] = fileInfo ? fileInfo.split(' : ').map(s => s.trim()) : [null, null];

                            await agent.PrivacyAgree.create({
                                customerIdx: newCustomerIdx,
                                isAgree: valuesWithoutMeta.personalDataConsent,
                                agreeDate: valuesWithoutMeta.personalDataConsentDate,
                                fname,
                                sname
                            });
                        } catch(error) {
                            console.error('파일등록 중 오류 발생:', error);
                        }
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

    const onDelete = async (e) => {
        if (!confirm("해당 사용자를 삭제 하면 더이상 보이지 않습니다. 진행할까요?")) {
            return;
        }

        try {
            if (e) {
                // 예를 들어 이 element 가 a 태그라면 href 의 기능을 항상 막겠다.
                e.preventDefault()
                // 이 버튼을 클릭하였을 때, 상위 element 로의 전파를 막고 이 기능만 실행한다.
                e.stopPropagation()
            }

            const re = await Promise.all([agent.Customer.delete({
                [PK_NAME]: formik.values[PK_NAME]
            })]);

            if (re[0].statusCode === 200) {
                //setOne(re[0].data)
                // alert('업데이트 완료.')
                //console.log(re[0])
                refreshAll();
                alert('삭제 성공.')
            } else {
                renderError({errorObj: re[0], formik});
            }
        } finally {
            setLoading(false);
        }
    }

    /* Life Cycle */
    useEffect(() => {
        const nullOrUndefinedSafeOne = CustomersListCommonUtil.createNullOrUndefinedSafeOne(one);


        console.log(one);

        initializeFormikCommon({
            one,
            modifiedOne,
            PK_NAME,
            customFormikSetOneFunc: (one) => {
                const formikValues = {
                    ...formik.initialValues,
                    ...one,
                    ...nullOrUndefinedSafeOne
                };

                if (one.birthDate) {
                    const [birthYear, birthMonth, birthDay] = one.birthDate.split('-').map(val => parseInt(val, 10));
                    formikValues.birthYear = birthYear;
                    formikValues.birthMonth = birthMonth;
                    formikValues.birthDay = birthDay;
                }

                if (one.meta && one.meta.privacyAgree) {
                    formikValues.privacyAgreeIdx = one.meta.privacyAgree.privacyAgreeIdx;
                    formikValues.isAgree = one.meta.privacyAgree.isAgree;
                    formikValues.fname = one.meta.privacyAgree.fname;
                    formikValues.sname = one.meta.privacyAgree.sname;
                }

                formik.setValues(formikValues);
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


                            <Flex p="md" justify="center" className={classNames('mt-4', { 'd-none': globalReadOnly })}>
                                <Flex gap="lg">
                                    <ButtonWrapper
                                        color={"info"}
                                        btnText={"신규"}
                                        handleClick={() => {
                                            createOne()
                                        }}
                                        me={me}
                                        recoilKey={recoilKey}
                                        crudColumn={CRUD_COLUMNS.CREATE}
                                    />

                                    <ButtonWrapper
                                        color={"primary"}
                                        btnText={"수정"}
                                        formik={formik}
                                        handleClick={onSubmit}
                                        me={me}
                                        recoilKey={recoilKey}
                                        crudColumn={CRUD_COLUMNS.UPDATE}
                                    />

                                    <ButtonWrapper
                                        color={"dark"}
                                        btnText={"취소"}
                                        handleClick={() => {
                                            refreshOne();
                                        }}
                                        me={me}
                                        recoilKey={recoilKey}
                                        crudColumn={CRUD_COLUMNS.UPDATE}
                                    />

                                    <ButtonWrapper
                                        color={"danger"}
                                        btnText={"삭제"}
                                        handleClick={onDelete}
                                        me={me}
                                        recoilKey={recoilKey}
                                        crudColumn={CRUD_COLUMNS.DELETE}
                                    />
                                </Flex>
                            </Flex>
                    </Form>
                </CardBody>
            </Card>
        </LoadingOverlay>
    );

};

export default React.memo(CustomersListUpdate);
