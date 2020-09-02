import { Moment } from 'moment';
import { IUser } from 'app/shared/model/user.model';
import { IPatientQuestionnaire } from 'app/shared/model/patient-questionnaire.model';
import { IMedecin } from 'app/shared/model/medecin.model';
import { ICentre } from 'app/shared/model/centre.model';
import { IGroupeDePatient } from 'app/shared/model/groupe-de-patient.model';
import { Sexe } from 'app/shared/model/enumerations/sexe.model';
import { Alcool } from 'app/shared/model/enumerations/alcool.model';
import { Tobacco } from 'app/shared/model/enumerations/tobacco.model';

export interface IPatient {
  id?: number;
  fullName?: string;
  phone?: string;
  adress?: string;
  sexe?: Sexe;
  alcool?: Alcool;
  startDateAlcool?: Moment;
  endDateAlcool?: Moment;
  tobacoo?: Tobacco;
  startDateTobacco?: Moment;
  endDateTobacco?: Moment;
  user?: IUser;
  patientQuestionnaires?: IPatientQuestionnaire[];
  medecin?: IMedecin;
  centre?: ICentre;
  groupeDePatients?: IGroupeDePatient[];
}

export const defaultValue: Readonly<IPatient> = {};
