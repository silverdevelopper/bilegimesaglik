import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { NursingHomePatientDetailComponent } from './nursing-home-patient-detail.component';

describe('NursingHomePatient Management Detail Component', () => {
  let comp: NursingHomePatientDetailComponent;
  let fixture: ComponentFixture<NursingHomePatientDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [NursingHomePatientDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ nursingHomePatient: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(NursingHomePatientDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(NursingHomePatientDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load nursingHomePatient on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.nursingHomePatient).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
