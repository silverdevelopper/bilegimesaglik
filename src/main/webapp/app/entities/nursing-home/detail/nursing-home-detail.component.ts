import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { INursingHome } from '../nursing-home.model';

@Component({
  selector: 'jhi-nursing-home-detail',
  templateUrl: './nursing-home-detail.component.html',
})
export class NursingHomeDetailComponent implements OnInit {
  nursingHome: INursingHome | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ nursingHome }) => {
      this.nursingHome = nursingHome;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
