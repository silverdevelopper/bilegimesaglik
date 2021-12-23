import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPatientAction } from '../patient-action.model';

@Component({
  selector: 'jhi-patient-action-detail',
  templateUrl: './patient-action-detail.component.html',
})
export class PatientActionDetailComponent implements OnInit {
  patientAction: IPatientAction | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ patientAction }) => {
      this.patientAction = patientAction;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
