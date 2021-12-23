import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'people',
        data: { pageTitle: 'bilegimesaglikApp.people.home.title' },
        loadChildren: () => import('./people/people.module').then(m => m.PeopleModule),
      },
      {
        path: 'patient-task',
        data: { pageTitle: 'bilegimesaglikApp.patientTask.home.title' },
        loadChildren: () => import('./patient-task/patient-task.module').then(m => m.PatientTaskModule),
      },
      {
        path: 'nursing-home',
        data: { pageTitle: 'bilegimesaglikApp.nursingHome.home.title' },
        loadChildren: () => import('./nursing-home/nursing-home.module').then(m => m.NursingHomeModule),
      },
      {
        path: 'patient-action',
        data: { pageTitle: 'bilegimesaglikApp.patientAction.home.title' },
        loadChildren: () => import('./patient-action/patient-action.module').then(m => m.PatientActionModule),
      },
      {
        path: 'people-patient-responsible',
        data: { pageTitle: 'bilegimesaglikApp.peoplePatientResponsible.home.title' },
        loadChildren: () =>
          import('./people-patient-responsible/people-patient-responsible.module').then(m => m.PeoplePatientResponsibleModule),
      },
      {
        path: 'nursing-home-patient',
        data: { pageTitle: 'bilegimesaglikApp.nursingHomePatient.home.title' },
        loadChildren: () => import('./nursing-home-patient/nursing-home-patient.module').then(m => m.NursingHomePatientModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
