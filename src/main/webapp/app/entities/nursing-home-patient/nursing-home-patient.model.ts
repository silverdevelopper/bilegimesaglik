import { IPeople } from 'app/entities/people/people.model';
import { INursingHome } from 'app/entities/nursing-home/nursing-home.model';

export interface INursingHomePatient {
  id?: number;
  patient?: IPeople | null;
  nusingHome?: INursingHome | null;
}

export class NursingHomePatient implements INursingHomePatient {
  constructor(public id?: number, public patient?: IPeople | null, public nusingHome?: INursingHome | null) {}
}

export function getNursingHomePatientIdentifier(nursingHomePatient: INursingHomePatient): number | undefined {
  return nursingHomePatient.id;
}
