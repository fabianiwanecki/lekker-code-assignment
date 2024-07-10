import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogMemberRequestComponent } from './dialog-member-request.component';

describe('DialogMemberRequestComponent', () => {
  let component: DialogMemberRequestComponent;
  let fixture: ComponentFixture<DialogMemberRequestComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DialogMemberRequestComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DialogMemberRequestComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
