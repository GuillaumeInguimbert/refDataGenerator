import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { ActionHistory } from './action-history.model';
import { ActionHistoryService } from './action-history.service';

@Injectable()
export class ActionHistoryPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private actionHistoryService: ActionHistoryService

    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.actionHistoryService.find(id)
                    .subscribe((actionHistoryResponse: HttpResponse<ActionHistory>) => {
                        const actionHistory: ActionHistory = actionHistoryResponse.body;
                        if (actionHistory.date) {
                            actionHistory.date = {
                                year: actionHistory.date.getFullYear(),
                                month: actionHistory.date.getMonth() + 1,
                                day: actionHistory.date.getDate()
                            };
                        }
                        this.ngbModalRef = this.actionHistoryModalRef(component, actionHistory);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.actionHistoryModalRef(component, new ActionHistory());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    actionHistoryModalRef(component: Component, actionHistory: ActionHistory): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.actionHistory = actionHistory;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
}
