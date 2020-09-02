import { Moment } from 'moment';
import { IUser } from 'app/shared/model/user.model';

export interface INotification {
  id?: number;
  description?: string;
  dateTime?: Moment;
  link?: string;
  seen?: boolean;
  user?: IUser;
}

export const defaultValue: Readonly<INotification> = {
  seen: false
};
