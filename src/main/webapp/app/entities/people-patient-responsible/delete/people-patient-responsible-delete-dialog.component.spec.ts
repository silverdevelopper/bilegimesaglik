jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { PeoplePatientResponsibleService } from '../service/people-patient-responsible.service';

import { PeoplePatientResponsibleDeleteDialogComponent } from './people-patient-responsible-delete-dialog.component';

describe('PeoplePatientResponsible Management Delete Component', () => {
  let comp: PeoplePatientResponsibleDeleteDialogComponent;
  let fixture: ComponentFixture<PeoplePatientResponsibleDeleteDialogComponent>;
  let service: PeoplePatientResponsibleService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [PeoplePatientResponsibleDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(PeoplePatientResponsibleDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PeoplePatientResponsibleDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(PeoplePatientResponsibleService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('Should call delete service on confirmDelete', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({})));

        // WHEN
        comp.confirmDelete(123);
        tick();

        // THEN
        expect(service.delete).toHaveBeenCalledWith(123);
        expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
      })
    ));

    it('Should not call delete service on clear', () => {
      // GIVEN
      jest.spyOn(service, 'delete');

      // WHEN
      comp.cancel();

      // THEN
      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});
