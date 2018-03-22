import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { ActionHistory } from './action-history.model';
import { ActionHistoryService } from './action-history.service';

@Component({
    selector: 'jhi-action-history-detail',
    templateUrl: './action-history-detail.component.html'
})
export class ActionHistoryDetailComponent implements OnInit, OnDestroy {

    actionHistory: ActionHistory;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private actionHistoryService: ActionHistoryService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInActionHistories();
    }

    load(id) {
        this.actionHistoryService.find(id)
            .subscribe((actionHistoryResponse: HttpResponse<ActionHistory>) => {
                this.actionHistory = actionHistoryResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInActionHistories() {
        this.eventSubscriber = this.eventManager.subscribe(
            'actionHistoryListModification',
            (response) => this.load(this.actionHistory.id)
        );
    }
}
