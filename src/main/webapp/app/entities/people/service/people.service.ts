import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPeople, getPeopleIdentifier } from '../people.model';

export type EntityResponseType = HttpResponse<IPeople>;
export type EntityArrayResponseType = HttpResponse<IPeople[]>;

@Injectable({ providedIn: 'root' })
export class PeopleService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/people');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(people: IPeople): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(people);
    return this.http
      .post<IPeople>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(people: IPeople): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(people);
    return this.http
      .put<IPeople>(`${this.resourceUrl}/${getPeopleIdentifier(people) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(people: IPeople): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(people);
    return this.http
      .patch<IPeople>(`${this.resourceUrl}/${getPeopleIdentifier(people) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPeople>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPeople[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPeopleToCollectionIfMissing(peopleCollection: IPeople[], ...peopleToCheck: (IPeople | null | undefined)[]): IPeople[] {
    const people: IPeople[] = peopleToCheck.filter(isPresent);
    if (people.length > 0) {
      const peopleCollectionIdentifiers = peopleCollection.map(peopleItem => getPeopleIdentifier(peopleItem)!);
      const peopleToAdd = people.filter(peopleItem => {
        const peopleIdentifier = getPeopleIdentifier(peopleItem);
        if (peopleIdentifier == null || peopleCollectionIdentifiers.includes(peopleIdentifier)) {
          return false;
        }
        peopleCollectionIdentifiers.push(peopleIdentifier);
        return true;
      });
      return [...peopleToAdd, ...peopleCollection];
    }
    return peopleCollection;
  }

  protected convertDateFromClient(people: IPeople): IPeople {
    return Object.assign({}, people, {
      birthDate: people.birthDate?.isValid() ? people.birthDate.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.birthDate = res.body.birthDate ? dayjs(res.body.birthDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((people: IPeople) => {
        people.birthDate = people.birthDate ? dayjs(people.birthDate) : undefined;
      });
    }
    return res;
  }
}
