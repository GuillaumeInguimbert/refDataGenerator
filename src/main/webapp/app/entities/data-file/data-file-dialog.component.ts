import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { DataFile } from './data-file.model';
import { DataFilePopupService } from './data-file-popup.service';
import { DataFileService } from './data-file.service';

@Component({
    selector: 'jhi-data-file-dialog',
    templateUrl: './data-file-dialog.component.html'
})
export class DataFileDialogComponent implements OnInit {

    dataFile: DataFile;
    isSaving: boolean;
    dateDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private dataFileService: DataFileService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.dataFile.id !== undefined) {
            this.subscribeToSaveResponse(
                this.dataFileService.update(this.dataFile));
        } else {
            this.subscribeToSaveResponse(
                this.dataFileService.create(this.dataFile));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<DataFile>>) {
        result.subscribe((res: HttpResponse<DataFile>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: DataFile) {
        this.eventManager.broadcast({ name: 'dataFileListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-data-file-popup',
    template: ''
})
export class DataFilePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private dataFilePopupService: DataFilePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.dataFilePopupService
                    .open(DataFileDialogComponent as Component, params['id']);
            } else {
                this.dataFilePopupService
                    .open(DataFileDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
