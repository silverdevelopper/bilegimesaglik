import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { INursingHomePatient, NursingHomePatient } from '../nursing-home-patient.model';
import { NursingHomePatientService } from '../service/nursing-home-patient.service';
import { IPeople } from 'app/entities/people/people.model';
import { PeopleService } from 'app/entities/people/service/people.service';
import { INursingHome } from 'app/entities/nursing-home/nursing-home.model';
import { NursingHomeService } from 'app/entities/nursing-home/service/nursing-home.service';

@Component({
  selector: 'jhi-nursing-home-patient-update',
  templateUrl: './nursing-home-patient-update.component.html',
})
export class NursingHomePatientUpdateComponent implements OnInit {
  isSaving = false;

  peopleSharedCollection: IPeople[] = [];
  nursingHomesSharedCollection: INursingHome[] = [];

  editForm = this.fb.group({
    id: [],
    patient: [],
    nusingHome: [],
  });

  constructor(
    protected nursingHomePatientService: NursingHomePatientService,
    protected peopleService: PeopleService,
    protected nursingHomeService: NursingHomeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ nursingHomePatient }) => {
      this.updateForm(nursingHomePatient);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const nursingHomePatient = this.createFromForm();
    if (nursingHomePatient.id !== undefined) {
      this.subscribeToSaveResponse(this.nursingHomePatientService.update(nursingHomePatient));
    } else {
      this.subscribeToSaveResponse(this.nursingHomePatientService.create(nursingHomePatient));
    }
  }

  trackPeopleById(index: number, item: IPeople): number {
    return item.id!;
  }

  trackNursingHomeById(index: number, item: INursingHome): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<INursingHomePatient>>): void {
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

  protected updateForm(nursingHomePatient: INursingHomePatient): void {
    this.editForm.patchValue({
      id: nursingHomePatient.id,
      patient: nursingHomePatient.patient,
      nusingHome: nursingHomePatient.nusingHome,
    });

    this.peopleSharedCollection = this.peopleService.addPeopleToCollectionIfMissing(
      this.peopleSharedCollection,
      nursingHomePatient.patient
    );
    this.nursingHomesSharedCollection = this.nursingHomeService.addNursingHomeToCollectionIfMissing(
      this.nursingHomesSharedCollection,
      nursingHomePatient.nusingHome
    );
  }

  protected loadRelationshipsOptions(): void {
    this.peopleService
      .query()
      .pipe(map((res: HttpResponse<IPeople[]>) => res.body ?? []))
      .pipe(map((people: IPeople[]) => this.peopleService.addPeopleToCollectionIfMissing(people, this.editForm.get('patient')!.value)))
      .subscribe((people: IPeople[]) => (this.peopleSharedCollection = people));

    this.nursingHomeService
      .query()
      .pipe(map((res: HttpResponse<INursingHome[]>) => res.body ?? []))
      .pipe(
        map((nursingHomes: INursingHome[]) =>
          this.nursingHomeService.addNursingHomeToCollectionIfMissing(nursingHomes, this.editForm.get('nusingHome')!.value)
        )
      )
      .subscribe((nursingHomes: INursingHome[]) => (this.nursingHomesSharedCollection = nursingHomes));
  }

  protected createFromForm(): INursingHomePatient {
    return {
      ...new NursingHomePatient(),
      id: this.editForm.get(['id'])!.value,
      patient: this.editForm.get(['patient'])!.value,
      nusingHome: this.editForm.get(['nusingHome'])!.value,
    };
  }
}
