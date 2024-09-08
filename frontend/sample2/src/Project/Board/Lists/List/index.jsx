import React from 'react';
import PropTypes from 'prop-types';
import moment from 'moment';
import { Droppable } from 'react-beautiful-dnd';
import { intersection } from 'lodash';

import {IssueStatus, IssueStatusCopy} from 'shared/constants/issues';

import Issue from './Issue';
import { List, Title, IssuesCount, Issues } from './Styles';

const propTypes = {
  stateId: PropTypes.number.isRequired,
  project: PropTypes.object.isRequired,
  filters: PropTypes.object.isRequired,
  currentUserId: PropTypes.number,
};

const defaultProps = {
  currentUserId: null,
};

const ProjectBoardList = ({ stateId, project, filters, currentUserId }) => {


  const filteredIssues = filterIssues(project.issues, filters, currentUserId);
  const filteredListIssues = getSortedListIssues(filteredIssues, stateId);
  const allListIssues = getSortedListIssues(project.issues, stateId);

  return (
    <Droppable key={stateId} droppableId={stateId}>
      {provided => (
        <List>
          <Title>
            {`${IssueStatusCopy[stateId]} `}
            <IssuesCount>({formatIssuesCount(allListIssues, filteredListIssues)})</IssuesCount>
          </Title>
          <Issues
            {...provided.droppableProps}
            ref={provided.innerRef}
            data-testid={`board-list:${stateId}`}
          >
            {filteredListIssues.map((issue, index) => (
              <Issue key={issue.id} projectUsers={project.users} issue={issue} index={index} />
            ))}
            {provided.placeholder}
          </Issues>
        </List>
      )}
    </Droppable>
  );
};

const filterIssues = (projectIssues, filters, currentUserId) => {
  const { searchTerm, userIds, myOnly, recent } = filters;
  let issues = projectIssues;

  if (searchTerm) {
    issues = issues.filter(issue => issue.title.toLowerCase().includes(searchTerm.toLowerCase()));
  }
  if (userIds.length > 0) {
    issues = issues.filter(issue => intersection(issue.userIds, userIds).length > 0);
  }
  if (myOnly && currentUserId) {
    issues = issues.filter(issue => issue.userIds.includes(currentUserId));
  }
  if (recent) {
    issues = issues.filter(issue => moment(issue.updatedAt).isAfter(moment().subtract(3, 'days')));
  }
  return issues;
};

const getSortedListIssues = (issues, stateId) =>
  issues.filter(issue => issue.stateId === stateId).sort((a, b) => a.listPosition - b.listPosition);

const formatIssuesCount = (allListIssues, filteredListIssues) => {
  if (allListIssues.length !== filteredListIssues.length) {
    return `${filteredListIssues.length} of ${allListIssues.length}`;
  }
  return allListIssues.length;
};

ProjectBoardList.propTypes = propTypes;
ProjectBoardList.defaultProps = defaultProps;

export default ProjectBoardList;
