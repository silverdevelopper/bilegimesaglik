import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { INursingHome, NursingHome } from '../nursing-home.model';

import { NursingHomeService } from './nursing-home.service';

describe('NursingHome Service', () => {
  let service: NursingHomeService;
  let httpMock: HttpTestingController;
  let elemDefault: INursingHome;
  let expectedResult: INursingHome | INursingHome[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(NursingHomeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
      streetAddress: 'AAAAAAA',
      postalCode: 'AAAAAAA',
      city: 'AAAAAAA',
      country: 'AAAAAAA',
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

    it('should create a NursingHome', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new NursingHome()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a NursingHome', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          streetAddress: 'BBBBBB',
          postalCode: 'BBBBBB',
          city: 'BBBBBB',
          country: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a NursingHome', () => {
      const patchObject = Object.assign(
        {
          name: 'BBBBBB',
          country: 'BBBBBB',
        },
        new NursingHome()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of NursingHome', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          streetAddress: 'BBBBBB',
          postalCode: 'BBBBBB',
          city: 'BBBBBB',
          country: 'BBBBBB',
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

    it('should delete a NursingHome', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addNursingHomeToCollectionIfMissing', () => {
      it('should add a NursingHome to an empty array', () => {
        const nursingHome: INursingHome = { id: 123 };
        expectedResult = service.addNursingHomeToCollectionIfMissing([], nursingHome);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(nursingHome);
      });

      it('should not add a NursingHome to an array that contains it', () => {
        const nursingHome: INursingHome = { id: 123 };
        const nursingHomeCollection: INursingHome[] = [
          {
            ...nursingHome,
          },
          { id: 456 },
        ];
        expectedResult = service.addNursingHomeToCollectionIfMissing(nursingHomeCollection, nursingHome);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a NursingHome to an array that doesn't contain it", () => {
        const nursingHome: INursingHome = { id: 123 };
        const nursingHomeCollection: INursingHome[] = [{ id: 456 }];
        expectedResult = service.addNursingHomeToCollectionIfMissing(nursingHomeCollection, nursingHome);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(nursingHome);
      });

      it('should add only unique NursingHome to an array', () => {
        const nursingHomeArray: INursingHome[] = [{ id: 123 }, { id: 456 }, { id: 8201 }];
        const nursingHomeCollection: INursingHome[] = [{ id: 123 }];
        expectedResult = service.addNursingHomeToCollectionIfMissing(nursingHomeCollection, ...nursingHomeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const nursingHome: INursingHome = { id: 123 };
        const nursingHome2: INursingHome = { id: 456 };
        expectedResult = service.addNursingHomeToCollectionIfMissing([], nursingHome, nursingHome2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(nursingHome);
        expect(expectedResult).toContain(nursingHome2);
      });

      it('should accept null and undefined values', () => {
        const nursingHome: INursingHome = { id: 123 };
        expectedResult = service.addNursingHomeToCollectionIfMissing([], null, nursingHome, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(nursingHome);
      });

      it('should return initial array if no NursingHome is added', () => {
        const nursingHomeCollection: INursingHome[] = [{ id: 123 }];
        expectedResult = service.addNursingHomeToCollectionIfMissing(nursingHomeCollection, undefined, null);
        expect(expectedResult).toEqual(nursingHomeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
