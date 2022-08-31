import {Show} from './show';

export interface Order {
    name: string;
    createdAt: string;
    orderId: number;
    show: Show;
    paymentMethod: string;
}
