import React, {useCallback, useState, useEffect} from 'react'
import {Link} from 'react-router-dom';
import {useHistory} from "react-router-dom";

import {
    Form,
    FormFeedback,
    FormGroup,
    FormText,
    Input,
    CustomInput,
    Button,
    Label,
    ThemeConsumer,
    Container,
    EmptyLayout,
    Col,
    Row, Card, CardHeader, CardBody

} from 'components';
import {ActionIcon, Tooltip, Box,  Flex, Menu, Text, Title} from '@mantine/core';
import agent from "shared/api/agent";
import {useFormik, FormikProps} from 'formik';
import * as Yup from 'yup';

import {renderError} from "shared/utils/CommonErrorHandler";

import LoadingOverlay from 'react-loading-overlay'
import ClockLoader from 'react-spinners/ClockLoader';
import {useRecoilValue, useResetRecoilState, useSetRecoilState} from "recoil";
import {
    boardUpdateOneSelector,
    boardUpdateModifiedOneSelector, boardUpdateResetModifiedOneSelector
} from "shared/recoil/board/boardUpdateState";

import {availableElement, CRUD_COLUMNS, isAuthorized} from "../../../../../shared/utils/authorization";
import {
    globalInfoAccessTokenUserInfoSelector,
    globalInfoAuthenticatedRoutesSelector
} from "../../../../../shared/recoil/globalInfoState";
import {useGlobalSubMenusReload} from "../../../../../shared/hooks/useGlobalSubMenusReload";

import {useFormikUtils} from "../../../../../shared/hooks/useFormiklUtils";
import {ButtonWrapper, DetailHeaderWrapper} from "../../../../../shared/components/OptimizedHtmlElements";

