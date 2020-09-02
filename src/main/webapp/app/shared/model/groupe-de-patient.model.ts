import { IPatient } from 'app/shared/model/patient.model';
import { IMedecin } from 'app/shared/model/medecin.model';

export interface IGroupeDePatient {
  id?: number;
  name?: string;
  patients?: IPatient[];
  medecin?: IMedecin;
}

export const defaultValue: Readonly<IGroupeDePatient> = {};
