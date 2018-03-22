/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { RefDataGeneratorTestModule } from '../../../test.module';
import { ActionHistoryComponent } from '../../../../../../main/webapp/app/entities/action-history/action-history.component';
import { ActionHistoryService } from '../../../../../../main/webapp/app/entities/action-history/action-history.service';
import { ActionHistory } from '../../../../../../main/webapp/app/entities/action-history/action-history.model';

describe('Component Tests', () => {

    describe('ActionHistory Management Component', () => {
        let comp: ActionHistoryComponent;
        let fixture: ComponentFixture<ActionHistoryComponent>;
        let service: ActionHistoryService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [RefDataGeneratorTestModule],
                declarations: [ActionHistoryComponent],
                providers: [
                    ActionHistoryService
                ]
            })
            .overrideTemplate(ActionHistoryComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ActionHistoryComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ActionHistoryService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new ActionHistory(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.actionHistories[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
