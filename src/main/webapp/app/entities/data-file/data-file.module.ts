import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { RefDataGeneratorSharedModule } from '../../shared';
import {
    DataFileService,
    DataFilePopupService,
    DataFileComponent,
    DataFileDetailComponent,
    DataFileDialogComponent,
    DataFilePopupComponent,
    DataFileDeletePopupComponent,
    DataFileDeleteDialogComponent,
    dataFileRoute,
    dataFilePopupRoute,
} from './';

const ENTITY_STATES = [
    ...dataFileRoute,
    ...dataFilePopupRoute,
];

@NgModule({
    imports: [
        RefDataGeneratorSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        DataFileComponent,
        DataFileDetailComponent,
        DataFileDialogComponent,
        DataFileDeleteDialogComponent,
        DataFilePopupComponent,
        DataFileDeletePopupComponent,
    ],
    entryComponents: [
        DataFileComponent,
        DataFileDialogComponent,
        DataFilePopupComponent,
        DataFileDeleteDialogComponent,
        DataFileDeletePopupComponent,
    ],
    providers: [
        DataFileService,
        DataFilePopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class RefDataGeneratorDataFileModule {}
