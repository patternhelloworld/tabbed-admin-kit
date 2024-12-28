import React, {useCallback, useState, useEffect} from 'react'
import {Link} from 'react-router-dom';
import {
    Form,
    FormFeedback,
    FormGroup,
    FormText,
    Input,
    CustomInput,
    Button,
    Label,
    Col,
    Row, Card
} from 'components';

import agent from "shared/api/agent";
import {useFormik, FormikProps} from 'formik';
import * as Yup from 'yup'

import {renderError} from "shared/utils/CommonErrorHandler";

import LoadingOverlay from 'react-loading-overlay'
import ClockLoader from 'react-spinners/ClockLoader';
import {useRecoilValue, useResetRecoilState, useSetRecoilState} from "recoil";
import {
    boardUpdateOneSelector,
    boardUpdateModifiedOneSelector, boardUpdateResetModifiedOneSelector
} from "shared/recoil/board/boardUpdateState";
import {isValidObject} from "shared/utils/utilities";
import {availableElement, CRUD_COLUMNS, isAuthorized} from "shared/utils/authorization";
import {globalInfoAccessTokenUserInfoSelector} from "shared/recoil/globalInfoState";
import {useGlobalSubMenusReload} from "shared/hooks/useGlobalSubMenusReload";

import {CardBody, Container, ThemeConsumer} from "components";
import {ButtonWrapper, DetailHeaderWrapper} from "shared/components/OptimizedHtmlElements";
import {CardHeader} from "reactstrap";
import {useFormikUtils} from "shared/hooks/useFormiklUtils";

import {Flex} from "@mantine/core";

