import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { DataFile } from './data-file.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<DataFile>;

@Injectable()
export class DataFileService {

    private resourceUrl =  SERVER_API_URL + 'api/data-files';

    constructor(private http: HttpClient, private dateUtils: JhiDateUtils) { }

    create(dataFile: DataFile): Observable<EntityResponseType> {
        const copy = this.convert(dataFile);
        return this.http.post<DataFile>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(dataFile: DataFile): Observable<EntityResponseType> {
        const copy = this.convert(dataFile);
        return this.http.put<DataFile>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<DataFile>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<DataFile[]>> {
        const options = createRequestOption(req);
        return this.http.get<DataFile[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<DataFile[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: DataFile = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<DataFile[]>): HttpResponse<DataFile[]> {
        const jsonResponse: DataFile[] = res.body;
        const body: DataFile[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to DataFile.
     */
    private convertItemFromServer(dataFile: DataFile): DataFile {
        const copy: DataFile = Object.assign({}, dataFile);
        copy.date = this.dateUtils
            .convertLocalDateFromServer(dataFile.date);
        return copy;
    }

    /**
     * Convert a DataFile to a JSON which can be sent to the server.
     */
    private convert(dataFile: DataFile): DataFile {
        const copy: DataFile = Object.assign({}, dataFile);
        copy.date = this.dateUtils
            .convertLocalDateToServer(dataFile.date);
        return copy;
    }
}
