import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PatientTaskDetailComponent } from './patient-task-detail.component';

describe('PatientTask Management Detail Component', () => {
  let comp: PatientTaskDetailComponent;
  let fixture: ComponentFixture<PatientTaskDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PatientTaskDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ patientTask: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PatientTaskDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PatientTaskDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load patientTask on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.patientTask).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
