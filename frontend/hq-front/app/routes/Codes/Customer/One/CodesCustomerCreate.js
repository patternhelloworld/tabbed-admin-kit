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
    boardUpdateModifiedOneSelector, boardUpdateResetModifiedOneSelector, boardUpdateResetOneSelector
} from "shared/recoil/board/boardUpdateState";
import * as Yup from "yup";
import {useFormik} from "formik";

import "assets/images/date-picker/react-datepicker.css";
import {CRUD_COLUMNS, isAuthorized} from "shared/utils/authorization";
import {globalInfoAccessTokenUserInfoSelector} from "shared/recoil/globalInfoState";
import {useGlobalSubMenusReload} from "shared/hooks/useGlobalSubMenusReload";
import {ButtonWrapper, DetailHeaderWrapper} from "shared/components/OptimizedHtmlElements";
import {useFormikUtils} from "../../../../shared/hooks/useFormiklUtils";
import {formatDateTimeWrapper, formatDateWrapper} from "../../../../shared/utils/date-handler";
import {ActionIcon, Tooltip, Box, Flex, Menu, Text, Title} from '@mantine/core';

const PK_NAME = "codeCustomerIdx";
const CodesCustomerCreate = ({refreshAll = ()=>{}, refreshOne = ()=>{}, recoilKey}) => {

    const me = useRecoilValue(globalInfoAccessTokenUserInfoSelector());

    const one = useRecoilValue(boardUpdateOneSelector({recoilKey}));

    const modifiedOne = useRecoilValue(boardUpdateModifiedOneSelector({recoilKey}));
    const setModifiedOne = useSetRecoilState(boardUpdateModifiedOneSelector({recoilKey}));

    const [loading, setLoading] = useState(false);

    const oneValidationSchema = Yup.object().shape({
        categoryCd : Yup.string().required('카테고리 코드는 필수입니다.'),
        categoryNm : Yup.string().required('카테고리 이름은 필수입니다.'),
    });

    const formik = useFormik({
        initialValues: {
            categoryCd: '',
            categoryNm: ''
        },

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
        validateOnChange: true,
        validateOnBlur: true
    });

    const { onKeyValueChangeByEventMemoized, onKeyValueChangeByNameValueMemoized,
        initializeFormikCommon, onKeyValueChangeByEvent, onKeyValueChangeByNameValue } = useFormikUtils({  formik, oneValidationSchema  });


    /*
    *
    *   Event Handler
    *
    * */
    const handleCreateOrUpdate = async (e) => {

        try {

            if (e) {
                // 예를 들어 이 element 가 a 태그라면 href 의 기능을 항상 막겠다.
                e.preventDefault()
                // 이 버튼을 클릭하였을 때, 상위 element 로의 전파를 막고 이 기능만 실행한다.
                e.stopPropagation()
            }

            if (formik.isValid && formik.dirty) {

                setLoading(true);

                const { meta, ...valuesWithoutMeta } = formik.values;

                const re = await Promise.all([agent.CodeCustomer.create({
                    ...valuesWithoutMeta
                })]);

                if (re[0].statusCode === 200) {
                    refreshAll();
                    alert('신규 등록 성공.')
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
        // one 이라는 recoil 이 바뀌는 순간
        initializeFormikCommon({
            one, modifiedOne, PK_NAME, customFormikSetModifiedOneFunc: (modifiedOne) => {
                formik.setValues({...formik.initialValues, ...modifiedOne })
            }, customFormikSetOneFunc: (one) => {
                formik.setValues({...formik.initialValues, ...one })
            }
        })

    }, [one])

    useEffect(() => {
        formik.setTouched({...Object.fromEntries(
                Object.entries(oneValidationSchema.fields).map(([key, value]) => [key, true])
            )
        });
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
                <CardHeader><DetailHeaderWrapper id={formik.values[PK_NAME]} name="신규생성"/></CardHeader>
                <CardBody>
                    <Form className="mt-4 mb-3">
                        <FormGroup row>
                            <Label for="categoryCd" sm={2} className="right">
                                카테고리 코드
                            </Label>
                            <Col sm={10}>
                                <Input
                                    id="categoryCd"
                                    name="categoryCd"
                                    type="text"
                                    valid={formik.touched.categoryCd && !formik.errors.categoryCd}
                                    invalid={formik.touched.categoryCd && !!formik.errors.categoryCd}
                                    value={formik.values.categoryCd || ""}
                                    onChange={onKeyValueChangeByEvent}
                                    onBlur={formik.handleBlur}
                                    maxLength={15}
                                />
                                <FormFeedback className={'ml-3'}>{formik.errors.categoryCd}</FormFeedback>
                            </Col>
                        </FormGroup>

                        <FormGroup row>
                            <Label for="categoryNm" sm={2} className="right">
                                카테고리 이름
                            </Label>
                            <Col sm={10}>
                                <Input
                                    id="categoryNm"
                                    name="categoryNm"
                                    type="text"
                                    valid={!formik.errors.categoryNm && formik.touched.categoryNm}
                                    invalid={!!formik.errors.categoryNm && formik.touched.categoryNm}
                                    value={formik.values.categoryNm || ""}
                                    onChange={onKeyValueChangeByEvent}
                                    onBlur={formik.handleBlur}
                                    maxLength={15}
                                />
                                <FormFeedback className={'ml-3'}>{formik.errors.categoryNm}</FormFeedback>
                            </Col>
                        </FormGroup>

                        <Flex p="md" justify="center" className={"mt-4"}>
                            <Flex gap="lg">
                                <ButtonWrapper
                                    color={"primary"}
                                    btnText={"등록"}
                                    formik={formik}
                                    handleClick={handleCreateOrUpdate}
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

export default React.memo(CodesCustomerCreate);
