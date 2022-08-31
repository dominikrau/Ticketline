export interface Page<T> {

    content: T[];
    numberOfPages: number;
    total: number;
    size: number;
    current: number;

}
