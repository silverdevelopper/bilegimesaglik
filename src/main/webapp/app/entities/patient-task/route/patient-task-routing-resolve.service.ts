import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPatientTask, PatientTask } from '../patient-task.model';
import { PatientTaskService } from '../service/patient-task.service';

@Injectable({ providedIn: 'root' })
export class PatientTaskRoutingResolveService implements Resolve<IPatientTask> {
  constructor(protected service: PatientTaskService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPatientTask> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((patientTask: HttpResponse<PatientTask>) => {
          if (patientTask.body) {
            return of(patientTask.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PatientTask());
  }
}
