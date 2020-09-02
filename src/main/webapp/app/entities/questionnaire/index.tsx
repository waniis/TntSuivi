import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Questionnaire from './questionnaire';
import QuestionnaireDetail from './questionnaire-detail';
import QuestionnaireUpdate from './questionnaire-update';
import QuestionnaireDeleteDialog from './questionnaire-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={QuestionnaireDeleteDialog} />
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={QuestionnaireUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={QuestionnaireUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={QuestionnaireDetail} />
      <ErrorBoundaryRoute path={match.url} component={Questionnaire} />
    </Switch>
  </>
);

export default Routes;
