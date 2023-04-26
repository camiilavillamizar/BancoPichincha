import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LoadingComponent } from '../../../src/app/components/loading/loading.component';

describe('LoadingComponent', () => {
  let component: LoadingComponent;
  let fixture: ComponentFixture<LoadingComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LoadingComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LoadingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  test('match with snapshot', () => {
    const fixture = TestBed.createComponent(LoadingComponent);
    fixture.detectChanges(); 
    const component = fixture.componentInstance;

    expect(component).toMatchSnapshot()
  })
});
