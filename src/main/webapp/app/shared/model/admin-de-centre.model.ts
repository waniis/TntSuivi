import { IUser } from 'app/shared/model/user.model';
import { ICentre } from 'app/shared/model/centre.model';

export interface IAdminDeCentre {
  id?: number;
  fullName?: string;
  phone?: string;
  user?: IUser;
  centre?: ICentre;
}

export const defaultValue: Readonly<IAdminDeCentre> = {};
