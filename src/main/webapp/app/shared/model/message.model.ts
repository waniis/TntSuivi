import { Moment } from 'moment';
import { IUser } from 'app/shared/model/user.model';

export interface IMessage {
  id?: number;
  contenu?: string;
  dateTime?: string;
  sender?: IUser;
  receiver?: IUser;
}

export const defaultValue: Readonly<IMessage> = {};
