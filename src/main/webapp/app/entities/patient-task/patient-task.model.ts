import { IPeople } from 'app/entities/people/people.model';

export interface IPatientTask {
  id?: number;
  title?: string | null;
  description?: string | null;
  schedule?: string | null;
  patient?: IPeople | null;
}

export class PatientTask implements IPatientTask {
  constructor(
    public id?: number,
    public title?: string | null,
    public description?: string | null,
    public schedule?: string | null,
    public patient?: IPeople | null
  ) {}
}

export function getPatientTaskIdentifier(patientTask: IPatientTask): number | undefined {
  return patientTask.id;
}
