import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ActionHistory } from './action-history.model';
import { ActionHistoryPopupService } from './action-history-popup.service';
import { ActionHistoryService } from './action-history.service';
import { DataFile, DataFileService } from '../data-file';

@Component({
    selector: 'jhi-action-history-dialog',
    templateUrl: './action-history-dialog.component.html'
})
export class ActionHistoryDialogComponent implements OnInit {

    actionHistory: ActionHistory;
    isSaving: boolean;

    datafiles: DataFile[];
    dateDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private actionHistoryService: ActionHistoryService,
        private dataFileService: DataFileService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.dataFileService.query()
            .subscribe((res: HttpResponse<DataFile[]>) => { this.datafiles = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.actionHistory.id !== undefined) {
            this.subscribeToSaveResponse(
                this.actionHistoryService.update(this.actionHistory));
        } else {
            this.subscribeToSaveResponse(
                this.actionHistoryService.create(this.actionHistory));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ActionHistory>>) {
        result.subscribe((res: HttpResponse<ActionHistory>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: ActionHistory) {
        this.eventManager.broadcast({ name: 'actionHistoryListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackDataFileById(index: number, item: DataFile) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-action-history-popup',
    template: ''
})
export class ActionHistoryPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private actionHistoryPopupService: ActionHistoryPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.actionHistoryPopupService
                    .open(ActionHistoryDialogComponent as Component, params['id']);
            } else {
                this.actionHistoryPopupService
                    .open(ActionHistoryDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
