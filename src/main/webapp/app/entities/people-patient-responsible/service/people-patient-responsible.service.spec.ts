import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPeoplePatientResponsible, PeoplePatientResponsible } from '../people-patient-responsible.model';

import { PeoplePatientResponsibleService } from './people-patient-responsible.service';

describe('PeoplePatientResponsible Service', () => {
  let service: PeoplePatientResponsibleService;
  let httpMock: HttpTestingController;
  let elemDefault: IPeoplePatientResponsible;
  let expectedResult: IPeoplePatientResponsible | IPeoplePatientResponsible[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PeoplePatientResponsibleService);
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

    it('should create a PeoplePatientResponsible', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new PeoplePatientResponsible()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PeoplePatientResponsible', () => {
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

    it('should partial update a PeoplePatientResponsible', () => {
      const patchObject = Object.assign({}, new PeoplePatientResponsible());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PeoplePatientResponsible', () => {
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

    it('should delete a PeoplePatientResponsible', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPeoplePatientResponsibleToCollectionIfMissing', () => {
      it('should add a PeoplePatientResponsible to an empty array', () => {
        const peoplePatientResponsible: IPeoplePatientResponsible = { id: 123 };
        expectedResult = service.addPeoplePatientResponsibleToCollectionIfMissing([], peoplePatientResponsible);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(peoplePatientResponsible);
      });

      it('should not add a PeoplePatientResponsible to an array that contains it', () => {
        const peoplePatientResponsible: IPeoplePatientResponsible = { id: 123 };
        const peoplePatientResponsibleCollection: IPeoplePatientResponsible[] = [
          {
            ...peoplePatientResponsible,
          },
          { id: 456 },
        ];
        expectedResult = service.addPeoplePatientResponsibleToCollectionIfMissing(
          peoplePatientResponsibleCollection,
          peoplePatientResponsible
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PeoplePatientResponsible to an array that doesn't contain it", () => {
        const peoplePatientResponsible: IPeoplePatientResponsible = { id: 123 };
        const peoplePatientResponsibleCollection: IPeoplePatientResponsible[] = [{ id: 456 }];
        expectedResult = service.addPeoplePatientResponsibleToCollectionIfMissing(
          peoplePatientResponsibleCollection,
          peoplePatientResponsible
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(peoplePatientResponsible);
      });

      it('should add only unique PeoplePatientResponsible to an array', () => {
        const peoplePatientResponsibleArray: IPeoplePatientResponsible[] = [{ id: 123 }, { id: 456 }, { id: 80785 }];
        const peoplePatientResponsibleCollection: IPeoplePatientResponsible[] = [{ id: 123 }];
        expectedResult = service.addPeoplePatientResponsibleToCollectionIfMissing(
          peoplePatientResponsibleCollection,
          ...peoplePatientResponsibleArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const peoplePatientResponsible: IPeoplePatientResponsible = { id: 123 };
        const peoplePatientResponsible2: IPeoplePatientResponsible = { id: 456 };
        expectedResult = service.addPeoplePatientResponsibleToCollectionIfMissing([], peoplePatientResponsible, peoplePatientResponsible2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(peoplePatientResponsible);
        expect(expectedResult).toContain(peoplePatientResponsible2);
      });

      it('should accept null and undefined values', () => {
        const peoplePatientResponsible: IPeoplePatientResponsible = { id: 123 };
        expectedResult = service.addPeoplePatientResponsibleToCollectionIfMissing([], null, peoplePatientResponsible, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(peoplePatientResponsible);
      });

      it('should return initial array if no PeoplePatientResponsible is added', () => {
        const peoplePatientResponsibleCollection: IPeoplePatientResponsible[] = [{ id: 123 }];
        expectedResult = service.addPeoplePatientResponsibleToCollectionIfMissing(peoplePatientResponsibleCollection, undefined, null);
        expect(expectedResult).toEqual(peoplePatientResponsibleCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
