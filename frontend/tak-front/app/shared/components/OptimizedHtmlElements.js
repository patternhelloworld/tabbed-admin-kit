import React, {useCallback} from 'react';
import {Checkbox} from '@mantine/core';
import { Container, Row, Col, Card, CardHeader, CardBody, Form, FormGroup, Label, Input, FormFeedback } from 'reactstrap';
import {Link} from "react-router-dom";
import {CRUD_COLUMNS, isAuthorized} from "../utils/authorization";
import { Button, Flex } from '@mantine/core';
import { IconPencilPlus, IconChecks, IconTrash, IconArrowBack } from '@tabler/icons-react';


const CheckboxWrapper = React.memo(({ checked, onChange, label, disabled = false }) => {
    const handleChange = useCallback(
        (e) => {
            onChange(e);
        },
        [onChange]
    );

    return (
        <Checkbox checked={checked} onChange={handleChange} label={label} disabled={disabled} />
    );
});

const TextInputWrapper = React.memo(({ id, name, placeholder, formik, onChange, onSubmitKeyDown }) => {
    return (
        <Input
            id={id}
            name={name}
            type="text"
            placeholder={placeholder}
            valid={!formik.errors[name] && formik.touched[name]}
            invalid={!!formik.errors[name] && formik.touched[name]}
            value={formik.values[name]}
            onChange={onChange}
            onBlur={formik.handleBlur}
            onKeyDown={onSubmitKeyDown}
            required
        />
    );
});


const ButtonWrapper = ({   iconModule = undefined,
                           color = undefined,
                           variant = undefined,
                           gradient = undefined,
                           size = undefined,
                           outline = undefined,
                           btnText = undefined,
                           formik = undefined,
                           handleClick,
                           me = undefined,
                           recoilKey,
                           crudColumn = CRUD_COLUMNS.UPDATE
                       }) => {
    const isUserAuthorized = me === undefined ? true : isAuthorized({
        accessTokenUserInfo: me,
        recoilKey,
        CRUD_COLUMN: crudColumn
    });

    const isDisabled = formik === undefined ? false : (!(formik.isValid && formik.dirty));

    const finalBtnText = btnText === undefined ? "등록" : btnText;

    const formikConsideredBtnText = () => {

        //return finalBtnText;

        if (formik === undefined) {
            return finalBtnText;
        } else{
            if (!formik.dirty) {
                return "수정 없음";
            } else if (!formik.isValid) {
                return "입력 값 확인";
            } else {
                return finalBtnText;
            }
        }
    };
    if(btnText === "신규" || btnText === "등록"){
        return (<Button
            btnText={btnText}
            variant="gradient"
            gradient={{ from: 'teal', to: 'lime', deg: 90 }}
            leftIcon={<IconPencilPlus size={14}/>}
            onClick={handleClick}
            me={me}
            className={!isUserAuthorized ? "d-none" : ""}
            disabled={isDisabled}
        >{formikConsideredBtnText()}</Button>)
    }else if(btnText === "수정"){
        return (<Button
            btnText={btnText}
            variant="gradient"
            gradient={{ from: 'blue', to: 'cyan', deg: 90 }}
            leftIcon={<IconChecks size={14}/>}
            onClick={handleClick}
            me={me}
            className={!isUserAuthorized ? "d-none" : ""}
            disabled={isDisabled}
        >{formikConsideredBtnText()}</Button>)
    }else if(btnText === "취소"){
        return (<Button
            btnText={btnText}
            variant="gradient"
            gradient={{ from: 'rgba(0, 0, 0, 0.9)', to: 'gray', deg: 216 }}
            leftIcon={<IconArrowBack size={14}/>}
            onClick={handleClick}
            me={me}
            className={!isUserAuthorized ? "d-none" : ""}
            disabled={isDisabled}
        >{formikConsideredBtnText()}</Button>)
    }else if(btnText === "삭제"){
        return (<Button
            btnText={btnText}
            variant="gradient"
            gradient={{ from: 'red', to: 'rgba(227, 168, 168, 1)', deg: 90 }}
            leftIcon={<IconTrash size={14}/>}
            onClick={handleClick}
            me={me}
            className={!isUserAuthorized ? "d-none" : ""}
            disabled={isDisabled}
        >{formikConsideredBtnText()}</Button>)
    }else{
        return (
            <Button
                color={variant && gradient ?  undefined : color}
                variant={variant}
                gradient={gradient}
                className={!isUserAuthorized ? "d-none" : ""}
                leftIcon={iconModule}
                size={size ? size : "sm"}
                disabled={isDisabled}
                onClick={handleClick}

            >{formikConsideredBtnText()}</Button>
        );
    }





};

