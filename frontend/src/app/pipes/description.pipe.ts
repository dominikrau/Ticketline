import {Pipe, PipeTransform} from '@angular/core';

@Pipe({
    name: 'description'
})
export class DescriptionPipe implements PipeTransform {

    private static readonly DEFAULT_MAX_LENGTH: number = 255;

    transform(value: string, maxLength?: number): string {
        const actMaxLength = isNaN(maxLength) ? DescriptionPipe.DEFAULT_MAX_LENGTH : maxLength;
        if (value.length <= actMaxLength) {
            return value;
        } else {
            return `${value.substring(0, actMaxLength - 2)}...`;
        }
    }

}
