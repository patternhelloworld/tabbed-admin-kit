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
    EmptyLayout,
    ThemeConsumer,
    Col,
    Row
} from './../../../components';

import agent from "../../../shared/api/agent";
import {useFormik, FormikProps} from 'formik';
import * as Yup from 'yup'

import {renderError} from "../../../shared/utils/CommonErrorHandler";

import LoadingOverlay from 'react-loading-overlay'
import ClockLoader from 'react-spinners/ClockLoader';
import {useRecoilValue, useResetRecoilState, useSetRecoilState} from "recoil";
import {
    boardUpdateOneSelector,
    boardUpdateModifiedOneSelector
} from "../../../shared/recoil/board/boardUpdateState";
import {isValidObject} from "../../../shared/utils/utilities";


const SampleEdit = ({ refetch = undefined, recoilKey = "issues"}) => {

    const history = useHistory();

    const one = useRecoilValue(boardUpdateOneSelector({ recoilKey}));
    const setOne = useSetRecoilState(boardUpdateOneSelector({ recoilKey }));

    const modifiedOne = useRecoilValue(boardUpdateModifiedOneSelector({ recoilKey }));
    const setModifiedOne = useSetRecoilState(boardUpdateModifiedOneSelector({ recoilKey }));
    const resetModifiedOne = useResetRecoilState(boardUpdateModifiedOneSelector({ recoilKey}));

    const [loading, setLoading] = useState(false);

    const oneValidationSchema = Yup.object().shape({
        title: Yup.string()
            //.title('올바르지 않은 형식의 제목입니다.')
            .required('제목 입력은 필수 입니다.'),
        projectName: Yup.string()
            .min(4, `프로젝트명는 최소 ${4} 글자입니다.`)
            /*      .matches(/(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{6,}/, 'ProjectName must contain: numbers, uppercase and lowercase letters\n')*/
            .required('프로젝트명는 필수 입니다.')
    });

    const formik = useFormik({
        initialValues: {
            title: one.title,
            projectName: one.projectName,
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

    const initializeFormik = () => {

        if(isValidObject(one)) {
            console.log("8 : one 이라는 객체에 값이 없으면 무시")
            // modifiedOne : 수정한 one 인데, 이 화면에서 나가질 때만 저장.
            if(isValidObject(modifiedOne)){
                if(JSON.stringify(modifiedOne) !== JSON.stringify(one)
                    && modifiedOne.id === one.id){

                    formik.setValues(modifiedOne)
                    console.log("9 : 수정한 one 과 원래받은 one 이 다르고 && id 가 서로 같을때 ")

                }else{
                    formik.setValues(one)
                    console.log("10")
                }
            }else{
                formik.setValues(one)
                console.log("11")
            }
        }
    }


    /* Life Cycle */
    useEffect(()=>{
        // one 이라는 recoil 이 바뀌는 순간
        initializeFormik();
    },[one])
    
    useEffect(() => {
        return () => {
            // 이 화면을 나가는 순간 실행됨
            setModifiedOne(formik.values);
        }
    })


    /*
    *
    *   Event Handler
    *
    * */

    const onKeyValueChangeByEvent = (e) => {
        formik.setFieldTouched(e.target.name);
        formik.handleChange(e);
    }
    const onKeyValueChangeByNameValue = (name, value) => {
        formik.setFieldTouched(name);
        formik.setFieldValue(name, value);
    }

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

                const re = await Promise.all([agent.Issue.update({
                    id : formik.values.id, title : formik.values.title,
                    projectId: formik.values.projectId,
                    projectName: formik.values.projectName})]);

               if (re[0].statusCode === 200) {
                   if(refetch){
                       refetch();
                   }
               } else {
                   renderError({errorObj: re[0], formik});
               }
            }
        } finally {
            resetModifiedOne();
            setLoading(false);
        }
    }

    const onSubmitKeyDown = (e) => {
        if (e.keyCode === 13) {
            onSubmitClick(e);
        }
    }

    return (
        <LoadingOverlay
            spinner={<ClockLoader color="#ffffff" size={20}/>}
            active={loading}
        >
            <Form className="mt-3 mb-3">
                <Row>
                    <Col lg={6}>
                        <FormGroup>
                            <Label for="title">
                                제목
                            </Label>
                            <Input id="title" name="title" type="text" placeholder="Title"
                                   valid={!formik.errors.title && formik.touched.title}
                                   invalid={!!formik.errors.title && formik.touched.title}
                                   value={formik.values.title}
                                   onChange={onKeyValueChangeByEvent}
                                   onBlur={formik.handleBlur}
                                   onKeyDown={onSubmitKeyDown}
                                   required
                            />
                            <FormFeedback className={'ml-3'}>{formik.errors.title}</FormFeedback>
                            {/*                        <FormText color="muted">
                            계정 공유에 대한 책임은 본인에게 있습니다.
                        </FormText>*/}
                        </FormGroup>
                    </Col>
                    <Col lg={6}>
                        <FormGroup>
                            <Label for="projectName">
                                프로젝트명
                            </Label>
                            <Input id="projectName" name="projectName" type="text" placeholder="프로젝트명"
                                   valid={!formik.errors.projectName && formik.touched.projectName}
                                   invalid={!!formik.errors.projectName && formik.touched.projectName}
                                   value={formik.values.projectName}
                                   onChange={onKeyValueChangeByEvent}
                                   onBlur={formik.handleBlur}
                                   onKeyDown={onSubmitKeyDown}
                                   required
                            />
                            <FormFeedback className={"ml-3"}>{formik.errors.projectName}</FormFeedback>
                        </FormGroup>
                    </Col>
                </Row>
                {/*                    <FormGroup>
                        <CustomInput type="checkbox" id="rememberPassword" label="Remember ProjectName" inline />
                    </FormGroup>*/}
                <Row className={"d-flex justify-content-end"}>
                    <Col lg={4}>
                        <ThemeConsumer>
                            {
                                ({color}) => (
                                    <Button color={color} block tag={Link} onClick={onSubmitClick} size="sm"
                                            disabled={!(formik.isValid && formik.dirty)}>
                                        {!formik.dirty
                                            ? "수정 데이터 없음"
                                            : !formik.isValid
                                                ? "데이터 확인 필요"
                                                : "제출 가능"}
                                    </Button>
                                )
                            }
                        </ThemeConsumer>
                    </Col>
                </Row>
            </Form>
            { /* END Form */}
            { /* START Bottom Links */}
            {/*                <div className="d-flex mb-5">
                    <Link to="/pages/forgotpassword" className="text-decoration-none">
                        Forgot ProjectName
                    </Link>
                    <Link to="/pages/register" className="ml-auto text-decoration-none">
                        Register
                    </Link>
                </div>*/}
            { /* END Bottom Links */}
            { /* START Footer */}
            { /* END Footer */}
        </LoadingOverlay>
    );
}

export default React.memo(SampleEdit);
