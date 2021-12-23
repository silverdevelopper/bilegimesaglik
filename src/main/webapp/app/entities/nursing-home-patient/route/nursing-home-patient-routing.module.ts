import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { NursingHomePatientComponent } from '../list/nursing-home-patient.component';
import { NursingHomePatientDetailComponent } from '../detail/nursing-home-patient-detail.component';
import { NursingHomePatientUpdateComponent } from '../update/nursing-home-patient-update.component';
import { NursingHomePatientRoutingResolveService } from './nursing-home-patient-routing-resolve.service';

const nursingHomePatientRoute: Routes = [
  {
    path: '',
    component: NursingHomePatientComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: NursingHomePatientDetailComponent,
    resolve: {
      nursingHomePatient: NursingHomePatientRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: NursingHomePatientUpdateComponent,
    resolve: {
      nursingHomePatient: NursingHomePatientRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: NursingHomePatientUpdateComponent,
    resolve: {
      nursingHomePatient: NursingHomePatientRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(nursingHomePatientRoute)],
  exports: [RouterModule],
})
export class NursingHomePatientRoutingModule {}
