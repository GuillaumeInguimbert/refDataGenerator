import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ActionHistory } from './action-history.model';
import { ActionHistoryPopupService } from './action-history-popup.service';
import { ActionHistoryService } from './action-history.service';

@Component({
    selector: 'jhi-action-history-delete-dialog',
    templateUrl: './action-history-delete-dialog.component.html'
})
export class ActionHistoryDeleteDialogComponent {

    actionHistory: ActionHistory;

    constructor(
        private actionHistoryService: ActionHistoryService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.actionHistoryService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'actionHistoryListModification',
                content: 'Deleted an actionHistory'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-action-history-delete-popup',
    template: ''
})
export class ActionHistoryDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private actionHistoryPopupService: ActionHistoryPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.actionHistoryPopupService
                .open(ActionHistoryDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
