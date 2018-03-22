/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { RefDataGeneratorTestModule } from '../../../test.module';
import { DataFileDetailComponent } from '../../../../../../main/webapp/app/entities/data-file/data-file-detail.component';
import { DataFileService } from '../../../../../../main/webapp/app/entities/data-file/data-file.service';
import { DataFile } from '../../../../../../main/webapp/app/entities/data-file/data-file.model';

describe('Component Tests', () => {

    describe('DataFile Management Detail Component', () => {
        let comp: DataFileDetailComponent;
        let fixture: ComponentFixture<DataFileDetailComponent>;
        let service: DataFileService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [RefDataGeneratorTestModule],
                declarations: [DataFileDetailComponent],
                providers: [
                    DataFileService
                ]
            })
            .overrideTemplate(DataFileDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(DataFileDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DataFileService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new DataFile(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.dataFile).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
