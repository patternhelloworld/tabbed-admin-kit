import React from 'react';
import { Router, Switch, Route, Redirect } from 'react-router-dom';
import { getStoredAuthToken } from 'shared/utils/authToken';

import history from 'browserHistory';
import Project from 'Project';
import Authenticate from 'Auth/Authenticate';
import Login from 'Login';

import IssueList from "Test/IssueList";

import PageError from 'shared/components/PageError';

const ProtectedRoute = ({ path, component: Component }) => (
  <Route
    path={path}
    render={props => (getStoredAuthToken() ? <Component {...props} /> : <Redirect to="/login" />)}
  />
);

const Routes = () => (
  <Router history={history}>
    <Switch>
      <Route path="/login" component={Login} />
      <Route path="/authenticate" component={Authenticate} />
      <Route path="/test/issueList" component={IssueList}  />
      <ProtectedRoute path="/project" component={Project} />
      <Redirect from="/" to="/project" />
      <Route component={PageError} />
    </Switch>
  </Router>
);

export default Routes;
