import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { INursingHomePatient } from '../nursing-home-patient.model';

@Component({
  selector: 'jhi-nursing-home-patient-detail',
  templateUrl: './nursing-home-patient-detail.component.html',
})
export class NursingHomePatientDetailComponent implements OnInit {
  nursingHomePatient: INursingHomePatient | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ nursingHomePatient }) => {
      this.nursingHomePatient = nursingHomePatient;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
