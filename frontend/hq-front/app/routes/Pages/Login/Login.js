import React, { useCallback, useState, useEffect } from 'react'
import { Link } from 'react-router-dom';
import { useHistory } from "react-router-dom";

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
    ThemeConsumer
} from '../../../components';

import agent from "../../../shared/api/agent";
import { useFormik, FormikProps } from 'formik';
import * as Yup from 'yup'

import { HeaderAuth } from "../../../components/Pages/HeaderAuth";
import { FooterAuth } from "../../../components/Pages/FooterAuth";
import { renderError } from "../../../shared/utils/CommonErrorHandler";

import LoadingOverlay from 'react-loading-overlay'
import ClockLoader from 'react-spinners/ClockLoader';

const Login = () => {

    const history = useHistory();

    const [loading, setLoading] = useState(false);
    

    const formik = useFormik({
        initialValues: {
            userId: '',
            password: '',
        },

        validate: values => {
            try {
                Yup.object().shape({
                    userId: Yup.string()
                        //.userId('올바르지 않은 형식의 이메일입니다.')
                        .required('ID 입력은 필수 입니다.'),
                    password: Yup.string()
                        .min(4, `비밀번호는 최소 ${4} 글자입니다.`)
                        /*      .matches(/(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{6,}/, 'Password must contain: numbers, uppercase and lowercase letters\n')*/
                        .required('비밀번호는 필수 입니다.')
                }).validateSync(values, { abortEarly: false });
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



    /*
    *
    *   Event Handler
    *
    * */

    const onKeyValueChangeByEvent = (e)=> {
        formik.setFieldTouched(e.target.name);
        formik.handleChange(e);
    }
    const onKeyValueChangeByNameValue = (name, value)=> {
        formik.setFieldTouched(name);
        formik.setFieldValue(name, value);
    }

    const onLoginClick = async (e)=>{

        try {

            if (e) {
                // 예를 들어 이 element 가 a 태그라면 href 의 기능을 항상 막겠다.
                e.preventDefault()
                // 이 버튼을 클릭하였을 때, 상위 element 로의 전파를 막고 이 기능만 실행한다.
                e.stopPropagation()
            }

            if (formik.isValid && formik.dirty) {

                setLoading(true);
                const re = await Promise.all([agent.Auth.login(formik.values)]);

                if (re[0].statusCode === 200) {
                    history.push('/dashboards/projects');
                } else {

                    renderError({errorObj: re[0], formik});
                    /*                renderError({errorObj : {...re[0], userMessage: null, userValidationMessage: {
                                                userId : "서버에서 잘못됨을 판단하였습니다."
                                            }}, formik});*/
                }
            }
        } finally {
            setLoading(false);
        }
    }

    const onSignInKeyDown = (e)=>{
        if (e.keyCode === 13) {
            e.preventDefault();
            onLoginClick();
        }
    }



    return (
        <LoadingOverlay
            spinner={<ClockLoader color="#ffffff" size={20} />}
            active={loading}
        >
        <EmptyLayout>
            <EmptyLayout.Section center>
                { /* START Header */}
                <HeaderAuth
                    title="로그인을 진행하십시오."
                />
                { /* END Header */}
                { /* START Form */}
                <Form className="mb-3">
                    <FormGroup>
                        <Label for="userId">
                            ID
                        </Label>
                        <Input id="userId" name="userId" type="text" placeholder="UserId"
                                valid={!formik.errors.userId && formik.touched.userId}
                                invalid={!!formik.errors.userId && formik.touched.userId}
                                value={formik.values.userId}
                                onChange={onKeyValueChangeByEvent}
                                onBlur={formik.handleBlur}
                                onKeyDown={onSignInKeyDown}
                                required
                        />
                        <FormFeedback className={'ml-3'}>{formik.errors.userId}</FormFeedback>
                        <FormText color="muted">
                            계정 공유에 대한 책임은 본인에게 있습니다.
                        </FormText>
                    </FormGroup>
                    <FormGroup>
                        <Label for="password">
                            비밀번호
                        </Label>
                        <Input id="password" name="password" type="password" placeholder="Password"
                                valid={!formik.errors.password && formik.touched.password}
                                invalid={!!formik.errors.password && formik.touched.password}
                                value={formik.values.password}
                                onChange={onKeyValueChangeByEvent}
                                onBlur={formik.handleBlur}
                                onKeyDown={onSignInKeyDown}
                                required
                        />
                        <FormFeedback className={"ml-3"}>{formik.errors.password}</FormFeedback>
                    </FormGroup>
{/*                    <FormGroup>
                        <CustomInput type="checkbox" id="rememberPassword" label="Remember Password" inline />
                    </FormGroup>*/}
                    <ThemeConsumer>
                        {
                            ({ color }) => (
                                <Button color={ color } block tag={ Link } onClick={onLoginClick}>
                                    로그인
                                </Button>
                            )
                        }
                    </ThemeConsumer>
                </Form>
                { /* END Form */}
                { /* START Bottom Links */}
{/*                <div className="d-flex mb-5">
                    <Link to="/pages/forgotpassword" className="text-decoration-none">
                        Forgot Password
                    </Link>
                    <Link to="/pages/register" className="ml-auto text-decoration-none">
                        Register
                    </Link>
                </div>*/}
                { /* END Bottom Links */}
                { /* START Footer */}
                <FooterAuth />
                { /* END Footer */}

            </EmptyLayout.Section>
        </EmptyLayout>
        </LoadingOverlay>
    );
}

export default Login;
