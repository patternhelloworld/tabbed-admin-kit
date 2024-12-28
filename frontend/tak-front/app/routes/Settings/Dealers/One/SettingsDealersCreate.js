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
import {Container, ThemeConsumer} from "components";

import "../../../../assets/images/date-picker/react-datepicker.css";

import KakaoAddress from "shared/components/kakao-address/KakaoAddress";
import {CRUD_COLUMNS, isAuthorized} from "shared/utils/authorization";
import {globalInfoAccessTokenUserInfoSelector} from "shared/recoil/globalInfoState";
import {ButtonWrapper, DetailHeaderWrapper} from "shared/components/OptimizedHtmlElements";
import {useFormikUtils} from "shared/hooks/useFormiklUtils";
import {Flex} from "@mantine/core";



const PK_NAME = "dealerCd";
const SettingsDealersCreate = ({refreshAll = ()=>{}, refreshOne = ()=>{}, recoilKey}) => {

    const me = useRecoilValue(globalInfoAccessTokenUserInfoSelector());

    const one = useRecoilValue(boardUpdateOneSelector({recoilKey}));

    const modifiedOne = useRecoilValue(boardUpdateModifiedOneSelector({recoilKey}));
    const setModifiedOne = useSetRecoilState(boardUpdateModifiedOneSelector({recoilKey}));

    const [loading, setLoading] = useState(false);

    const oneValidationSchema = Yup.object().shape({
        dealerNm: Yup.string()
            .required('이름은 필수입니다'),
        shortNm: Yup.string()
            .min(1).max(20)
            .required('딜러약어는 필수입니다'),
    });

    const formik = useFormik({
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
        validateOnChange: true,
        validateOnBlur: true
    });

    const { onKeyValueChangeByEventMemoized, onKeyValueChangeByNameValueMemoized,
        initializeFormikCommon, onKeyValueChangeByEvent, onKeyValueChangeByNameValue } = useFormikUtils({  formik, oneValidationSchema  });

    const setAddress = ({zipcode, address, si, gugun, bname}) => {
        formik.setFieldValue("addr1", address);
        formik.setFieldValue("zipcode", zipcode);
        formik.setFieldValue("addrSi", si);
        formik.setFieldValue("addrGugun", gugun);
        formik.setFieldValue("addrBname", bname);
    };

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

                const re = await Promise.all([agent.Dealer.create({
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
                            <Label for="dealerGb" sm={2} className="right">
                                딜러구분
                            </Label>
                            <Col sm={10}>
                                <CustomInput
                                    type="radio"
                                    id="dealerGbD"
                                    name="dealerGb"
                                    label="딜러"
                                    inline
                                    value={"D"}
                                    checked={formik.values.dealerGb === "D"}
                                    onChange={onKeyValueChangeByEvent}
                                />
                                <CustomInput
                                    type="radio"
                                    id="dealerGbP"
                                    name="dealerGb"
                                    label="pdi"
                                    inline
                                    value={"P"}
                                    checked={formik.values.dealerGb === "P"}
                                    onChange={onKeyValueChangeByEvent}
                                />
                                <CustomInput
                                    type="radio"
                                    id="dealerGbS"
                                    name="dealerGb"
                                    label="전시장"
                                    inline
                                    value={"S"}
                                    checked={formik.values.dealerGb === "S"}
                                    onChange={onKeyValueChangeByEvent}
                                />
                                <CustomInput
                                    type="radio"
                                    id="dealerGbA"
                                    name="dealerGb"
                                    label="대리점"
                                    inline
                                    value={"A"}
                                    checked={formik.values.dealerGb === "A"}
                                    onChange={onKeyValueChangeByEvent}
                                />
                                <CustomInput
                                    type="radio"
                                    id="dealerGbC"
                                    name="dealerGb"
                                    label="일반거래처"
                                    inline
                                    value={"C"}
                                    checked={formik.values.dealerGb === "C"}
                                    onChange={onKeyValueChangeByEvent}
                                />
                                <CustomInput
                                    type="radio"
                                    id="dealerGbH"
                                    name="dealerGb"
                                    label="본사부품"
                                    inline
                                    value={"H"}
                                    checked={formik.values.dealerGb === "H"}
                                    onChange={onKeyValueChangeByEvent}
                                />
                                <FormFeedback className={'ml-3'}>{formik.errors.dealerGb}</FormFeedback>
                            </Col>
                        </FormGroup>

                        <FormGroup row>
                            <Label for="isMain" sm={2} className="right">
                                본사유무
                            </Label>
                            <Col sm={10}>
                                <CustomInput
                                    type="radio"
                                    id="isMainY"
                                    name="isMain"
                                    label="본사"
                                    value={"Y"}
                                    inline
                                    checked={formik.values.isMain === "Y"}
                                    onChange={onKeyValueChangeByEvent}
                                />
                                <CustomInput
                                    type="radio"
                                    id="isMainN"
                                    name="isMain"
                                    label="딜러"
                                    value={"N"}
                                    inline
                                    checked={formik.values.isMain === "N"}
                                    onChange={onKeyValueChangeByEvent}
                                />
                                <FormFeedback className={'ml-3'}>{formik.errors.isMain}</FormFeedback>
                            </Col>
                        </FormGroup>

                        <FormGroup row>
                            <Label for="dealerNm" sm={2} className="right">딜러명</Label>
                            <Col sm={10}>
                                <Input
                                    type="text"
                                    name="dealerNm"
                                    id="dealerNm"
                                    valid={!formik.errors.dealerNm && formik.touched.dealerNm}
                                    invalid={!!formik.errors.dealerNm && formik.touched.dealerNm}
                                    value={formik.values.dealerNm}
                                    onChange={onKeyValueChangeByEvent}
                                    onBlur={formik.handleBlur}
                                    size="40"
                                />
                            </Col>
                        </FormGroup>

                        <FormGroup row>
                            <Label for="shortNm" sm={2} className="right">딜러명 약어</Label>
                            <Col sm={10}>
                                <Input
                                    type="text"
                                    name="shortNm"
                                    id="shortNm"
                                    valid={!formik.errors.shortNm && formik.touched.shortNm}
                                    invalid={!!formik.errors.shortNm && formik.touched.shortNm}
                                    value={formik.values.shortNm}
                                    onChange={onKeyValueChangeByEvent}
                                    onBlur={formik.handleBlur}
                                    size="10"
                                />
                            </Col>
                        </FormGroup>

                        <FormGroup row>
                            <Label for="tel" sm={2} className="right">대표번호</Label>
                            <Col sm={10}>
                                <Input
                                    type="text"
                                    name="tel"
                                    id="tel"
                                    valid={!formik.errors.tel && formik.touched.tel}
                                    invalid={!!formik.errors.tel && formik.touched.tel}
                                    value={formik.values.tel}
                                    onChange={onKeyValueChangeByEvent}
                                    onBlur={formik.handleBlur}

                                    size="15"
                                />
                            </Col>
                        </FormGroup>

                        <FormGroup row>
                            <Label for="hphone" sm={2} className="right">전화번호</Label>
                            <Col sm={10}>
                                <Input
                                    type="text"
                                    name="hphone"
                                    id="hphone"
                                    valid={!formik.errors.hphone && formik.touched.hphone}
                                    invalid={!!formik.errors.hphone && formik.touched.hphone}
                                    value={formik.values.hphone}
                                    onChange={onKeyValueChangeByEvent}
                                    onBlur={formik.handleBlur}

                                    size="15"
                                />
                            </Col>
                        </FormGroup>

                        <FormGroup row>
                            <Label for="fax" sm={2} className="right">팩스번호</Label>
                            <Col sm={10}>
                                <Input
                                    type="text"
                                    name="fax"
                                    id="fax"
                                    valid={!formik.errors.fax && formik.touched.fax}
                                    invalid={!!formik.errors.fax && formik.touched.fax}
                                    value={formik.values.fax}
                                    onChange={onKeyValueChangeByEvent}
                                    onBlur={formik.handleBlur}
                                    size="20"
                                />
                            </Col>
                        </FormGroup>

                        <FormGroup row>
                            <Label for="chiefNm" sm={2} className="right">대표자</Label>
                            <Col sm={10}>
                                <Input
                                    type="text"
                                    name="chiefNm"
                                    id="chiefNm"
                                    valid={!formik.errors.chiefNm && formik.touched.chiefNm}
                                    invalid={!!formik.errors.chiefNm && formik.touched.chiefNm}
                                    value={formik.values.chiefNm}
                                    onChange={onKeyValueChangeByEvent}
                                    onBlur={formik.handleBlur}
                                    size="20"
                                />
                            </Col>
                        </FormGroup>

                        <FormGroup row>
                            <Label for="email" sm={2} className="right">E-Mail</Label>
                            <Col sm={10}>
                                <Input
                                    type="text"
                                    name="email"
                                    id="email"
                                    valid={!formik.errors.email && formik.touched.email}
                                    invalid={!!formik.errors.email && formik.touched.email}
                                    value={formik.values.email}
                                    onChange={onKeyValueChangeByEvent}
                                    onBlur={formik.handleBlur}
                                    size="40"
                                />
                            </Col>
                        </FormGroup>

                        <FormGroup row>
                            <Label for="uptae" sm={2} className="right important">업태/업종</Label>
                            <Col sm={10} className="d-flex">
                                <Input
                                    type="text"
                                    name="uptae"
                                    id="uptae"
                                    valid={!formik.errors.uptae && formik.touched.uptae}
                                    invalid={!!formik.errors.uptae && formik.touched.uptae}
                                    value={formik.values.uptae}
                                    onChange={onKeyValueChangeByEvent}
                                    onBlur={formik.handleBlur}

                                    size="20"
                                />
                                <Input
                                    type="text"
                                    name="upjong"
                                    id="upjong"
                                    valid={!formik.errors.upjong && formik.touched.upjong}
                                    invalid={!!formik.errors.upjong && formik.touched.upjong}
                                    value={formik.values.upjong}
                                    onChange={onKeyValueChangeByEvent}
                                    onBlur={formik.handleBlur}

                                    size="20"
                                />
                            </Col>
                        </FormGroup>

                        <FormGroup row>
                            <Label for="zipcode" sm={2} className="right important">사업자주소</Label>
                            <Col sm={10} className="d-flex">
                                <Input
                                    type="text"
                                    name="zipcode"
                                    id="zipcode"
                                    valid={!formik.errors.zipcode && formik.touched.zipcode}
                                    invalid={!!formik.errors.zipcode && formik.touched.zipcode}
                                    value={formik.values.zipcode}
                                    onChange={onKeyValueChangeByEvent}
                                    onBlur={formik.handleBlur}
                                />
                                <KakaoAddress
                                    setAddress={setAddress}
                                ></KakaoAddress>
                                <Input
                                    type="text"
                                    name="addr1"
                                    id="addr1"
                                    valid={!formik.errors.addr1 && formik.touched.addr1}
                                    invalid={!!formik.errors.addr1 && formik.touched.addr1}
                                    value={formik.values.addr1}
                                    onChange={onKeyValueChangeByEvent}
                                    onBlur={formik.handleBlur}
                                />
                                <Input
                                    type="text"
                                    name="addr2"
                                    id="addr2"
                                    valid={!formik.errors.addr2 && formik.touched.addr2}
                                    invalid={!!formik.errors.addr2 && formik.touched.addr2}
                                    value={formik.values.addr2}
                                    onChange={onKeyValueChangeByEvent}
                                    onBlur={formik.handleBlur}
                                />
                            </Col>
                        </FormGroup>

                        <FormGroup row>
                            <Label for="pdiRequestCount" sm={2} className="right">PDI 요청대수</Label>
                            <Col sm={10}>
                                <Input
                                    type="text"
                                    name="pdiRequestCount"
                                    id="pdiRequestCount"
                                    valid={!formik.errors.pdiRequestCount && formik.touched.pdiRequestCount}
                                    invalid={!!formik.errors.pdiRequestCount && formik.touched.pdiRequestCount}
                                    value={formik.values.pdiRequestCount}
                                    onChange={onKeyValueChangeByEvent}
                                    onBlur={formik.handleBlur}
                                    size="30"
                                />
                            </Col>
                        </FormGroup>

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

export default React.memo(SettingsDealersCreate);
