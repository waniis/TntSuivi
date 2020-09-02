import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import PatientReponse from './patient-reponse';
import PatientReponseDetail from './patient-reponse-detail';
import PatientReponseUpdate from './patient-reponse-update';
import PatientReponseDeleteDialog from './patient-reponse-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={PatientReponseDeleteDialog} />
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={PatientReponseUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={PatientReponseUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={PatientReponseDetail} />
      <ErrorBoundaryRoute path={match.url} component={PatientReponse} />
    </Switch>
  </>
);

export default Routes;
