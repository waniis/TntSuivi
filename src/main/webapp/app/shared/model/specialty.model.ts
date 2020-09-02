import { IMedecin } from 'app/shared/model/medecin.model';

export interface ISpecialty {
  id?: number;
  name?: string;
  medecins?: IMedecin[];
}

export const defaultValue: Readonly<ISpecialty> = {};
