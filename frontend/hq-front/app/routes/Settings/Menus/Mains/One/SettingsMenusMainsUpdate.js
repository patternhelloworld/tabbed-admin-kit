import React, {useCallback, useState, useEffect} from 'react'
import {Link} from 'react-router-dom';
import {useHistory} from "react-router-dom";

import {
    Form,
    FormFeedback,
    FormGroup,
    FormText,
    Input,
    Button,
    Label,
    ThemeConsumer,
    Col,
    Row, Card, CardHeader, CardBody
} from 'components';
import {ActionIcon, Tooltip, Box, Flex, Menu, Text, Title} from '@mantine/core';
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
import {availableElement, CRUD_COLUMNS, isAuthorized} from "../../../../../shared/utils/authorization";
import {globalInfoAccessTokenUserInfoSelector} from "../../../../../shared/recoil/globalInfoState";
import {useGlobalSubMenusReload} from "../../../../../shared/hooks/useGlobalSubMenusReload";

import {useFormikUtils} from "../../../../../shared/hooks/useFormiklUtils";
import {ButtonWrapper, DetailHeaderWrapper} from "../../../../../shared/components/OptimizedHtmlElements";

const PK_NAME = "mainMenuIdx";
const SettingsMenusMainsUpdate = ({
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
        mainMenuNm: Yup.string()
            //.mainMenuNm('올바르지 않은 형식의 제목입니다.')
            .min(2, `메인 메뉴명는 최소 ${2} 글자입니다.`)
            .required('메인 메뉴명 입력은 필수 입니다.'),
        mainMenuSort: Yup.number()
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

                const re = await Promise.all([agent.Menu.update({
                    id: formik.values[PK_NAME], mainMenuNm: formik.values.mainMenuNm,
                    mainMenuSort: formik.values.mainMenuSort,
                    mainMenuPath: formik.values.mainMenuPath
                })]);

                if (re[0].statusCode === 200) {
                    // setOne(re[0].data);
                    refreshAll()
                    reloadDynamicRoutes()
                    alert("업데이트 완료.")
                } else {
                    renderError({errorObj: re[0], formik});
                }
            }
        } finally {
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
                    <DetailHeaderWrapper id={formik.values[PK_NAME]} name={formik.values.mainMenuNm}/>
                </CardHeader>
                <CardBody>
                    <Form className="mt-3 mb-3">
                        <Row>
                            <Col lg={6}>
                                <FormGroup>
                                    <Label for="mainMenuNm">
                                        메인 메뉴 명
                                    </Label>
                                    <Input id="mainMenuNm" name="mainMenuNm" type="text"
                                           valid={!formik.errors.mainMenuNm && formik.touched.mainMenuNm}
                                           invalid={!!formik.errors.mainMenuNm && formik.touched.mainMenuNm}
                                           value={formik.values.mainMenuNm}
                                           onChange={onKeyValueChangeByEvent}
                                           onBlur={formik.handleBlur}
                                           readOnly={globalReadOnly}
                                    />
                                    <FormFeedback className={'ml-3'}>{formik.errors.mainMenuNm}</FormFeedback>
                                    {/*                        <FormText color="muted">
                            계정 공유에 대한 책임은 본인에게 있습니다.
                        </FormText>*/}
                                </FormGroup>
                            </Col>
                            <Col lg={6}>
                                <FormGroup>
                                    <Label for="mainMenuSort">
                                        메인 메뉴 순서 (소팅)
                                    </Label>
                                    <Input id="mainMenuSort" name="mainMenuSort" type="text"
                                           valid={!formik.errors.mainMenuSort && formik.touched.mainMenuSort}
                                           invalid={!!formik.errors.mainMenuSort && formik.touched.mainMenuSort}
                                           value={formik.values.mainMenuSort}
                                           onChange={onKeyValueChangeByEvent}
                                           onBlur={formik.handleBlur}
                                           readOnly={globalReadOnly}
                                    />
                                    <FormFeedback className={"ml-3"}>{formik.errors.mainMenuSort}</FormFeedback>
                                </FormGroup>
                            </Col>
                        </Row>
                        <Row>
                            <Col lg={12}>
                                <FormGroup>
                                    <Label for="mainMenuPath">
                                        메인 메뉴 주소
                                    </Label>
                                    <Input id="mainMenuPath" name="mainMenuPath" type="text" placeholder="메인 메뉴 주소"
                                           value={formik.values.mainMenuPath}
                                           readOnly={true}
                                    />
                                    <FormText color="muted">
                                        메인 메뉴 주소 변경은 서버 재시작을 필요로 하는 위험한 변경사항이기 때문에 현재 '읽기 전용' 으로 세팅되어 있습니다.
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
                                    btnText={"등록"}
                                    formik={formik}
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

export default React.memo(SettingsMenusMainsUpdate);