const PK_NAME = "ext_color_code_idx";
const ExtColorUpdate = ({  createOne = () => {}, refreshAll = () => {}, refreshOne = () => {}, recoilKey}) => {

    const me = useRecoilValue(globalInfoAccessTokenUserInfoSelector());

    const one = useRecoilValue(boardUpdateOneSelector({recoilKey}));

    const modifiedOne = useRecoilValue(boardUpdateModifiedOneSelector({recoilKey}));
    const setModifiedOne = useSetRecoilState(boardUpdateModifiedOneSelector({recoilKey}));

    const [loading, setLoading] = useState(false);

    const oneValidationSchema = Yup.object().shape({
        code : Yup.string().required('외장코드는 필수입니다.'),
    });

    const formik= useFormik({
        initialValues: {
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
        // 값 변경시마다 validation 체크
        validateOnChange: true,
        // 인풋창 블러시에 validation 체크
        validateOnBlur: true
    });

    const { onKeyValueChangeByEventMemoized, onKeyValueChangeByNameValueMemoized,
        initializeFormikCommon, onKeyValueChangeByEvent, onKeyValueChangeByNameValue } = useFormikUtils({  formik, oneValidationSchema  });

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

                const { meta, ...valuesWithoutMeta } = formik.values;
                const re = await Promise.all([agent.ExtCode.update({
                    ...valuesWithoutMeta
                })]);

                if (re[0].statusCode === 200) {
                    refreshAll();
                    alert('업데이트 성공.')
                } else {
                    renderError({errorObj: re[0], formik});
                }
            }
        } finally {
            setLoading(false);
        }
    }

    const onDelete = async (e) => {
        if(!confirm("삭제 하면 더이상 보이지 않습니다. 진행할까요?")){
            return;
        }

        try {
            if (e) {
                // 예를 들어 이 element 가 a 태그라면 href 의 기능을 항상 막겠다.
                e.preventDefault()
                // 이 버튼을 클릭하였을 때, 상위 element 로의 전파를 막고 이 기능만 실행한다.
                e.stopPropagation()
            }

            const re = await Promise.all([agent.ExtCode.delete({
                [PK_NAME] : formik.values[PK_NAME]
            })]);

            if (re[0].statusCode === 200) {
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
        // one 이라는 recoil 이 바뀌는 순간
        initializeFormikCommon({
            one, modifiedOne, PK_NAME, customFormikSetModifiedOneFunc: (modifiedOne) => {
                formik.setValues({...formik.initialValues, ...modifiedOne});
            }, customFormikSetOneFunc: (one) => {
                formik.setValues({...formik.initialValues, ...one, delYn: one.delDt ? "Y" : "N"})
            }
        })
    }, [one])

    useEffect(() => {
        console.log("Formik 값 변화")
        setModifiedOne(formik.values);
        console.log(formik.values);
    }, [formik.values])
    return (
        <LoadingOverlay
            spinner={<ClockLoader color="#ffffff" size={20}/>}
            active={loading}
        >
            <Container>
                <Card className="mb-3">
                    <CardHeader> <DetailHeaderWrapper id={formik.values[PK_NAME]} name={formik.values.ext_color_code_idx}/></CardHeader>
                    <CardBody>
                        <Form className="mt-4 mb-3">
                            <FormGroup row>
                                <Label for="categoryCd" sm={2} className="right">
                                    코드
                                </Label>
                                <Col sm={10}>
                                    <Input
                                        id="code"
                                        name="code"
                                        type="text"
                                        valid={!formik.errors.code && formik.touched.code}
                                        invalid={!!formik.errors.code && formik.touched.code}
                                        value={formik.values.code || ""}
                                        onChange={onKeyValueChangeByEvent}
                                        onBlur={formik.handleBlur}
                                        maxLength={45}
                                    />
                                    <FormFeedback className={'ml-3'}>{formik.errors.code}</FormFeedback>
                                </Col>
                            </FormGroup>

                            <FormGroup row>
                                <Label for="description" sm={2} className="right">
                                    설명
                                </Label>
                                <Col sm={10}>
                                    <Input
                                        id="description"
                                        name="description"
                                        type="text"
                                        valid={!formik.errors.description && formik.touched.description}
                                        invalid={!!formik.errors.description && formik.touched.description}
                                        value={formik.values.description || ""}
                                        onChange={onKeyValueChangeByEvent}
                                        onBlur={formik.handleBlur}
                                        maxLength={45}
                                    />
                                    <FormFeedback className={'ml-3'}>{formik.errors.description}</FormFeedback>
                                </Col>
                            </FormGroup>


                            <FormGroup row>
                                <Label for="regUserid" sm={2} className="right important">등록자</Label>
                                <Col sm={10}>
                                    <Input
                                        type="text"
                                        name="regUserid"
                                        id="regUserid"
                                        value={
                                            formik.values.regUserid
                                                ? formik.values.regDt
                                                    ? `${formik.values.regUserid} (${formik.values.regDt})`
                                                    : `${formik.values.regUserid} ()`
                                                : formik.values.regDt
                                                    ? `(${formik.values.regDt})`
                                                    : ""
                                        }
                                        disabled
                                        style={{borderColor: '#dee2e6', cursor: 'not-allowed' }}
                                    />
                                </Col>
                            </FormGroup>

                            <FormGroup row>
                                <Label for="modUserid" sm={2} className="right important">수정자</Label>
                                <Col sm={10}>
                                    <Input
                                        type="text"
                                        name="modUserid"
                                        id="modUserid"
                                        value={
                                            formik.values.modUserid
                                                ? formik.values.modDt
                                                    ? `${formik.values.modUserid} (${formik.values.modDt})`
                                                    : `${formik.values.modUserid} ()`
                                                : formik.values.modDt
                                                    ? `(${formik.values.modDt})`
                                                    : ""
                                        }
                                        disabled
                                        style={{borderColor: '#dee2e6', cursor: 'not-allowed' }}
                                    />
                                </Col>
                            </FormGroup>

                            <Flex p="md" justify="center" className={"mt-4"}>
                                <Flex gap="lg">
                                    <ButtonWrapper
                                        color={"info"}
                                        btnText={"신규"}
                                        handleClick={()=>{
                                            createOne()
                                        }}
                                        me={me}
                                        recoilKey={recoilKey}
                                        crudColumn={CRUD_COLUMNS.CREATE}
                                    />
                                    <ButtonWrapper
                                        color={"primary"}
                                        btnText={"등록"}
                                        formik={formik}
                                        handleClick={onSubmit}
                                        me={me}
                                        recoilKey={recoilKey}
                                        crudColumn={CRUD_COLUMNS.UPDATE}
                                    />
                                    <ButtonWrapper
                                        color={"dark"}
                                        btnText={"취소"}
                                        handleClick={()=>{
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
            </Container>
        </LoadingOverlay>
    );
}

export default React.memo(ExtColorUpdate);
