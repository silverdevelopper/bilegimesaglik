import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPeoplePatientResponsible } from '../people-patient-responsible.model';

@Component({
  selector: 'jhi-people-patient-responsible-detail',
  templateUrl: './people-patient-responsible-detail.component.html',
})
export class PeoplePatientResponsibleDetailComponent implements OnInit {
  peoplePatientResponsible: IPeoplePatientResponsible | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ peoplePatientResponsible }) => {
      this.peoplePatientResponsible = peoplePatientResponsible;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
