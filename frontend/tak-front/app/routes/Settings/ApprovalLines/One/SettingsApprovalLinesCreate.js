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

import "assets/images/date-picker/react-datepicker.css";
import {CRUD_COLUMNS, isAuthorized} from "shared/utils/authorization";
import {globalInfoAccessTokenUserInfoSelector} from "shared/recoil/globalInfoState";
import {useGlobalSubMenusReload} from "shared/hooks/useGlobalSubMenusReload";
import {ButtonWrapper, DetailHeaderWrapper} from "shared/components/OptimizedHtmlElements";
import SearchUserModal from "./SearchUserModal";
import SimpleBtnImgs from "../../../../assets/images/btns/SimpleBtnImgs";
import {ActionIcon, Tooltip, Box, Flex, Menu, Text, Title} from '@mantine/core';
import {useFormikUtils} from "../../../../shared/hooks/useFormiklUtils";

const PK_NAME = "approvalLineIdx";
const SettingsApprovalLinesCreate = ({refreshAll = ()=>{}, refreshOne = ()=>{}, recoilKey}) => {

    const me = useRecoilValue(globalInfoAccessTokenUserInfoSelector());

    const one = useRecoilValue(boardUpdateOneSelector({recoilKey}));

    const modifiedOne = useRecoilValue(boardUpdateModifiedOneSelector({recoilKey}));
    const setModifiedOne = useSetRecoilState(boardUpdateModifiedOneSelector({recoilKey}));

    const [loading, setLoading] = useState(false);
    const [formGroups, setFormGroups] = useState([{id: 1, agree: '', userid: '', head: ''}]);
    const [userInfos, setUserInfos] = useState({});

    const [showSearchUserModal, setShowSearchUserModal] = useState(false);
    const [currentKey, setCurrentKey] = useState(null);

    const [selectedShowroom, setSelectedShowroom] = useState(null);

    const openSearchUserModal = (key) => {
        setCurrentKey(key);
        setShowSearchUserModal(true);
    };

    const oneValidationSchema = Yup.object().shape({
        showroomIdx: Yup.string().required('전시장 선택은 필수입니다.'),
        lineGb: Yup.string().required('결재 구분 선택은 필수입니다.'),
    });

    const formik = useFormik({
        initialValues: {
            lineDetails: formGroups.reduce((acc, group) => {
                acc[group.id] = {head: group.head, agree: group.agree, userid: group.userid};
                return acc;
            }, {})
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

    const {
        initializeFormikCommon
    } = useFormikUtils({formik, oneValidationSchema});

    const handleUserSelection = (selectedUser) => {
        setUserInfos(prevUserInfos => ({
            ...prevUserInfos,
            [currentKey]: {
                dept: selectedUser.deptNm,
                position: selectedUser.position,
                name: selectedUser.name
            }
        }));
        handleInputChange(currentKey, "userid", selectedUser.userIdx);
    };

    const handleInputChange = (key, field, value) => {
        formik.setFieldValue(`lineDetails.${key}.${field}`, value);
    };

    /* Life Cycle */
    useEffect(() => {
        // one 이라는 recoil 이 바뀌는 순간
        initializeFormikCommon({
            one, modifiedOne, formik, PK_NAME, oneValidationSchema, customFormikSetModifiedOneFunc: (modifiedOne) => {
                formik.setValues({...formik.initialValues, ...modifiedOne, delYn: modifiedOne.delDt ? "Y" : "N"})
            }, customFormikSetOneFunc: (one) => {
                formik.setValues({...formik.initialValues, ...one, delYn: modifiedOne.delDt ? "Y" : "N"})
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


    /*
    *
    *   Event Handler
    *
    * */
    const onKeyValueChangeByEvent = (e) => {
        if(e.target.id == 'showroomIdx') {

            const selectedDeptIdx = e.target.value;
            const selected = one.meta.showrooms.find(showroom => showroom.deptIdx.toString() === selectedDeptIdx);
            setSelectedShowroom(selected);

            formik.setFieldValue('lineGb', '');
        }
        formik.setFieldTouched(e.target.name);
        formik.handleChange(e);
    }

    const getAvailableLineOptions = () => {
        const options = [
            { value: "A", text: "계약" },
            { value: "C", text: "해약" },
            { value: "D", text: "시승" }
        ];
        return options.map(option => ({
            ...option
        }));
    };

    const handleCreateOrUpdate = async (e) => {

        try {

            if (e) {
                // 예를 들어 이 element 가 a 태그라면 href 의 기능을 항상 막겠다.
                e.preventDefault()
                // 이 버튼을 클릭하였을 때, 상위 element 로의 전파를 막고 이 기능만 실행한다.
                e.stopPropagation()
            }

            if (formik.isValid && formik.dirty) {

                setLoading(true);

                const {meta, ...valuesWithoutMeta} = formik.values;

                let newLineDetails = {};
                let newIndex = 1;
                Object.entries(valuesWithoutMeta.lineDetails).forEach(([key, value]) => {
                    if (value.userid !== "" && value.userid !== undefined) {
                        newLineDetails[newIndex] = value;
                        newIndex++;
                    }
                });
                const lineDetailsString = JSON.stringify(newLineDetails);

                const re = await Promise.all([agent.ApprovalLine.create({...valuesWithoutMeta, lineDetails: lineDetailsString})]);

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

    const onSubmitKeyDown = (e) => {
        if (e.keyCode === 13) {
            handleCreateOrUpdate(e);
        }
    }

    const handleAddFormGroup = () => {
        if (formGroups.length > 9) {
            alert("최대 10개 까지만 가능합니다.");
            return false;
        }
        const newId = Math.max(...formGroups.map(g => g.id), 0) + 1;
        const updatedFormGroups = [...formGroups, {id: newId, userid: '', agree: '', head: ''}];
        setFormGroups(updatedFormGroups);

        formik.setFieldValue('lineDetails', {
            ...formik.values.lineDetails,
            [newId]: {userid: '', agree: '', head: ''}
        });
    };

    const handleDeleteFormGroup = (id) => {
        const updatedFormGroups = formGroups.filter(group => group.id !== id);
        setFormGroups(updatedFormGroups);

        const updatedLineDetails = {...formik.values.lineDetails};
        delete updatedLineDetails[id];
        formik.setFieldValue('lineDetails', updatedLineDetails);
    };

    return (
        <LoadingOverlay
            spinner={<ClockLoader color="#ffffff" size={20}/>}
            active={loading}
        >
            <Card className="mb-3">
                <CardHeader><DetailHeaderWrapper id={formik.values[PK_NAME]} name="신규생성"/></CardHeader>
                <CardBody>
                    <Form className="mt-4 mb-3">
                        <div className="d-flex justify-content-end mb-3">
                            <Button color="primary" onClick={handleAddFormGroup}>
                                결재추가
                            </Button>
                        </div>
                        <FormGroup row>
                            <Label for="lineGb" sm={2} className="right">전시장</Label>
                            <Col sm={10}>
                                <CustomInput
                                    type="select"
                                    name="showroomIdx"
                                    id="showroomIdx"
                                    valid={!formik.errors.showroomIdx && formik.touched.showroomIdx}
                                    invalid={!!formik.errors.showroomIdx && formik.touched.showroomIdx}
                                    value={formik.values.showroomIdx}
                                    onChange={onKeyValueChangeByEvent}
                                    onBlur={formik.handleBlur}
                                    onKeyDown={onSubmitKeyDown}
                                    disabled
                                    style={{
                                        backgroundColor: '#e9ecef',
                                        color: 'black',
                                        border: '1px solid #ced4da',
                                    }}
                                >
                                    <option
                                        value={formik.values.showroomIdx}
                                        key={formik.values.showroomIdx}>
                                        {formik.values.deptNm}
                                    </option>
                                </CustomInput>
                            </Col>
                        </FormGroup>

                        <FormGroup row>
                            <Label for="lineGb" sm={2} className="right">결재 구분</Label>
                            <Col sm={10}>
                                <CustomInput
                                    type="select"
                                    name="lineGb"
                                    id="lineGb"
                                    valid={!formik.errors.lineGb && formik.touched.lineGb}
                                    invalid={!!formik.errors.lineGb && formik.touched.lineGb}
                                    value={formik.values.lineGb || ""}
                                    onChange={onKeyValueChangeByEvent}
                                    onBlur={formik.handleBlur}
                                    onKeyDown={onSubmitKeyDown}
                                    disabled
                                    style={{
                                        backgroundColor: '#e9ecef',
                                        color: 'black',
                                        border: '1px solid #ced4da',
                                    }}
                                >
                                    {getAvailableLineOptions().map((option) => (
                                        <option
                                            key={option.value}
                                            value={option.value}
                                        >
                                            {option.disabled && option.value !== one.LineGb
                                                ? `${option.text} (등록 완료된 결재라인)`
                                                : option.text}
                                        </option>
                                    ))}
                                </CustomInput>
                            </Col>
                        </FormGroup>

                        {formGroups.map((group) => (
                            <FormGroup row key={group.id}>
                                <Label sm={2} className="right">
                                    결재 {formGroups.indexOf(group) + 1}
                                </Label>
                                <Col sm={10} className="d-flex">
                                    부서
                                    <Input
                                        type="text"
                                        name={`lineDetails.${group.id}.dept`}
                                        id={`lineDetails-${group.id}-dept`}
                                        value={userInfos[group.id]?.dept || ''}
                                        onClick={() => {
                                            openSearchUserModal(group.id);
                                        }}
                                        onBlur={formik.handleBlur}
                                        style={{ width: '100px', marginRight: '10px', marginLeft: '5px' }}
                                    />
                                    직위
                                    <Input
                                        type="text"
                                        name={`lineDetails.${group.id}.position`}
                                        id={`lineDetails-${group.id}-position`}
                                        value={userInfos[group.id]?.position || ''}
                                        onClick={() => {
                                            openSearchUserModal(group.id);
                                        }}
                                        onBlur={formik.handleBlur}
                                        style={{ width: '100px', marginRight: '10px', marginLeft: '5px' }}
                                    />
                                    이름
                                    <Input
                                        type="text"
                                        name={`lineDetails.${group.id}.name`}
                                        id={`lineDetails-${group.id}-name`}
                                        value={userInfos[group.id]?.name || ''}
                                        onClick={() => {
                                            openSearchUserModal(group.id);
                                        }}
                                        onBlur={formik.handleBlur}
                                        style={{ width: '110px', marginRight: '10px', marginLeft: '5px' }}
                                    />
                                    결재란
                                    <Input
                                        type="text"
                                        name={`lineDetails.${group.id}.head`}
                                        id={`lineDetails-${group.id}-head`}
                                        value={formik.values.lineDetails?.[group.id]?.head || ''}
                                        onChange={(e) => handleInputChange(group.id, 'head', e.target.value)}
                                        onBlur={formik.handleBlur}
                                        onKeyDown={onSubmitKeyDown}
                                        style={{ width: '100px', marginRight: '10px', marginLeft: '5px' }}
                                        maxLength="10"
                                    />
                                    <button
                                        type="button"
                                        onClick={() => handleDeleteFormGroup(group.id)}
                                        disabled={formGroups.length === 1}
                                        style={{
                                            background: 'transparent',
                                            border: 'none',
                                            padding: 0,
                                            cursor: 'pointer',
                                            outline: 'none',
                                            position: 'absolute',
                                            right: '15px',
                                            top: '48%',
                                            transform: 'translateY(-50%)',
                                            marginLeft: '5px',
                                        }}
                                    >
                                        <SimpleBtnImgs action="delete" width="35px"height="30px" />
                                    </button>
                                </Col>
                            </FormGroup>
                        ))}

                        <Flex p="md" justify="center" className={"mt-4"}>
                            <Flex gap="lg">
                                <ButtonWrapper
                                    color={"primary"}
                                    formik={formik}
                                    handleClick={handleCreateOrUpdate}
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
            <SearchUserModal
                isOpen={showSearchUserModal}
                onClose={() => setShowSearchUserModal(false)}
                onSelect={handleUserSelection}
            />

        </LoadingOverlay>
    );
};

export default React.memo(SettingsApprovalLinesCreate);
