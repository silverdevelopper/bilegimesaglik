jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { NursingHomeService } from '../service/nursing-home.service';
import { INursingHome, NursingHome } from '../nursing-home.model';

import { NursingHomeUpdateComponent } from './nursing-home-update.component';

describe('NursingHome Management Update Component', () => {
  let comp: NursingHomeUpdateComponent;
  let fixture: ComponentFixture<NursingHomeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let nursingHomeService: NursingHomeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [NursingHomeUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(NursingHomeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(NursingHomeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    nursingHomeService = TestBed.inject(NursingHomeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const nursingHome: INursingHome = { id: 456 };

      activatedRoute.data = of({ nursingHome });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(nursingHome));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<NursingHome>>();
      const nursingHome = { id: 123 };
      jest.spyOn(nursingHomeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ nursingHome });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: nursingHome }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(nursingHomeService.update).toHaveBeenCalledWith(nursingHome);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<NursingHome>>();
      const nursingHome = new NursingHome();
      jest.spyOn(nursingHomeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ nursingHome });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: nursingHome }));
      saveSubject.complete();

      // THEN
      expect(nursingHomeService.create).toHaveBeenCalledWith(nursingHome);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<NursingHome>>();
      const nursingHome = { id: 123 };
      jest.spyOn(nursingHomeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ nursingHome });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(nursingHomeService.update).toHaveBeenCalledWith(nursingHome);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
