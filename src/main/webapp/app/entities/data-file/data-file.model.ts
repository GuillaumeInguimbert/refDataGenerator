import { BaseEntity } from './../../shared';

export const enum Type {
    'REF_DATA',
    'FBC_GROUP'
}

export class DataFile implements BaseEntity {
    constructor(
        public id?: number,
        public date?: any,
        public logicalName?: string,
        public type?: Type,
        public version?: string,
        public path?: string,
        public actionHistories?: BaseEntity[],
    ) {
    }
}
