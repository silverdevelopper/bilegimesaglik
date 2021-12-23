import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPatientAction, getPatientActionIdentifier } from '../patient-action.model';

export type EntityResponseType = HttpResponse<IPatientAction>;
export type EntityArrayResponseType = HttpResponse<IPatientAction[]>;

@Injectable({ providedIn: 'root' })
export class PatientActionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/patient-actions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(patientAction: IPatientAction): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(patientAction);
    return this.http
      .post<IPatientAction>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(patientAction: IPatientAction): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(patientAction);
    return this.http
      .put<IPatientAction>(`${this.resourceUrl}/${getPatientActionIdentifier(patientAction) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(patientAction: IPatientAction): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(patientAction);
    return this.http
      .patch<IPatientAction>(`${this.resourceUrl}/${getPatientActionIdentifier(patientAction) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPatientAction>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPatientAction[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPatientActionToCollectionIfMissing(
    patientActionCollection: IPatientAction[],
    ...patientActionsToCheck: (IPatientAction | null | undefined)[]
  ): IPatientAction[] {
    const patientActions: IPatientAction[] = patientActionsToCheck.filter(isPresent);
    if (patientActions.length > 0) {
      const patientActionCollectionIdentifiers = patientActionCollection.map(
        patientActionItem => getPatientActionIdentifier(patientActionItem)!
      );
      const patientActionsToAdd = patientActions.filter(patientActionItem => {
        const patientActionIdentifier = getPatientActionIdentifier(patientActionItem);
        if (patientActionIdentifier == null || patientActionCollectionIdentifiers.includes(patientActionIdentifier)) {
          return false;
        }
        patientActionCollectionIdentifiers.push(patientActionIdentifier);
        return true;
      });
      return [...patientActionsToAdd, ...patientActionCollection];
    }
    return patientActionCollection;
  }

  protected convertDateFromClient(patientAction: IPatientAction): IPatientAction {
    return Object.assign({}, patientAction, {
      startDate: patientAction.startDate?.isValid() ? patientAction.startDate.toJSON() : undefined,
      endDate: patientAction.endDate?.isValid() ? patientAction.endDate.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.startDate = res.body.startDate ? dayjs(res.body.startDate) : undefined;
      res.body.endDate = res.body.endDate ? dayjs(res.body.endDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((patientAction: IPatientAction) => {
        patientAction.startDate = patientAction.startDate ? dayjs(patientAction.startDate) : undefined;
        patientAction.endDate = patientAction.endDate ? dayjs(patientAction.endDate) : undefined;
      });
    }
    return res;
  }
}
