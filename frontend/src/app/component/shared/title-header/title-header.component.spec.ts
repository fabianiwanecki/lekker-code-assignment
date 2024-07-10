import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TitleHeaderComponent } from './title-header.component';
import {provideRouter, Routes} from "@angular/router";

const routes: Routes = [
  { path: '', component: {} as any },
];

describe('TitleHeaderComponent', () => {
  let component: TitleHeaderComponent;
  let fixture: ComponentFixture<TitleHeaderComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TitleHeaderComponent],
      providers: [
        provideRouter(routes)
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TitleHeaderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
