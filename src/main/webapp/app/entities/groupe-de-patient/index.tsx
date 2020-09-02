import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import GroupeDePatient from './groupe-de-patient';
import GroupeDePatientDetail from './groupe-de-patient-detail';
import GroupeDePatientUpdate from './groupe-de-patient-update';
import GroupeDePatientDeleteDialog from './groupe-de-patient-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={GroupeDePatientDeleteDialog} />
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={GroupeDePatientUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={GroupeDePatientUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={GroupeDePatientDetail} />
      <ErrorBoundaryRoute path={match.url} component={GroupeDePatient} />
    </Switch>
  </>
);

export default Routes;
