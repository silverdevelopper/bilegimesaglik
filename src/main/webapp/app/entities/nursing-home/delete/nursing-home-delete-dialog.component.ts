import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { INursingHome } from '../nursing-home.model';
import { NursingHomeService } from '../service/nursing-home.service';

@Component({
  templateUrl: './nursing-home-delete-dialog.component.html',
})
export class NursingHomeDeleteDialogComponent {
  nursingHome?: INursingHome;

  constructor(protected nursingHomeService: NursingHomeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.nursingHomeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
