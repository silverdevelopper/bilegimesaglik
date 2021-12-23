import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PatientActionDetailComponent } from './patient-action-detail.component';

describe('PatientAction Management Detail Component', () => {
  let comp: PatientActionDetailComponent;
  let fixture: ComponentFixture<PatientActionDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PatientActionDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ patientAction: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PatientActionDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PatientActionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load patientAction on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.patientAction).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
