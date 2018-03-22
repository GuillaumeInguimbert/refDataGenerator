/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { RefDataGeneratorTestModule } from '../../../test.module';
import { DataFileComponent } from '../../../../../../main/webapp/app/entities/data-file/data-file.component';
import { DataFileService } from '../../../../../../main/webapp/app/entities/data-file/data-file.service';
import { DataFile } from '../../../../../../main/webapp/app/entities/data-file/data-file.model';

describe('Component Tests', () => {

    describe('DataFile Management Component', () => {
        let comp: DataFileComponent;
        let fixture: ComponentFixture<DataFileComponent>;
        let service: DataFileService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [RefDataGeneratorTestModule],
                declarations: [DataFileComponent],
                providers: [
                    DataFileService
                ]
            })
            .overrideTemplate(DataFileComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(DataFileComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DataFileService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new DataFile(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.dataFiles[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
