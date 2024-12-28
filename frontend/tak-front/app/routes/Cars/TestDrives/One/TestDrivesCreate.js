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

import {Container, ThemeConsumer} from "../../../../components";


import KakaoAddress from "../../../../shared/components/kakao-address/KakaoAddress";
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

import {CodeGeneralUtil, safeValue} from "../../../../shared/utils/utilities";
import {TestDrivesCommonUtil} from "../common";
import TestDrivesCommon from "./TestDrivesCommon";
import {ActionIcon, Tooltip, Box, Flex, Menu, Text, Title} from '@mantine/core';

// true && false : false , true || false : true
const TestDrivesCreate = ({
                                 createOne = () => {
                                 }, refreshAll = () => {
    }, refreshOne = () => {
    }, recoilKey, PK_NAME
                             }) => {

    const history = useHistory();

    const me = useRecoilValue(globalInfoAccessTokenUserInfoSelector());
    const globalReadOnly = !isAuthorized({recoilKey, accessTokenUserInfo: me, CRUD_COLUMN: CRUD_COLUMNS.CREATE})


    const one = useRecoilValue(boardUpdateOneSelector({recoilKey}));

    const modifiedOne = useRecoilValue(boardUpdateModifiedOneSelector({recoilKey}));
    const setModifiedOne = useSetRecoilState(boardUpdateModifiedOneSelector({recoilKey}));


    const [loading, setLoading] = useState(false);

    const oneValidationSchema = Yup.object().shape({

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
                const re = await Promise.all([agent.TestDrive.create({
                    ...valuesWithoutMeta
                })]);

                if (re[0].statusCode === 200) {
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

        const nullOrUndefinedSafeOne = TestDrivesCommonUtil.createNullOrUndefinedSafeOne(one);

        initializeFormikCommon({
            one,
            modifiedOne,
            PK_NAME,
            customFormikSetOneFunc: (one) => {
                formik.setValues({
                    ...formik.initialValues,
                    ...one,
                    ...nullOrUndefinedSafeOne
                });
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
                    <DetailHeaderWrapper id={formik.values.testDriveIdx} />
                </CardHeader>
                <CardBody>
                    <Form className="mt-4 mb-3">
                        <TestDrivesCommon formik={formik} globalReadOnly={globalReadOnly}
                                             onKeyValueChangeByEvent={onKeyValueChangeByEvent}
                                             onKeyValueChangeByNameValue={onKeyValueChangeByNameValue}
                                             PK_NAME={PK_NAME} me={me} recoilKey={recoilKey}></TestDrivesCommon>

                        <Flex p="md" justify="center" className={"mt-5"}>
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

export default React.memo(TestDrivesCreate);
