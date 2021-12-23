import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPatientTask, getPatientTaskIdentifier } from '../patient-task.model';

export type EntityResponseType = HttpResponse<IPatientTask>;
export type EntityArrayResponseType = HttpResponse<IPatientTask[]>;

@Injectable({ providedIn: 'root' })
export class PatientTaskService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/patient-tasks');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(patientTask: IPatientTask): Observable<EntityResponseType> {
    return this.http.post<IPatientTask>(this.resourceUrl, patientTask, { observe: 'response' });
  }

  update(patientTask: IPatientTask): Observable<EntityResponseType> {
    return this.http.put<IPatientTask>(`${this.resourceUrl}/${getPatientTaskIdentifier(patientTask) as number}`, patientTask, {
      observe: 'response',
    });
  }

  partialUpdate(patientTask: IPatientTask): Observable<EntityResponseType> {
    return this.http.patch<IPatientTask>(`${this.resourceUrl}/${getPatientTaskIdentifier(patientTask) as number}`, patientTask, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPatientTask>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPatientTask[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPatientTaskToCollectionIfMissing(
    patientTaskCollection: IPatientTask[],
    ...patientTasksToCheck: (IPatientTask | null | undefined)[]
  ): IPatientTask[] {
    const patientTasks: IPatientTask[] = patientTasksToCheck.filter(isPresent);
    if (patientTasks.length > 0) {
      const patientTaskCollectionIdentifiers = patientTaskCollection.map(patientTaskItem => getPatientTaskIdentifier(patientTaskItem)!);
      const patientTasksToAdd = patientTasks.filter(patientTaskItem => {
        const patientTaskIdentifier = getPatientTaskIdentifier(patientTaskItem);
        if (patientTaskIdentifier == null || patientTaskCollectionIdentifiers.includes(patientTaskIdentifier)) {
          return false;
        }
        patientTaskCollectionIdentifiers.push(patientTaskIdentifier);
        return true;
      });
      return [...patientTasksToAdd, ...patientTaskCollection];
    }
    return patientTaskCollection;
  }
}
