import { IPatientQuestionnaire } from 'app/shared/model/patient-questionnaire.model';
import { IQuestion } from 'app/shared/model/question.model';
import { IQuestionAnswer } from 'app/shared/model/question-answer.model';

export interface IPatientReponse {
  id?: number;
  content?: string;
  patientQuestionnaire?: IPatientQuestionnaire;
  question?: IQuestion;
  questionAnswer?: IQuestionAnswer;
}

export const defaultValue: Readonly<IPatientReponse> = {};
