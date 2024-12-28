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
    Col,
    Row, Card, CardHeader
} from 'reactstrap';
import {ActionIcon, Tooltip, Box,  Flex, Menu, Text, Title} from '@mantine/core';

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
import {DeptHierarchyUtil} from "shared/utils/utilities";
import {availableElement, CRUD_COLUMNS, isAuthorized} from "shared/utils/authorization";
import {globalInfoAccessTokenUserInfoSelector} from "shared/recoil/globalInfoState";
import {useGlobalSubMenusReload} from "shared/hooks/useGlobalSubMenusReload";

import KakaoAddress from "shared/components/kakao-address/KakaoAddress";
import {CardBody, Container, ThemeConsumer} from "components";
import CommonDepthSelectBoxes from "./CommonDepthSelectBoxes";
import {useFormikUtils} from "../../../../shared/hooks/useFormiklUtils";
import {ButtonWrapper, DetailHeaderWrapper} from "../../../../shared/components/OptimizedHtmlElements";
import ValidationMessages from "./ValidateMessages";

const PK_NAME = "deptIdx";
const SettingsDeptsCreate = ({refreshAll = () => {}, refreshOne = () => {}, recoilKey}) => {

    const history = useHistory();

    const me = useRecoilValue(globalInfoAccessTokenUserInfoSelector());

    const one = useRecoilValue(boardUpdateOneSelector({recoilKey}));

    const modifiedOne = useRecoilValue(boardUpdateModifiedOneSelector({recoilKey}));
    const setModifiedOne = useSetRecoilState(boardUpdateModifiedOneSelector({recoilKey}));

    const [loading, setLoading] = useState(false);


    const [fileUploading, setFileUploading] = useState(false);
    const [fileError, setFileError] = useState(null);

    const oneValidationSchema = Yup.object().shape({
        deptSort: Yup.number().required('정렬은 숫자만 가능합니다.'),
        selfBizNo: Yup.string()
            .matches(/^[0-9-]*$/, '사업자 등록번호는 공백없이 숫자와 하이픈(-)으로만 이루어져야 합니다.')
            .nullable(), // 비어있어도 유효한 값으로 처리
    });


    const formik = useFormik({
        initialValues: {
            depts: [],
            setDepth: 1
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

    const {
        onKeyValueChangeByEventMemoized, onKeyValueChangeByNameValueMemoized,
        initializeFormikCommon, onKeyValueChangeByEvent, onKeyValueChangeByNameValue
    } = useFormikUtils({formik, oneValidationSchema});


    const setAddress = ({zipcode, address, si, gugun, bname}) => {
        formik.setFieldValue("selfAddr1", address);
        formik.setFieldValue("selfZipcode", zipcode);
        formik.setFieldValue("selfAddrSi", si);
        formik.setFieldValue("selfAddrGugun", gugun);
        formik.setFieldValue("selfAddrBname", bname);
    };

    /* Life Cycle */
    useEffect(() => {
        // one 이라는 recoil 이 바뀌는 순간
        initializeFormikCommon({
            one, modifiedOne, PK_NAME, customFormikSetModifiedOneFunc: (modifiedOne) => {
                formik.setValues({...formik.initialValues, ...modifiedOne})
            }, customFormikSetOneFunc: (one) => {
                formik.setValues({
                    ...formik.initialValues, ...one, dealerCd: me.info.dealerCd,
                    dealerNm: me.info.dealerNm
                })
            }
        })


    }, [one])

    useEffect(() => {
        formik.setTouched({
            ...Object.fromEntries(
                Object.entries(oneValidationSchema.fields).map(([key, value]) => [key, true])
            )
        });

        console.log("Formik 값 변화")
        console.log(formik.values)

      //  formik.setFieldValue('depth', DeptHierarchyUtil.stringToArray(formik.values.upDeptIdx).length + 1);
        setModifiedOne({...formik.values});

    }, [formik.values])


    // Memoized function to check for self-reference and circular reference errors
    const selfReferenceError = useCallback(() => {
        return (
            DeptHierarchyUtil.validateSelfReference(
                formik.values.setDepth,
                formik.values.deptIdx,
                formik.values.parentCd
            ) ||
            DeptHierarchyUtil.validateCircularReference(
                formik.values.depts,
                formik.values.deptIdx,
                formik.values.parentCd,
                formik.values.setDepth
            )
        );
    }, [formik.values.setDepth, formik.values.deptIdx, formik.values.parentCd, formik.values.depts]);

    // Memoized function to get the current depth
    const currentDepth = useCallback(() => {
        return Number(
            DeptHierarchyUtil.getDepthByDeptIdx(
                formik.values.depts,
                formik.values.deptIdx
            )
        ) - 1;
    }, [formik.values.depts, formik.values.deptIdx]);

    // Memoized function to check for depth mismatch warning
    const depthMismatchWarning = useCallback(() => {
        return formik.values.setDepth !== currentDepth();
    }, [formik.values.setDepth, currentDepth]);

    /*
    *
    *   Event Handler
    *
    * */
    // Memoize the upDeptIdxArray calculation
    const upDeptIdxArray = useCallback(() => {
        return DeptHierarchyUtil.getDeptIdxArrayByParentCd(
            formik.values.depts,
            formik.values.parentCd,
            formik.values.deptIdx
        );
    }, [formik.values.depts, formik.values.parentCd, formik.values.deptIdx]);



    const onDepthSelectBoxesChange = useCallback((selectedValue, depth) => {
        const updatedArray = [...upDeptIdxArray()];
        updatedArray[depth - 1] = selectedValue ? Number(selectedValue) : null;

        formik.setFieldValue('parentCd', updatedArray[depth - 1]);
    }, [upDeptIdxArray, formik.setFieldValue]);


    const onFileChange = async (event) => {
        const selectedFile = event.target.files[0];
        if (!selectedFile) return;

        setFileUploading(true);
        setFileError(null);

        const formData = new FormData();
        formData.append('file', selectedFile);

        try {
            const response = await fetch(`${process.env.API_URL}/depts/files/images/${one.deptIdx}`, {
                method: 'POST',
                body: formData,
                headers: {
                    'Accept': 'application/json',
                },
            });

            if (!response.ok) {
                throw new Error('파일 업로드에 실패하였습니다.');
            }

            const result = await response.json();
            formik.setFieldValue('selfImgFname', result.data.imgSrcPath);
            alert('파일이 성공적으로 업로드 되었습니다.');
        } catch (error) {
            console.error(error);
            setFileError(error.message);
        } finally {
            setFileUploading(false);
        }
    };

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

                const re = await Promise.all([agent.Dept.create({
                    ...formik.values
                })]);

                if (re[0].statusCode === 200) {
                    refreshAll()
                    alert("신규 등록 성공.");
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
                <CardHeader><DetailHeaderWrapper/></CardHeader>
                <CardBody>
                    <Form className="mt-4 mb-3">
                        <FormGroup row>
                            <Label for="dealerNm" sm={3} className="right">딜러</Label>
                            <Col sm={9}>
                                <Input
                                    type="text"
                                    name="dealerNm"
                                    id="dealerNm"
                                    value={formik.values.dealerNm}
                                    onChange={onKeyValueChangeByEvent}
                                    onBlur={formik.handleBlur}
                                    readOnly
                                />
                                <FormText className="mute">
                                    딜러는 현재 로그인 된 사용자와 같은 딜러로 자동 세팅됩니다.
                                </FormText>
                            </Col>
                        </FormGroup>

                        <FormGroup row>
                            <Label for="setDepth" sm={3} className="right">단계 설정</Label>
                            <Col sm={9}>
                                <CustomInput
                                    type="select"
                                    name="setDepth"
                                    id="setDepth"
                                    valid={!formik.errors.setDepth && formik.touched.setDepth}
                                    invalid={!!formik.errors.setDepth && formik.touched.setDepth}
                                    value={formik.values.setDepth}
                                    onChange={(e)=>{
                                        const selectedDepth = Number(e.target.value);
                                        formik.setFieldValue('setDepth', selectedDepth);

                                        // Update parentCd similar to onDepthSelectBoxesChange
                                        const updatedArray = [...upDeptIdxArray()];
                                        /*                                        console.log("sdadsa")
                                                                                console.log(selectedDepth)
                                                                                console.log(updatedArray)*/

                                        formik.setFieldValue('parentCd', updatedArray[selectedDepth - 1]);
                                    }}
                                    onBlur={formik.handleBlur}
                                >
                                    {/*<option key="" value={0}>1단계 조직이 됩니다</option>*/}
                                    <option key={1} value={1}>2단계</option>
                                    <option key={2} value={2}>3단계</option>
                                    <option key={3} value={3}>4단계</option>
                                    <option key={4} value={4}>5단계</option>
                                </CustomInput>
                            </Col>
                        </FormGroup>


                        <CommonDepthSelectBoxes deptsObj={formik.values?.depts?.reduce((acc, item) => {
                            const currentDepth = DeptHierarchyUtil.getDepthByDeptIdx(formik.values.depts, item.deptIdx)
                            if (!acc[currentDepth]) {
                                acc[currentDepth] = [];
                            }
                            acc[currentDepth].push(item);
                            return acc;
                        }, {})}
                                                upDeptIdxArr={DeptHierarchyUtil.getDeptIdxArrayByParentCd(formik.values.depts, formik.values.parentCd)}
                                                formik={formik}
                                                onDepthSelectBoxesChange={onDepthSelectBoxesChange}
                                                setDepth={formik.values.setDepth}/>


                        <FormGroup row>
                            <Label for="deptNm" sm={3} className="right">{Number(formik.values.setDepth) + 1} 단계 조직명을
                                정하세요</Label>
                            <Col sm={9}>
                                <Input
                                    type="text"
                                    name="deptNm"
                                    id="deptNm"
                                    valid={!formik.errors.deptNm && formik.touched.deptNm}
                                    invalid={!!formik.errors.deptNm && formik.touched.deptNm}
                                    value={formik.values.deptNm || ""}
                                    onChange={onKeyValueChangeByEvent}
                                    onBlur={formik.handleBlur}
                                />
                            </Col>
                        </FormGroup>


                        <div>
                            <ValidationMessages
                                currentDepth={currentDepth}
                                selfReferenceError={selfReferenceError}
                                depthMismatchWarning={depthMismatchWarning}
                            />
                        </div>

                        <FormGroup row>
                            <Label for="deptSort" sm={3} className="right">정렬</Label>
                            <Col sm={9}>
                                <Input
                                    type="text"
                                    name="deptSort"
                                    id="deptSort"
                                    valid={!formik.errors.deptSort && formik.touched.deptSort}
                                    invalid={!!formik.errors.deptSort && formik.touched.deptSort}
                                    value={formik.values.deptSort || ""}
                                    onChange={onKeyValueChangeByEvent}
                                    onBlur={formik.handleBlur}
                                    size="5"
                                />
                            </Col>
                        </FormGroup>


                        {Number(formik.values.setDepth) === 1 && ( //등록시 현재 접속 아이디, 수정시 등록한 아이디 + 일시
                            <>
                                <FormGroup row>
                                    <Label for="regUserid" sm={3} className="right important">등록자</Label>
                                    <Col sm={9}>
                                        <Input
                                            type="text"
                                            name="regUserid"
                                            id="regUserid"
                                            value={formik.values.regUserid || ""}
                                            readOnly
                                        />
                                        <FormText className="mute">
                                            등록자는 임의로 수정 불가 합니다.
                                        </FormText>
                                    </Col>
                                </FormGroup>

                                <FormGroup row>
                                    <Label for="modUserid" sm={3} className="right important">수정자</Label>
                                    <Col sm={9}>
                                        <Input
                                            type="text"
                                            name="modUserid"
                                            id="modUserid"
                                            value={formik.values.modUserid || ""}
                                            readOnly
                                        />
                                        <FormText className="mute">
                                            수정자는 임의로 수정 불가 합니다.
                                        </FormText>
                                    </Col>
                                </FormGroup>

                                <FormGroup row>
                                    <Label for="selfNm" sm={3} className="right">공급자명</Label>
                                    <Col sm={9}>
                                        <Input
                                            type="text"
                                            name="selfNm"
                                            id="selfNm"
                                            valid={!formik.errors.selfNm && formik.touched.selfNm}
                                            invalid={!!formik.errors.selfNm && formik.touched.selfNm}
                                            value={formik.values.selfNm || ""}
                                            onChange={onKeyValueChangeByEvent}
                                            onBlur={formik.handleBlur}
                                            size="100"
                                        />
                                    </Col>
                                </FormGroup>

                                <FormGroup row>
                                    <Label for="selfChiefNm" sm={3} className="right">대표자명</Label>
                                    <Col sm={9}>
                                        <Input
                                            type="text"
                                            name="selfChiefNm"
                                            id="selfChiefNm"
                                            valid={!formik.errors.selfChiefNm && formik.touched.selfChiefNm}
                                            invalid={!!formik.errors.selfChiefNm && formik.touched.selfChiefNm}
                                            value={formik.values.selfChiefNm || ""}
                                            onChange={onKeyValueChangeByEvent}
                                            onBlur={formik.handleBlur}
                                            size="45"
                                        />
                                    </Col>
                                </FormGroup>

                                <FormGroup row>
                                    <Label for="selfBizNo" sm={3} className="right">사업자번호</Label>
                                    <Col sm={9}>
                                        <Input
                                            type="text"
                                            name="selfBizNo"
                                            id="selfBizNo"
                                            valid={!formik.errors.selfBizNo && formik.touched.selfBizNo}
                                            invalid={!!formik.errors.selfBizNo && formik.touched.selfBizNo}
                                            value={formik.values.selfBizNo || ""}
                                            onChange={onKeyValueChangeByEvent}
                                            onBlur={formik.handleBlur}
                                            size="50"
                                        />
                                        <FormFeedback className={'ml-3'}>{formik.errors.selfBizNo}</FormFeedback>
                                    </Col>
                                </FormGroup>

                                <FormGroup row>
                                    <Label for="selfRegId" sm={3} className="right">종사업자번호</Label>
                                    <Col sm={9}>
                                        <Input
                                            type="text"
                                            name="selfRegId"
                                            id="selfRegId"
                                            valid={!formik.errors.selfRegId && formik.touched.selfRegId}
                                            invalid={!!formik.errors.selfRegId && formik.touched.selfRegId}
                                            value={formik.values.selfRegId || ""}
                                            onChange={onKeyValueChangeByEvent}
                                            onBlur={formik.handleBlur}
                                            size="100"
                                        />
                                    </Col>
                                </FormGroup>

                                <FormGroup row>
                                    <Label for="selfUptae" sm={3} className="right important">업태/종목</Label>
                                    <Col sm={9} className="d-flex">
                                        <Input
                                            type="text"
                                            name="selfUptae"
                                            id="selfUptae"
                                            valid={!formik.errors.selfUptae && formik.touched.selfUptae}
                                            invalid={!!formik.errors.selfUptae && formik.touched.selfUptae}
                                            value={formik.values.selfUptae || ""}
                                            onChange={onKeyValueChangeByEvent}
                                            onBlur={formik.handleBlur}
                                            size="30"
                                        />
                                        <Input
                                            type="text"
                                            name="selfUpjong"
                                            id="selfUpjong"
                                            valid={!formik.errors.selfUpjong && formik.touched.selfUpjong}
                                            invalid={!!formik.errors.selfUpjong && formik.touched.selfUpjong}
                                            value={formik.values.selfUpjong || ""}
                                            onChange={onKeyValueChangeByEvent}
                                            onBlur={formik.handleBlur}
                                            size="50"
                                        />
                                    </Col>
                                </FormGroup>


                                <Card>
                                    <CardBody>
                                        <FormGroup row>
                                            <KakaoAddress
                                                setAddress={setAddress}
                                            ></KakaoAddress>
                                        </FormGroup>

                                        <FormGroup row>
                                            <Label for="selfZipcode" lg={3}
                                                   className="right important">Zip Code</Label>
                                            <Col lg={9}>
                                                <Input
                                                    type="text"
                                                    name="selfZipcode"
                                                    id="selfZipcode"
                                                    value={formik.values.selfZipcode}
                                                    readOnly={true}
                                                />
                                            </Col>
                                        </FormGroup>
                                        <FormGroup row>
                                            <Label for="selfAddr1" lg={3} className="right important">주소</Label>
                                            <Col lg={9}>
                                                <Input
                                                    type="text"
                                                    name="selfAddr1"
                                                    id="selfAddr1"
                                                    value={formik.values.selfAddr1}
                                                    readOnly={true}
                                                />
                                            </Col>
                                        </FormGroup>
                                        <FormGroup row>
                                            <Label for="selfAddr2" lg={3} className="right important">
                                                상세주소 직접 입력
                                            </Label>
                                            <Col lg={9}>
                                                <Input
                                                    type="text"
                                                    name="selfAddr2"
                                                    id="selfAddr2"
                                                    valid={!formik.errors.selfAddr2 && formik.touched.selfAddr2}
                                                    invalid={!!formik.errors.selfAddr2 && formik.touched.selfAddr2}
                                                    value={formik.values.selfAddr2 || ""}
                                                    onChange={onKeyValueChangeByEvent}
                                                    onBlur={formik.handleBlur}
                                                />
                                            </Col>

                                        </FormGroup>
                                    </CardBody>
                                </Card>


                                {/* 파일 업로드 FormGroup */}
                                <FormGroup row className={"mt-3"}>
                                    <Label for="selfImgFname" sm={3}>사업장직인</Label>
                                    <Col sm={9}>
                                        <CustomInput
                                            type="file"
                                            id="selfImgFname"
                                            name="selfImgFname"
                                            onChange={onFileChange}
                                            disabled={fileUploading}
                                            label={formik.values.selfImgFname ? formik.values.selfImgFname : "파일을 끌어오세요."}
                                            onBlur={formik.handleBlur}
                                        />
                                        <FormText color="muted">
                                            ※파일은 1개, 10MBtype 까지 가능합니다.
                                        </FormText>
                                        {fileUploading && <FormText color="info">업로드 중...</FormText>}
                                        {fileError && <FormText color="danger">{fileError}</FormText>}
                                        <FormFeedback className="ml-3">{formik.errors.selfImgFname}</FormFeedback>
                                    </Col>
                                </FormGroup>

                                {/* 이미지 표시 FormGroup */}
                                {formik.values.selfImgFname && (
                                    <FormGroup row>
                                        <Label for="uploadedImage" sm={3}>직인 이미지</Label>
                                        <Col sm={9}>
                                            <img
                                                src={`${formik.values.selfImgFname}`}
                                                alt="Uploaded"
                                                style={{maxWidth: '100px', maxHeight: '100px', display: 'block'}}
                                            />
                                        </Col>
                                    </FormGroup>
                                )}
                            </>
                        )}
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
                                    crudColumn={CRUD_COLUMNS.CREATE}
                                />
                                <ButtonWrapper
                                    color={"primary"}
                                    btnText={"등록"}
                                    formik={formik}
                                    handleClick={onSubmitClick}
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
}
export default React.memo(SettingsDeptsCreate);