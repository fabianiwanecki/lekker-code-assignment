import { CustomErrorStateMatcher } from './custom-error-state-matcher';
import {FormControl, FormGroupDirective} from "@angular/forms";

describe('CustomErrorStateMatcher', () => {
  let errorStateMatcher: CustomErrorStateMatcher;

  beforeEach(() => {
    errorStateMatcher = new CustomErrorStateMatcher();
  });

  it('should create an instance', () => {
    expect(new CustomErrorStateMatcher()).toBeTruthy();
  });

  it('should return false when control is null', () => {
    const result = errorStateMatcher.isErrorState(null, null);
    expect(result).toBeFalse();
  });

  it('should return false when control is valid', () => {
    const control = new FormControl('');
    const result = errorStateMatcher.isErrorState(control, null);
    expect(result).toBeFalse();
  });

  it('should return true when control is invalid and dirty', () => {
    const control = new FormControl('');
    control.setErrors({ required: true });
    control.markAsDirty();
    const result = errorStateMatcher.isErrorState(control, null);
    expect(result).toBeTrue();
  });

  it('should return true when control is invalid and touched', () => {
    const control = new FormControl('');
    control.setErrors({ required: true });
    control.markAsTouched();
    const result = errorStateMatcher.isErrorState(control, null);
    expect(result).toBeTrue();
  });

  it('should return true when control is invalid and form is submitted', () => {
    const control = new FormControl('');
    control.setErrors({ required: true });
    const formGroupDirective = { submitted: true } as FormGroupDirective;
    const result = errorStateMatcher.isErrorState(control, formGroupDirective);
    expect(result).toBeTrue();
  });

  it('should return false when control is valid and form is submitted', () => {
    const control = new FormControl('');
    const formGroupDirective = { submitted: true } as FormGroupDirective;
    const result = errorStateMatcher.isErrorState(control, formGroupDirective);
    expect(result).toBeFalse();
  });

  it('should return false when control is invalid but not dirty, touched, or submitted', () => {
    const control = new FormControl('');
    control.setErrors({ required: true });
    const result = errorStateMatcher.isErrorState(control, null);
    expect(result).toBeFalse();
  });
});
