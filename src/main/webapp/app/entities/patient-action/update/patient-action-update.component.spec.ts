jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PatientActionService } from '../service/patient-action.service';
import { IPatientAction, PatientAction } from '../patient-action.model';
import { IPeople } from 'app/entities/people/people.model';
import { PeopleService } from 'app/entities/people/service/people.service';

import { PatientActionUpdateComponent } from './patient-action-update.component';

describe('PatientAction Management Update Component', () => {
  let comp: PatientActionUpdateComponent;
  let fixture: ComponentFixture<PatientActionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let patientActionService: PatientActionService;
  let peopleService: PeopleService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [PatientActionUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(PatientActionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PatientActionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    patientActionService = TestBed.inject(PatientActionService);
    peopleService = TestBed.inject(PeopleService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call People query and add missing value', () => {
      const patientAction: IPatientAction = { id: 456 };
      const patient: IPeople = { id: 81946 };
      patientAction.patient = patient;
      const staff: IPeople = { id: 34235 };
      patientAction.staff = staff;

      const peopleCollection: IPeople[] = [{ id: 4985 }];
      jest.spyOn(peopleService, 'query').mockReturnValue(of(new HttpResponse({ body: peopleCollection })));
      const additionalPeople = [patient, staff];
      const expectedCollection: IPeople[] = [...additionalPeople, ...peopleCollection];
      jest.spyOn(peopleService, 'addPeopleToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ patientAction });
      comp.ngOnInit();

      expect(peopleService.query).toHaveBeenCalled();
      expect(peopleService.addPeopleToCollectionIfMissing).toHaveBeenCalledWith(peopleCollection, ...additionalPeople);
      expect(comp.peopleSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const patientAction: IPatientAction = { id: 456 };
      const patient: IPeople = { id: 20657 };
      patientAction.patient = patient;
      const staff: IPeople = { id: 27472 };
      patientAction.staff = staff;

      activatedRoute.data = of({ patientAction });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(patientAction));
      expect(comp.peopleSharedCollection).toContain(patient);
      expect(comp.peopleSharedCollection).toContain(staff);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PatientAction>>();
      const patientAction = { id: 123 };
      jest.spyOn(patientActionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ patientAction });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: patientAction }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(patientActionService.update).toHaveBeenCalledWith(patientAction);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PatientAction>>();
      const patientAction = new PatientAction();
      jest.spyOn(patientActionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ patientAction });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: patientAction }));
      saveSubject.complete();

      // THEN
      expect(patientActionService.create).toHaveBeenCalledWith(patientAction);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PatientAction>>();
      const patientAction = { id: 123 };
      jest.spyOn(patientActionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ patientAction });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(patientActionService.update).toHaveBeenCalledWith(patientAction);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackPeopleById', () => {
      it('Should return tracked People primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPeopleById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
