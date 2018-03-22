import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { DataFile } from './data-file.model';
import { DataFileService } from './data-file.service';

@Injectable()
export class DataFilePopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private dataFileService: DataFileService

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
                this.dataFileService.find(id)
                    .subscribe((dataFileResponse: HttpResponse<DataFile>) => {
                        const dataFile: DataFile = dataFileResponse.body;
                        if (dataFile.date) {
                            dataFile.date = {
                                year: dataFile.date.getFullYear(),
                                month: dataFile.date.getMonth() + 1,
                                day: dataFile.date.getDate()
                            };
                        }
                        this.ngbModalRef = this.dataFileModalRef(component, dataFile);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.dataFileModalRef(component, new DataFile());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    dataFileModalRef(component: Component, dataFile: DataFile): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.dataFile = dataFile;
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
