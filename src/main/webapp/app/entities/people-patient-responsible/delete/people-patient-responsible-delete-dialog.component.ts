import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPeoplePatientResponsible } from '../people-patient-responsible.model';
import { PeoplePatientResponsibleService } from '../service/people-patient-responsible.service';

@Component({
  templateUrl: './people-patient-responsible-delete-dialog.component.html',
})
export class PeoplePatientResponsibleDeleteDialogComponent {
  peoplePatientResponsible?: IPeoplePatientResponsible;

  constructor(protected peoplePatientResponsibleService: PeoplePatientResponsibleService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.peoplePatientResponsibleService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
