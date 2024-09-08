import React from 'react';
import PropTypes from 'prop-types';

import { IssueType, IssueTypeCopy } from 'shared/constants/issues';
import { IssueTypeIcon, Select } from 'shared/components';

import { TypeButton, Type, TypeLabel } from './Styles';

const propTypes = {
  issue: PropTypes.object.isRequired,
  updateIssue: PropTypes.func.isRequired,
};

const ProjectBoardIssueDetailsType = ({ issue, updateIssue }) => (
  <Select
    variant="empty"
    dropdownWidth={150}
    withClearValue={false}
    name="issueTypeId"
    value={issue.issueTypeId}
    options={Object.values(IssueType).map(issueTypeId => ({
      value: issueTypeId,
      label: IssueTypeCopy[issueTypeId],
    }))}
    onChange={issueTypeId => updateIssue({ issueTypeId })}
    renderValue={({ value: issueTypeId }) => (
      <TypeButton variant="empty" icon={<IssueTypeIcon issueTypeId={issueTypeId} />}>
        {`${IssueTypeCopy[issueTypeId]}-${issue.id}`}
      </TypeButton>
    )}
    renderOption={({ value: issueTypeId }) => (
      <Type key={issueTypeId} onClick={() => updateIssue({ issueTypeId })}>
        <IssueTypeIcon issueTypeId={issueTypeId} top={1} />
        <TypeLabel>{IssueTypeCopy[issueTypeId]}</TypeLabel>
      </Type>
    )}
  />
);

ProjectBoardIssueDetailsType.propTypes = propTypes;

export default ProjectBoardIssueDetailsType;
