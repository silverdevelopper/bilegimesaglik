import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PatientTaskComponent } from '../list/patient-task.component';
import { PatientTaskDetailComponent } from '../detail/patient-task-detail.component';
import { PatientTaskUpdateComponent } from '../update/patient-task-update.component';
import { PatientTaskRoutingResolveService } from './patient-task-routing-resolve.service';

const patientTaskRoute: Routes = [
  {
    path: '',
    component: PatientTaskComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PatientTaskDetailComponent,
    resolve: {
      patientTask: PatientTaskRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PatientTaskUpdateComponent,
    resolve: {
      patientTask: PatientTaskRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PatientTaskUpdateComponent,
    resolve: {
      patientTask: PatientTaskRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(patientTaskRoute)],
  exports: [RouterModule],
})
export class PatientTaskRoutingModule {}
