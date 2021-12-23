import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PeoplePatientResponsibleDetailComponent } from './people-patient-responsible-detail.component';

describe('PeoplePatientResponsible Management Detail Component', () => {
  let comp: PeoplePatientResponsibleDetailComponent;
  let fixture: ComponentFixture<PeoplePatientResponsibleDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PeoplePatientResponsibleDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ peoplePatientResponsible: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PeoplePatientResponsibleDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PeoplePatientResponsibleDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load peoplePatientResponsible on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.peoplePatientResponsible).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
