import { IPeople } from 'app/entities/people/people.model';

export interface IPeoplePatientResponsible {
  id?: number;
  patient?: IPeople | null;
  responsiblePerson?: IPeople | null;
}

export class PeoplePatientResponsible implements IPeoplePatientResponsible {
  constructor(public id?: number, public patient?: IPeople | null, public responsiblePerson?: IPeople | null) {}
}

export function getPeoplePatientResponsibleIdentifier(peoplePatientResponsible: IPeoplePatientResponsible): number | undefined {
  return peoplePatientResponsible.id;
}
