/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { RefDataGeneratorTestModule } from '../../../test.module';
import { ActionHistoryDialogComponent } from '../../../../../../main/webapp/app/entities/action-history/action-history-dialog.component';
import { ActionHistoryService } from '../../../../../../main/webapp/app/entities/action-history/action-history.service';
import { ActionHistory } from '../../../../../../main/webapp/app/entities/action-history/action-history.model';
import { DataFileService } from '../../../../../../main/webapp/app/entities/data-file';

describe('Component Tests', () => {

    describe('ActionHistory Management Dialog Component', () => {
        let comp: ActionHistoryDialogComponent;
        let fixture: ComponentFixture<ActionHistoryDialogComponent>;
        let service: ActionHistoryService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [RefDataGeneratorTestModule],
                declarations: [ActionHistoryDialogComponent],
                providers: [
                    DataFileService,
                    ActionHistoryService
                ]
            })
            .overrideTemplate(ActionHistoryDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ActionHistoryDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ActionHistoryService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new ActionHistory(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.actionHistory = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'actionHistoryListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new ActionHistory();
                        spyOn(service, 'create').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.actionHistory = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'actionHistoryListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
