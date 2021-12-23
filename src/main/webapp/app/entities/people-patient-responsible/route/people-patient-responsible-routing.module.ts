import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PeoplePatientResponsibleComponent } from '../list/people-patient-responsible.component';
import { PeoplePatientResponsibleDetailComponent } from '../detail/people-patient-responsible-detail.component';
import { PeoplePatientResponsibleUpdateComponent } from '../update/people-patient-responsible-update.component';
import { PeoplePatientResponsibleRoutingResolveService } from './people-patient-responsible-routing-resolve.service';

const peoplePatientResponsibleRoute: Routes = [
  {
    path: '',
    component: PeoplePatientResponsibleComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PeoplePatientResponsibleDetailComponent,
    resolve: {
      peoplePatientResponsible: PeoplePatientResponsibleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PeoplePatientResponsibleUpdateComponent,
    resolve: {
      peoplePatientResponsible: PeoplePatientResponsibleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PeoplePatientResponsibleUpdateComponent,
    resolve: {
      peoplePatientResponsible: PeoplePatientResponsibleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(peoplePatientResponsibleRoute)],
  exports: [RouterModule],
})
export class PeoplePatientResponsibleRoutingModule {}
