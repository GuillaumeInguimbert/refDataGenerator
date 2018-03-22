import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { RefDataGeneratorSharedModule } from '../../shared';
import {
    ActionHistoryService,
    ActionHistoryPopupService,
    ActionHistoryComponent,
    ActionHistoryDetailComponent,
    ActionHistoryDialogComponent,
    ActionHistoryPopupComponent,
    ActionHistoryDeletePopupComponent,
    ActionHistoryDeleteDialogComponent,
    actionHistoryRoute,
    actionHistoryPopupRoute,
} from './';

const ENTITY_STATES = [
    ...actionHistoryRoute,
    ...actionHistoryPopupRoute,
];

@NgModule({
    imports: [
        RefDataGeneratorSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        ActionHistoryComponent,
        ActionHistoryDetailComponent,
        ActionHistoryDialogComponent,
        ActionHistoryDeleteDialogComponent,
        ActionHistoryPopupComponent,
        ActionHistoryDeletePopupComponent,
    ],
    entryComponents: [
        ActionHistoryComponent,
        ActionHistoryDialogComponent,
        ActionHistoryPopupComponent,
        ActionHistoryDeleteDialogComponent,
        ActionHistoryDeletePopupComponent,
    ],
    providers: [
        ActionHistoryService,
        ActionHistoryPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class RefDataGeneratorActionHistoryModule {}
