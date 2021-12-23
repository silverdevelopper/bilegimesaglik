import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPatientAction, PatientAction } from '../patient-action.model';
import { PatientActionService } from '../service/patient-action.service';

@Injectable({ providedIn: 'root' })
export class PatientActionRoutingResolveService implements Resolve<IPatientAction> {
  constructor(protected service: PatientActionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPatientAction> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((patientAction: HttpResponse<PatientAction>) => {
          if (patientAction.body) {
            return of(patientAction.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PatientAction());
  }
}
