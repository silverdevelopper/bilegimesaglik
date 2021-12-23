import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { NursingHomePatientComponent } from './list/nursing-home-patient.component';
import { NursingHomePatientDetailComponent } from './detail/nursing-home-patient-detail.component';
import { NursingHomePatientUpdateComponent } from './update/nursing-home-patient-update.component';
import { NursingHomePatientDeleteDialogComponent } from './delete/nursing-home-patient-delete-dialog.component';
import { NursingHomePatientRoutingModule } from './route/nursing-home-patient-routing.module';

@NgModule({
  imports: [SharedModule, NursingHomePatientRoutingModule],
  declarations: [
    NursingHomePatientComponent,
    NursingHomePatientDetailComponent,
    NursingHomePatientUpdateComponent,
    NursingHomePatientDeleteDialogComponent,
  ],
  entryComponents: [NursingHomePatientDeleteDialogComponent],
})
export class NursingHomePatientModule {}
