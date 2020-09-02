import React from 'react';
import { Switch } from 'react-router-dom';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import GroupeDePatient from './groupe-de-patient';
import Region from './region';
import Centre from './centre';
import Specialty from './specialty';
import Medicament from './medicament';
import AdminDeCentre from './admin-de-centre';
import Medecin from './medecin';
import Patient from './patient';
import Notification from './notification';
import Questionnaire from './questionnaire';
import PatientQuestionnaire from './patient-questionnaire';
import Question from './question';
import QuestionAnswer from './question-answer';
import PatientReponse from './patient-reponse';
import PrivateRoute from 'app/shared/auth/private-route';
import { AUTHORITIES } from 'app/config/constants';
import Message from './message';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}groupe-de-patient`} component={GroupeDePatient} />
      <PrivateRoute path={`${match.url}region`} component={Region} hasAnyAuthorities={[AUTHORITIES.ADMIN]} />
      <ErrorBoundaryRoute path={`${match.url}centre`} component={Centre} />
      <ErrorBoundaryRoute path={`${match.url}specialty`} component={Specialty} />
      <ErrorBoundaryRoute path={`${match.url}medicament`} component={Medicament} />
      <ErrorBoundaryRoute path={`${match.url}admin-de-centre`} component={AdminDeCentre} />
      <ErrorBoundaryRoute path={`${match.url}medecin`} component={Medecin} />
      <ErrorBoundaryRoute path={`${match.url}patient`} component={Patient} />
      <ErrorBoundaryRoute path={`${match.url}notification`} component={Notification} />
      <PrivateRoute path={`${match.url}questionnaire`} component={Questionnaire} />
      <PrivateRoute path={`${match.url}patient-questionnaire`} component={PatientQuestionnaire} />
      <PrivateRoute path={`${match.url}question`} component={Question} />
      <PrivateRoute path={`${match.url}question-answer`} component={QuestionAnswer} />
      <PrivateRoute path={`${match.url}patient-reponse`} component={PatientReponse} />
      <ErrorBoundaryRoute path={`${match.url}question-answer`} component={QuestionAnswer} />
      <PrivateRoute path={`${match.url}patient-reponse`} component={PatientReponse} />
      <ErrorBoundaryRoute path={`${match.url}questionnaire`} component={Questionnaire} />
      <ErrorBoundaryRoute path={`${match.url}patient-questionnaire`} component={PatientQuestionnaire} />
      <ErrorBoundaryRoute path={`${match.url}question`} component={Question} />
      <ErrorBoundaryRoute path={`${match.url}patient-reponse`} component={PatientReponse} />
      <ErrorBoundaryRoute path={`${match.url}message`} component={Message} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
