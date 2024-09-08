import React, { useState } from 'react';
import { useHistory } from 'react-router-dom';
import PropTypes from 'prop-types';
import api from 'shared/utils/api';
import useMergeState from 'shared/hooks/mergeState';
import { storeAuthToken } from 'shared/utils/authToken';

import {
  StyledLogo,
  LoginMainSection,
  LoginPageHeader,
  HeaderItem1,
  HeaderItem1H5,
  HeaderItem2,
  HeaderItem2Section,
  HeaderItem2SectionDiv,
  HeaderItem2SectionDivSpan,
  LoginPageMain,
  MainId,
  MainIdInput,
  MainPw,
  MainPwInput,
  MainBtn,
  MainBtnSpan,
  LoginPageSns,
  LoginPageBottom,
  BottomFooterUl,
  BottomFooterLi,
  BottomFooterA,
  BottomFooterP,
  LoginPageFooter,
  FooterFooter,
  FooterMsg,
} from './Styles';

const defaultFilters = {
  username: '',
  password: '',
};

const propTypes = {};

const LoginSection = ({}) => {
  const history = useHistory();

  const [filters, mergeFilters] = useMergeState(defaultFilters);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const handleLoginFormSubmit = async event => {
    event.preventDefault();
    setError(null);
    try {
      const data = await api.loginApi({
        username: filters.username,
        password: filters.password,
      });

      if (data.access_token && data.expires_in && data.expires_in > 0) {
        storeAuthToken(data.access_token);
        history.push('/project');
      }
    } catch (err) {
      setError(err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <LoginMainSection>
      <LoginPageHeader>
        <StyledLogo />
        <HeaderItem1>
          <HeaderItem1H5>계속하려면 가입하세요.</HeaderItem1H5>
        </HeaderItem1>
        {error && (
          <HeaderItem2>
            <HeaderItem2Section>
              <HeaderItem2SectionDiv>
                <HeaderItem2SectionDivSpan>{error}</HeaderItem2SectionDivSpan>
              </HeaderItem2SectionDiv>
            </HeaderItem2Section>
          </HeaderItem2>
        )}
      </LoginPageHeader>
      <LoginPageMain>
        <form onSubmit={handleLoginFormSubmit}>
          <MainId>
            <MainIdInput
              id="username"
              name="username"
              type="email"
              placeholder="이메일을 입력하세요"
              value={filters.username || ''}
              onChange={e => mergeFilters({ username: e.target.value })}
              required
            />
          </MainId>
          <MainPw>
            <MainPwInput
              id="password"
              name="password"
              type="password"
              placeholder="비밀번호 입력"
              value={filters.password || ''}
              onChange={e => mergeFilters({ password: e.target.value })}
              required
            />
          </MainPw>
          <MainBtn tabIndex="0" type="submit">
            <MainBtnSpan>로그인</MainBtnSpan>
          </MainBtn>
        </form>
      </LoginPageMain>

      <LoginPageSns></LoginPageSns>

      <LoginPageBottom>
        <BottomFooterUl>
          <BottomFooterLi>
            <BottomFooterA id="reset" href="/resetpassword">
              로그인할 수 없습니까?
            </BottomFooterA>
          </BottomFooterLi>
          <BottomFooterP>•</BottomFooterP>
          <BottomFooterLi>
            <BottomFooterA id="signup" href="/signup">
              계정 만들기
            </BottomFooterA>
          </BottomFooterLi>
        </BottomFooterUl>
      </LoginPageBottom>

      <div className="" />

      <LoginPageFooter>
        <FooterFooter>
          <FooterMsg>회사메일 1개 계정을 사용합니다.</FooterMsg>
        </FooterFooter>
        <br />
        <BottomFooterUl>
          <BottomFooterLi>
            <BottomFooterA href="https://www.naver.com" target="_blank" rel="noreferrer noopener">
              개인정보 보호정책
            </BottomFooterA>
          </BottomFooterLi>
          <BottomFooterP>•</BottomFooterP>
          <BottomFooterLi>
            <BottomFooterA href="https://www.naver.com" target="_blank" rel="noreferrer noopener">
              사용자 알림
            </BottomFooterA>
          </BottomFooterLi>
        </BottomFooterUl>
      </LoginPageFooter>
    </LoginMainSection>
  );
};

LoginSection.propTypes = propTypes;

export default LoginSection;
