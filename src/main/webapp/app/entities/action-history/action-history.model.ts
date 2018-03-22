import { BaseEntity } from './../../shared';

export class ActionHistory implements BaseEntity {
    constructor(
        public id?: number,
        public user?: string,
        public message?: string,
        public date?: any,
        public dataFileId?: number,
    ) {
    }
}
