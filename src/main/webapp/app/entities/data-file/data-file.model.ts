import { BaseEntity } from './../../shared';

export const enum Status {
    'NEW',
    'PROCESSING',
    'ERROR',
    'SUCCESS'
}

export const enum Type {
    'REF_DATA',
    'FBC_GROUP'
}

export class DataFile implements BaseEntity {
    constructor(
        public id?: number,
        public date?: any,
        public logicalName?: string,
        public status?: Status,
        public type?: Type,
        public version?: string,
        public sourcePath?: string,
        public tagetPath?: string,
        public actionHistories?: BaseEntity[],
    ) {
    }
}
