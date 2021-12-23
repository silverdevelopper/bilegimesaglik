import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IPatientAction, PatientAction } from '../patient-action.model';

import { PatientActionService } from './patient-action.service';

describe('PatientAction Service', () => {
  let service: PatientActionService;
  let httpMock: HttpTestingController;
  let elemDefault: IPatientAction;
  let expectedResult: IPatientAction | IPatientAction[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PatientActionService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      startDate: currentDate,
      endDate: currentDate,
      actionDescription: 'AAAAAAA',
      healtstatus: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          startDate: currentDate.format(DATE_TIME_FORMAT),
          endDate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a PatientAction', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          startDate: currentDate.format(DATE_TIME_FORMAT),
          endDate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          startDate: currentDate,
          endDate: currentDate,
        },
        returnedFromService
      );

      service.create(new PatientAction()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PatientAction', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          startDate: currentDate.format(DATE_TIME_FORMAT),
          endDate: currentDate.format(DATE_TIME_FORMAT),
          actionDescription: 'BBBBBB',
          healtstatus: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          startDate: currentDate,
          endDate: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PatientAction', () => {
      const patchObject = Object.assign({}, new PatientAction());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          startDate: currentDate,
          endDate: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PatientAction', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          startDate: currentDate.format(DATE_TIME_FORMAT),
          endDate: currentDate.format(DATE_TIME_FORMAT),
          actionDescription: 'BBBBBB',
          healtstatus: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          startDate: currentDate,
          endDate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a PatientAction', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPatientActionToCollectionIfMissing', () => {
      it('should add a PatientAction to an empty array', () => {
        const patientAction: IPatientAction = { id: 123 };
        expectedResult = service.addPatientActionToCollectionIfMissing([], patientAction);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(patientAction);
      });

      it('should not add a PatientAction to an array that contains it', () => {
        const patientAction: IPatientAction = { id: 123 };
        const patientActionCollection: IPatientAction[] = [
          {
            ...patientAction,
          },
          { id: 456 },
        ];
        expectedResult = service.addPatientActionToCollectionIfMissing(patientActionCollection, patientAction);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PatientAction to an array that doesn't contain it", () => {
        const patientAction: IPatientAction = { id: 123 };
        const patientActionCollection: IPatientAction[] = [{ id: 456 }];
        expectedResult = service.addPatientActionToCollectionIfMissing(patientActionCollection, patientAction);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(patientAction);
      });

      it('should add only unique PatientAction to an array', () => {
        const patientActionArray: IPatientAction[] = [{ id: 123 }, { id: 456 }, { id: 26201 }];
        const patientActionCollection: IPatientAction[] = [{ id: 123 }];
        expectedResult = service.addPatientActionToCollectionIfMissing(patientActionCollection, ...patientActionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const patientAction: IPatientAction = { id: 123 };
        const patientAction2: IPatientAction = { id: 456 };
        expectedResult = service.addPatientActionToCollectionIfMissing([], patientAction, patientAction2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(patientAction);
        expect(expectedResult).toContain(patientAction2);
      });

      it('should accept null and undefined values', () => {
        const patientAction: IPatientAction = { id: 123 };
        expectedResult = service.addPatientActionToCollectionIfMissing([], null, patientAction, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(patientAction);
      });

      it('should return initial array if no PatientAction is added', () => {
        const patientActionCollection: IPatientAction[] = [{ id: 123 }];
        expectedResult = service.addPatientActionToCollectionIfMissing(patientActionCollection, undefined, null);
        expect(expectedResult).toEqual(patientActionCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
