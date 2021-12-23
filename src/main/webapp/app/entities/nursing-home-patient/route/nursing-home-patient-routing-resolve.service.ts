import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { INursingHomePatient, NursingHomePatient } from '../nursing-home-patient.model';
import { NursingHomePatientService } from '../service/nursing-home-patient.service';

@Injectable({ providedIn: 'root' })
export class NursingHomePatientRoutingResolveService implements Resolve<INursingHomePatient> {
  constructor(protected service: NursingHomePatientService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<INursingHomePatient> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((nursingHomePatient: HttpResponse<NursingHomePatient>) => {
          if (nursingHomePatient.body) {
            return of(nursingHomePatient.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new NursingHomePatient());
  }
}
