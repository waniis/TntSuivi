import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Medicament from './medicament';
import MedicamentDetail from './medicament-detail';
import MedicamentUpdate from './medicament-update';
import MedicamentDeleteDialog from './medicament-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={MedicamentDeleteDialog} />
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={MedicamentUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={MedicamentUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={MedicamentDetail} />
      <ErrorBoundaryRoute path={match.url} component={Medicament} />
    </Switch>
  </>
);

export default Routes;
