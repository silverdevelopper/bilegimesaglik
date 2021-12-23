jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IPeople, People } from '../people.model';
import { PeopleService } from '../service/people.service';

import { PeopleRoutingResolveService } from './people-routing-resolve.service';

describe('People routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: PeopleRoutingResolveService;
  let service: PeopleService;
  let resultPeople: IPeople | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(PeopleRoutingResolveService);
    service = TestBed.inject(PeopleService);
    resultPeople = undefined;
  });

  describe('resolve', () => {
    it('should return IPeople returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPeople = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPeople).toEqual({ id: 123 });
    });

    it('should return new IPeople if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPeople = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultPeople).toEqual(new People());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as People })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPeople = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPeople).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
