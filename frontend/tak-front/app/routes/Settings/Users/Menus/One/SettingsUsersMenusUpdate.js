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
    Row, Table, FormText, Card, CardBody, CardHeader
} from 'reactstrap'
import {ActionIcon, Tooltip, Box, Flex, Menu, Text, Title} from '@mantine/core';
import agent from "../../../../../shared/api/agent";
import {renderError} from "../../../../../shared/utils/CommonErrorHandler";

import LoadingOverlay from 'react-loading-overlay';
import ClockLoader from 'react-spinners/ClockLoader';
import {useRecoilValue, useResetRecoilState, useSetRecoilState} from "recoil";
import {
    boardUpdateOneSelector,
    boardUpdateModifiedOneSelector, boardUpdateOthersSelector
} from "../../../../../shared/recoil/board/boardUpdateState";
import {Checkbox} from "@mantine/core";
import Switch from "react-switch";
import {
    ButtonWrapper,
    CheckboxWrapper,
    DetailHeaderWrapper
} from "../../../../../shared/components/OptimizedHtmlElements";
import {CRUD_COLUMNS, isAuthorized} from "../../../../../shared/utils/authorization";
import {globalInfoAccessTokenUserInfoSelector} from "../../../../../shared/recoil/globalInfoState";

const SettingsUsersMenusUpdate = ({
                                      refreshAll = () => {
                                      }, refreshOne = () => {
    }, recoilKey
                                  }) => {

    const headRef = useRef(null);

    const history = useHistory();

    const me = useRecoilValue(globalInfoAccessTokenUserInfoSelector());
    const globalReadOnly = !isAuthorized({recoilKey, accessTokenUserInfo: me, CRUD_COLUMN: CRUD_COLUMNS.UPDATE})


    const one = useRecoilValue(boardUpdateOneSelector({recoilKey}));
    const setOne = useSetRecoilState(boardUpdateOneSelector({recoilKey}));

    const others = useRecoilValue(boardUpdateOthersSelector({recoilKey}));
    const setOthers = useSetRecoilState(boardUpdateOthersSelector({recoilKey}));

    const [loading, setLoading] = useState(false);
    const [errors, setErrors] = useState({});


    const handleCheckboxChange = useCallback((e, userMenuAuthIdx, ynKey) => {
        const {checked} = e.target;
        setOne((prevState) =>
            prevState.map((item) =>
                item.userMenuAuthIdx === userMenuAuthIdx ? {...item, [ynKey]: checked ? 'Y' : 'N'} : item
            )
        );
    }, [setOne]);

    const handleCheckboxesChange = useCallback((e, ynKey) => {
        const {name, checked} = e.target;
        setOne(prevState => {
            return prevState.map(item => {
                return {...item, [ynKey]: checked ? 'Y' : 'N'};
            });
        });
    }, [setOne]);


    const handleTextChange = ({key, e}) => {
        setOne(prevState => {
            return prevState.map(item => {
                return {...item, [key]: e.target.value}
            })
        })
    }

    const onUpdateButtonClick = async (e) => {
        try {

            if (e) {
                e.preventDefault();
                e.stopPropagation();
            }

            if (one.length > 0) {

                setLoading(true);

                const re = await Promise.all([agent.Menu.updateForUser({
                    item: one,
                    userIdx: one[0].userIdx,
                    copyToUserIdx: others.copyPermissions ? others.selectedUserIdx : undefined
                })]);

                if (re[0] && re[0].statusCode === 200) {
                    setOne(re[0].data);
                    alert('업데이트 되었습니다. 자기 자신의 권한 적용의 경우 F5 (새로 고침) 을 통해 확인 가능합니다.')
                } else {
                    renderError({errorObj: re[0]});
                }

            } else {

                alert("소유한 권한 내역이 하나도 없습니다.")

            }
        } finally {
            setLoading(false);
        }
    };


    return (
        <LoadingOverlay
            spinner={<ClockLoader color="#ffffff" size={20}/>}
            active={loading}
        > <Card className="mb-3">
            <CardHeader>사용자 ID {one.length > 0 ? one[0].userIdx : ""} 님의 권한 내역입니다.</CardHeader>
            <CardBody>
                <Form className="mt-4 mb-3" ref={headRef}>
                    <Row className="mt-4 mb-2">
                        <Col>
                            <label htmlFor="material-switch">
                                {!others.copyPermissions ? (<span className={"mb-2 mr-2"}>권한 복사</span>) : (
                                    <FormText color="muted">대상을 지정하고, 권한 체크 후, "적용" 버튼을 클릭하여 완료하세요</FormText>)}
                                <Switch onChange={(v) => {
                                    setOthers({
                                        ...others,
                                        copyPermissions: v
                                    })
                                }} onColor="#86d3ff"
                                        onHandleColor="#2693e6"
                                        handleDiameter={30}
                                        uncheckedIcon={false}
                                        checkedIcon={false}
                                        boxShadow="0px 1px 5px rgba(0, 0, 0, 0.6)"
                                        activeBoxShadow="0px 0px 1px 10px rgba(0, 0, 0, 0.2)"
                                        height={20}
                                        width={48}
                                        className="react-switch"
                                        id="material-switch" checked={others.copyPermissions} disabled={globalReadOnly}/>
                            </label>
                            {/*                        <CheckboxWrapper checked={others.copyPermissions} onChange={(e)=> {
                            const { checked } = e.target;
                            setOthers({
                                ...others,
                                copyPermissions : checked
                            })
                        }} />*/}
                        </Col>
                        <Col className={others.copyPermissions ? "" : "d-none"}>
                            <FormGroup>
                                <Label for="userIdx">
                                    권한 복사 대상
                                </Label>
                                <Input
                                    type={"select"}
                                    value={others.selectedUserIdx}
                                    onChange={(e) => {
                                        setOthers(prevState => {
                                            return {
                                                ...others,
                                                selectedUserIdx: e.target.value
                                            }
                                        })
                                    }}
                                    disabled={globalReadOnly}
                                >
                                    {others.users ? others.users.map((option) => (
                                        <option value={option.userIdx}>
                                            {option.name} ({option.deptNm})
                                        </option>
                                    )) : ""}
                                    <option value={""}>
                                        없음
                                    </option>
                                </Input>
                            </FormGroup>
                        </Col>
                    </Row>
                    <Table responsive>
                        <thead>
                        <tr>
                            <th><b>메인메뉴 이름</b></th>
                            <th><b>서브메뉴 이름</b></th>
                            <th><Label><b>목록</b><CheckboxWrapper disabled={globalReadOnly} className={"mt-1"}
                                                                 checked={!one.find(x => x.ynLst === "N")}
                                                                 onChange={(e) => handleCheckboxesChange(e, "ynLst")}/></Label>
                            </th>
                            <th><Label><b>신규</b><CheckboxWrapper disabled={globalReadOnly} className={"mt-1"}
                                                                 checked={!one.find(x => x.ynInt === "N")}
                                                                 onChange={(e) => handleCheckboxesChange(e, "ynInt")}/></Label>
                            </th>
                            <th><Label><b>수정</b><CheckboxWrapper disabled={globalReadOnly} className={"mt-1"}
                                                                 checked={!one.find(x => x.ynMod === "N")}
                                                                 onChange={(e) => handleCheckboxesChange(e, "ynMod")}/></Label>
                            </th>
                            <th><Label><b>삭제</b><CheckboxWrapper disabled={globalReadOnly} className={"mt-1"}
                                                                 checked={!one.find(x => x.ynDel === "N")}
                                                                 onChange={(e) => handleCheckboxesChange(e, "ynDel")}/></Label>
                            </th>
                            <th><Label><b>Excel</b><CheckboxWrapper disabled={globalReadOnly} className={"mt-1"}
                                                                    checked={!one.find(x => x.ynXls === "N")}
                                                                    onChange={(e) => handleCheckboxesChange(e, "ynXls")}/></Label>
                            </th>
                            {/*       <th><Label><b>최초등록자</b></Label></th>
                        <th><Label><b>마지막수정자</b></Label></th>*/}
                        </tr>
                        </thead>
                        <tbody>
                        {one.length > 0 && one.map((userMenuAuth, index) => (
                            <tr key={index}>
                                <td>정비</td>
                                <td className="left">{userMenuAuth.subMenuNm} <span>({userMenuAuth.subMenuIdx})</span>
                                </td>
                                <td><CheckboxWrapper disabled={globalReadOnly}
                                                     checked={one.find(x => x.userMenuAuthIdx === userMenuAuth.userMenuAuthIdx)?.ynLst === "Y"}
                                                     onChange={(e) => handleCheckboxChange(e, userMenuAuth.userMenuAuthIdx, "ynLst")}/>
                                </td>
                                <td><CheckboxWrapper disabled={globalReadOnly}
                                                     checked={one.find(x => x.userMenuAuthIdx === userMenuAuth.userMenuAuthIdx)?.ynInt === "Y"}
                                                     onChange={(e) => handleCheckboxChange(e, userMenuAuth.userMenuAuthIdx, "ynInt")}/>
                                </td>
                                <td><CheckboxWrapper disabled={globalReadOnly}
                                                     checked={one.find(x => x.userMenuAuthIdx === userMenuAuth.userMenuAuthIdx)?.ynMod === "Y"}
                                                     onChange={(e) => handleCheckboxChange(e, userMenuAuth.userMenuAuthIdx, "ynMod")}/>
                                </td>
                                <td><CheckboxWrapper disabled={globalReadOnly}
                                                     checked={one.find(x => x.userMenuAuthIdx === userMenuAuth.userMenuAuthIdx)?.ynDel === "Y"}
                                                     onChange={(e) => handleCheckboxChange(e, userMenuAuth.userMenuAuthIdx, "ynDel")}/>
                                </td>
                                <td><CheckboxWrapper disabled={globalReadOnly}
                                                     checked={one.find(x => x.userMenuAuthIdx === userMenuAuth.userMenuAuthIdx)?.ynXls === "Y"}
                                                     onChange={(e) => handleCheckboxChange(e, userMenuAuth.userMenuAuthIdx, "ynXls")}/>
                                </td>
                                {/*            <td>
                                <div>{one.find(x => x.userMenuAuthIdx === userMenuAuth.userMenuAuthIdx)?.regUserId}</div>
                                <div>{one.find(x => x.userMenuAuthIdx === userMenuAuth.userMenuAuthIdx)?.regDt}</div>
                            </td>
                            <td>
                                <div>{one.find(x => x.userMenuAuthIdx === userMenuAuth.userMenuAuthIdx)?.modUserId}</div>
                                <div>{one.find(x => x.userMenuAuthIdx === userMenuAuth.userMenuAuthIdx)?.modDt}</div>
                            </td>*/}
                            </tr>
                        ))}
                        {/*checked={one.find(x => x.userMenuAuthIdx === userMenuAuth.userMenuAuthIdx)?.ynLst === "Y"} */}
                        </tbody>
                    </Table>

                    <FormGroup>
                        <Row>
                            <Col>
                                <Label for="reason" className="important">변경 사유</Label>
                                <Input
                                    type="text"
                                    value={one.length > 0 ? one[0].reason : ""}
                                    onChange={(e) => handleTextChange({
                                        e, key: "reason"
                                    })}
                                    invalid={!!errors.reason}
                                    readOnly={globalReadOnly}
                                />
                                <FormFeedback className="ml-3">{errors.reason}</FormFeedback>
                            </Col>
                        </Row>
                    </FormGroup>

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
                                handleClick={onUpdateButtonClick}
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
};

export default React.memo(SettingsUsersMenusUpdate);
