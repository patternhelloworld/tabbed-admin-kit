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
import {ButtonWrapper, DetailHeaderWrapper} from "../../../../shared/components/OptimizedHtmlElements";
import {CardHeader} from "reactstrap";

import SearchUserModal from './SearchUserModal';

import SimpleBtnImgs from "assets/images/btns/SimpleBtnImgs";
import {ActionIcon, Tooltip, Box, Flex, Menu, Text, Title} from '@mantine/core';

const PK_NAME = "approvalLineIdx";
//const SettingsApprovalLinesUpdate = ({ refetch = undefined, recoilKey}) => {
const SettingsApprovalLinesUpdate = ({  createOne = () => {}, refreshAll = () => {}, refreshOne = () => {}, recoilKey}) => {

    const me = useRecoilValue(globalInfoAccessTokenUserInfoSelector());

    const one = useRecoilValue(boardUpdateOneSelector({recoilKey}));
    const setOne = useSetRecoilState(boardUpdateOneSelector({recoilKey}));

    const modifiedOne = useRecoilValue(boardUpdateModifiedOneSelector({recoilKey}));
    const setModifiedOne = useSetRecoilState(boardUpdateModifiedOneSelector({recoilKey}));

    const [loading, setLoading] = useState(false);

    const [formGroups, setFormGroups] = useState([]);
    const [userInfos, setUserInfos] = useState({});
	
    const [showSearchUserModal, setShowSearchUserModal] = useState(false);
    const [currentKey, setCurrentKey] = useState(null);

    const [selectedShowroom, setSelectedShowroom] = useState(null);

    const openSearchUserModal = (key) => {
        setCurrentKey(key);
        setShowSearchUserModal(true);
    };
    const oneValidationSchema = Yup.object().shape({
        lineGb: Yup.string().required('결재 구분 선택은 필수입니다.'),
    });

    const formik = useFormik({
        initialValues: {
            lineDetails: {}
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
        if (isValidObject(one)) {
            const initialUserInfos = {};

            if (one.userInfos && typeof one.userInfos === 'object') {
                for (const [key, value] of Object.entries(one.userInfos)) {
                    if (Array.isArray(value) && value.length === 3) {
                        initialUserInfos[key] = {
                            name: value[0] || '',
                            position: value[1] || '',
                            dept: value[2] || ''
                        };
                    } else {
                        initialUserInfos[key] = {
                            name: '',
                            position: '',
                            dept: ''
                        };
                    }
                }
            }

            setUserInfos(initialUserInfos);

            const lineDetailsJson = (() => {
                try {
                    if (!one.lineDetails) {
                        return {};
                    }
                    return JSON.parse(one.lineDetails);
                } catch (error) {
                    return {};
                }
            })();

            const lineDetailsArray = Object.entries(lineDetailsJson).map(([key, detail]) => ({
                id: Number(key),
                ...detail
            }));

            setFormGroups(lineDetailsArray);

            const selected = one.meta.showrooms.find(showroom => showroom.deptIdx == one.deptIdx);

            setSelectedShowroom(selected);

            console.log("8 : one 이라는 객체에 값이 없으면 무시")
            // modifiedOne : 수정한 one 인데, 이 화면에서 나가질 때만 저장.
            if (isValidObject(modifiedOne)) {
                if (JSON.stringify(modifiedOne) !== JSON.stringify(one)
                    && modifiedOne[PK_NAME] === one[PK_NAME]) {

                    formik.setValues({
                        ...modifiedOne,
                        lineDetails: lineDetailsJson
                    });
                    console.log("9 : 수정한 one 과 원래받은 one 이 다르고 && id 가 서로 같을때 ")

                } else {
                    formik.setValues({
                        ...one,
                        lineDetails: lineDetailsJson
                    });
                    console.log("10")
                }
            } else {
                formik.setValues({
                    ...one,
                    lineDetails: lineDetailsJson
                });
                console.log("11")
            }
        }
    }

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
        initializeFormik();
    }, [one])

    useEffect(() => {
        setModifiedOne({...formik.values});


        console.log("===================================================");
        console.log(formik.values);

    }, [formik.values])

    /*
    *
    *   Event Handler
    *
    * */

    const onKeyValueChangeByEvent = (e) => {
        formik.setFieldTouched(e.target.name);
        formik.handleChange(e);
    }

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

                let newLineDetails = {};
                let newIndex = 1;
                Object.entries(formik.values.lineDetails).forEach(([key, value]) => {
                    if (value.userid !== "" && value.userid !== undefined) {
                        newLineDetails[newIndex] = value;
                        newIndex++;
                    }
                });
                const lineDetailsString = JSON.stringify(newLineDetails);

                const re = await Promise.all([agent.ApprovalLine.update({
                    approvalLineIdx: formik.values.approvalLineIdx,
                    showroomIdx: formik.values.showroomIdx,
                    lineGb: formik.values.lineGb,
                    lineDetails: lineDetailsString
                })]);

                if (re[0].statusCode === 200) {
                    refreshAll();
                    alert("업데이트 완료.");
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

            const re = await Promise.all([agent.ApprovalLine.delete({
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

    const onSubmitKeyDown = (e) => {
        if (e.keyCode === 13) {
            onSubmit(e);
        }
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

    const handleAddFormGroup = () => {
        if (formGroups.length >= 10) {
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
            <Container>

                <Card className="mb-3">
                    <CardHeader> <DetailHeaderWrapper id={formik.values[PK_NAME]} name={formik.values.approvalLineIdx}/></CardHeader>
                    <CardBody>
                        <Form className="mt-4 mb-3">
                            <div className="d-flex justify-content-end mb-3">
                                <Button color="primary" onClick={handleAddFormGroup}>
                                    결재추가
                                </Button>
                            </div>
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
                                        style={{
                                            backgroundColor: '#e9ecef',
                                            color: 'black',
                                            border: '1px solid #ced4da',
                                        }}
                                        disabled
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

                            {formGroups.map(group => (
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
                                            onClick={() => openSearchUserModal(group.id)}
                                            onBlur={formik.handleBlur}
                                            style={{ width: '100px', marginRight: '10px', marginLeft: '5px' }}
                                        />
                                        직위
                                        <Input
                                            type="text"
                                            name={`lineDetails.${group.id}.position`}
                                            id={`lineDetails-${group.id}-position`}
                                            value={userInfos[group.id]?.position || ''}
                                            onClick={() => openSearchUserModal(group.id)}
                                            onBlur={formik.handleBlur}
                                            style={{ width: '100px', marginRight: '10px', marginLeft: '5px' }}
                                        />
                                        이름
                                        <Input
                                            type="text"
                                            name={`lineDetails.${group.id}.name`}
                                            id={`lineDetails-${group.id}-name`}
                                            value={userInfos[group.id]?.name || ''}
                                            onClick={() => openSearchUserModal(group.id)}
                                            onBlur={formik.handleBlur}
                                            style={{ width: '110px', marginRight: '10px', marginLeft: '5px' }}
                                        />
                                        결재란
                                        <Input
                                            type="text"
                                            name={`lineDetails.${group.id}.head`}
                                            id={`lineDetails-${group.id}-head`}
                                            value={formik.values.lineDetails[group.id]?.head || ''}
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

                            <FormGroup row>
                                <Label for="regUserid" sm={2} className="right important">등록자</Label>
                                <Col sm={10}>
                                    <Input
                                        type="text"
                                        name="regUserid"
                                        id="regUserid"
                                        value={
                                            formik.values.regUserid
                                                ? formik.values.regDt
                                                    ? `${formik.values.regUserid} (${formik.values.regDt})`
                                                    : `${formik.values.regUserid} ()`
                                                : formik.values.regDt
                                                    ? `(${formik.values.regDt})`
                                                    : ""
                                        }
                                        disabled
                                        style={{borderColor: '#dee2e6', cursor: 'not-allowed' }}
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
                                        value={
                                            formik.values.modUserid
                                                ? formik.values.modDt
                                                    ? `${formik.values.modUserid} (${formik.values.modDt})`
                                                    : `${formik.values.modUserid} ()`
                                                : formik.values.modDt
                                                    ? `(${formik.values.modDt})`
                                                    : ""
                                        }
                                        disabled
                                        style={{borderColor: '#dee2e6', cursor: 'not-allowed' }}
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
                <SearchUserModal
                    isOpen={showSearchUserModal}
                    onClose={() => setShowSearchUserModal(false)}
                    onSelect={handleUserSelection}
                />
            </Container>
        </LoadingOverlay>
    );
}

export default React.memo(SettingsApprovalLinesUpdate);
