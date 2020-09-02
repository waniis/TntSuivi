import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import QuestionAnswer from './question-answer';
import QuestionAnswerDetail from './question-answer-detail';
import QuestionAnswerUpdate from './question-answer-update';
import QuestionAnswerDeleteDialog from './question-answer-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={QuestionAnswerDeleteDialog} />
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={QuestionAnswerUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={QuestionAnswerUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={QuestionAnswerDetail} />
      <ErrorBoundaryRoute path={match.url} component={QuestionAnswer} />
    </Switch>
  </>
);

export default Routes;
