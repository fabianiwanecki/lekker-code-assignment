import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditTeamComponent } from './edit-team.component';
import {provideRouter, Routes} from "@angular/router";
import {provideHttpClient} from "@angular/common/http";
import {provideAnimations} from "@angular/platform-browser/animations";

const routes: Routes = [
  { path: '', component: {} as any },
];

describe('EditTeamComponent', () => {
  let component: EditTeamComponent;
  let fixture: ComponentFixture<EditTeamComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EditTeamComponent],
      providers: [provideRouter(routes), provideHttpClient(), provideAnimations()]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EditTeamComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
