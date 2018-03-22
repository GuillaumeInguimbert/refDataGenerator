import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { ActionHistoryComponent } from './action-history.component';
import { ActionHistoryDetailComponent } from './action-history-detail.component';
import { ActionHistoryPopupComponent } from './action-history-dialog.component';
import { ActionHistoryDeletePopupComponent } from './action-history-delete-dialog.component';

export const actionHistoryRoute: Routes = [
    {
        path: 'action-history',
        component: ActionHistoryComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ActionHistories'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'action-history/:id',
        component: ActionHistoryDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ActionHistories'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const actionHistoryPopupRoute: Routes = [
    {
        path: 'action-history-new',
        component: ActionHistoryPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ActionHistories'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'action-history/:id/edit',
        component: ActionHistoryPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ActionHistories'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'action-history/:id/delete',
        component: ActionHistoryDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ActionHistories'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
