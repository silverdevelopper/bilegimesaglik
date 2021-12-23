import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IPatientAction, PatientAction } from '../patient-action.model';
import { PatientActionService } from '../service/patient-action.service';
import { IPeople } from 'app/entities/people/people.model';
import { PeopleService } from 'app/entities/people/service/people.service';

@Component({
  selector: 'jhi-patient-action-update',
  templateUrl: './patient-action-update.component.html',
})
export class PatientActionUpdateComponent implements OnInit {
  isSaving = false;

  peopleSharedCollection: IPeople[] = [];

  editForm = this.fb.group({
    id: [],
    startDate: [],
    endDate: [],
    actionDescription: [],
    healtstatus: [],
    patient: [],
    staff: [],
  });

  constructor(
    protected patientActionService: PatientActionService,
    protected peopleService: PeopleService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ patientAction }) => {
      if (patientAction.id === undefined) {
        const today = dayjs().startOf('day');
        patientAction.startDate = today;
        patientAction.endDate = today;
      }

      this.updateForm(patientAction);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const patientAction = this.createFromForm();
    if (patientAction.id !== undefined) {
      this.subscribeToSaveResponse(this.patientActionService.update(patientAction));
    } else {
      this.subscribeToSaveResponse(this.patientActionService.create(patientAction));
    }
  }

  trackPeopleById(index: number, item: IPeople): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPatientAction>>): void {
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

  protected updateForm(patientAction: IPatientAction): void {
    this.editForm.patchValue({
      id: patientAction.id,
      startDate: patientAction.startDate ? patientAction.startDate.format(DATE_TIME_FORMAT) : null,
      endDate: patientAction.endDate ? patientAction.endDate.format(DATE_TIME_FORMAT) : null,
      actionDescription: patientAction.actionDescription,
      healtstatus: patientAction.healtstatus,
      patient: patientAction.patient,
      staff: patientAction.staff,
    });

    this.peopleSharedCollection = this.peopleService.addPeopleToCollectionIfMissing(
      this.peopleSharedCollection,
      patientAction.patient,
      patientAction.staff
    );
  }

  protected loadRelationshipsOptions(): void {
    this.peopleService
      .query()
      .pipe(map((res: HttpResponse<IPeople[]>) => res.body ?? []))
      .pipe(
        map((people: IPeople[]) =>
          this.peopleService.addPeopleToCollectionIfMissing(people, this.editForm.get('patient')!.value, this.editForm.get('staff')!.value)
        )
      )
      .subscribe((people: IPeople[]) => (this.peopleSharedCollection = people));
  }

  protected createFromForm(): IPatientAction {
    return {
      ...new PatientAction(),
      id: this.editForm.get(['id'])!.value,
      startDate: this.editForm.get(['startDate'])!.value ? dayjs(this.editForm.get(['startDate'])!.value, DATE_TIME_FORMAT) : undefined,
      endDate: this.editForm.get(['endDate'])!.value ? dayjs(this.editForm.get(['endDate'])!.value, DATE_TIME_FORMAT) : undefined,
      actionDescription: this.editForm.get(['actionDescription'])!.value,
      healtstatus: this.editForm.get(['healtstatus'])!.value,
      patient: this.editForm.get(['patient'])!.value,
      staff: this.editForm.get(['staff'])!.value,
    };
  }
}