const DetailHeaderWrapper = ({ id = undefined, name = undefined }) => {
    if (!id) {
        return <b>{`신규 생성`}</b>;
    } else {
        return <b>{`ID${id} ${name ? `(${name})` : ''}`}</b>;
    }
};



const FormikBirthDateForm = ({ formik =  {
    values : {}, errors : {},
}}) => {

    const years = Array.from(new Array(100), (val, index) => (new Date().getFullYear() - index));
    const months = Array.from({ length: 12 }, (v, i) => i + 1);
    const days = Array.from({ length: 31 }, (v, i) => i + 1);

    const styles = {
        inputContainer: {
            minWidth: '100px',
        },
        flexContainer: {
            flex: 1,
        },
        label: {
            minWidth: '100px',
            textAlign: 'left',
            marginRight: '10px',
            paddingTop : "3px"
        },
    };

    return (
        <>
            <Flex gap="lg" align="left" wrap="wrap" className={"mt-1 mb-3"}>
                <label style={styles.label}>
                    생년월일
                </label>

                <Flex direction="row" gap="sm" style={styles.flexContainer}>
                    <div style={styles.inputContainer}>
                        <Input
                            type="select"
                            id="birthYear"
                            name="birthYear"
                            value={formik.values.birthYear}
                            onChange={formik.handleChange}
                            onBlur={formik.handleBlur}
                            valid={!formik.errors.birthYear && formik.touched.birthYear}
                            invalid={!!formik.errors.birthYear && formik.touched.birthYear}
                        >
                            <option value="">연도</option>
                            {years.map((year) => (
                                <option key={year} value={year}>
                                    {year}
                                </option>
                            ))}
                        </Input>
                        <FormFeedback className={'ml-3'}>{formik.errors.birthYear}</FormFeedback>
                    </div>

                    <div style={styles.inputContainer}>
                        <Input
                            type="select"
                            id="birthMonth"
                            name="birthMonth"
                            value={formik.values.birthMonth}
                            onChange={formik.handleChange}
                            onBlur={formik.handleBlur}
                            valid={!formik.errors.birthMonth && formik.touched.birthMonth}
                            invalid={!!formik.errors.birthMonth && formik.touched.birthMonth}
                        >
                            <option value="">월</option>
                            {months.map((month) => (
                                <option key={month} value={month}>
                                    {month}
                                </option>
                            ))}
                        </Input>
                        <FormFeedback className={'ml-3'}>{formik.errors.birthMonth}</FormFeedback>
                    </div>

                    <div style={styles.inputContainer}>
                        <Input
                            type="select"
                            id="birthDay"
                            name="birthDay"
                            value={formik.values.birthDay}
                            onChange={formik.handleChange}
                            onBlur={formik.handleBlur}
                            valid={!formik.errors.birthDay && formik.touched.birthDay}
                            invalid={!!formik.errors.birthDay && formik.touched.birthDay}
                        >
                            <option value="">일</option>
                            {days.map((day) => (
                                <option key={day} value={day}>
                                    {day}
                                </option>
                            ))}
                        </Input>
                        <FormFeedback className={'ml-3'}>{formik.errors.birthDay}</FormFeedback>
                    </div>
                </Flex>
            </Flex>
        </>
    );
};


export { CheckboxWrapper, TextInputWrapper, ButtonWrapper, DetailHeaderWrapper, FormikBirthDateForm };
