import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPatientTask } from '../patient-task.model';

@Component({
  selector: 'jhi-patient-task-detail',
  templateUrl: './patient-task-detail.component.html',
})
export class PatientTaskDetailComponent implements OnInit {
  patientTask: IPatientTask | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ patientTask }) => {
      this.patientTask = patientTask;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
