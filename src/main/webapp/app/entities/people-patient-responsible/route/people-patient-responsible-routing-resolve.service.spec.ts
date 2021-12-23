jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IPeoplePatientResponsible, PeoplePatientResponsible } from '../people-patient-responsible.model';
import { PeoplePatientResponsibleService } from '../service/people-patient-responsible.service';

import { PeoplePatientResponsibleRoutingResolveService } from './people-patient-responsible-routing-resolve.service';

describe('PeoplePatientResponsible routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: PeoplePatientResponsibleRoutingResolveService;
  let service: PeoplePatientResponsibleService;
  let resultPeoplePatientResponsible: IPeoplePatientResponsible | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(PeoplePatientResponsibleRoutingResolveService);
    service = TestBed.inject(PeoplePatientResponsibleService);
    resultPeoplePatientResponsible = undefined;
  });

  describe('resolve', () => {
    it('should return IPeoplePatientResponsible returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPeoplePatientResponsible = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPeoplePatientResponsible).toEqual({ id: 123 });
    });

    it('should return new IPeoplePatientResponsible if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPeoplePatientResponsible = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultPeoplePatientResponsible).toEqual(new PeoplePatientResponsible());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as PeoplePatientResponsible })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPeoplePatientResponsible = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPeoplePatientResponsible).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
