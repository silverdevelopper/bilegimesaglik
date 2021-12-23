jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IPatientTask, PatientTask } from '../patient-task.model';
import { PatientTaskService } from '../service/patient-task.service';

import { PatientTaskRoutingResolveService } from './patient-task-routing-resolve.service';

describe('PatientTask routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: PatientTaskRoutingResolveService;
  let service: PatientTaskService;
  let resultPatientTask: IPatientTask | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(PatientTaskRoutingResolveService);
    service = TestBed.inject(PatientTaskService);
    resultPatientTask = undefined;
  });

  describe('resolve', () => {
    it('should return IPatientTask returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPatientTask = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPatientTask).toEqual({ id: 123 });
    });

    it('should return new IPatientTask if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPatientTask = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultPatientTask).toEqual(new PatientTask());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as PatientTask })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPatientTask = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPatientTask).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
