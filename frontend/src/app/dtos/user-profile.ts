import {Address} from './address';

export interface UserProfile {
    userId: number;
    firstName: string;
    lastName: string;
    emailAddress: string;
    // public password: string
    dateOfBirth: string;
    gender: string;
    address: Address;
    roles?: string[];
    blocked?: boolean;
}
