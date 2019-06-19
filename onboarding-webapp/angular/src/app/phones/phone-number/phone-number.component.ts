import { Component} from '@angular/core';
import { forwardRef} from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';

@Component({
  selector: 'app-phone-number',
  templateUrl: './phone-number.component.html',
  styleUrls: ['./phone-number.component.scss'],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => PhoneNumberComponent),
      multi: true
    }
  ]
})
export class PhoneNumberComponent implements ControlValueAccessor {
  
  constructor() { }

  disabled:boolean;
  phoneNumberInput : string;

  onChange: any = () => {}
  onTouch: any = () => { }


  get value(){
    return this.phoneNumberInput;
  }

  writeValue(value: any) {
    this.phoneNumberInput = value;
  }

  registerOnChange(fn: any) {
    this.onChange = fn
  }

  registerOnTouched(fn: any) {
    this.onTouch = fn
  }

  setDisabledState(disabled:boolean) {
    this.disabled = disabled;
  }

}
