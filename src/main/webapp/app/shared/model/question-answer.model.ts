import { IPatientReponse } from 'app/shared/model/patient-reponse.model';
import { IQuestion } from 'app/shared/model/question.model';

export interface IQuestionAnswer {
  id?: number;
  label?: string;
  patientReponses?: IPatientReponse[];
  question?: IQuestion;
}

export const defaultValue: Readonly<IQuestionAnswer> = {};
