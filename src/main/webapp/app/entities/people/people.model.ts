import * as dayjs from 'dayjs';
import { IUser } from 'app/entities/user/user.model';

export interface IPeople {
  id?: number;
  firstName?: string | null;
  lastName?: string | null;
  email?: string | null;
  phoneNumber?: string | null;
  birthDate?: dayjs.Dayjs | null;
  user?: IUser | null;
}

export class People implements IPeople {
  constructor(
    public id?: number,
    public firstName?: string | null,
    public lastName?: string | null,
    public email?: string | null,
    public phoneNumber?: string | null,
    public birthDate?: dayjs.Dayjs | null,
    public user?: IUser | null
  ) {}
}

export function getPeopleIdentifier(people: IPeople): number | undefined {
  return people.id;
}
