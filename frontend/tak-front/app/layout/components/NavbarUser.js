import React from 'react';
import { Link } from 'react-router-dom';
import PropTypes from 'prop-types';
import {useRecoilValue, useResetRecoilState, useSetRecoilState} from "recoil";
import {
    NavItem,
    NavLink
} from './../../components';
import agent from "../../shared/api/agent";
import ReactTimer from "../../shared/components/ReactTimer";
import {globalInfoAccessTokenUserInfoSelector} from "../../shared/recoil/globalInfoState";

const NavbarUser = (props) => {

    const me = useRecoilValue(globalInfoAccessTokenUserInfoSelector());

    return (
        <NavItem { ...props }>
            <NavLink tag={ Link } onClick={()=>{
                agent.Auth.logout();
            }} style={{ display: 'flex', alignItems: 'center' }}>
                <ReactTimer expiryTimestamp={me?.accessTokenRemainingSeconds}/>
                <i className="fa fa-sign-out fa-2x" aria-hidden="true" style={{ marginLeft: '8px' }} ></i> {/* 아이콘 옆 간격 추가 */}

            </NavLink>
        </NavItem>
)};


export { NavbarUser };
