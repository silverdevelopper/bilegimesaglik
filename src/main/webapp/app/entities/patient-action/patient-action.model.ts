import * as dayjs from 'dayjs';
import { IPeople } from 'app/entities/people/people.model';

export interface IPatientAction {
  id?: number;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  actionDescription?: string | null;
  healtstatus?: string | null;
  patient?: IPeople | null;
  staff?: IPeople | null;
}

export class PatientAction implements IPatientAction {
  constructor(
    public id?: number,
    public startDate?: dayjs.Dayjs | null,
    public endDate?: dayjs.Dayjs | null,
    public actionDescription?: string | null,
    public healtstatus?: string | null,
    public patient?: IPeople | null,
    public staff?: IPeople | null
  ) {}
}

export function getPatientActionIdentifier(patientAction: IPatientAction): number | undefined {
  return patientAction.id;
}
