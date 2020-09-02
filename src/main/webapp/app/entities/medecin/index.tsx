import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Medecin from './medecin';
import MedecinDetail from './medecin-detail';
import MedecinUpdate from './medecin-update';
import MedecinDeleteDialog from './medecin-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={MedecinDeleteDialog} />
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={MedecinUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={MedecinUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={MedecinDetail} />
      <ErrorBoundaryRoute path={match.url} component={Medecin} />
    </Switch>
  </>
);

export default Routes;
