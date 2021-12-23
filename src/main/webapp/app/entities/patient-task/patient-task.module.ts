import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PatientTaskComponent } from './list/patient-task.component';
import { PatientTaskDetailComponent } from './detail/patient-task-detail.component';
import { PatientTaskUpdateComponent } from './update/patient-task-update.component';
import { PatientTaskDeleteDialogComponent } from './delete/patient-task-delete-dialog.component';
import { PatientTaskRoutingModule } from './route/patient-task-routing.module';

@NgModule({
  imports: [SharedModule, PatientTaskRoutingModule],
  declarations: [PatientTaskComponent, PatientTaskDetailComponent, PatientTaskUpdateComponent, PatientTaskDeleteDialogComponent],
  entryComponents: [PatientTaskDeleteDialogComponent],
})
export class PatientTaskModule {}
