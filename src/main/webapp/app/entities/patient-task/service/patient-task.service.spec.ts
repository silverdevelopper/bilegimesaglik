import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPatientTask, PatientTask } from '../patient-task.model';

import { PatientTaskService } from './patient-task.service';

describe('PatientTask Service', () => {
  let service: PatientTaskService;
  let httpMock: HttpTestingController;
  let elemDefault: IPatientTask;
  let expectedResult: IPatientTask | IPatientTask[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PatientTaskService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      title: 'AAAAAAA',
      description: 'AAAAAAA',
      schedule: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a PatientTask', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new PatientTask()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PatientTask', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          title: 'BBBBBB',
          description: 'BBBBBB',
          schedule: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PatientTask', () => {
      const patchObject = Object.assign(
        {
          title: 'BBBBBB',
          schedule: 'BBBBBB',
        },
        new PatientTask()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PatientTask', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          title: 'BBBBBB',
          description: 'BBBBBB',
          schedule: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a PatientTask', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPatientTaskToCollectionIfMissing', () => {
      it('should add a PatientTask to an empty array', () => {
        const patientTask: IPatientTask = { id: 123 };
        expectedResult = service.addPatientTaskToCollectionIfMissing([], patientTask);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(patientTask);
      });

      it('should not add a PatientTask to an array that contains it', () => {
        const patientTask: IPatientTask = { id: 123 };
        const patientTaskCollection: IPatientTask[] = [
          {
            ...patientTask,
          },
          { id: 456 },
        ];
        expectedResult = service.addPatientTaskToCollectionIfMissing(patientTaskCollection, patientTask);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PatientTask to an array that doesn't contain it", () => {
        const patientTask: IPatientTask = { id: 123 };
        const patientTaskCollection: IPatientTask[] = [{ id: 456 }];
        expectedResult = service.addPatientTaskToCollectionIfMissing(patientTaskCollection, patientTask);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(patientTask);
      });

      it('should add only unique PatientTask to an array', () => {
        const patientTaskArray: IPatientTask[] = [{ id: 123 }, { id: 456 }, { id: 44787 }];
        const patientTaskCollection: IPatientTask[] = [{ id: 123 }];
        expectedResult = service.addPatientTaskToCollectionIfMissing(patientTaskCollection, ...patientTaskArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const patientTask: IPatientTask = { id: 123 };
        const patientTask2: IPatientTask = { id: 456 };
        expectedResult = service.addPatientTaskToCollectionIfMissing([], patientTask, patientTask2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(patientTask);
        expect(expectedResult).toContain(patientTask2);
      });

      it('should accept null and undefined values', () => {
        const patientTask: IPatientTask = { id: 123 };
        expectedResult = service.addPatientTaskToCollectionIfMissing([], null, patientTask, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(patientTask);
      });

      it('should return initial array if no PatientTask is added', () => {
        const patientTaskCollection: IPatientTask[] = [{ id: 123 }];
        expectedResult = service.addPatientTaskToCollectionIfMissing(patientTaskCollection, undefined, null);
        expect(expectedResult).toEqual(patientTaskCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
