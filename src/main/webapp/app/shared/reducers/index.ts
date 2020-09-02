import { combineReducers } from 'redux';
import { loadingBarReducer as loadingBar } from 'react-redux-loading-bar';

import locale, { LocaleState } from './locale';
import authentication, { AuthenticationState } from './authentication';
import applicationProfile, { ApplicationProfileState } from './application-profile';

import administration, { AdministrationState } from 'app/modules/administration/administration.reducer';
import userManagement, { UserManagementState } from 'app/modules/administration/user-management/user-management.reducer';
import register, { RegisterState } from 'app/modules/account/register/register.reducer';
import activate, { ActivateState } from 'app/modules/account/activate/activate.reducer';
import password, { PasswordState } from 'app/modules/account/password/password.reducer';
import settings, { SettingsState } from 'app/modules/account/settings/settings.reducer';
import passwordReset, { PasswordResetState } from 'app/modules/account/password-reset/password-reset.reducer';
// prettier-ignore
import groupeDePatient, {
  GroupeDePatientState
} from 'app/entities/groupe-de-patient/groupe-de-patient.reducer';
// prettier-ignore
import region, {
  RegionState
} from 'app/entities/region/region.reducer';
// prettier-ignore
import centre, {
  CentreState
} from 'app/entities/centre/centre.reducer';
// prettier-ignore
import specialty, {
  SpecialtyState
} from 'app/entities/specialty/specialty.reducer';
// prettier-ignore
import medicament, {
  MedicamentState
} from 'app/entities/medicament/medicament.reducer';
// prettier-ignore
import adminDeCentre, {
  AdminDeCentreState
} from 'app/entities/admin-de-centre/admin-de-centre.reducer';
// prettier-ignore
import medecin, {
  MedecinState
} from 'app/entities/medecin/medecin.reducer';
// prettier-ignore
import patient, {
  PatientState
} from 'app/entities/patient/patient.reducer';
// prettier-ignore
import notification, {
  NotificationState
} from 'app/entities/notification/notification.reducer';
// prettier-ignore
import questionnaire, {
  QuestionnaireState
} from 'app/entities/questionnaire/questionnaire.reducer';
// prettier-ignore
import patientQuestionnaire, {
  PatientQuestionnaireState
} from 'app/entities/patient-questionnaire/patient-questionnaire.reducer';
// prettier-ignore
import question, {
  QuestionState
} from 'app/entities/question/question.reducer';
// prettier-ignore
import questionAnswer, {
  QuestionAnswerState
} from 'app/entities/question-answer/question-answer.reducer';
// prettier-ignore
import patientReponse, {
  PatientReponseState
} from 'app/entities/patient-reponse/patient-reponse.reducer';
// prettier-ignore
import message, {
  MessageState
} from 'app/entities/message/message.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

export interface IRootState {
  readonly authentication: AuthenticationState;
  readonly locale: LocaleState;
  readonly applicationProfile: ApplicationProfileState;
  readonly administration: AdministrationState;
  readonly userManagement: UserManagementState;
  readonly register: RegisterState;
  readonly activate: ActivateState;
  readonly passwordReset: PasswordResetState;
  readonly password: PasswordState;
  readonly settings: SettingsState;
  readonly groupeDePatient: GroupeDePatientState;
  readonly region: RegionState;
  readonly centre: CentreState;
  readonly specialty: SpecialtyState;
  readonly medicament: MedicamentState;
  readonly adminDeCentre: AdminDeCentreState;
  readonly medecin: MedecinState;
  readonly patient: PatientState;
  readonly notification: NotificationState;
  readonly questionnaire: QuestionnaireState;
  readonly patientQuestionnaire: PatientQuestionnaireState;
  readonly question: QuestionState;
  readonly questionAnswer: QuestionAnswerState;
  readonly patientReponse: PatientReponseState;
  readonly message: MessageState;
  /* jhipster-needle-add-reducer-type - JHipster will add reducer type here */
  readonly loadingBar: any;
}

const rootReducer = combineReducers<IRootState>({
  authentication,
  locale,
  applicationProfile,
  administration,
  userManagement,
  register,
  activate,
  passwordReset,
  password,
  settings,
  groupeDePatient,
  region,
  centre,
  specialty,
  medicament,
  adminDeCentre,
  medecin,
  patient,
  notification,
  questionnaire,
  patientQuestionnaire,
  question,
  questionAnswer,
  patientReponse,
  message,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
  loadingBar,
});

export default rootReducer;
