import React, { Fragment } from 'react';
import PropTypes from 'prop-types';

import { IssueStatus, IssueStatusCopy } from 'shared/constants/issues';
import { Select, Icon } from 'shared/components';

import { SectionTitle } from '../Styles';
import { Status } from './Styles';

const propTypes = {
  issue: PropTypes.object.isRequired,
  updateIssue: PropTypes.func.isRequired,
};

const ProjectBoardIssueDetailsStatus = ({ issue, updateIssue }) => (
  <Fragment>
    <SectionTitle>Status</SectionTitle>
    <Select
      variant="empty"
      dropdownWidth={343}
      withClearValue={false}
      name="stateId"
      value={issue.stateId}
      options={Object.values(IssueStatus).map(stateId => ({
        value: stateId,
        label: IssueStatusCopy[stateId],
      }))}
      onChange={stateId => updateIssue({ stateId })}
      renderValue={({ value: stateId }) => (
        <Status isValue color={stateId}>
          <div>{IssueStatusCopy[stateId]}</div>
          <Icon type="chevron-down" size={18} />
        </Status>
      )}
      renderOption={({ value: stateId }) => (
        <Status color={stateId}>{IssueStatusCopy[stateId]}</Status>
      )}
    />
  </Fragment>
);

ProjectBoardIssueDetailsStatus.propTypes = propTypes;

export default ProjectBoardIssueDetailsStatus;
