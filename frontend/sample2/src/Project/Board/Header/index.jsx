import React from 'react';

import { Button } from 'shared/components';

import { Header, BoardName } from './Styles';

const ProjectBoardHeader = () => (
    <Header>
        <BoardName>실시간 이슈 현황</BoardName>
   {/*      <a href="https://github.com/oldboyxx/jira_clone" target="_blank" rel="noreferrer noopener">
            <Button icon="github">Github Repo</Button>
        </a> */}
    </Header>
);

export default ProjectBoardHeader;
