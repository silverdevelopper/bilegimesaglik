import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPatientAction } from '../patient-action.model';
import { PatientActionService } from '../service/patient-action.service';

@Component({
  templateUrl: './patient-action-delete-dialog.component.html',
})
export class PatientActionDeleteDialogComponent {
  patientAction?: IPatientAction;

  constructor(protected patientActionService: PatientActionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.patientActionService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
