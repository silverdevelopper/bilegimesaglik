import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PatientActionComponent } from '../list/patient-action.component';
import { PatientActionDetailComponent } from '../detail/patient-action-detail.component';
import { PatientActionUpdateComponent } from '../update/patient-action-update.component';
import { PatientActionRoutingResolveService } from './patient-action-routing-resolve.service';

const patientActionRoute: Routes = [
  {
    path: '',
    component: PatientActionComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PatientActionDetailComponent,
    resolve: {
      patientAction: PatientActionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PatientActionUpdateComponent,
    resolve: {
      patientAction: PatientActionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PatientActionUpdateComponent,
    resolve: {
      patientAction: PatientActionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(patientActionRoute)],
  exports: [RouterModule],
})
export class PatientActionRoutingModule {}
