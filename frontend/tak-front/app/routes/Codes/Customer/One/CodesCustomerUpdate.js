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

import SimpleBtnImgs from "assets/images/btns/SimpleBtnImgs";
import {ActionIcon, Tooltip, Box,  Flex, Menu, Text, Title} from '@mantine/core';

const PK_NAME = "codeCustomerIdx";
const CodesCustomerUpdate = ({  createOne = () => {}, refreshAll = () => {}, refreshOne = () => {}, recoilKey}) => {

    const me = useRecoilValue(globalInfoAccessTokenUserInfoSelector());

    const one = useRecoilValue(boardUpdateOneSelector({recoilKey}));

    const modifiedOne = useRecoilValue(boardUpdateModifiedOneSelector({recoilKey}));
    const setModifiedOne = useSetRecoilState(boardUpdateModifiedOneSelector({recoilKey}));

    const [loading, setLoading] = useState(false);

    const oneValidationSchema = Yup.object().shape({
        categoryCd : Yup.string().required('카테고리 코드는 필수입니다.'),
        categoryNm : Yup.string().required('카테고리 이름은 필수입니다.'),
    });

    const subOneValidationSchema = Yup.object().shape({
        codeCustomerNm: Yup.string().required('코드 고객 이름은 필수입니다.'),
        sort: Yup.number().transform((value) => (isNaN(value) ? undefined : value)).required('노출 순서는 필수입니다.')
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
                const re = await Promise.all([agent.CodeCustomer.update({
                    ...valuesWithoutMeta
                })]);

                if (re[0].statusCode === 200) {
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

            const re = await Promise.all([agent.CodeCustomer.delete({
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

    const handleMetasButton = async (e) => {
        const action = e.currentTarget.dataset.action;
        const id = e.currentTarget.dataset.id;
        const index = e.currentTarget.dataset.index;

        if(action === "create") {
            try {
                const codeMeta = {codeCustomerNm : formik.values.codeCustomerNm, sort : formik.values.sort};
                await subOneValidationSchema.validate(codeMeta, { abortEarly: false });

                const re = await Promise.all([agent.CodeCustomer.metaCreate({
                    [PK_NAME] : formik.values[PK_NAME],
                    codeCustomerNm : formik.values.codeCustomerNm,
                    sort : formik.values.sort,
                })]);

                if (re[0].statusCode !== 200) {
                    throw new Error(JSON.stringify(re[0].userValidationMessage) || '실패. 관리자에게 문의바랍니다');
                }
                refreshAll();
                alert('등록 성공.')

            } catch(error) {
                if (error instanceof Yup.ValidationError) {
                    error.inner.forEach(error => {
                        formik.setFieldError(error.path, error.message);
                        formik.setFieldTouched(error.path, true, false);
                    });
                } else {
                    alert(error.message);
                }
            } finally {
                setLoading(false);
            }

        } else if(action === "update") {
            try {
                const codeMeta = formik.values.meta.codeMetas[index];
                await subOneValidationSchema.validate(codeMeta, { abortEarly: false });

                const re = await Promise.all([agent.CodeCustomer.metaUpdate({
                    codeCustomerIdx: id,
                    codeCustomerNm: codeMeta.codeCustomerNm,
                    sort: codeMeta.sort,
                })]);

                if (re[0].statusCode !== 200) {
                    throw new Error(JSON.stringify(re[0].userValidationMessage) || '실패. 관리자에게 문의바랍니다');
                }
                refreshAll();
                alert('등록 성공.')

            } catch(error) {
                if (error instanceof Yup.ValidationError) {
                    error.inner.forEach(error => {
                        formik.setFieldError(`meta.codeMetas[${index}].${error.path}`, error.message);
                        formik.setFieldTouched(`meta.codeMetas[${index}].${error.path}`, true, false);
                    });
                } else {
                    alert(error.message);
                }
            } finally {
                setLoading(false);
            }

        } else if(action === "delete") {
            if(!confirm("삭제하시겠습니까?")){
                return;
            }

            try {
                const re = await Promise.all([agent.CodeCustomer.delete({
                    codeCustomerIdx: id,
                })]);

                if (re[0].statusCode !== 200) {
                    throw new Error(JSON.stringify(re[0].userValidationMessage) || '실패. 관리자에게 문의바랍니다');
                }
                refreshAll();
                alert('삭제 성공.')

            } catch(error) {
                alert(error.message);
            } finally {
                setLoading(false);
            }
        }
    };

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

                <Card className="mb-3">
                    <CardHeader> <DetailHeaderWrapper id={formik.values[PK_NAME]} name={formik.values.codeCustomerIdx}/></CardHeader>
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
                                        valid={!formik.errors.categoryCd && formik.touched.categoryCd}
                                        invalid={!!formik.errors.categoryCd && formik.touched.categoryCd}
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

                            <FormGroup row>
                                <Label for="regUserid" sm={2} className="right important">등록자</Label>
                                <Col sm={10}>
                                    <Input
                                        type="text"
                                        name="regUserid"
                                        id="regUserid"
                                        value={formik.values.regUserid || ""}
                                        readOnly
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
                                        value={formik.values.modUserid || ""}
                                        readOnly
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

                            <FormGroup row style={{ border: '1px solid #d6d6d6', borderRadius: '4px', padding: '1rem', backgroundColor: '#eafaee', marginTop: '2rem', marginBottom: '1rem' }}>
                                <Label for='codeCustomerNm' sm={2} className="right">
                                    신규 코드
                                </Label>
                                <Col sm={4}>
                                    <Input
                                        id='codeCustomerNm'
                                        name='codeCustomerNm'
                                        type="text"
                                        value={formik.values.codeCustomerNm || ''}
                                        onChange={onKeyValueChangeByEvent}
                                        onBlur={formik.handleBlur}
                                        placeholder={"코드 이름"}
                                        maxLength={20}
                                    />
                                    {formik.errors.codeCustomerNm && formik.touched.codeCustomerNm && (
                                            <div className="error-message" style={{ color: 'red' }}>
                                                {formik.errors.codeCustomerNm}
                                            </div>
                                        )}
                                </Col>
                                <Col sm={4}>
                                    <Input
                                        id='sort'
                                        name='sort'
                                        type='text'
                                        value={formik.values.sort || ''}
                                        onChange={onKeyValueChangeByEvent}
                                        onBlur={formik.handleBlur}
                                        placeholder={"노출 순서"}
                                        maxLength={4}
                                        onKeyDown={(e) => {
                                            const regex = /\D/;
                                            if (regex.test(e.key) && e.key !== 'Backspace' && e.key !== 'Tab' && e.key !== 'ArrowLeft' && e.key !== 'ArrowRight' && e.key !== 'Delete') {
                                                e.preventDefault();
                                            }
                                        }}
                                    />
                                    {formik.errors.sort && formik.touched.sort && (
                                        <div className="error-message" style={{ color: 'red' }}>
                                            {formik.errors.sort}
                                        </div>
                                    )}
                                </Col>
                                <Col sm={2} className="text-right d-flex align-items-center">
                                    <button
                                        type="button"
                                        data-action="create"
                                        onClick={handleMetasButton}
                                        style={{
                                            background: 'transparent',
                                            border: 'none',
                                            padding: 0,
                                            cursor: 'pointer',
                                            outline: 'none',
                                            marginLeft: '33px'
                                        }}
                                    >
                                        <SimpleBtnImgs action="add" width="35px"height="30px" />
                                    </button>
                                </Col>
                            </FormGroup>

                            {formik.values.meta && formik.values.meta.codeMetas && formik.values.meta.codeMetas.map((codeMeta, index) => (
                                <div key={codeMeta.codeCustomerIdx} style={{ marginBottom: '1rem' }}>
                                    <FormGroup row className="align-items-center">
                                        <Label for={`codeCustomerNm-${index}`} sm={2} className="right">
                                            코드 {index + 1}
                                        </Label>
                                        <Col sm={4}>
                                            <Input
                                                id={`codeCustomerNm-${index}`}
                                                name={`meta.codeMetas[${index}].codeCustomerNm`}
                                                type="text"
                                                placeholder=""
                                                value={codeMeta.codeCustomerNm}
                                                onChange={onKeyValueChangeByEvent}
                                                onBlur={formik.handleBlur}
                                                maxLength={20}
                                            />
                                            {formik.errors.meta?.codeMetas?.[index]?.codeCustomerNm &&
                                                formik.touched.meta?.codeMetas?.[index]?.codeCustomerNm && (
                                                    <div className="error-message" style={{ color: 'red' }}>
                                                        {formik.errors.meta.codeMetas[index].codeCustomerNm}
                                                    </div>
                                                )}
                                        </Col>
                                        <Col sm={4}>
                                            <Input
                                                id={`sort-${index}`}
                                                name={`meta.codeMetas[${index}].sort`}
                                                type="text"
                                                value={codeMeta.sort}
                                                onChange={onKeyValueChangeByEvent}
                                                onBlur={formik.handleBlur}
                                                maxLength={4}
                                                onKeyDown={(e) => {
                                                    const regex = /\D/;
                                                    if (regex.test(e.key) && e.key !== 'Backspace' && e.key !== 'Tab' && e.key !== 'ArrowLeft' && e.key !== 'ArrowRight' && e.key !== 'Delete') {
                                                        e.preventDefault();
                                                    }
                                                }}
                                            />
                                            {formik.errors.meta?.codeMetas?.[index]?.sort &&
                                                formik.touched.meta?.codeMetas?.[index]?.sort && (
                                                    <div className="error-message" style={{ color: 'red' }}>
                                                        {formik.errors.meta.codeMetas[index].sort}
                                                    </div>
                                                )}
                                        </Col>
                                        <Col sm={2} className="text-right d-flex align-items-center">
                                            <button
                                                type="button"
                                                data-action="update"
                                                data-id={codeMeta.codeCustomerIdx}
                                                data-index={index}
                                                onClick={handleMetasButton}
                                                style={{
                                                    background: 'transparent',
                                                    border: 'none',
                                                    padding: 0,
                                                    cursor: 'pointer',
                                                    outline: 'none',
                                                }}
                                            >
                                                <SimpleBtnImgs action="update" width="35px"height="30px" />
                                            </button>

                                            <button
                                                type="button"
                                                data-action="delete"
                                                data-id={codeMeta.codeCustomerIdx}
                                                data-index={index}
                                                onClick={handleMetasButton}
                                                style={{
                                                    background: 'transparent',
                                                    border: 'none',
                                                    padding: 0,
                                                    cursor: 'pointer',
                                                    outline: 'none',
                                                }}
                                            >
                                                <SimpleBtnImgs action="delete" width="35px"height="30px" />
                                            </button>
                                        </Col>
                                    </FormGroup>
                                </div>
                            ))}

                        </Form>
                    </CardBody>
                </Card>

        </LoadingOverlay>
    );
}

export default React.memo(CodesCustomerUpdate);
