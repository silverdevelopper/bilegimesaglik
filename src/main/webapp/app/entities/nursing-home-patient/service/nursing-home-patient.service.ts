import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { INursingHomePatient, getNursingHomePatientIdentifier } from '../nursing-home-patient.model';

export type EntityResponseType = HttpResponse<INursingHomePatient>;
export type EntityArrayResponseType = HttpResponse<INursingHomePatient[]>;

@Injectable({ providedIn: 'root' })
export class NursingHomePatientService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/nursing-home-patients');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(nursingHomePatient: INursingHomePatient): Observable<EntityResponseType> {
    return this.http.post<INursingHomePatient>(this.resourceUrl, nursingHomePatient, { observe: 'response' });
  }

  update(nursingHomePatient: INursingHomePatient): Observable<EntityResponseType> {
    return this.http.put<INursingHomePatient>(
      `${this.resourceUrl}/${getNursingHomePatientIdentifier(nursingHomePatient) as number}`,
      nursingHomePatient,
      { observe: 'response' }
    );
  }

  partialUpdate(nursingHomePatient: INursingHomePatient): Observable<EntityResponseType> {
    return this.http.patch<INursingHomePatient>(
      `${this.resourceUrl}/${getNursingHomePatientIdentifier(nursingHomePatient) as number}`,
      nursingHomePatient,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<INursingHomePatient>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<INursingHomePatient[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addNursingHomePatientToCollectionIfMissing(
    nursingHomePatientCollection: INursingHomePatient[],
    ...nursingHomePatientsToCheck: (INursingHomePatient | null | undefined)[]
  ): INursingHomePatient[] {
    const nursingHomePatients: INursingHomePatient[] = nursingHomePatientsToCheck.filter(isPresent);
    if (nursingHomePatients.length > 0) {
      const nursingHomePatientCollectionIdentifiers = nursingHomePatientCollection.map(
        nursingHomePatientItem => getNursingHomePatientIdentifier(nursingHomePatientItem)!
      );
      const nursingHomePatientsToAdd = nursingHomePatients.filter(nursingHomePatientItem => {
        const nursingHomePatientIdentifier = getNursingHomePatientIdentifier(nursingHomePatientItem);
        if (nursingHomePatientIdentifier == null || nursingHomePatientCollectionIdentifiers.includes(nursingHomePatientIdentifier)) {
          return false;
        }
        nursingHomePatientCollectionIdentifiers.push(nursingHomePatientIdentifier);
        return true;
      });
      return [...nursingHomePatientsToAdd, ...nursingHomePatientCollection];
    }
    return nursingHomePatientCollection;
  }
}
