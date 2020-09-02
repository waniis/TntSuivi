import { Moment } from 'moment';
import { IPatientReponse } from 'app/shared/model/patient-reponse.model';
import { IPatient } from 'app/shared/model/patient.model';
import { IQuestionnaire } from 'app/shared/model/questionnaire.model';

export interface IPatientQuestionnaire {
  id?: number;
  doneTimeDate?: Moment;
  done?: boolean;
  patientReponses?: IPatientReponse[];
  patient?: IPatient;
  questionnaire?: IQuestionnaire;
}

export const defaultValue: Readonly<IPatientQuestionnaire> = {
  
};
