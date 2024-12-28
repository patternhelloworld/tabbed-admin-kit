import React, {useEffect, useState} from "react";
import { faker } from "@faker-js/faker";
import { Link } from "react-router-dom";

import {
  Sidebar,
  UncontrolledButtonDropdown,
  Avatar,
  AvatarAddOn,
  DropdownToggle,
  DropdownMenu,
  DropdownItem,
} from "../../../components";
import { randomAvatar } from "../../../shared/utils/utilities";
import agent from "../../../shared/api/agent";
import {renderError} from "../../../shared/utils/CommonErrorHandler";
import {useRecoilValue, useSetRecoilState} from "recoil";
import {boardUpdateOneSelector} from "../../../shared/recoil/board/boardUpdateState";
import {globalInfoAccessTokenUserInfoSelector} from "../../../shared/recoil/globalInfoState";
import {useGlobalSubMenusReload} from "../../../shared/hooks/useGlobalSubMenusReload";

const avatarImg = randomAvatar();

const SidebarTopA = () => {


  const me = useRecoilValue(globalInfoAccessTokenUserInfoSelector());
  const setMe = useSetRecoilState(globalInfoAccessTokenUserInfoSelector());

  const {reloadDynamicRoutes }  = useGlobalSubMenusReload();

  const fetchMe = async () => {

    if(window.location.pathname !== '/pages/login') {

      const re = await Promise.all([agent.Auth.current()]);

      if (re[0].statusCode === 200) {

        setMe(re[0].data)

      } else {

        renderError({errorObj: re[0]});
        /*                renderError({errorObj : {...re[0], userMessage: null, userValidationMessage: {
                                    email : "서버에서 잘못됨을 판단하였습니다."
                                }}, formik});*/
      }
    }
  }

  useEffect(()=>{

    fetchMe()

  },[])

  useEffect(()=>{
    // 옆에 서브 메뉴들은 자기 자신 정보 me 가 다 로드되고 me 의 권한에 따라 있는 권한들만 보여준다.
    reloadDynamicRoutes()
  },[me])

  return (<React.Fragment>
    {/* START: Sidebar Default */}
    <Sidebar.HideSlim>
      <Sidebar.Section className="pt-0">
        <Link to="/" className="d-block">
          <Sidebar.HideSlim>
            <Avatar.Image
              size="lg"
              src={""}
              addOns={[
                <AvatarAddOn.Icon
                  className="fa fa-circle"
                  color="white"
                  key="avatar-icon-bg"
                />,
                <AvatarAddOn.Icon
                  className="fa fa-circle"
                  color="success"
                  key="avatar-icon-fg"
                />,
              ]}
            />
          </Sidebar.HideSlim>
        </Link>

        <UncontrolledButtonDropdown>
          <DropdownToggle
            color="link"
            className="pl-0 pb-0 btn-profile sidebar__link"
          >
            {me.info.userId}
            <i className="fa fa-angle-down ml-2"></i>
          </DropdownToggle>
          <DropdownMenu persist>
            <DropdownItem header>
              {me.info.userId}
            </DropdownItem>
            <DropdownItem divider />
            <DropdownItem tag={Link} to="/apps/profile-details">
              My Profile
            </DropdownItem>
            <DropdownItem tag={Link} to="/apps/settings-edit">
              Settings
            </DropdownItem>
            <DropdownItem tag={Link} to="/apps/billing-edit">
              Billings
            </DropdownItem>
            <DropdownItem divider />
            <DropdownItem tag={Link} onClick={()=>{
              agent.Auth.logout();
            }}>
              <i className="fa fa-fw fa-sign-out mr-2"></i>
              로그 아웃
            </DropdownItem>
          </DropdownMenu>
        </UncontrolledButtonDropdown>
        <div className="small sidebar__link--muted">
          {me.info.dealerNm ? me.info.dealerNm : "소속 딜러 없음"} ({me.info.deptNm ? me.info.deptNm : "소속 부서 없음"})
        </div>
      </Sidebar.Section>
    </Sidebar.HideSlim>
    {/* END: Sidebar Default */}

    {/* START: Sidebar Slim */}
    <Sidebar.ShowSlim>
      <Sidebar.Section>
        <Avatar.Image
          size="sm"
          src={avatarImg}
          addOns={[
            <AvatarAddOn.Icon
              className="fa fa-circle"
              color="white"
              key="avatar-icon-bg"
            />,
            <AvatarAddOn.Icon
              className="fa fa-circle"
              color="success"
              key="avatar-icon-fg"
            />,
          ]}
        />
      </Sidebar.Section>
    </Sidebar.ShowSlim>
    {/* END: Sidebar Slim */}
  </React.Fragment>
)};

export { SidebarTopA };
