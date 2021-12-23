import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PatientActionComponent } from './list/patient-action.component';
import { PatientActionDetailComponent } from './detail/patient-action-detail.component';
import { PatientActionUpdateComponent } from './update/patient-action-update.component';
import { PatientActionDeleteDialogComponent } from './delete/patient-action-delete-dialog.component';
import { PatientActionRoutingModule } from './route/patient-action-routing.module';

@NgModule({
  imports: [SharedModule, PatientActionRoutingModule],
  declarations: [PatientActionComponent, PatientActionDetailComponent, PatientActionUpdateComponent, PatientActionDeleteDialogComponent],
  entryComponents: [PatientActionDeleteDialogComponent],
})
export class PatientActionModule {}
