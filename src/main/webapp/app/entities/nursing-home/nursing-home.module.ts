import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { NursingHomeComponent } from './list/nursing-home.component';
import { NursingHomeDetailComponent } from './detail/nursing-home-detail.component';
import { NursingHomeUpdateComponent } from './update/nursing-home-update.component';
import { NursingHomeDeleteDialogComponent } from './delete/nursing-home-delete-dialog.component';
import { NursingHomeRoutingModule } from './route/nursing-home-routing.module';

@NgModule({
  imports: [SharedModule, NursingHomeRoutingModule],
  declarations: [NursingHomeComponent, NursingHomeDetailComponent, NursingHomeUpdateComponent, NursingHomeDeleteDialogComponent],
  entryComponents: [NursingHomeDeleteDialogComponent],
})
export class NursingHomeModule {}
