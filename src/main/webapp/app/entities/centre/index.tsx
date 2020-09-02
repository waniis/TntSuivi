import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Centre from './centre';
import CentreDetail from './centre-detail';
import CentreUpdate from './centre-update';
import CentreDeleteDialog from './centre-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={CentreDeleteDialog} />
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={CentreUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={CentreUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={CentreDetail} />
      <ErrorBoundaryRoute path={match.url} component={Centre} />
    </Switch>
  </>
);

export default Routes;
