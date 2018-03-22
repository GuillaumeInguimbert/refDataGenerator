import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { RefDataGeneratorActionHistoryModule } from './action-history/action-history.module';
import { RefDataGeneratorDataFileModule } from './data-file/data-file.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        RefDataGeneratorActionHistoryModule,
        RefDataGeneratorDataFileModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class RefDataGeneratorEntityModule {}
