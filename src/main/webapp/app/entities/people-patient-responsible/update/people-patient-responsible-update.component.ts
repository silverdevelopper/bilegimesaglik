import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPeoplePatientResponsible, PeoplePatientResponsible } from '../people-patient-responsible.model';
import { PeoplePatientResponsibleService } from '../service/people-patient-responsible.service';
import { IPeople } from 'app/entities/people/people.model';
import { PeopleService } from 'app/entities/people/service/people.service';

@Component({
  selector: 'jhi-people-patient-responsible-update',
  templateUrl: './people-patient-responsible-update.component.html',
})
export class PeoplePatientResponsibleUpdateComponent implements OnInit {
  isSaving = false;

  peopleSharedCollection: IPeople[] = [];

  editForm = this.fb.group({
    id: [],
    patient: [],
    responsiblePerson: [],
  });

  constructor(
    protected peoplePatientResponsibleService: PeoplePatientResponsibleService,
    protected peopleService: PeopleService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ peoplePatientResponsible }) => {
      this.updateForm(peoplePatientResponsible);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const peoplePatientResponsible = this.createFromForm();
    if (peoplePatientResponsible.id !== undefined) {
      this.subscribeToSaveResponse(this.peoplePatientResponsibleService.update(peoplePatientResponsible));
    } else {
      this.subscribeToSaveResponse(this.peoplePatientResponsibleService.create(peoplePatientResponsible));
    }
  }

  trackPeopleById(index: number, item: IPeople): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPeoplePatientResponsible>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(peoplePatientResponsible: IPeoplePatientResponsible): void {
    this.editForm.patchValue({
      id: peoplePatientResponsible.id,
      patient: peoplePatientResponsible.patient,
      responsiblePerson: peoplePatientResponsible.responsiblePerson,
    });

    this.peopleSharedCollection = this.peopleService.addPeopleToCollectionIfMissing(
      this.peopleSharedCollection,
      peoplePatientResponsible.patient,
      peoplePatientResponsible.responsiblePerson
    );
  }

  protected loadRelationshipsOptions(): void {
    this.peopleService
      .query()
      .pipe(map((res: HttpResponse<IPeople[]>) => res.body ?? []))
      .pipe(
        map((people: IPeople[]) =>
          this.peopleService.addPeopleToCollectionIfMissing(
            people,
            this.editForm.get('patient')!.value,
            this.editForm.get('responsiblePerson')!.value
          )
        )
      )
      .subscribe((people: IPeople[]) => (this.peopleSharedCollection = people));
  }

  protected createFromForm(): IPeoplePatientResponsible {
    return {
      ...new PeoplePatientResponsible(),
      id: this.editForm.get(['id'])!.value,
      patient: this.editForm.get(['patient'])!.value,
      responsiblePerson: this.editForm.get(['responsiblePerson'])!.value,
    };
  }
}
