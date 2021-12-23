import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPeoplePatientResponsible, getPeoplePatientResponsibleIdentifier } from '../people-patient-responsible.model';

export type EntityResponseType = HttpResponse<IPeoplePatientResponsible>;
export type EntityArrayResponseType = HttpResponse<IPeoplePatientResponsible[]>;

@Injectable({ providedIn: 'root' })
export class PeoplePatientResponsibleService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/people-patient-responsibles');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(peoplePatientResponsible: IPeoplePatientResponsible): Observable<EntityResponseType> {
    return this.http.post<IPeoplePatientResponsible>(this.resourceUrl, peoplePatientResponsible, { observe: 'response' });
  }

  update(peoplePatientResponsible: IPeoplePatientResponsible): Observable<EntityResponseType> {
    return this.http.put<IPeoplePatientResponsible>(
      `${this.resourceUrl}/${getPeoplePatientResponsibleIdentifier(peoplePatientResponsible) as number}`,
      peoplePatientResponsible,
      { observe: 'response' }
    );
  }

  partialUpdate(peoplePatientResponsible: IPeoplePatientResponsible): Observable<EntityResponseType> {
    return this.http.patch<IPeoplePatientResponsible>(
      `${this.resourceUrl}/${getPeoplePatientResponsibleIdentifier(peoplePatientResponsible) as number}`,
      peoplePatientResponsible,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPeoplePatientResponsible>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPeoplePatientResponsible[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPeoplePatientResponsibleToCollectionIfMissing(
    peoplePatientResponsibleCollection: IPeoplePatientResponsible[],
    ...peoplePatientResponsiblesToCheck: (IPeoplePatientResponsible | null | undefined)[]
  ): IPeoplePatientResponsible[] {
    const peoplePatientResponsibles: IPeoplePatientResponsible[] = peoplePatientResponsiblesToCheck.filter(isPresent);
    if (peoplePatientResponsibles.length > 0) {
      const peoplePatientResponsibleCollectionIdentifiers = peoplePatientResponsibleCollection.map(
        peoplePatientResponsibleItem => getPeoplePatientResponsibleIdentifier(peoplePatientResponsibleItem)!
      );
      const peoplePatientResponsiblesToAdd = peoplePatientResponsibles.filter(peoplePatientResponsibleItem => {
        const peoplePatientResponsibleIdentifier = getPeoplePatientResponsibleIdentifier(peoplePatientResponsibleItem);
        if (
          peoplePatientResponsibleIdentifier == null ||
          peoplePatientResponsibleCollectionIdentifiers.includes(peoplePatientResponsibleIdentifier)
        ) {
          return false;
        }
        peoplePatientResponsibleCollectionIdentifiers.push(peoplePatientResponsibleIdentifier);
        return true;
      });
      return [...peoplePatientResponsiblesToAdd, ...peoplePatientResponsibleCollection];
    }
    return peoplePatientResponsibleCollection;
  }
}
