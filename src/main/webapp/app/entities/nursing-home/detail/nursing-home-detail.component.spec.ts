import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { NursingHomeDetailComponent } from './nursing-home-detail.component';

describe('NursingHome Management Detail Component', () => {
  let comp: NursingHomeDetailComponent;
  let fixture: ComponentFixture<NursingHomeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [NursingHomeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ nursingHome: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(NursingHomeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(NursingHomeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load nursingHome on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.nursingHome).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
