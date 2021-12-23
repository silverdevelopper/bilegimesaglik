import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { INursingHome, NursingHome } from '../nursing-home.model';
import { NursingHomeService } from '../service/nursing-home.service';

@Component({
  selector: 'jhi-nursing-home-update',
  templateUrl: './nursing-home-update.component.html',
})
export class NursingHomeUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [],
    streetAddress: [],
    postalCode: [],
    city: [],
    country: [],
  });

  constructor(protected nursingHomeService: NursingHomeService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ nursingHome }) => {
      this.updateForm(nursingHome);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const nursingHome = this.createFromForm();
    if (nursingHome.id !== undefined) {
      this.subscribeToSaveResponse(this.nursingHomeService.update(nursingHome));
    } else {
      this.subscribeToSaveResponse(this.nursingHomeService.create(nursingHome));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<INursingHome>>): void {
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

  protected updateForm(nursingHome: INursingHome): void {
    this.editForm.patchValue({
      id: nursingHome.id,
      name: nursingHome.name,
      streetAddress: nursingHome.streetAddress,
      postalCode: nursingHome.postalCode,
      city: nursingHome.city,
      country: nursingHome.country,
    });
  }

  protected createFromForm(): INursingHome {
    return {
      ...new NursingHome(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      streetAddress: this.editForm.get(['streetAddress'])!.value,
      postalCode: this.editForm.get(['postalCode'])!.value,
      city: this.editForm.get(['city'])!.value,
      country: this.editForm.get(['country'])!.value,
    };
  }
}
