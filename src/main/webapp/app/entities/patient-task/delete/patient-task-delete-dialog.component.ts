import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPatientTask } from '../patient-task.model';
import { PatientTaskService } from '../service/patient-task.service';

@Component({
  templateUrl: './patient-task-delete-dialog.component.html',
})
export class PatientTaskDeleteDialogComponent {
  patientTask?: IPatientTask;

  constructor(protected patientTaskService: PatientTaskService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.patientTaskService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
