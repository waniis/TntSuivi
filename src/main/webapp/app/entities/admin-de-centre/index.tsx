import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import AdminDeCentre from './admin-de-centre';
import AdminDeCentreDetail from './admin-de-centre-detail';
import AdminDeCentreUpdate from './admin-de-centre-update';
import AdminDeCentreDeleteDialog from './admin-de-centre-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={AdminDeCentreDeleteDialog} />
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={AdminDeCentreUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={AdminDeCentreUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={AdminDeCentreDetail} />
      <ErrorBoundaryRoute path={match.url} component={AdminDeCentre} />
    </Switch>
  </>
);

export default Routes;
