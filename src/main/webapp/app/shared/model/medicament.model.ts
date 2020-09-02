import { Forme } from 'app/shared/model/enumerations/forme.model';

export interface IMedicament {
  id?: number;
  name?: string;
  forme?: Forme;
  descrpition?: string;
}

export const defaultValue: Readonly<IMedicament> = {};
