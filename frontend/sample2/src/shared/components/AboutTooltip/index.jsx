import React from 'react';

import Button from 'shared/components/Button';
import Tooltip from 'shared/components/Tooltip';

import feedbackImage from './assets/feedback.png';
import { FeedbackDropdown, FeedbackImageCont, FeedbackImage, FeedbackParagraph } from './Styles';

const AboutTooltip = tooltipProps => (
  <Tooltip
    width={300}
    {...tooltipProps}
    renderContent={() => (
      <FeedbackDropdown>
        <FeedbackImageCont>
          <FeedbackImage src={feedbackImage} alt="Give feedback" />
        </FeedbackImageCont>

        <FeedbackParagraph>
            오토포커스의 산출물을 관리하는 PMS (Production Management System) 입니다.
        </FeedbackParagraph>

        <a href="https://auto-focus.co.kr/" target="_blank" rel="noreferrer noopener">
          <Button variant="primary">오토포커스 홈페이지</Button>
        </a>

      </FeedbackDropdown>
    )}
  />
);

export default AboutTooltip;
