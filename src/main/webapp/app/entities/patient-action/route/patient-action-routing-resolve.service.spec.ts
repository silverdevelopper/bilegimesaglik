jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IPatientAction, PatientAction } from '../patient-action.model';
import { PatientActionService } from '../service/patient-action.service';

import { PatientActionRoutingResolveService } from './patient-action-routing-resolve.service';

describe('PatientAction routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: PatientActionRoutingResolveService;
  let service: PatientActionService;
  let resultPatientAction: IPatientAction | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(PatientActionRoutingResolveService);
    service = TestBed.inject(PatientActionService);
    resultPatientAction = undefined;
  });

  describe('resolve', () => {
    it('should return IPatientAction returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPatientAction = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPatientAction).toEqual({ id: 123 });
    });

    it('should return new IPatientAction if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPatientAction = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultPatientAction).toEqual(new PatientAction());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as PatientAction })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPatientAction = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPatientAction).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
