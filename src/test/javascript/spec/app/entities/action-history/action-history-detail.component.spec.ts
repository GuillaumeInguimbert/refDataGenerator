/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { RefDataGeneratorTestModule } from '../../../test.module';
import { ActionHistoryDetailComponent } from '../../../../../../main/webapp/app/entities/action-history/action-history-detail.component';
import { ActionHistoryService } from '../../../../../../main/webapp/app/entities/action-history/action-history.service';
import { ActionHistory } from '../../../../../../main/webapp/app/entities/action-history/action-history.model';

describe('Component Tests', () => {

    describe('ActionHistory Management Detail Component', () => {
        let comp: ActionHistoryDetailComponent;
        let fixture: ComponentFixture<ActionHistoryDetailComponent>;
        let service: ActionHistoryService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [RefDataGeneratorTestModule],
                declarations: [ActionHistoryDetailComponent],
                providers: [
                    ActionHistoryService
                ]
            })
            .overrideTemplate(ActionHistoryDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ActionHistoryDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ActionHistoryService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new ActionHistory(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.actionHistory).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
