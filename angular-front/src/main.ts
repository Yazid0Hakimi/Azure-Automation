import { platformBrowserDynamic } from '@angular/platform-browser-dynamic';
import { AppModule } from './app/app.module';

function bootstrapApp() {
  platformBrowserDynamic()
    .bootstrapModule(AppModule)
    .catch(err => console.error(err));
}

fetch('/assets/environment.json')
  .then(response => {
    if (!response.ok) {
      throw new Error('Failed to load environment.json');
    }
    return response.json();
  })
  .then(config => {
    (window as any).__env = config;
    bootstrapApp();
  })
  .catch(error => {
    console.warn('Could not load environment config:', error);
    (window as any).__env = {}; // fallback to empty config
    bootstrapApp();
  });
