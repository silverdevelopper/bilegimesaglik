import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { INursingHomePatient, NursingHomePatient } from '../nursing-home-patient.model';

import { NursingHomePatientService } from './nursing-home-patient.service';

describe('NursingHomePatient Service', () => {
  let service: NursingHomePatientService;
  let httpMock: HttpTestingController;
  let elemDefault: INursingHomePatient;
  let expectedResult: INursingHomePatient | INursingHomePatient[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(NursingHomePatientService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
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

    it('should create a NursingHomePatient', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new NursingHomePatient()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a NursingHomePatient', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a NursingHomePatient', () => {
      const patchObject = Object.assign({}, new NursingHomePatient());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of NursingHomePatient', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
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

    it('should delete a NursingHomePatient', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addNursingHomePatientToCollectionIfMissing', () => {
      it('should add a NursingHomePatient to an empty array', () => {
        const nursingHomePatient: INursingHomePatient = { id: 123 };
        expectedResult = service.addNursingHomePatientToCollectionIfMissing([], nursingHomePatient);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(nursingHomePatient);
      });

      it('should not add a NursingHomePatient to an array that contains it', () => {
        const nursingHomePatient: INursingHomePatient = { id: 123 };
        const nursingHomePatientCollection: INursingHomePatient[] = [
          {
            ...nursingHomePatient,
          },
          { id: 456 },
        ];
        expectedResult = service.addNursingHomePatientToCollectionIfMissing(nursingHomePatientCollection, nursingHomePatient);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a NursingHomePatient to an array that doesn't contain it", () => {
        const nursingHomePatient: INursingHomePatient = { id: 123 };
        const nursingHomePatientCollection: INursingHomePatient[] = [{ id: 456 }];
        expectedResult = service.addNursingHomePatientToCollectionIfMissing(nursingHomePatientCollection, nursingHomePatient);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(nursingHomePatient);
      });

      it('should add only unique NursingHomePatient to an array', () => {
        const nursingHomePatientArray: INursingHomePatient[] = [{ id: 123 }, { id: 456 }, { id: 35708 }];
        const nursingHomePatientCollection: INursingHomePatient[] = [{ id: 123 }];
        expectedResult = service.addNursingHomePatientToCollectionIfMissing(nursingHomePatientCollection, ...nursingHomePatientArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const nursingHomePatient: INursingHomePatient = { id: 123 };
        const nursingHomePatient2: INursingHomePatient = { id: 456 };
        expectedResult = service.addNursingHomePatientToCollectionIfMissing([], nursingHomePatient, nursingHomePatient2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(nursingHomePatient);
        expect(expectedResult).toContain(nursingHomePatient2);
      });

      it('should accept null and undefined values', () => {
        const nursingHomePatient: INursingHomePatient = { id: 123 };
        expectedResult = service.addNursingHomePatientToCollectionIfMissing([], null, nursingHomePatient, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(nursingHomePatient);
      });

      it('should return initial array if no NursingHomePatient is added', () => {
        const nursingHomePatientCollection: INursingHomePatient[] = [{ id: 123 }];
        expectedResult = service.addNursingHomePatientToCollectionIfMissing(nursingHomePatientCollection, undefined, null);
        expect(expectedResult).toEqual(nursingHomePatientCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
