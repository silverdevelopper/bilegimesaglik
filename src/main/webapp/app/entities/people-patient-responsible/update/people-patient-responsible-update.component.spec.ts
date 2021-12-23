jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PeoplePatientResponsibleService } from '../service/people-patient-responsible.service';
import { IPeoplePatientResponsible, PeoplePatientResponsible } from '../people-patient-responsible.model';
import { IPeople } from 'app/entities/people/people.model';
import { PeopleService } from 'app/entities/people/service/people.service';

import { PeoplePatientResponsibleUpdateComponent } from './people-patient-responsible-update.component';

describe('PeoplePatientResponsible Management Update Component', () => {
  let comp: PeoplePatientResponsibleUpdateComponent;
  let fixture: ComponentFixture<PeoplePatientResponsibleUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let peoplePatientResponsibleService: PeoplePatientResponsibleService;
  let peopleService: PeopleService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [PeoplePatientResponsibleUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(PeoplePatientResponsibleUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PeoplePatientResponsibleUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    peoplePatientResponsibleService = TestBed.inject(PeoplePatientResponsibleService);
    peopleService = TestBed.inject(PeopleService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call People query and add missing value', () => {
      const peoplePatientResponsible: IPeoplePatientResponsible = { id: 456 };
      const patient: IPeople = { id: 25554 };
      peoplePatientResponsible.patient = patient;
      const responsiblePerson: IPeople = { id: 80996 };
      peoplePatientResponsible.responsiblePerson = responsiblePerson;

      const peopleCollection: IPeople[] = [{ id: 70606 }];
      jest.spyOn(peopleService, 'query').mockReturnValue(of(new HttpResponse({ body: peopleCollection })));
      const additionalPeople = [patient, responsiblePerson];
      const expectedCollection: IPeople[] = [...additionalPeople, ...peopleCollection];
      jest.spyOn(peopleService, 'addPeopleToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ peoplePatientResponsible });
      comp.ngOnInit();

      expect(peopleService.query).toHaveBeenCalled();
      expect(peopleService.addPeopleToCollectionIfMissing).toHaveBeenCalledWith(peopleCollection, ...additionalPeople);
      expect(comp.peopleSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const peoplePatientResponsible: IPeoplePatientResponsible = { id: 456 };
      const patient: IPeople = { id: 57896 };
      peoplePatientResponsible.patient = patient;
      const responsiblePerson: IPeople = { id: 73569 };
      peoplePatientResponsible.responsiblePerson = responsiblePerson;

      activatedRoute.data = of({ peoplePatientResponsible });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(peoplePatientResponsible));
      expect(comp.peopleSharedCollection).toContain(patient);
      expect(comp.peopleSharedCollection).toContain(responsiblePerson);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PeoplePatientResponsible>>();
      const peoplePatientResponsible = { id: 123 };
      jest.spyOn(peoplePatientResponsibleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ peoplePatientResponsible });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: peoplePatientResponsible }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(peoplePatientResponsibleService.update).toHaveBeenCalledWith(peoplePatientResponsible);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PeoplePatientResponsible>>();
      const peoplePatientResponsible = new PeoplePatientResponsible();
      jest.spyOn(peoplePatientResponsibleService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ peoplePatientResponsible });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: peoplePatientResponsible }));
      saveSubject.complete();

      // THEN
      expect(peoplePatientResponsibleService.create).toHaveBeenCalledWith(peoplePatientResponsible);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PeoplePatientResponsible>>();
      const peoplePatientResponsible = { id: 123 };
      jest.spyOn(peoplePatientResponsibleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ peoplePatientResponsible });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(peoplePatientResponsibleService.update).toHaveBeenCalledWith(peoplePatientResponsible);
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
