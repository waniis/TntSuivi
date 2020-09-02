import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import PatientQuestionnaire from './patient-questionnaire';
import PatientQuestionnaireDetail from './patient-questionnaire-detail';
import PatientQuestionnaireUpdate from './patient-questionnaire-update';
import PatientQuestionnaireDeleteDialog from './patient-questionnaire-delete-dialog';
import patientQuestionnaireReponse from './patient-questionnaire-reponse';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={PatientQuestionnaireDeleteDialog} />
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={PatientQuestionnaireUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={PatientQuestionnaireUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/reponse`} component={patientQuestionnaireReponse} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={PatientQuestionnaireDetail} />
      <ErrorBoundaryRoute path={match.url} component={PatientQuestionnaire} />
    </Switch>
  </>
);

export default Routes;