const PK_NAME = "subMenuIdx";
const SettingsMenusSubsUpdate = ({
                                     refreshAll = () => {
                                     }, refreshOne = () => {
    }, recoilKey
                                 }) => {

    const history = useHistory();

    const me = useRecoilValue(globalInfoAccessTokenUserInfoSelector());
    const globalReadOnly = !isAuthorized({recoilKey, accessTokenUserInfo: me, CRUD_COLUMN: CRUD_COLUMNS.UPDATE})

    const one = useRecoilValue(boardUpdateOneSelector({recoilKey}));

    const modifiedOne = useRecoilValue(boardUpdateModifiedOneSelector({recoilKey}));
    const setModifiedOne = useSetRecoilState(boardUpdateModifiedOneSelector({recoilKey}));

    const [loading, setLoading] = useState(false);

    const {reloadDynamicRoutes} = useGlobalSubMenusReload();

    const oneValidationSchema = Yup.object().shape({
        subMenuNm: Yup.string()
            //.subMenuNm('올바르지 않은 형식의 제목입니다.')
            .min(2, `서브 메뉴명는 최소 ${2} 글자입니다.`)
            .required('서브 메뉴명 입력은 필수 입니다.'),
        subMenuSort: Yup.number()
            .typeError("숫자만 입력할 수 있습니다.")
            .nullable()
            .transform((_, val) => (val !== "" ? Number(val) : null)),
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


    /* Life Cycle */
    useEffect(() => {
        initializeFormikCommon({
            one, modifiedOne, PK_NAME, customFormikSetModifiedOneFunc: (modifiedOne) => {
                formik.setValues({...formik.initialValues, ...modifiedOne})
            }, customFormikSetOneFunc: (one) => {
                formik.setValues({...formik.initialValues, ...one})
            }
        })
    }, [one])

    useEffect(() => {
        setModifiedOne({...formik.values});
    }, [formik.values])


    /*
    *
    *   Event Handler
    *
    * */
    const onSubmitClick = async (e) => {

        try {
            if (e) {
                // 예를 들어 이 element 가 a 태그라면 href 의 기능을 항상 막겠다.
                e.preventDefault()
                // 이 버튼을 클릭하였을 때, 상위 element 로의 전파를 막고 이 기능만 실행한다.
                e.stopPropagation()
            }

            if (formik.isValid && formik.dirty) {

                setLoading(true);

                const re = await Promise.all([agent.Menu.updateSub({
                    id: formik.values[PK_NAME], subMenuNm: formik.values.subMenuNm,
                    subMenuSort: formik.values.subMenuSort,
                    subMenuPath: formik.values.subMenuPath
                })]);

                if (re[0].statusCode === 200) {
                    refreshAll()
                    reloadDynamicRoutes()
                    alert("업데이트 완료.")
                } else {
                    renderError({errorObj: re[0], formik});
                }
            }
        } finally {
            //resetModifiedOne();
            setLoading(false);
        }
    }

    return (
        <LoadingOverlay
            spinner={<ClockLoader color="#ffffff" size={20}/>}
            active={loading}
        >
            <Card className="mb-3">
                <CardHeader>
                    <DetailHeaderWrapper id={formik.values[PK_NAME]} name={formik.values.subMenuNm}/>
                </CardHeader>
                <CardBody>
                    <Form className="mt-3 mb-3">
                        <Row>
                            <Col lg={6}>
                                <FormGroup>
                                    <Label for="subMenuNm">
                                        서브 메뉴 명
                                    </Label>
                                    <Input id="subMenuNm" name="subMenuNm" type="text"
                                           valid={!formik.errors.subMenuNm && formik.touched.subMenuNm}
                                           invalid={!!formik.errors.subMenuNm && formik.touched.subMenuNm}
                                           value={formik.values.subMenuNm}
                                           onChange={onKeyValueChangeByEvent}
                                           onBlur={formik.handleBlur}
                                           readOnly={globalReadOnly}
                                    />
                                    <FormFeedback className={'ml-3'}>{formik.errors.subMenuNm}</FormFeedback>
                                    {/*                        <FormText color="muted">
                            계정 공유에 대한 책임은 본인에게 있습니다.
                        </FormText>*/}
                                </FormGroup>
                            </Col>
                            <Col lg={6}>
                                <FormGroup>
                                    <Label for="subMenuPath">
                                        서브 메뉴 순서 (소팅)
                                    </Label>
                                    <Input id="subMenuSort" name="subMenuSort" type="text"
                                           valid={!formik.errors.subMenuSort && formik.touched.subMenuSort}
                                           invalid={!!formik.errors.subMenuSort && formik.touched.subMenuSort}
                                           value={formik.values.subMenuSort}
                                           onChange={onKeyValueChangeByEvent}
                                           onBlur={formik.handleBlur}
                                           readOnly={globalReadOnly}
                                    />
                                    <FormFeedback className={"ml-3"}>{formik.errors.subMenuSort}</FormFeedback>
                                </FormGroup>
                            </Col>
                        </Row>
                        <Row>
                            <Col lg={12}>
                                <FormGroup>
                                    <Label for="subMenuPath">
                                        서브 메뉴 주소
                                    </Label>
                                    <Input id="subMenuPath" name="subMenuPath" type="text" placeholder="서브 메뉴 주소"
                                           value={formik.values.subMenuPath}
                                           readOnly={true}
                                    />
                                    <FormText color="muted">
                                        서브 메뉴 주소 변경은 서버 재시작을 필요로 하는 위험한 변경사항이기 때문에 현재 '읽기 전용' 으로 세팅되어 있습니다.
                                    </FormText>
                                </FormGroup>
                            </Col>
                        </Row>

                        <Flex p="md" justify="center" className={"mt-4"}>
                            <Flex gap="lg">
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
                                    color={"primary"}
                                    formik={formik}
                                    btnText={"등록"}
                                    handleClick={onSubmitClick}
                                    me={me}
                                    recoilKey={recoilKey}
                                    crudColumn={CRUD_COLUMNS.UPDATE}
                                />
                            </Flex>
                        </Flex>
                    </Form>
                </CardBody>
            </Card>
        </LoadingOverlay>
    );
}

export default React.memo(SettingsMenusSubsUpdate);
