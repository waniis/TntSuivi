import { Moment } from 'moment';
import { IPatientQuestionnaire } from 'app/shared/model/patient-questionnaire.model';
import { IQuestion } from 'app/shared/model/question.model';
import { IMedecin } from 'app/shared/model/medecin.model';

export interface IQuestionnaire {
  id?: number;
  subject?: string;
  startDate?: Moment;
  endDate?: Moment;
  patientQuestionnaires?: IPatientQuestionnaire[];
  questions?: IQuestion[];
  medecin?: IMedecin;
}

export const defaultValue: Readonly<IQuestionnaire> = {};
