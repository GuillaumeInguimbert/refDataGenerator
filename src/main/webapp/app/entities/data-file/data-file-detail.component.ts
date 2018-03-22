import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { DataFile } from './data-file.model';
import { DataFileService } from './data-file.service';

@Component({
    selector: 'jhi-data-file-detail',
    templateUrl: './data-file-detail.component.html'
})
export class DataFileDetailComponent implements OnInit, OnDestroy {

    dataFile: DataFile;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private dataFileService: DataFileService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInDataFiles();
    }

    load(id) {
        this.dataFileService.find(id)
            .subscribe((dataFileResponse: HttpResponse<DataFile>) => {
                this.dataFile = dataFileResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInDataFiles() {
        this.eventSubscriber = this.eventManager.subscribe(
            'dataFileListModification',
            (response) => this.load(this.dataFile.id)
        );
    }
}
