import styled from 'styled-components';

import { sizes, mixin } from 'shared/utils/styles';

import { Logo } from 'shared/components';

export const LoginMainSection = styled.section`
  display: flex;
  flex-direction: column;
  margin: 0px auto;
  width: 400px;
  padding: 32px 40px;
  background: rgb(255, 255, 255);
  border-radius: 3px;
  box-shadow: rgba(0, 0, 0, 0.1) 0px 0px 10px;
  box-sizing: border-box;
  color: var(--ds-text-subtle, #42526e);
`;

export const LoginPageHeader = styled.div`
  display: flex;
  flex-direction: column;
  -webkit-box-align: center;
  align-items: center;
  text-align: center;
  margin-bottom: 16px;
`;

export const HeaderItem1 = styled.div`
  display: flex;
  flex-direction: column;
  padding-top: 24px;
  -webkit-box-align: center;
  align-items: center;
  margin-bottom: 16px;
`;

export const HeaderItem1H5 = styled.h5`
  font-weight: 600;
  font-size: 16px;
  line-height: 20px;
  color: rgb(23, 43, 77);
`;

export const HeaderItem2 = styled.div`
  margin-bottom: 16px;
`;

export const HeaderItem2Section = styled.section`
  box-sizing: border-box;
  appearance: none;
  border: none;
  background-color: var(--ds-background-warning, #fffae6);
  padding: var(--ds-space-200, 16px);
  word-break: break-word;
  border-radius: var(--ds-border-radius, 3px);
`;

export const HeaderItem2SectionDiv = styled.div`
  display: flex;
  box-sizing: border-box;
  gap: var(--ds-space-100, 8px);
  flex-direction: column;
  justify-content: stretch;
`;

export const HeaderItem2SectionDivSpan = styled.span`
  box-sizing: border-box;
  margin: var(--ds-space-0, 0px);
  padding: var(--ds-space-0, 0px);
  font-family: var(
    --ds-font-family-sans,
    -apple-system,
    BlinkMacSystemFont,
    'Segoe UI',
    'Roboto',
    'Oxygen',
    'Ubuntu',
    'Fira Sans',
    'Droid Sans',
    'Helvetica Neue',
    sans-serif
  );
  text-align: left;
`;

export const LoginPageMain = styled.div`
  margin: 0;
  padding: 0;
  display: block;
  unicode-bidi: isolate;
`;

export const MainId = styled.div`
  -webkit-box-align: center;
  align-items: center;
  background-color: var(--ds-background-input, #fafbfc);
  border-color: var(--ds-border-input, #dfe1e6);
  color: var(--ds-text, #091e42);
  cursor: text;
  border-radius: 3px;
  border-width: 2px;
  border-style: solid;
  box-sizing: border-box;
  display: flex;
  flex: 1 1 100%;
  font-size: 14px;
  -webkit-box-pack: justify;
  justify-content: space-between;
  max-width: 100%;
  overflow: hidden;
  transition: background-color 0.2s ease-in-out 0s, border-color 0.2s ease-in-out 0s;
  overflow-wrap: break-word;
  vertical-align: top;
  pointer-events: auto;
  margin-bottom: 8px;

  &:focus-within {
    border-color: #0055cc;
  }
`;

export const MainIdInput = styled.input`
  background-color: transparent;
  border: 0px;
  box-sizing: border-box;
  color: inherit;
  cursor: inherit;
  font-size: 14px;
  min-width: 0px;
  outline: none;
  width: 100%;
  line-height: 1.42857;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, 'Fira Sans',
    'Droid Sans', 'Helvetica Neue', sans-serif;
  padding: var(--ds-space-100, 8px) var(--ds-space-075, 6px);
  height: 2.57em;

  &:focus {
    outline: none;
  }
`;

