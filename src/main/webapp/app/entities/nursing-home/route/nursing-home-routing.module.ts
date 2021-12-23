import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { NursingHomeComponent } from '../list/nursing-home.component';
import { NursingHomeDetailComponent } from '../detail/nursing-home-detail.component';
import { NursingHomeUpdateComponent } from '../update/nursing-home-update.component';
import { NursingHomeRoutingResolveService } from './nursing-home-routing-resolve.service';

const nursingHomeRoute: Routes = [
  {
    path: '',
    component: NursingHomeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: NursingHomeDetailComponent,
    resolve: {
      nursingHome: NursingHomeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: NursingHomeUpdateComponent,
    resolve: {
      nursingHome: NursingHomeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: NursingHomeUpdateComponent,
    resolve: {
      nursingHome: NursingHomeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(nursingHomeRoute)],
  exports: [RouterModule],
})
export class NursingHomeRoutingModule {}
