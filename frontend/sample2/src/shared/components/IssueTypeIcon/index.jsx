import React from 'react';
import PropTypes from 'prop-types';

import { TypeIcon } from './Styles';

const propTypes = {
  issueTypeId: PropTypes.string.isRequired,
};

const IssueTypeIcon = ({ issueTypeId, ...otherProps }) => {
  console.log(issueTypeId)
  return (
  <TypeIcon type={issueTypeId} color={issueTypeId} size={18} {...otherProps} />
)};

IssueTypeIcon.propTypes = propTypes;

export default IssueTypeIcon;
