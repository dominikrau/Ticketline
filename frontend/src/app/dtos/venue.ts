import {Address} from './address';

export class Venue {
    constructor(
        public venueId: number,
        public createdAt: string,
        public name: string,
        public address: Address) {
    }
}
