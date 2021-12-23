jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { NursingHomePatientService } from '../service/nursing-home-patient.service';
import { INursingHomePatient, NursingHomePatient } from '../nursing-home-patient.model';
import { IPeople } from 'app/entities/people/people.model';
import { PeopleService } from 'app/entities/people/service/people.service';
import { INursingHome } from 'app/entities/nursing-home/nursing-home.model';
import { NursingHomeService } from 'app/entities/nursing-home/service/nursing-home.service';

import { NursingHomePatientUpdateComponent } from './nursing-home-patient-update.component';

describe('NursingHomePatient Management Update Component', () => {
  let comp: NursingHomePatientUpdateComponent;
  let fixture: ComponentFixture<NursingHomePatientUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let nursingHomePatientService: NursingHomePatientService;
  let peopleService: PeopleService;
  let nursingHomeService: NursingHomeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [NursingHomePatientUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(NursingHomePatientUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(NursingHomePatientUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    nursingHomePatientService = TestBed.inject(NursingHomePatientService);
    peopleService = TestBed.inject(PeopleService);
    nursingHomeService = TestBed.inject(NursingHomeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call People query and add missing value', () => {
      const nursingHomePatient: INursingHomePatient = { id: 456 };
      const patient: IPeople = { id: 8922 };
      nursingHomePatient.patient = patient;

      const peopleCollection: IPeople[] = [{ id: 38376 }];
      jest.spyOn(peopleService, 'query').mockReturnValue(of(new HttpResponse({ body: peopleCollection })));
      const additionalPeople = [patient];
      const expectedCollection: IPeople[] = [...additionalPeople, ...peopleCollection];
      jest.spyOn(peopleService, 'addPeopleToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ nursingHomePatient });
      comp.ngOnInit();

      expect(peopleService.query).toHaveBeenCalled();
      expect(peopleService.addPeopleToCollectionIfMissing).toHaveBeenCalledWith(peopleCollection, ...additionalPeople);
      expect(comp.peopleSharedCollection).toEqual(expectedCollection);
    });

    it('Should call NursingHome query and add missing value', () => {
      const nursingHomePatient: INursingHomePatient = { id: 456 };
      const nusingHome: INursingHome = { id: 52105 };
      nursingHomePatient.nusingHome = nusingHome;

      const nursingHomeCollection: INursingHome[] = [{ id: 8795 }];
      jest.spyOn(nursingHomeService, 'query').mockReturnValue(of(new HttpResponse({ body: nursingHomeCollection })));
      const additionalNursingHomes = [nusingHome];
      const expectedCollection: INursingHome[] = [...additionalNursingHomes, ...nursingHomeCollection];
      jest.spyOn(nursingHomeService, 'addNursingHomeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ nursingHomePatient });
      comp.ngOnInit();

      expect(nursingHomeService.query).toHaveBeenCalled();
      expect(nursingHomeService.addNursingHomeToCollectionIfMissing).toHaveBeenCalledWith(nursingHomeCollection, ...additionalNursingHomes);
      expect(comp.nursingHomesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const nursingHomePatient: INursingHomePatient = { id: 456 };
      const patient: IPeople = { id: 54419 };
      nursingHomePatient.patient = patient;
      const nusingHome: INursingHome = { id: 28532 };
      nursingHomePatient.nusingHome = nusingHome;

      activatedRoute.data = of({ nursingHomePatient });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(nursingHomePatient));
      expect(comp.peopleSharedCollection).toContain(patient);
      expect(comp.nursingHomesSharedCollection).toContain(nusingHome);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<NursingHomePatient>>();
      const nursingHomePatient = { id: 123 };
      jest.spyOn(nursingHomePatientService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ nursingHomePatient });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: nursingHomePatient }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(nursingHomePatientService.update).toHaveBeenCalledWith(nursingHomePatient);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<NursingHomePatient>>();
      const nursingHomePatient = new NursingHomePatient();
      jest.spyOn(nursingHomePatientService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ nursingHomePatient });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: nursingHomePatient }));
      saveSubject.complete();

      // THEN
      expect(nursingHomePatientService.create).toHaveBeenCalledWith(nursingHomePatient);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<NursingHomePatient>>();
      const nursingHomePatient = { id: 123 };
      jest.spyOn(nursingHomePatientService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ nursingHomePatient });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(nursingHomePatientService.update).toHaveBeenCalledWith(nursingHomePatient);
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

    describe('trackNursingHomeById', () => {
      it('Should return tracked NursingHome primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackNursingHomeById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
