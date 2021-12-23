import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { INursingHome, getNursingHomeIdentifier } from '../nursing-home.model';

export type EntityResponseType = HttpResponse<INursingHome>;
export type EntityArrayResponseType = HttpResponse<INursingHome[]>;

@Injectable({ providedIn: 'root' })
export class NursingHomeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/nursing-homes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(nursingHome: INursingHome): Observable<EntityResponseType> {
    return this.http.post<INursingHome>(this.resourceUrl, nursingHome, { observe: 'response' });
  }

  update(nursingHome: INursingHome): Observable<EntityResponseType> {
    return this.http.put<INursingHome>(`${this.resourceUrl}/${getNursingHomeIdentifier(nursingHome) as number}`, nursingHome, {
      observe: 'response',
    });
  }

  partialUpdate(nursingHome: INursingHome): Observable<EntityResponseType> {
    return this.http.patch<INursingHome>(`${this.resourceUrl}/${getNursingHomeIdentifier(nursingHome) as number}`, nursingHome, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<INursingHome>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<INursingHome[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addNursingHomeToCollectionIfMissing(
    nursingHomeCollection: INursingHome[],
    ...nursingHomesToCheck: (INursingHome | null | undefined)[]
  ): INursingHome[] {
    const nursingHomes: INursingHome[] = nursingHomesToCheck.filter(isPresent);
    if (nursingHomes.length > 0) {
      const nursingHomeCollectionIdentifiers = nursingHomeCollection.map(nursingHomeItem => getNursingHomeIdentifier(nursingHomeItem)!);
      const nursingHomesToAdd = nursingHomes.filter(nursingHomeItem => {
        const nursingHomeIdentifier = getNursingHomeIdentifier(nursingHomeItem);
        if (nursingHomeIdentifier == null || nursingHomeCollectionIdentifiers.includes(nursingHomeIdentifier)) {
          return false;
        }
        nursingHomeCollectionIdentifiers.push(nursingHomeIdentifier);
        return true;
      });
      return [...nursingHomesToAdd, ...nursingHomeCollection];
    }
    return nursingHomeCollection;
  }
}
