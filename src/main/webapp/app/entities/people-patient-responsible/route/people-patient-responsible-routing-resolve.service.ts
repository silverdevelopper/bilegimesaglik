import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPeoplePatientResponsible, PeoplePatientResponsible } from '../people-patient-responsible.model';
import { PeoplePatientResponsibleService } from '../service/people-patient-responsible.service';

@Injectable({ providedIn: 'root' })
export class PeoplePatientResponsibleRoutingResolveService implements Resolve<IPeoplePatientResponsible> {
  constructor(protected service: PeoplePatientResponsibleService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPeoplePatientResponsible> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((peoplePatientResponsible: HttpResponse<PeoplePatientResponsible>) => {
          if (peoplePatientResponsible.body) {
            return of(peoplePatientResponsible.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PeoplePatientResponsible());
  }
}
