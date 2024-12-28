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
import SettingsUsersCommon from "./SettingsUsersCommon";


// true && false : false , true || false : true
const SettingsUsersUpdate = ({createOne = () =>{}, refreshAll = ()=>{}, refreshOne = ()=>{}, recoilKey, PK_NAME}) => {

    const history = useHistory();

    const me = useRecoilValue(globalInfoAccessTokenUserInfoSelector());
    const globalReadOnly = !isAuthorized({ recoilKey, accessTokenUserInfo : me, CRUD_COLUMN : CRUD_COLUMNS.UPDATE })


    const one = useRecoilValue(boardUpdateOneSelector({recoilKey}));

    const modifiedOne = useRecoilValue(boardUpdateModifiedOneSelector({recoilKey}));
    const setModifiedOne = useSetRecoilState(boardUpdateModifiedOneSelector({recoilKey}));


    const [loading, setLoading] = useState(false);

    const oneValidationSchema = Yup.object().shape({
        name: Yup.string()
            .min(2, '사용자 이름은 최소 2 글자입니다.')
            .max(50, '사용자 이름은 최대 50 글자입니다.')
            .required('사용자 이름은 필수 입니다.'),
        phoneNumber: Yup.string()
            .matches(/^[\d\s-]+$/, '올바른 핸드폰 번호 형식이 아닙니다.')
            .required('핸드폰 번호는 필수 입니다.'),
        userId: Yup.string()
            .required('사용자 ID는 필수 입니다.'),
        deptIdx: Yup.number()
            .required('조직 선택은 필수입니다.')
            .notOneOf([0], '조직 선택은 필수입니다.')
    });

    const formik = useFormik({
        initialValues: {
            passwordUpdate: "0"
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
                const re = await Promise.all([agent.User.update({
                    ...valuesWithoutMeta,
                    birthDate : formatDateWrapper(valuesWithoutMeta.birthDate),
                    delDt : formatDateTimeWrapper(valuesWithoutMeta.delDt),
                    joiningDate : formatDateWrapper(valuesWithoutMeta.joiningDate),
                    modDt : formatDateTimeWrapper(valuesWithoutMeta.modDt),
                    outDt : formatDateWrapper(valuesWithoutMeta.outDt),
                    regDt : formatDateTimeWrapper(valuesWithoutMeta.regDt),
                    resignationDate : formatDateWrapper(valuesWithoutMeta.resignationDate),
                    passwordChangedAt : formatDateTimeWrapper(valuesWithoutMeta.passwordChangedAt),
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
        if(!confirm("해당 사용자를 삭제 하면 더이상 보이지 않습니다. 진행할까요?")){
            return;
        }

        try {
            if (e) {
                // 예를 들어 이 element 가 a 태그라면 href 의 기능을 항상 막겠다.
                e.preventDefault()
                // 이 버튼을 클릭하였을 때, 상위 element 로의 전파를 막고 이 기능만 실행한다.
                e.stopPropagation()
            }

            const re = await Promise.all([agent.User.delete({
                [PK_NAME] : formik.values[PK_NAME]
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
        // one 이라는 recoil 이 바뀌는 순간
        initializeFormikCommon({
            one, modifiedOne, PK_NAME, customFormikSetModifiedOneFunc: (modifiedOne) => {
                formik.setValues({...formik.initialValues, ...modifiedOne})
            }, customFormikSetOneFunc: (one) => {
                formik.setValues({...formik.initialValues, ...one, delYn: one.delDt ? "Y" : "N", outYn: one.outDt ? "Y" : "N"})
            }
        })

    }, [one])

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

                <Row>
                    <Col lg={12}>
                        <Card className="mb-3">
                            <CardHeader> <DetailHeaderWrapper id={formik.values[PK_NAME]} name={formik.values.name}/></CardHeader>
                            <CardBody>
                                <Form className="mt-4 mb-3">
                                    <FormGroup row>
                                        <Label for="dealerCd" lg={3} className="right">
                                            딜러
                                        </Label>
                                        <Col lg={9}>
                                            <Input
                                                type={"text"}
                                                id="dealerCd"
                                                name="dealerCd"
                                                value={formik.values.dealerNm}
                                                required
                                                readOnly={true}
                                            >
                                    {/*            {one.meta ? one.meta.dealers.map((option) => (
                                                    <option value={option.dealerCd}>
                                                        {option.dealerNm}
                                                    </option>
                                                )) : ""}*/}
                                            </Input>
                                            <FormText color="muted">
                                                딜러는 변경할 수 없습니다.
                                            </FormText>
                                        </Col>
                                    </FormGroup>


                                    <FormGroup row>
                                        <Label for="passwordUpdate" lg={3} className="right">
                                            암호
                                        </Label>
                                        <Col lg={9}>
                                            <FormGroup check>
                                                <Input type="radio"
                                                       className="form-check-input"
                                                       id="passwordUpdateNo"
                                                       name="passwordUpdate"
                                                       value={"0"}
                                                       checked={formik.values.passwordUpdate === "0"}
                                                       onChange={onKeyValueChangeByEventMemoized}
                                                       onBlur={formik.handleBlur}
                                                       disabled={globalReadOnly}
                                                />
                                                <Label for="passwordUpdateNo" check>
                                                    초기화 하지 않음
                                                </Label>
                                            </FormGroup>

                                            <FormGroup check>
                                                <Input
                                                    type="radio"
                                                    className="form-check-input"
                                                    id="passwordUpdateYes"
                                                    name="passwordUpdate"
                                                    value="1"
                                                    checked={formik.values.passwordUpdate === "1"}
                                                    onChange={onKeyValueChangeByEventMemoized}
                                                    onBlur={formik.handleBlur}
                                                    disabled={globalReadOnly}
                                                />
                                                <Label for="passwordUpdateYes" check>
                                                    초기화
                                                </Label>
                                            </FormGroup>

                                            <FormText color="muted">
                                                비밀번호는 랜덤 숫자로 생성됩니다.
                                            </FormText>
                                        </Col>
                                    </FormGroup>

                                    <SettingsUsersCommon formik={formik} globalReadOnly={globalReadOnly}
                                                         onKeyValueChangeByEvent={onKeyValueChangeByEvent}
                                                         onKeyValueChangeByNameValue={onKeyValueChangeByNameValue}
                                                         PK_NAME={PK_NAME}/>


                                    <Flex p="md" justify="center" className={"mt-4 overflow-auto"}>
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
                    </Col>
                </Row>

        </LoadingOverlay>
    );
};

export default React.memo(SettingsUsersUpdate);
