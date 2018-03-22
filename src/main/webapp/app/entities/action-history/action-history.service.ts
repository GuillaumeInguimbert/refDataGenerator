import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { ActionHistory } from './action-history.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<ActionHistory>;

@Injectable()
export class ActionHistoryService {

    private resourceUrl =  SERVER_API_URL + 'api/action-histories';

    constructor(private http: HttpClient, private dateUtils: JhiDateUtils) { }

    create(actionHistory: ActionHistory): Observable<EntityResponseType> {
        const copy = this.convert(actionHistory);
        return this.http.post<ActionHistory>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(actionHistory: ActionHistory): Observable<EntityResponseType> {
        const copy = this.convert(actionHistory);
        return this.http.put<ActionHistory>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ActionHistory>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<ActionHistory[]>> {
        const options = createRequestOption(req);
        return this.http.get<ActionHistory[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<ActionHistory[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: ActionHistory = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<ActionHistory[]>): HttpResponse<ActionHistory[]> {
        const jsonResponse: ActionHistory[] = res.body;
        const body: ActionHistory[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to ActionHistory.
     */
    private convertItemFromServer(actionHistory: ActionHistory): ActionHistory {
        const copy: ActionHistory = Object.assign({}, actionHistory);
        copy.date = this.dateUtils
            .convertLocalDateFromServer(actionHistory.date);
        return copy;
    }

    /**
     * Convert a ActionHistory to a JSON which can be sent to the server.
     */
    private convert(actionHistory: ActionHistory): ActionHistory {
        const copy: ActionHistory = Object.assign({}, actionHistory);
        copy.date = this.dateUtils
            .convertLocalDateToServer(actionHistory.date);
        return copy;
    }
}
