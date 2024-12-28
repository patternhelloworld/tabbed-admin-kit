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
import {
    getDealerStockInfoOptions,
    getDealerStockTypeOptions, getGenderOptions,
    getNationalityOptions,
    getPurchasePlanOptions
} from "../../../../shared/enums";
import {CodeGeneralUtil, isValidObject} from "../../../../shared/utils/utilities";
import {DealerStocksCommonUtil} from "../common";
import DealerStockListCommon from "./DealerStocksCommon";
import DealerStocksCommon from "./DealerStocksCommon";
import {ActionIcon, Tooltip, Box,  Flex, Menu, Text, Title} from '@mantine/core';

// true && false : false , true || false : true
const DealerStocksUpdate = ({createOne = () => {}, refreshAll = () => {}, refreshOne = () => {}, recoilKey, PK_NAME, injectedGlobalReadOnly = null}) => {

    const history = useHistory();

    const me = useRecoilValue(globalInfoAccessTokenUserInfoSelector());
    const globalReadOnly = injectedGlobalReadOnly != null ? injectedGlobalReadOnly : !isAuthorized({recoilKey, accessTokenUserInfo: me, CRUD_COLUMN: CRUD_COLUMNS.UPDATE})


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
                const re = await Promise.all([agent.DealerStock.update({
                    ...valuesWithoutMeta,
                    importDate: formatDateWrapper(valuesWithoutMeta.importDate),
                    delDt: formatDateTimeWrapper(valuesWithoutMeta.delDt),
                    modDt: formatDateTimeWrapper(valuesWithoutMeta.modDt),
                    regDt: formatDateTimeWrapper(valuesWithoutMeta.regDt)
                })]);

                if (re[0].statusCode === 200) {
                    //setOne(re[0].data)
                    // alert('업데이트 완료.')
                    //console.log(re[0])
                    refreshAll();
                    alert('업데이트 완료.')
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

            const re = await Promise.all([agent.DealerStock.delete({
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

        const nullOrUndefinedSafeOne = DealerStocksCommonUtil.createNullOrUndefinedSafeOne(one);

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
                    <DetailHeaderWrapper id={formik.values.dealerStockIdx}/>
                </CardHeader>
                <CardBody>
                    <Form className="mt-4 mb-3">
                        <DealerStocksCommon formik={formik} globalReadOnly={globalReadOnly}
                                             onKeyValueChangeByEvent={onKeyValueChangeByEvent}
                                             onKeyValueChangeByNameValue={onKeyValueChangeByNameValue}
                                             PK_NAME={PK_NAME}></DealerStocksCommon>


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

export default React.memo(DealerStocksUpdate);
