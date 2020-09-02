import { IQuestionAnswer } from 'app/shared/model/question-answer.model';
import { IPatientReponse } from 'app/shared/model/patient-reponse.model';
import { IMedecin } from 'app/shared/model/medecin.model';
import { IQuestionnaire } from 'app/shared/model/questionnaire.model';
import { TypeQuestion } from 'app/shared/model/enumerations/type-question.model';

export interface IQuestion {
  id?: number;
  label?: string;
  typeQuestion?: TypeQuestion;
  questionAnswers?: IQuestionAnswer[];
  patientReponses?: IPatientReponse[];
  medecin?: IMedecin;
  questionnaires?: IQuestionnaire[];
}

export const defaultValue: Readonly<IQuestion> = {};
