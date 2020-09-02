import { IUser } from 'app/shared/model/user.model';
import { IQuestion } from 'app/shared/model/question.model';
import { IPatient } from 'app/shared/model/patient.model';
import { IQuestionnaire } from 'app/shared/model/questionnaire.model';
import { IGroupeDePatient } from 'app/shared/model/groupe-de-patient.model';
import { ICentre } from 'app/shared/model/centre.model';
import { ISpecialty } from 'app/shared/model/specialty.model';
import { Sexe } from 'app/shared/model/enumerations/sexe.model';

export interface IMedecin {
  id?: number;
  fullName?: string;
  phone?: string;
  phone2?: string;
  adress?: string;
  sexe?: Sexe;
  user?: IUser;
  questions?: IQuestion[];
  patients?: IPatient[];
  questionnaires?: IQuestionnaire[];
  groupeDePatients?: IGroupeDePatient[];
  centre?: ICentre;
  specialty?: ISpecialty;
}

export const defaultValue: Readonly<IMedecin> = {};