export const MainPw = styled.div`
  -webkit-box-align: center;
  align-items: center;
  background-color: var(--ds-background-input, #fafbfc);
  border-color: var(--ds-border-input, #dfe1e6);
  color: var(--ds-text, #091e42);
  cursor: text;
  border-radius: 3px;
  border-width: 2px;
  border-style: solid;
  box-sizing: border-box;
  display: flex;
  flex: 1 1 100%;
  font-size: 14px;
  -webkit-box-pack: justify;
  justify-content: space-between;
  max-width: 100%;
  overflow: hidden;
  transition: background-color 0.2s ease-in-out 0s, border-color 0.2s ease-in-out 0s;
  overflow-wrap: break-word;
  vertical-align: top;
  pointer-events: auto;
  margin-bottom: 8px;

  &:focus-within {
    border-color: #0055cc;
  }
`;

export const MainPwInput = styled.input`
  background-color: transparent;
  border: 0px;
  box-sizing: border-box;
  color: inherit;
  cursor: inherit;
  font-size: 14px;
  min-width: 0px;
  outline: none;
  width: 100%;
  line-height: 1.42857;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, 'Fira Sans',
    'Droid Sans', 'Helvetica Neue', sans-serif;
  -webkit-text-security: disc !important;
  padding-block: 1px;
  padding-inline: 2px;
  padding: var(--ds-space-100, 8px) var(--ds-space-075, 6px);
  height: 2.57em;

  &:focus {
    outline: none;
  }
`;

export const MainBtn = styled.button`
  height: 40px !important;
  line-height: 40px !important;
  -webkit-box-align: baseline;
  align-items: baseline;
  border-width: 0px;
  border-radius: var(--ds-border-radius, 3px);
  box-sizing: border-box;
  display: inline-flex;
  font-size: inherit;
  font-style: normal;
  font-family: inherit;
  font-weight: 500;
  max-width: 100%;
  position: relative;
  text-align: center;
  text-decoration: none;
  transition: background 0.1s ease-out 0s, box-shadow 0.15s cubic-bezier(0.47, 0.03, 0.49, 1.38) 0s;
  white-space: nowrap;
  background: var(--ds-background-brand-bold, #0052cc);
  cursor: pointer;
  height: 2.28571em;
  line-height: 2.28571em;
  padding: 0px 10px;
  vertical-align: middle;
  width: 100%;
  -webkit-box-pack: center;
  justify-content: center;
  color: var(--ds-text-inverse, #ffffff) !important;
`;

export const MainBtnSpan = styled.span`
  -webkit-box-pack: center;
  justify-content: center;
  display: flex !important;
  opacity: 1;
  transition: opacity 0.3s ease 0s;
  margin: 0px 2px;
  -webkit-box-flex: 1;
  flex-grow: 1;
  flex-shrink: 1;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
`;

export const LoginPageSns = styled.div`
  margin: 0 24px 0 0;
  padding: 0;
  display: block;
  unicode-bidi: isolate;
`;

export const LoginPageBottom = styled.div`
  color: var(--ds-text-subtle, #42526e);
  margin-top: 24px;
  font-size: 14px;
  text-align: center;
  line-height: 20px;
`;

export const BottomFooterUl = styled.ul`
  display: flex;
  -webkit-box-pack: center;
  justify-content: center;
  list-style: none;
  padding: 0px;
`;

export const BottomFooterLi = styled.li`
  display: list-item;
  mainbtntext-align: -webkit-match-parent;
  unicode-bidi: isolate;
`;

export const BottomFooterA = styled.a`
  color: var(--ds-link, #0052cc);
  text-decoration: none;
`;

export const BottomFooterP = styled.p`
  margin: 0px 8px;
`;

export const LoginPageFooter = styled.div`
  color: var(--ds-text-subtle, #42526e);
  padding-top: 16px;
  font-size: 11px;
  text-align: center;
  line-height: 14px;
`;

export const FooterFooter = styled.footer`
  background: transparent;
  display: flex;
  -webkit-box-align: center;
  align-items: center;
  text-align: center;
  box-sizing: border-box;
  flex-direction: column;
  margin: 0px auto;
  border-top: 1px solid rgb(193, 199, 208);
  padding-top: 24px;
  font-size: 11px;
  color: rgb(23, 43, 77);
`;

export const FooterMsg = styled.div`
  padding-top: 8px;
`;

export const StyledLogo = styled(Logo)`
  display: inline-block;
  margin-left: 8px;
  padding: 10px;
  ${mixin.clickable}
`;
