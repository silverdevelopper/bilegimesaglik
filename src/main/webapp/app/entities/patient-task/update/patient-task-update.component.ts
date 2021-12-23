import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPatientTask, PatientTask } from '../patient-task.model';
import { PatientTaskService } from '../service/patient-task.service';
import { IPeople } from 'app/entities/people/people.model';
import { PeopleService } from 'app/entities/people/service/people.service';

@Component({
  selector: 'jhi-patient-task-update',
  templateUrl: './patient-task-update.component.html',
})
export class PatientTaskUpdateComponent implements OnInit {
  isSaving = false;

  peopleSharedCollection: IPeople[] = [];

  editForm = this.fb.group({
    id: [],
    title: [],
    description: [],
    schedule: [],
    patient: [],
  });

  constructor(
    protected patientTaskService: PatientTaskService,
    protected peopleService: PeopleService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ patientTask }) => {
      this.updateForm(patientTask);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const patientTask = this.createFromForm();
    if (patientTask.id !== undefined) {
      this.subscribeToSaveResponse(this.patientTaskService.update(patientTask));
    } else {
      this.subscribeToSaveResponse(this.patientTaskService.create(patientTask));
    }
  }

  trackPeopleById(index: number, item: IPeople): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPatientTask>>): void {
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

  protected updateForm(patientTask: IPatientTask): void {
    this.editForm.patchValue({
      id: patientTask.id,
      title: patientTask.title,
      description: patientTask.description,
      schedule: patientTask.schedule,
      patient: patientTask.patient,
    });

    this.peopleSharedCollection = this.peopleService.addPeopleToCollectionIfMissing(this.peopleSharedCollection, patientTask.patient);
  }

  protected loadRelationshipsOptions(): void {
    this.peopleService
      .query()
      .pipe(map((res: HttpResponse<IPeople[]>) => res.body ?? []))
      .pipe(map((people: IPeople[]) => this.peopleService.addPeopleToCollectionIfMissing(people, this.editForm.get('patient')!.value)))
      .subscribe((people: IPeople[]) => (this.peopleSharedCollection = people));
  }

  protected createFromForm(): IPatientTask {
    return {
      ...new PatientTask(),
      id: this.editForm.get(['id'])!.value,
      title: this.editForm.get(['title'])!.value,
      description: this.editForm.get(['description'])!.value,
      schedule: this.editForm.get(['schedule'])!.value,
      patient: this.editForm.get(['patient'])!.value,
    };
  }
}
