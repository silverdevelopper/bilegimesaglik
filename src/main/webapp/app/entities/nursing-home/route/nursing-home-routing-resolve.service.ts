import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { INursingHome, NursingHome } from '../nursing-home.model';
import { NursingHomeService } from '../service/nursing-home.service';

@Injectable({ providedIn: 'root' })
export class NursingHomeRoutingResolveService implements Resolve<INursingHome> {
  constructor(protected service: NursingHomeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<INursingHome> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((nursingHome: HttpResponse<NursingHome>) => {
          if (nursingHome.body) {
            return of(nursingHome.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new NursingHome());
  }
}
