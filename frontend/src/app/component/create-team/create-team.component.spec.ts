import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateTeamComponent } from './create-team.component';
import {provideHttpClient} from "@angular/common/http";
import {provideRouter, Routes} from "@angular/router";
import {provideAnimations} from "@angular/platform-browser/animations";

const routes: Routes = [
  { path: '', component: {} as any },
];

describe('CreateTeamComponent', () => {
  let component: CreateTeamComponent;
  let fixture: ComponentFixture<CreateTeamComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreateTeamComponent],
      providers: [provideHttpClient(), provideRouter(routes), provideAnimations()]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreateTeamComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
