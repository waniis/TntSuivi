import { ICentre } from 'app/shared/model/centre.model';

export interface IRegion {
  id?: number;
  name?: string;
  centres?: ICentre[];
}

export const defaultValue: Readonly<IRegion> = {};
