/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { RefDataGeneratorTestModule } from '../../../test.module';
import { ActionHistoryDeleteDialogComponent } from '../../../../../../main/webapp/app/entities/action-history/action-history-delete-dialog.component';
import { ActionHistoryService } from '../../../../../../main/webapp/app/entities/action-history/action-history.service';

describe('Component Tests', () => {

    describe('ActionHistory Management Delete Component', () => {
        let comp: ActionHistoryDeleteDialogComponent;
        let fixture: ComponentFixture<ActionHistoryDeleteDialogComponent>;
        let service: ActionHistoryService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [RefDataGeneratorTestModule],
                declarations: [ActionHistoryDeleteDialogComponent],
                providers: [
                    ActionHistoryService
                ]
            })
            .overrideTemplate(ActionHistoryDeleteDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ActionHistoryDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ActionHistoryService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        spyOn(service, 'delete').and.returnValue(Observable.of({}));

                        // WHEN
                        comp.confirmDelete(123);
                        tick();

                        // THEN
                        expect(service.delete).toHaveBeenCalledWith(123);
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
