jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PatientTaskService } from '../service/patient-task.service';
import { IPatientTask, PatientTask } from '../patient-task.model';
import { IPeople } from 'app/entities/people/people.model';
import { PeopleService } from 'app/entities/people/service/people.service';

import { PatientTaskUpdateComponent } from './patient-task-update.component';

describe('PatientTask Management Update Component', () => {
  let comp: PatientTaskUpdateComponent;
  let fixture: ComponentFixture<PatientTaskUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let patientTaskService: PatientTaskService;
  let peopleService: PeopleService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [PatientTaskUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(PatientTaskUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PatientTaskUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    patientTaskService = TestBed.inject(PatientTaskService);
    peopleService = TestBed.inject(PeopleService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call People query and add missing value', () => {
      const patientTask: IPatientTask = { id: 456 };
      const patient: IPeople = { id: 45959 };
      patientTask.patient = patient;

      const peopleCollection: IPeople[] = [{ id: 33795 }];
      jest.spyOn(peopleService, 'query').mockReturnValue(of(new HttpResponse({ body: peopleCollection })));
      const additionalPeople = [patient];
      const expectedCollection: IPeople[] = [...additionalPeople, ...peopleCollection];
      jest.spyOn(peopleService, 'addPeopleToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ patientTask });
      comp.ngOnInit();

      expect(peopleService.query).toHaveBeenCalled();
      expect(peopleService.addPeopleToCollectionIfMissing).toHaveBeenCalledWith(peopleCollection, ...additionalPeople);
      expect(comp.peopleSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const patientTask: IPatientTask = { id: 456 };
      const patient: IPeople = { id: 36445 };
      patientTask.patient = patient;

      activatedRoute.data = of({ patientTask });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(patientTask));
      expect(comp.peopleSharedCollection).toContain(patient);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PatientTask>>();
      const patientTask = { id: 123 };
      jest.spyOn(patientTaskService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ patientTask });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: patientTask }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(patientTaskService.update).toHaveBeenCalledWith(patientTask);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PatientTask>>();
      const patientTask = new PatientTask();
      jest.spyOn(patientTaskService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ patientTask });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: patientTask }));
      saveSubject.complete();

      // THEN
      expect(patientTaskService.create).toHaveBeenCalledWith(patientTask);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PatientTask>>();
      const patientTask = { id: 123 };
      jest.spyOn(patientTaskService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ patientTask });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(patientTaskService.update).toHaveBeenCalledWith(patientTask);
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
