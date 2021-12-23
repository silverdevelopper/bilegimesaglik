import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PeoplePatientResponsibleComponent } from './list/people-patient-responsible.component';
import { PeoplePatientResponsibleDetailComponent } from './detail/people-patient-responsible-detail.component';
import { PeoplePatientResponsibleUpdateComponent } from './update/people-patient-responsible-update.component';
import { PeoplePatientResponsibleDeleteDialogComponent } from './delete/people-patient-responsible-delete-dialog.component';
import { PeoplePatientResponsibleRoutingModule } from './route/people-patient-responsible-routing.module';

@NgModule({
  imports: [SharedModule, PeoplePatientResponsibleRoutingModule],
  declarations: [
    PeoplePatientResponsibleComponent,
    PeoplePatientResponsibleDetailComponent,
    PeoplePatientResponsibleUpdateComponent,
    PeoplePatientResponsibleDeleteDialogComponent,
  ],
  entryComponents: [PeoplePatientResponsibleDeleteDialogComponent],
})
export class PeoplePatientResponsibleModule {}
