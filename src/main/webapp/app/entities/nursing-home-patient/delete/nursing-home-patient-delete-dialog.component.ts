import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { INursingHomePatient } from '../nursing-home-patient.model';
import { NursingHomePatientService } from '../service/nursing-home-patient.service';

@Component({
  templateUrl: './nursing-home-patient-delete-dialog.component.html',
})
export class NursingHomePatientDeleteDialogComponent {
  nursingHomePatient?: INursingHomePatient;

  constructor(protected nursingHomePatientService: NursingHomePatientService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.nursingHomePatientService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
